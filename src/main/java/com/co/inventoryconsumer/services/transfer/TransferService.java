package com.co.inventoryconsumer.services.transfer;

import com.co.inventoryconsumer.domain.location.LocationDomain;
import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.domain.transfer.Transfer;
import com.co.inventoryconsumer.domain.transfer.TransferStatus;
import com.co.inventoryconsumer.dto.transfer.TransferRequestDTO;
import com.co.inventoryconsumer.dto.transfer.TransferResponseDTO;
import com.co.inventoryconsumer.repositories.location.LocationRepository;
import com.co.inventoryconsumer.repositories.product.ProductRepository;
import com.co.inventoryconsumer.repositories.transfer.TransferRepository;
import com.co.inventoryconsumer.utils.transfer.publisher.TransferResponsePublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransferService {

    private record TransferProducts(Product originProduct, Product destinationProduct) {
    }

    private final TransferRepository transferRepository;
    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    private final TransferResponsePublisher publisher;

    public TransferService(TransferRepository transferRepository,
                           ProductRepository productRepository,
                           LocationRepository locationRepository,
                           TransferResponsePublisher publisher) {
        this.transferRepository = transferRepository;
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TransferResponseDTO processTransfer(TransferRequestDTO request) {
        System.out.println("======================================");
        System.out.println("START PROCESS TRANSFER");
        System.out.println("Request received: " + request);
        System.out.println("======================================");

        TransferProducts products = validateAndLoadRequest(request);

        Transfer transfer = new Transfer();
        transfer.setSedeOrigen(request.sedeOrigen().trim());
        transfer.setSedeDestino(request.sedeDestino().trim());
        transfer.setFechaEnvio(request.fechaEnvio());
        transfer.setFechaLlegada(request.fechaLlegada());
        transfer.setResponsable(request.responsable().trim());
        transfer.setProducto(request.producto().trim());
        transfer.setCantidad(request.cantidad());
        transfer.setObservaciones(normalizeObservations(request.observaciones()));
        transfer.setEstado(TransferStatus.EN_PROCESO);
        transfer.setStock(products.originProduct().getStock());

        Transfer savedTransfer = transferRepository.save(transfer);
        return toResponse(savedTransfer);
    }

    public void publishResponse(TransferResponseDTO response) {
        System.out.println("======================================");
        System.out.println("ATTEMPTING TO PUBLISH TRANSFER RESPONSE");
        System.out.println("OBJECT: " + response);
        System.out.println("======================================");

        publisher.publish(response);
    }

    private TransferProducts validateAndLoadRequest(TransferRequestDTO request) {
        validateRequest(request);
        LocationDomain originLocation = validateLocationExistsAndActive(request.sedeOrigen(), "origen");
        LocationDomain destinationLocation = validateLocationExistsAndActive(request.sedeDestino(), "destino");

        Product originProduct = findProductByNameAndLocation(request.producto(), originLocation.getId(), "origen");
        Product destinationProduct = findProductByNameAndLocation(request.producto(), destinationLocation.getId(), "destino");
        validateStockAndQuantity(originProduct.getStock(), request.cantidad());

        return new TransferProducts(originProduct, destinationProduct);
    }

    private void validateRequest(TransferRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede estar vacia");
        }
        validateLocations(request);
        validateCoreFields(request);
        validateDates(request);
        validateQuantity(request);
    }

    private void validateLocations(TransferRequestDTO request) {
        if (request.sedeOrigen() == null || request.sedeOrigen().isBlank()) {
            throw new IllegalArgumentException("La sede de origen es obligatoria y debe ser valida");
        }
        if (request.sedeDestino() == null || request.sedeDestino().isBlank()) {
            throw new IllegalArgumentException("La sede de destino es obligatoria y debe ser valida");
        }
        if (request.sedeOrigen().trim().equals(request.sedeDestino().trim())) {
            throw new IllegalArgumentException("La sede de origen no puede ser igual a la sede de destino");
        }
    }

    private void validateCoreFields(TransferRequestDTO request) {
        if (request.fechaEnvio() == null) {
            throw new IllegalArgumentException("La fecha de transferencia es obligatoria");
        }
        if (request.fechaLlegada() == null) {
            throw new IllegalArgumentException("La fecha de llegada es obligatoria");
        }
        if (request.responsable() == null || request.responsable().isBlank()) {
            throw new IllegalArgumentException("El responsable es obligatorio");
        }
        if (request.producto() == null || request.producto().isBlank()) {
            throw new IllegalArgumentException("El producto es obligatorio y debe ser valido");
        }
    }

    private void validateDates(TransferRequestDTO request) {
        LocalDateTime now = LocalDateTime.now();

        if (request.fechaEnvio().isBefore(now)) {
            throw new IllegalArgumentException("La fecha de envio no puede ser anterior a la fecha actual");
        }
        if (request.fechaLlegada().isBefore(now)) {
            throw new IllegalArgumentException("La fecha de llegada no puede ser anterior a la fecha actual");
        }
        if (request.fechaLlegada().isBefore(request.fechaEnvio())) {
            throw new IllegalArgumentException("La fecha de llegada no puede ser anterior a la fecha de envio");
        }
    }

    private void validateQuantity(TransferRequestDTO request) {
        if (request.cantidad() == null || request.cantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
    }

    private LocationDomain validateLocationExistsAndActive(String locationId, String role) {
        UUID parsedLocationId;
        try {
            parsedLocationId = UUID.fromString(locationId);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("La sede de " + role + " debe ser un UUID valido");
        }

        return locationRepository.findById(parsedLocationId)
                .filter(LocationDomain::isActive)
                .orElseThrow(() -> new IllegalArgumentException("La sede de " + role + " no existe o esta inactiva"));
    }

    private Product findProductByNameAndLocation(String productName, UUID locationId, String role) {
        return productRepository.findAll().stream()
                .filter(product -> locationId.equals(product.getLocationId()))
                .filter(product -> product.getName() != null)
                .filter(product -> product.getName().trim().equalsIgnoreCase(productName.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el producto '" + productName + "' en la sede de " + role
                ));
    }

    private void validateStockAndQuantity(BigDecimal stock, Integer cantidad) {
        if (stock == null || stock.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El producto tiene un stock invalido para realizar la transferencia");
        }
        if (BigDecimal.valueOf(cantidad.longValue()).compareTo(stock) > 0) {
            throw new IllegalArgumentException("La cantidad a transferir no puede superar el stock disponible");
        }
    }

    private String normalizeObservations(String observaciones) {
        if (observaciones == null || observaciones.isBlank()) {
            return null;
        }
        return observaciones.trim();
    }

    private TransferResponseDTO toResponse(Transfer transfer) {
        return new TransferResponseDTO(
                transfer.getIdTraslado(),
                transfer.getSedeOrigen(),
                transfer.getSedeDestino(),
                transfer.getFechaEnvio(),
                transfer.getFechaLlegada(),
                transfer.getResponsable(),
                transfer.getProducto(),
                transfer.getStock(),
                transfer.getCantidad(),
                transfer.getObservaciones(),
                transfer.getEstado(),
                transfer.getCreatedAt(),
                transfer.getUpdatedAt()
        );
    }
}

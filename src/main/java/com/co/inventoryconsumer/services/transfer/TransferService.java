package com.co.inventoryconsumer.services.transfer;

import com.co.inventoryconsumer.domain.location.LocationDomain;
import com.co.inventoryconsumer.domain.product.Product;
import com.co.inventoryconsumer.domain.transfer.Transfer;
import com.co.inventoryconsumer.domain.transfer.TransferStatus;
import com.co.inventoryconsumer.dto.transfer.TransferRequestDTO;
import com.co.inventoryconsumer.dto.transfer.TransferResponseDTO;
import com.co.inventoryconsumer.dto.transfer.TransferStatusUpdateDTO;
import com.co.inventoryconsumer.repositories.location.LocationRepository;
import com.co.inventoryconsumer.repositories.product.ProductRepository;
import com.co.inventoryconsumer.repositories.transfer.TransferRepository;
import com.co.inventoryconsumer.utils.transfer.publisher.TransferResponsePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferService.class);
    private static final String LOG_SEPARATOR = "======================================";
    private static final String ORIGIN_ROLE = "origen";
    private static final String DESTINATION_ROLE = "destino";

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
        LOGGER.info(LOG_SEPARATOR);
        LOGGER.info("START PROCESS TRANSFER");
        LOGGER.info("Request received: {}", request);
        LOGGER.info(LOG_SEPARATOR);

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

    @Transactional
    public TransferResponseDTO updateTransferStatus(TransferStatusUpdateDTO request) {
        LOGGER.info(LOG_SEPARATOR);
        LOGGER.info("START UPDATE TRANSFER STATUS");
        LOGGER.info("Request received: {}", request);
        LOGGER.info(LOG_SEPARATOR);

        validateStatusUpdateRequest(request);

        Transfer transfer = transferRepository.findById(request.idTraslado())
                .orElseThrow(() -> new IllegalArgumentException("Transferencia no encontrada con id: " + request.idTraslado()));

        syncTransitStatus(transfer);

        if (request.estado() == TransferStatus.CANCELADO) {
            validateCancellationAllowed(transfer, request.sedeOrigen());
        }
        if (request.estado() == TransferStatus.EN_TRANSITO) {
            validateTransitAllowed(transfer, request.sedeOrigen());
        }
        if (request.estado() == TransferStatus.COMPLETADO || request.estado() == TransferStatus.RECLAMADO) {
            validateDestinationActionAllowed(transfer, request);
        }

        validateStatusTransition(transfer.getEstado(), request.estado());

        if (request.estado() == TransferStatus.COMPLETADO) {
            applyInventoryMovement(transfer);
        }
        if (request.estado() == TransferStatus.RECLAMADO) {
            transfer.setObservaciones(normalizeClaimObservations(request.observaciones()));
        }

        transfer.setEstado(request.estado());

        Transfer savedTransfer = transferRepository.save(transfer);
        return toResponse(savedTransfer);
    }

    public void publishResponse(TransferResponseDTO response) {
        LOGGER.info(LOG_SEPARATOR);
        LOGGER.info("ATTEMPTING TO PUBLISH TRANSFER RESPONSE");
        LOGGER.info("OBJECT: {}", response);
        LOGGER.info(LOG_SEPARATOR);

        publisher.publish(response);
    }

    private TransferProducts validateAndLoadRequest(TransferRequestDTO request) {
        validateRequest(request);
        LocationDomain originLocation = validateLocationExistsAndActive(request.sedeOrigen(), ORIGIN_ROLE);
        LocationDomain destinationLocation = validateLocationExistsAndActive(request.sedeDestino(), DESTINATION_ROLE);

        Product originProduct = findProductByNameAndLocation(request.producto(), originLocation.getId(), ORIGIN_ROLE);
        Product destinationProduct = findProductByNameAndLocation(request.producto(), destinationLocation.getId(), DESTINATION_ROLE);
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

    private void validateStatusUpdateRequest(TransferStatusUpdateDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud de actualizacion no puede estar vacia");
        }
        if (request.idTraslado() == null || request.idTraslado() <= 0) {
            throw new IllegalArgumentException("El id de la transferencia es obligatorio y debe ser mayor a cero");
        }
        if (request.estado() == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
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

    private void applyInventoryMovement(Transfer transfer) {
        TransferProducts products = loadProductsFromTransfer(transfer);
        BigDecimal quantity = BigDecimal.valueOf(transfer.getCantidad().longValue());

        validateStockAndQuantity(products.originProduct().getStock(), transfer.getCantidad());

        products.originProduct().setStock(products.originProduct().getStock().subtract(quantity));
        products.destinationProduct().setStock(products.destinationProduct().getStock().add(quantity));

        productRepository.save(products.originProduct());
        productRepository.save(products.destinationProduct());
        transfer.setStock(products.originProduct().getStock());
    }

    private TransferProducts loadProductsFromTransfer(Transfer transfer) {
        LocationDomain originLocation = validateLocationExistsAndActive(transfer.getSedeOrigen(), ORIGIN_ROLE);
        LocationDomain destinationLocation = validateLocationExistsAndActive(transfer.getSedeDestino(), DESTINATION_ROLE);

        Product originProduct = findProductByNameAndLocation(transfer.getProducto(), originLocation.getId(), ORIGIN_ROLE);
        Product destinationProduct = findProductByNameAndLocation(transfer.getProducto(), destinationLocation.getId(), DESTINATION_ROLE);

        return new TransferProducts(originProduct, destinationProduct);
    }

    private void validateCancellationAllowed(Transfer transfer, String sedeOrigen) {
        if (sedeOrigen == null || sedeOrigen.isBlank()) {
            throw new IllegalArgumentException("La sede origen es obligatoria para cancelar");
        }

        validateLocationExistsAndActive(sedeOrigen, ORIGIN_ROLE);

        if (!sedeOrigen.trim().equals(transfer.getSedeOrigen())) {
            throw new IllegalArgumentException("Solo la sede origen puede cancelar el traslado");
        }

        if (transfer.getEstado() != TransferStatus.EN_PROCESO) {
            throw new IllegalArgumentException("La sede origen solo puede cancelar traslados en estado EN_PROCESO");
        }
    }

    private void validateDestinationActionAllowed(Transfer transfer, TransferStatusUpdateDTO request) {
        if (request.sedeDestino() == null || request.sedeDestino().isBlank()) {
            throw new IllegalArgumentException("La sede destino es obligatoria para gestionar el traslado");
        }

        validateLocationExistsAndActive(request.sedeDestino(), DESTINATION_ROLE);

        if (!request.sedeDestino().trim().equals(transfer.getSedeDestino())) {
            throw new IllegalArgumentException("Solo la sede destino puede gestionar esta accion");
        }

        if (transfer.getEstado() != TransferStatus.EN_TRANSITO) {
            throw new IllegalArgumentException("Solo se pueden gestionar traslados en estado EN_TRANSITO");
        }
    }

    private void validateTransitAllowed(Transfer transfer, String sedeOrigen) {
        if (sedeOrigen == null || sedeOrigen.isBlank()) {
            throw new IllegalArgumentException("La sede origen es obligatoria para enviar el traslado");
        }

        validateLocationExistsAndActive(sedeOrigen, ORIGIN_ROLE);

        if (!sedeOrigen.trim().equals(transfer.getSedeOrigen())) {
            throw new IllegalArgumentException("Solo la sede origen puede marcar el traslado en transito");
        }

        if (transfer.getEstado() != TransferStatus.EN_PROCESO) {
            throw new IllegalArgumentException("Solo se pueden enviar a transito traslados en estado EN_PROCESO");
        }
    }

    private String normalizeClaimObservations(String observaciones) {
        if (observaciones == null || observaciones.isBlank()) {
            throw new IllegalArgumentException("La observacion del reclamo es obligatoria");
        }
        return observaciones.trim();
    }

    private Transfer syncTransitStatus(Transfer transfer) {
        if (transfer.getEstado() == TransferStatus.EN_PROCESO
                && transfer.getFechaEnvio() != null
                && !LocalDateTime.now().isBefore(transfer.getFechaEnvio())) {
            transfer.setEstado(TransferStatus.EN_TRANSITO);
            return transferRepository.save(transfer);
        }
        return transfer;
    }

    private void validateStatusTransition(TransferStatus currentStatus, TransferStatus nextStatus) {
        if (currentStatus == nextStatus) {
            return;
        }

        boolean validTransition = switch (currentStatus) {
            case EN_PROCESO -> nextStatus == TransferStatus.CANCELADO || nextStatus == TransferStatus.EN_TRANSITO;
            case EN_TRANSITO -> nextStatus == TransferStatus.COMPLETADO || nextStatus == TransferStatus.RECLAMADO;
            case COMPLETADO, RECLAMADO, CANCELADO -> false;
        };

        if (!validTransition) {
            throw new IllegalArgumentException(
                    "No se permite cambiar el estado de " + currentStatus + " a " + nextStatus
            );
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

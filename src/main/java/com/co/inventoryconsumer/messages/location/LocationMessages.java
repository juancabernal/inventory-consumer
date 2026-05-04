package com.co.inventoryconsumer.messages.location;

import com.co.inventoryconsumer.dto.location.LocationRequestDTO;
import com.co.inventoryconsumer.services.location.LocationService;
import com.co.inventoryconsumer.utils.gson.MapperJsonObjeto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LocationMessages {

    private final LocationService locationService;
    private final MapperJsonObjeto mapper;

    public LocationMessages(LocationService locationService, MapperJsonObjeto mapper) {
        this.locationService = locationService;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.location.create}")
    public void receiveCreate(String messageJson) {
        printMessageReceived("location.create", messageJson);

        try {
            Optional<LocationRequestDTO> requestOptional = mapper.ejecutar(messageJson, LocationRequestDTO.class);

            if (requestOptional.isEmpty()) {
                printConversionError("LocationRequestDTO", messageJson);
                return;
            }

            LocationRequestDTO dto = requestOptional.get();

            printCreateDto(dto);

            locationService.create(dto);

            System.out.println("SUCCESS: La sede ha sido creada correctamente en la base de datos.");
        } catch (Exception ex) {
            printProcessError("crear la sede", ex);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.location.update}")
    public void receiveUpdate(String messageJson) {
        printMessageReceived("location.update", messageJson);

        try {
            Optional<LocationRequestUpdateWrapper> wrapperOptional =
                    mapper.ejecutar(messageJson, LocationRequestUpdateWrapper.class);

            if (wrapperOptional.isEmpty()) {
                printConversionError("LocationRequestUpdateWrapper", messageJson);
                return;
            }

            LocationRequestUpdateWrapper wrapper = wrapperOptional.get();

            if (wrapper.getId() == null || wrapper.getRequest() == null) {
                System.out.println("ERROR: El mensaje de actualización no tiene id o request.");
                return;
            }

            locationService.update(wrapper.getId(), wrapper.getRequest());

            System.out.println("SUCCESS: La sede ha sido actualizada correctamente con PUT.");
            System.out.println("ID de informacion actualizada: " + wrapper.getId());
        } catch (Exception ex) {
            printProcessError("actualizar la sede con PUT", ex);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.location.patch}")
    public void receivePatch(String messageJson) {
        printMessageReceived("location.patch", messageJson);

        try {
            Optional<LocationPatchWrapper> wrapperOptional =
                    mapper.ejecutar(messageJson, LocationPatchWrapper.class);

            if (wrapperOptional.isEmpty()) {
                printConversionError("LocationPatchWrapper", messageJson);
                return;
            }

            LocationPatchWrapper wrapper = wrapperOptional.get();

            if (wrapper.getId() == null || wrapper.getPatch() == null) {
                System.out.println("ERROR: El mensaje de actualización parcial no tiene id o patch.");
                return;
            }

            locationService.patchPartial(wrapper.getId(), wrapper.getPatch());

            System.out.println("SUCCESS: La sede ha sido actualizada correctamente con PATCH.");
            System.out.println("ID de informacion actualizada: " + wrapper.getId());
        } catch (Exception ex) {
            printProcessError("actualizar parcialmente la sede con PATCH", ex);
        }
    }

    private void printMessageReceived(String queueName, String messageJson) {
        System.out.println("======================================");
        System.out.println("Mensaje recibido en cola " + queueName + ":");
        System.out.println(messageJson);
        System.out.println("======================================");
    }

    private void printConversionError(String expectedType, String messageJson) {
        System.out.println("ERROR: No se pudo convertir el JSON a " + expectedType);
        System.out.println("Mensaje original:");
        System.out.println(messageJson);
    }

    private void printProcessError(String action, Exception ex) {
        System.out.println("ERROR: No fue posible " + action + ".");
        System.out.println("Tipo de error: " + ex.getClass().getSimpleName());
        System.out.println("Mensaje de error: " + ex.getMessage());
        ex.printStackTrace();
    }

    private void printCreateDto(LocationRequestDTO dto) {
        System.out.println("DTO convertido correctamente:");
        System.out.println("Name: " + dto.getName());
        System.out.println("City: " + dto.getCity());
        System.out.println("Address: " + dto.getAddress());
        System.out.println("Email: " + dto.getEmail());
        System.out.println("Phone: " + dto.getPhoneNumber());
        System.out.println("StartTime: " + dto.getStartTime());
        System.out.println("EndTime: " + dto.getEndTime());
    }
}
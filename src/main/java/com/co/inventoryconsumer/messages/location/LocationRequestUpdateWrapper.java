package com.co.inventoryconsumer.messages.location;


import com.co.inventoryconsumer.dto.location.LocationRequestDTO;
import java.util.UUID;

/**
 * Envoltura para mensajes de actualización completa (PUT).
 * Contiene el id de la sede y el cuerpo de la solicitud.
 */
public class LocationRequestUpdateWrapper {
    private UUID id;
    private LocationRequestDTO request;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public LocationRequestDTO getRequest() {
        return request;
    }
    public void setRequest(LocationRequestDTO request) {
        this.request = request;
    }
}

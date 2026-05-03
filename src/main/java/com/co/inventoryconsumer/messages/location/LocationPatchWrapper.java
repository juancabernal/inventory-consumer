package com.co.inventoryconsumer.messages.location;

import com.co.inventoryconsumer.dto.location.LocationPatchDTO;
import java.util.UUID;

/**
 * Envoltura para mensajes de actualización parcial (PATCH).
 * Contiene el id de la sede y el cuerpo del patch.
 */
public class LocationPatchWrapper {
    private UUID id;
    private LocationPatchDTO patch;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public LocationPatchDTO getPatch() {
        return patch;
    }
    public void setPatch(LocationPatchDTO patch) {
        this.patch = patch;
    }
}
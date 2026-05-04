package com.co.inventoryconsumer.utils.location.mapper;

import com.co.inventoryconsumer.domain.location.LocationDomain;
import com.co.inventoryconsumer.dto.location.LocationRequestDTO;

import com.co.inventoryconsumer.utils.exceptions.ValidationException;

import java.util.UUID;

public class LocationMapperDomain {

    private LocationMapperDomain() {}

    public static LocationDomain toDomain(LocationRequestDTO dto) {
        if (dto == null) {
            throw new ValidationException("La solicitud no puede estar vacía");
        }
        // Creamos una sede con un nuevo UUID
        return new LocationDomain(
                UUID.randomUUID(),
                dto.getName(),
                dto.getCity(),
                dto.getAddress(),
                dto.getActive(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getStartTime(),
                dto.getEndTime()
        );
    }

    public static LocationDomain toDomain(UUID id, LocationRequestDTO dto) {
        if (dto == null) {
            throw new ValidationException("La solicitud no puede estar vacía");
        }
        // Creamos una sede usando el id que viene como argumento
        return new LocationDomain(
                id,
                dto.getName(),
                dto.getCity(),
                dto.getAddress(),
                dto.getActive(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getStartTime(),
                dto.getEndTime()
        );
    }
}
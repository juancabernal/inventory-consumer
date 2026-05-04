package com.co.inventoryconsumer.services.location;




import com.co.inventoryconsumer.dto.location.LocationPatchDTO;
import com.co.inventoryconsumer.dto.location.LocationRequestDTO;
import com.co.inventoryconsumer.dto.location.LocationResponseDTO;

import java.util.List;
import java.util.UUID;

public interface LocationService {
    List<LocationResponseDTO> findAll();

    List<LocationResponseDTO> findAllActive();

    LocationResponseDTO findById(UUID id);

    LocationResponseDTO create(LocationRequestDTO request);

    LocationResponseDTO update(UUID id, LocationRequestDTO request);

    LocationResponseDTO patchPartial(UUID id, LocationPatchDTO patch);


}

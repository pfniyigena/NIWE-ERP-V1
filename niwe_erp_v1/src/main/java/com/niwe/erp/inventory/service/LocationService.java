package com.niwe.erp.inventory.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.inventory.domain.InventoryLocation;
import com.niwe.erp.inventory.repository.InventoryLocationRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class LocationService {
	private final SequenceNumberService sequenceNumberService;

	private final InventoryLocationRepository inventoryLocationRepository;

	public InventoryLocation findById(String locationId) {
		return inventoryLocationRepository.findById(UUID.fromString(locationId))
				.orElseThrow(() -> new ResourceNotFoundException("InventoryLocation not found with id " + locationId));
	}

	public List<InventoryLocation> findAll() {
		return inventoryLocationRepository.findAll();

	}

	public List<InventoryLocation> findByWarehouseId(UUID warehouseId) {
		return inventoryLocationRepository.findByWarehouseId(warehouseId);
	}

	public InventoryLocation save(InventoryLocation stand) {
		InventoryLocation location = null;
		String code = stand.getInternalCode();
		if (code == null || code.isEmpty()) {
			code = sequenceNumberService.getNextStandCode();
		}

		if (stand.getId() != null) {

			location = inventoryLocationRepository.getReferenceById(stand.getId());
			location.setInternalCode(code);
			location.setLocationCode(stand.getLocationCode());
			location.setLocationName(stand.getLocationName());
			location.setManagerName(stand.getManagerName());
		} else {

			location = stand;
			location.setInternalCode(code);

		}

		return inventoryLocationRepository.save(location);

	}
}

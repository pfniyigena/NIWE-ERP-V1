package com.niwe.erp.inventory.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.repository.CoreItemRepository;
import com.niwe.erp.inventory.domain.InventoryLocation;
import com.niwe.erp.inventory.domain.MovementType;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.repository.WarehouseRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class InventoryService {
	private final CoreItemRepository coreItemRepository;
	private final WarehouseRepository warehouseRepository;
	private final LocationService locationService;
	private final StockMovementService movementService;

	@Transactional
	public void receiveToWarehouse(String itemId, String warehouseId, BigDecimal qty, String reference) {
		CoreItem item = coreItemRepository.findById(UUID.fromString(itemId))
				.orElseThrow(() -> new ResourceNotFoundException("Item not found" + itemId));
		Warehouse warehouse = warehouseRepository.findById(UUID.fromString(warehouseId))
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found " + warehouseId));
		movementService.logReceive(warehouse, item, qty, reference, MovementType.GOOD_RECEIVED_NOTE);
	}

	/**
	 * Transfer from warehouse -> location (common case for preparing sale)
	 */
	@Transactional
	public void transferWarehouseToLocation(String warehouseId, String locationId, String itemId, BigDecimal qty,
			String reference) {
		CoreItem item = coreItemRepository.findById(UUID.fromString(itemId))
				.orElseThrow(() -> new ResourceNotFoundException("Item not found" + itemId));
		Warehouse warehouse = warehouseRepository.findById(UUID.fromString(warehouseId))
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found " + warehouseId));
		InventoryLocation location = locationService.findById(locationId);
		movementService.logTransferToLocation(warehouse.getId(), location.getId(), item, qty, reference,
				MovementType.TRANSFER);

	}

	@Transactional
	public void sellFromLocation(UUID warehouseId, UUID itemId, BigDecimal qty, String reference) {
		CoreItem item = coreItemRepository.findById(itemId)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found" + itemId));
		movementService.logSaleFromLocations(warehouseId, item, qty, reference);

	}
}

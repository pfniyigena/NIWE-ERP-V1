package com.niwe.erp.inventory.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.inventory.domain.InventoryLocation;
import com.niwe.erp.inventory.domain.LocationStock;
import com.niwe.erp.inventory.repository.LocationStockRepository;
import com.niwe.erp.inventory.web.view.LocationStockView;
import com.niwe.erp.inventory.web.view.OutflowItemListView;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class LocationStockService {
	private final LocationStockRepository locationStockRepository;

	@Transactional(readOnly = true)
	public BigDecimal getQuantity(UUID locationId, UUID itemId) {
		return locationStockRepository.findByLocationIdAndItemId(locationId, itemId).map(LocationStock::getQuantity)
				.orElse(BigDecimal.ZERO);
	}

	@Transactional
	public LocationStock ensureRecord(UUID locationId, CoreItem item) {
		return locationStockRepository.findByLocationIdAndItemId(locationId, item.getId()).orElseGet(() -> {
			LocationStock ls = LocationStock.builder().location(InventoryLocation.builder().id(locationId).build())
					.item(item).quantity(BigDecimal.ZERO).createdAt(Instant.now()).lastUpdated(LocalDateTime.now())
					.build();
			return locationStockRepository.save(ls);
		});
	}

	@Transactional
	public void increase(UUID locationId, CoreItem item, BigDecimal qty) {
		LocationStock ls = locationStockRepository.findByLocationAndItemForUpdate(locationId, item.getId())
				.orElseGet(() -> LocationStock.builder().location(InventoryLocation.builder().id(locationId).build())
						.item(item).quantity(BigDecimal.ZERO).createdAt(Instant.now()).build());
		ls.setQuantity(ls.getQuantity().add(qty));
		ls.setLastUpdated(LocalDateTime.now());
		locationStockRepository.save(ls);
	}

	@Transactional
	public LocationStock ensureLocationStock(UUID locationId, CoreItem item) {
		return locationStockRepository.findByLocationIdAndItemId(locationId, item.getId()).orElseGet(() -> {
			LocationStock ls = LocationStock.builder().location(InventoryLocation.builder().id(locationId).build())
					.item(item).quantity(BigDecimal.ZERO)

					.build();
			return locationStockRepository.save(ls);
		});
	}

	@Transactional
	public void decrease(UUID locationId, CoreItem item, BigDecimal qty) {
		LocationStock ls = locationStockRepository.findByLocationAndItemForUpdate(locationId, item.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No location stock"));
		if (ls.getQuantity().compareTo(qty) < 0) {
			throw new ResourceNotFoundException("Insufficient location stock");
		}
		ls.setQuantity(ls.getQuantity().subtract(qty));
		ls.setLastUpdated(LocalDateTime.now());
		locationStockRepository.save(ls);
	}

	@Transactional(readOnly = true)
	public List<LocationStock> findAvailableByWarehouseAndItem(UUID warehouseId, UUID itemId) {
		return locationStockRepository.findAvailableByWarehouseAndItem(warehouseId, itemId);
	}

	@Transactional
	public void setQuantity(UUID locationId, CoreItem item, BigDecimal qty) {
		LocationStock ls = locationStockRepository.findByLocationAndItemForUpdate(locationId, item.getId())
				.orElseGet(() -> LocationStock.builder().location(InventoryLocation.builder().id(locationId).build())
						.item(item).quantity(BigDecimal.ZERO).createdAt(Instant.now()).build());
		ls.setQuantity(qty);
		ls.setLastUpdated(LocalDateTime.now());
		locationStockRepository.save(ls);
	}

	public Page<OutflowItemListView> findAvailableStockByLocation(UUID warehouseId, String searchValue,
			Pageable pageable) {
		return locationStockRepository.findOutflowItems(warehouseId, searchValue, pageable);
	}

	List<LocationStockView> getAvailableLocationsFIFO(UUID warehouseId, UUID itemId) {
		return locationStockRepository.getAvailableLocationsFIFO(warehouseId, itemId);
	}

	List<LocationStock> findLocationsFIFO(UUID warehouseId, UUID itemId) {
		return locationStockRepository.findLocationsFIFO(warehouseId, itemId);
	}
}

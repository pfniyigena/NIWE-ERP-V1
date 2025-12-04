package com.niwe.erp.inventory.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niwe.erp.common.service.NotificationService;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.inventory.domain.InventoryLocation;
import com.niwe.erp.inventory.domain.LocationStock;
import com.niwe.erp.inventory.domain.MovementType;
import com.niwe.erp.inventory.domain.StockMovement;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.repository.StockMovementRepository;
import com.niwe.erp.inventory.web.view.LocationStockView;
import com.niwe.erp.inventory.web.view.StockMovementListView;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class StockMovementService {

	private final StockMovementRepository stockMovementRepository;
	private final WarehouseStockService warehouseStockService;
	private final LocationStockService locationStockService;
	private final NotificationService notificationService;

	public List<StockMovement> findAll() {
		return stockMovementRepository.findAll();

	}

	public StockMovement logMovement2(CoreItem item, Warehouse fromWarehouse, Warehouse toWarehouse,
			InventoryLocation fromLocation, InventoryLocation toLocation, BigDecimal qty, MovementType type,
			String reference) {

		StockMovement m = StockMovement.builder().item(item).fromWarehouse(fromWarehouse).toWarehouse(toWarehouse)
				.fromLocation(fromLocation).toLocation(toLocation).movedQuantity(qty).movementType(type)
				.reference(reference).build();
		return stockMovementRepository.save(m);
	}

	public Page<StockMovementListView> getMovements(UUID itemId, UUID warehouseId, String search, Pageable pageable) {
		if (search != null && search.trim().isEmpty()) {
			search = null;
		}

		return stockMovementRepository.findMovements(itemId, warehouseId, search, pageable);
	}

	public Page<StockMovementListView> findMovementByItemAndLocations(UUID itemId, UUID locationId, String search,
			Pageable pageable) {
		if (search != null && search.trim().isEmpty()) {
			search = null;
		}

		return stockMovementRepository.findMovementByItemAndLocations(itemId, locationId, search, pageable);
	}

	@Transactional
	public StockMovement logReceive(Warehouse warehouse, CoreItem item, BigDecimal qty, String reference,
			MovementType movementType) {

		BigDecimal prevWh = warehouseStockService.getQuantity(warehouse.getId(), item.getId());
		BigDecimal newWh = prevWh.add(qty);

		// update warehouse stock
		warehouseStockService.increase(warehouse.getId(), item, qty);

		StockMovement sm = StockMovement.builder().item(item).fromWarehouse(null)
				.toWarehouse(Warehouse.builder().id(warehouse.getId()).build()).movedQuantity(qty)
				.movementType(movementType).previousWarehouseQuantity(prevWh).currentWarehouseQuantity(newWh)
				.movementDate(Instant.now()).reference(reference).build();
		return stockMovementRepository.save(sm);
	}

	@Transactional
	public StockMovement logTransferToLocation(UUID warehouseId, UUID locationId, CoreItem item, BigDecimal qty,
			String reference, MovementType movementType) {

		BigDecimal prevWh = warehouseStockService.getQuantity(warehouseId, item.getId());
		BigDecimal prevLoc = locationStockService.getQuantity(locationId, item.getId());

		// update stocks
		warehouseStockService.decrease(warehouseId, item, qty);
		locationStockService.increase(locationId, item, qty);

		BigDecimal newWh = prevWh.subtract(qty);
		BigDecimal newLoc = prevLoc.add(qty);

		StockMovement sm = StockMovement.builder().item(item).fromWarehouse(Warehouse.builder().id(warehouseId).build())
				.toLocation(InventoryLocation.builder().id(locationId).build()).movedQuantity(qty)
				.movementType(movementType).previousWarehouseQuantity(prevWh).currentWarehouseQuantity(newWh)
				.previousLocationQuantity(prevLoc).currentLocationQuantity(newLoc).movementDate(Instant.now())
				.reference(reference).build();
		return stockMovementRepository.save(sm);
	}

	@Transactional
	public List<StockMovement> logSaleFromLocations2(UUID warehouseId, CoreItem item, BigDecimal saleQty,
			String reference) {

		BigDecimal remaining = saleQty;
		List<StockMovement> movementList = new ArrayList<>();

		// 1️⃣ Get FIFO list of locations with available quantities
		List<LocationStockView> fifoLocations = locationStockService.getAvailableLocationsFIFO(warehouseId,
				item.getId());

		// 2️⃣ Get previous warehouse quantity before global sale deduction
		BigDecimal prevWarehouseQty = warehouseStockService.getQuantity(warehouseId, item.getId());

		for (LocationStockView loc : fifoLocations) {

			if (remaining.compareTo(BigDecimal.ZERO) <= 0)
				break;

			BigDecimal available = loc.getAvailableQty();
			if (available.compareTo(BigDecimal.ZERO) <= 0)
				continue;

			// Quantity to consume from this location
			BigDecimal deductQty = available.min(remaining);

			// Quantities BEFORE deduction
			BigDecimal prevLocQty = locationStockService.getQuantity(loc.getLocationId(), item.getId());
			BigDecimal prevWhQty = prevWarehouseQty;

			// Perform actual stock deduction
			// update stocks
			warehouseStockService.decrease(warehouseId, item, deductQty);
			locationStockService.decrease(loc.getLocationId(), item, deductQty);

			// Quantities AFTER deduction
			BigDecimal newLocQty = prevLocQty.subtract(deductQty);
			BigDecimal newWhQty = prevWhQty.subtract(deductQty);

			prevWarehouseQty = newWhQty; // update for next iteration

			// Save movement entry
			StockMovement sm = StockMovement.builder().item(CoreItem.builder().id(item.getId()).build())
					.fromWarehouse(Warehouse.builder().id(warehouseId).build())
					.fromLocation(InventoryLocation.builder().id(loc.getLocationId()).build()).toWarehouse(null)
					.toLocation(null).movedQuantity(deductQty).movementType(MovementType.SALE)
					.previousWarehouseQuantity(prevWhQty).currentWarehouseQuantity(newWhQty)
					.previousLocationQuantity(prevLocQty).currentLocationQuantity(newLocQty).movementDate(Instant.now())
					.reference(reference).build();

			movementList.add(stockMovementRepository.save(sm));

			remaining = remaining.subtract(deductQty);
		}

		// Not enough stock to complete sale
		if (remaining.compareTo(BigDecimal.ZERO) > 0) {
			throw new IllegalArgumentException("Insufficient stock to complete sale.");
		}

		return movementList;
	}

	@Transactional
	public StockMovement logSaleFromLocations(UUID warehouseId, CoreItem item, BigDecimal saleQty, String reference) {
		BigDecimal remaining = saleQty;
		List<LocationStock> locations = locationStockService.findLocationsFIFO(warehouseId, item.getId());
		StockMovement lastMovement = null;
		for (LocationStock loc : locations) {
			if (remaining.compareTo(BigDecimal.ZERO) <= 0)
				break;
			BigDecimal available = loc.getQuantity();
			BigDecimal deduct = available.min(remaining);

			BigDecimal prevLoc = available;
			BigDecimal newLoc = available.subtract(deduct);
			// Decrease only location stock
			locationStockService.decrease(loc.getLocation().getId(), loc.getItem(), deduct);
			// Log stock movement
			StockMovement sm = StockMovement.builder().item(loc.getItem()).fromLocation(loc.getLocation())
					.toLocation(null).movementType(MovementType.SALE).movedQuantity(deduct)
					.previousLocationQuantity(prevLoc).currentLocationQuantity(newLoc).movementDate(Instant.now())
					.reference(reference).build();
			lastMovement = stockMovementRepository.save(sm);
			remaining = remaining.subtract(deduct);
		}
		if (remaining.compareTo(BigDecimal.ZERO) > 0) {

			notificationService.sentEmail("Not enough stock in locations");

			throw new IllegalStateException(
					"Not enough stock in locations: Available Stock in:" + remaining + " for Item:" + item);
		}

		return lastMovement;
	}

}

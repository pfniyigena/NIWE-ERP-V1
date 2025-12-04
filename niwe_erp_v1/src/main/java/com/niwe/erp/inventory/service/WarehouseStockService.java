package com.niwe.erp.inventory.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.inventory.domain.MovementType;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.domain.WarehouseStock;
import com.niwe.erp.inventory.repository.WarehouseStockRepository;
import com.niwe.erp.inventory.web.dto.ProductStockAgingDto;
import com.niwe.erp.inventory.web.dto.ProductStockSummaryDto;
import com.niwe.erp.inventory.web.dto.ProductStockValuationDto;
import com.niwe.erp.inventory.web.dto.WarehouseStockDetailDto;
import com.niwe.erp.inventory.web.view.InflowItemListView;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class WarehouseStockService {
	private final WarehouseStockRepository warehouseStockRepository;

	@Transactional
	public void increase(Warehouse warehouse, CoreItem product, BigDecimal quantityChange, MovementType movementType,
			String reference) {
		try {

			log.debug("-------increase");
			WarehouseStock stock = warehouseStockRepository.findByWarehouseAndItem(warehouse, product).orElseGet(() -> {
				WarehouseStock newStock = new WarehouseStock();
				newStock.setWarehouse(warehouse);
				newStock.setItem(product);
				newStock.setQuantity(new BigDecimal("0.00"));
				return newStock;
			});
			BigDecimal previousQuantiry = stock.getQuantity();
			BigDecimal newQuantity = new BigDecimal("0.00");
			newQuantity = previousQuantiry.add(quantityChange);
			switch (movementType) {
			case STOCK_INITIAL: {
				stock.setReceivedDate(Instant.now());
				break;
			}
			case PURCHASE: {
				stock.setReceivedDate(Instant.now());
				break;
			}
			case GOOD_RECEIVED_NOTE: {
				stock.setReceivedDate(Instant.now());
				break;
			}
			default:
				log.debug("Do not mark receivedDate");

			}
			stock.setQuantity(newQuantity);
			warehouseStockRepository.save(stock);
		} catch (Exception e) {
			log.error("-------updateProductQuantity:{}", e);
		}
	}

	@Transactional
	public void decrease(Warehouse warehouse, CoreItem product, BigDecimal quantityChange, MovementType movementType,
			String reference) {
		try {

			log.debug("-------updateProductQuantity");
			WarehouseStock stock = warehouseStockRepository.findByWarehouseAndItem(warehouse, product).orElseGet(() -> {
				WarehouseStock newStock = new WarehouseStock();
				newStock.setWarehouse(warehouse);
				newStock.setItem(product);
				newStock.setQuantity(new BigDecimal("0.00"));
				return newStock;
			});
			BigDecimal previousQuantiry = stock.getQuantity();
			BigDecimal newQuantity = new BigDecimal("0.00");
			newQuantity = previousQuantiry.subtract(quantityChange);

			switch (movementType) {
			case STOCK_INITIAL: {
				stock.setReceivedDate(Instant.now());
				break;
			}
			case PURCHASE: {
				stock.setReceivedDate(Instant.now());
				break;
			}
			case GOOD_RECEIVED_NOTE: {
				stock.setReceivedDate(Instant.now());
				break;
			}
			default:
				log.debug("Do not mark receivedDate");

			}
			/*
			 * if (newQuantity.compareTo(new BigDecimal("0.00")) < 0) { throw new
			 * IllegalStateException( "Not enough stock for product " +
			 * product.getItemName() + " in warehouse " + warehouse.getWarehouseName()); }
			 */
			stock.setQuantity(newQuantity);
			warehouseStockRepository.save(stock);
		} catch (Exception e) {
			log.error("-------updateProductQuantity:{}", e);
		}
	}

	public List<WarehouseStock> findAll() {
		return warehouseStockRepository.findAll();
	}

	public List<WarehouseStock> getStockByProduct(UUID itemId) {
		return warehouseStockRepository.findByItemId(itemId);
	}

	public List<ProductStockSummaryDto> getStockSummary() {
		List<WarehouseStock> stocks = warehouseStockRepository.findAll();

		Map<CoreItem, BigDecimal> totalByProduct = stocks.stream()
				.collect(Collectors.groupingBy(WarehouseStock::getItem,
						Collectors.reducing(BigDecimal.ZERO, s -> s.getQuantity(), // getter returning BigDecimal
								BigDecimal::add)));

		return totalByProduct.entrySet().stream().map(e -> new ProductStockSummaryDto(e.getKey().getId(),
				e.getKey().getItemCode(), e.getKey().getItemName(), e.getValue())).toList();
	}

	public List<WarehouseStockDetailDto> getWarehouseStockDetails(String productId) {
		List<WarehouseStock> stocks = warehouseStockRepository.findByItemId(UUID.fromString(productId));
		return stocks.stream().map(s -> new WarehouseStockDetailDto(s.getWarehouse().getWarehouseName(),
				s.getQuantity(), s.getModifiedAt())).toList();
	}

	public List<ProductStockAgingDto> getStockSummaryWithAging() {
		List<WarehouseStock> stocks = warehouseStockRepository.findAll();

		// Group by product
		Map<CoreItem, List<WarehouseStock>> grouped = stocks.stream()
				.collect(Collectors.groupingBy(WarehouseStock::getItem));

		return grouped.entrySet().stream().map(entry -> {
			CoreItem item = entry.getKey();
			List<WarehouseStock> stockList = entry.getValue();

			// total quantity across all warehouses
			BigDecimal totalQty = stockList.stream().map(WarehouseStock::getQuantity).reduce(BigDecimal.ZERO,
					BigDecimal::add);

			// compute aging buckets
			Map<String, BigDecimal> agingBuckets = new HashMap<>();
			agingBuckets.put("0-30", BigDecimal.ZERO);
			agingBuckets.put("31-60", BigDecimal.ZERO);
			agingBuckets.put("61-90", BigDecimal.ZERO);
			agingBuckets.put("91-180", BigDecimal.ZERO);
			agingBuckets.put("181-365", BigDecimal.ZERO);
			agingBuckets.put("365+", BigDecimal.ZERO);

			for (WarehouseStock ws : stockList) {
				Instant today = Instant.now();
				Instant date = ws.getReceivedDate() != null ? ws.getReceivedDate() : Instant.now();
				long days = ChronoUnit.DAYS.between(date, today);
				BigDecimal qty = ws.getQuantity();

				if (days <= 30)
					agingBuckets.put("0-30", agingBuckets.get("0-30").add(qty));
				else if (days <= 60)
					agingBuckets.put("31-60", agingBuckets.get("31-60").add(qty));
				else if (days <= 90)
					agingBuckets.put("61-90", agingBuckets.get("61-90").add(qty));
				else if (days <= 180)
					agingBuckets.put("91-180", agingBuckets.get("91-180").add(qty));
				else if (days <= 365)
					agingBuckets.put("181-365", agingBuckets.get("181-365").add(qty));
				else
					agingBuckets.put("365+", agingBuckets.get("365+").add(qty));
			}

			ProductStockAgingDto dto = new ProductStockAgingDto();
			dto.setItemId(item.getId());
			dto.setProductCode(item.getItemCode());
			dto.setProductName(item.getItemName());
			dto.setTotalQuantity(totalQty);
			dto.setAgingBuckets(agingBuckets);
			return dto;
		}).toList();
	}

	public List<ProductStockValuationDto> getStockValuationSummary() {
		List<WarehouseStock> stocks = warehouseStockRepository.findAll();

		Map<CoreItem, BigDecimal> totalByProduct = stocks.stream().collect(Collectors.groupingBy(
				WarehouseStock::getItem, Collectors.reducing(BigDecimal.ZERO, s -> s.getQuantity(), BigDecimal::add)));
		return totalByProduct.entrySet().stream()
				.map(e -> new ProductStockValuationDto(e.getKey().getId(), e.getKey().getItemCode(),
						e.getKey().getItemName(), e.getValue(), e.getKey().getUnitCost(),
						e.getKey().getUnitCost().multiply(e.getValue())))
				.toList();
	}

	public List<ProductStockValuationDto> getStockValuationSummaryV2() {
		return warehouseStockRepository.findValuationSummary().stream()
				.map(p -> new ProductStockValuationDto(p.getItemId(), p.getItemCode(), p.getItemName(),
						p.getTotalQuantity(), p.getUnitCost(), p.getUnitCost().multiply(p.getTotalQuantity())))
				.toList();
	}

	public Page<ProductStockValuationDto> getItems(Pageable pageable, String name, String code) {

		return warehouseStockRepository.aggregateStockValuation(name, code, pageable);
	}

	public Page<ProductStockValuationDto> getItems(Pageable pageable) {
		return warehouseStockRepository.aggregateStockValuation(pageable);
	}

	public long countAll() {
		return warehouseStockRepository.countDistinctProducts();
	}

	public Page<InflowItemListView> findAllItemsWithWarehouseStock(UUID warehouseId, String searchValue,
			Pageable pageable) {

		return warehouseStockRepository.findAllItemsWithWarehouseStock(warehouseId, searchValue, searchValue,
				searchValue, searchValue, pageable);
	}

	@Transactional
	public void increase(UUID warehouseId, CoreItem item, BigDecimal qty) {
		WarehouseStock ws = warehouseStockRepository.findByWarehouseAndItemForUpdate(warehouseId, item.getId())
				.orElseGet(() -> WarehouseStock.builder().warehouse(Warehouse.builder().id(warehouseId).build())
						.item(item).quantity(BigDecimal.ZERO).build());
		ws.setQuantity(ws.getQuantity().add(qty));
		warehouseStockRepository.save(ws);
	}

	@Transactional
	public void decrease(UUID warehouseId, CoreItem item, BigDecimal qty) {
		WarehouseStock ws = warehouseStockRepository.findByWarehouseAndItemForUpdate(warehouseId, item.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No warehouse stock"));
		if (ws.getQuantity().compareTo(qty) < 0) {
			throw new ResourceNotFoundException("Insufficient warehouse stock");
		}
		ws.setQuantity(ws.getQuantity().subtract(qty));
		warehouseStockRepository.save(ws);
	}

	@Transactional(readOnly = true)
	public BigDecimal getQuantity(UUID warehouseId, UUID itemId) {
		return warehouseStockRepository.findByWarehouseIdAndItemId(warehouseId, itemId).map(WarehouseStock::getQuantity)
				.orElse(BigDecimal.ZERO);
	}

	@Transactional
	public void ensureRecord(UUID warehouseId, CoreItem item) {
		warehouseStockRepository.findByWarehouseIdAndItemId(warehouseId, item.getId()).orElseGet(() -> {
			WarehouseStock ws = WarehouseStock.builder().warehouse(Warehouse.builder().id(warehouseId).build())
					.item(item).quantity(BigDecimal.ZERO).build();
			return warehouseStockRepository.save(ws);
		});
	}

	@Transactional
	public void setQuantity(UUID warehouseId, CoreItem item, BigDecimal qty) {
		WarehouseStock ws = warehouseStockRepository.findByWarehouseAndItemForUpdate(warehouseId, item.getId())
				.orElseGet(() -> WarehouseStock.builder().warehouse(Warehouse.builder().id(warehouseId).build())
						.item(item).quantity(BigDecimal.ZERO).build());
		ws.setQuantity(qty);
		warehouseStockRepository.save(ws);
	}

	 

	public WarehouseStock save(WarehouseStock stock) {
		return warehouseStockRepository.save(stock);

	}

	 
}

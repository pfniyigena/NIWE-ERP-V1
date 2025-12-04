package com.niwe.erp.inventory.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.core.domain.CoreBranch;
import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.core.repository.CoreBranchRepository;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.repository.WarehouseRepository;
import com.niwe.erp.sale.domain.Shelf;
import com.niwe.erp.sale.repository.ShelfRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class WarehouseService {
	private final CoreBranchRepository coreBranchRepository;
	private final SequenceNumberService sequenceNumberService;
	private final ShelfRepository shelfRepository;
	private final CoreItemService coreItemService;
	private final WarehouseRepository warehouseRepository;

	public List<Warehouse> findAll() {
		return warehouseRepository.findAll();
	}

	public Warehouse findById(String id) {
		return warehouseRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id " + id));

	}

	public void initiateInventory(CoreTaxpayer taxpayer) {

		try {

			List<CoreBranch> branches = coreBranchRepository.findByTaxpayerId(taxpayer.getId());
			warehouseRepository.findByIsMain(true).isPresent();
			if (warehouseRepository.findByIsMain(true).isPresent()) {
				return;
			}

			Warehouse warehouse = warehouseRepository.save(
					Warehouse.builder().warehouseName("MAIN").internalCode(sequenceNumberService.getNextWarehouseCode())
							.branch(branches.get(0)).taxpayer(taxpayer).isMain(true).build());
			shelfRepository.save(Shelf.builder().internalCode(sequenceNumberService.getNextShelfCode())
					.warehouse(warehouse).name("MAIN POS").description("MAIN POS").build());

			coreItemService.initItems();

		} catch (Exception e) {
			log.error("InitiateInventory:{}", e);
		}

	}

	public Warehouse save(Warehouse warehouse) {
		Warehouse savedWarehouse = null;
		String code = warehouse.getInternalCode();
		if (code == null || code.isEmpty()) {
			code = sequenceNumberService.getNextWarehouseCode();
		}

		if (warehouse.getId() != null) {

			Warehouse exist = warehouseRepository.getReferenceById(warehouse.getId());
			exist.setInternalCode(code);
			exist.setWarehouseName(warehouse.getWarehouseName());
			exist.setBranch(warehouse.getBranch());
			exist.setTaxpayer(warehouse.getBranch().getTaxpayer());
			savedWarehouse = warehouseRepository.save(exist);
		} else {
			warehouse.setInternalCode(code);
			warehouse.setTaxpayer(warehouse.getBranch().getTaxpayer());
			savedWarehouse = warehouseRepository.save(warehouse);
		}

		return savedWarehouse;

	}

	public List<Warehouse> findByIdNotIn(List<UUID> ids) {
		return warehouseRepository.findByIdNotIn(ids);
	}

	public Warehouse findMain() {

		return warehouseRepository.findByIsMain(true)
				.orElseThrow(() -> new ResourceNotFoundException("Main Warehouse not found"));
	}

}

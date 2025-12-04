package com.niwe.erp.inventory.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.inventory.domain.Supplier;
import com.niwe.erp.inventory.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierService {
	private final SupplierRepository supplierRepository;
	private final SequenceNumberService sequenceNumberService;

	public Supplier save(Supplier supplier) {
		log.info("=============Supplier:{}", supplier);
		if (supplier.getId() != null || supplierRepository.findBySupplierTin(supplier.getSupplierTin()).isPresent()) {
			return update(supplier);
		} else {
			if (supplier.getInternalCode() == null || supplier.getInternalCode().isBlank())
				supplier.setInternalCode(sequenceNumberService.getNextSupplierCode());
			return supplierRepository.save(supplier);
		}

	}

	public Supplier update(Supplier supplier) {
		Supplier exist = supplierRepository.findById(supplier.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id " + supplier.getId()));
		exist.setSupplierTin(supplier.getSupplierTin());
		exist.setSupplierPhone(supplier.getSupplierPhone());
		exist.setSupplierName(supplier.getSupplierName());
		return supplierRepository.save(exist);

	}

	public List<Supplier> findAll() {

		return supplierRepository.findAll();
	}

	public Supplier findById(String id) {
		return supplierRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id " + id));
	}

}

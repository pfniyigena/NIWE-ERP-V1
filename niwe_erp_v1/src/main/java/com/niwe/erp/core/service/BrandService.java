package com.niwe.erp.core.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.core.domain.ItemBrand;
import com.niwe.erp.core.helper.ItemExcelHelper;
import com.niwe.erp.core.repository.ItemBrandRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class BrandService {
	private final SequenceNumberService sequenceNumberService;
	private final ItemBrandRepository itemBrandRepository;

	public List<ItemBrand> findAll() {
		return itemBrandRepository.findAll();
	}

	public ItemBrand findById(String id) {
		return itemBrandRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("Item Brand not found with id " + id));
	}
	public ItemBrand save(ItemBrand itemCategory) {
		ItemBrand savedTaxpayerBranch = null;
		String code = itemCategory.getInternalCode();
		if (code == null || code.isEmpty()) {
			code = sequenceNumberService.getNextItemBrandCode();
		}
		if (itemCategory.getId() != null) {
			savedTaxpayerBranch = itemBrandRepository.getReferenceById(itemCategory.getId());
			savedTaxpayerBranch.setInternalCode(code);
			savedTaxpayerBranch.setBrandName(itemCategory.getBrandName());
			savedTaxpayerBranch.setExternalId(itemCategory.getExternalId());
		} else {
			savedTaxpayerBranch = itemCategory;
			savedTaxpayerBranch.setInternalCode(code);
		}
		return itemBrandRepository.save(savedTaxpayerBranch);
	}
	@Transactional
	public void impotExcelFile(MultipartFile file) throws IOException {
		List<ItemBrand> brands = ItemExcelHelper.excelToBrands(file.getInputStream());
		brands.forEach((n) -> {
			n.setInternalCode(sequenceNumberService.getNextItemBrandCode());
			itemBrandRepository.save(n);
		});
	}

}

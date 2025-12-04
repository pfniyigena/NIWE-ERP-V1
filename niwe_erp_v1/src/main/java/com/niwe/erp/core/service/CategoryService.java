package com.niwe.erp.core.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.core.domain.ItemCategory;
import com.niwe.erp.core.helper.ItemExcelHelper;
import com.niwe.erp.core.repository.ItemCategoryRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryService {
	private final SequenceNumberService sequenceNumberService;
	private final ItemCategoryRepository itemCategoryRepository;

	public List<ItemCategory> findAll() {
		return itemCategoryRepository.findAll();
	}

	public ItemCategory findById(String id) {
		return itemCategoryRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("Item Category not found with id " + id));
	}

	public ItemCategory save(ItemCategory itemCategory) {
		ItemCategory savedTaxpayerBranch = null;
		String code = itemCategory.getInternalCode();
		if (code == null || code.isEmpty()) {
			code = sequenceNumberService.getNextItemCategoryCode();
		}

		if (itemCategory.getId() != null) {

			savedTaxpayerBranch = itemCategoryRepository.getReferenceById(itemCategory.getId());
			savedTaxpayerBranch.setInternalCode(code);
			savedTaxpayerBranch.setCategoryName(itemCategory.getCategoryName());
			savedTaxpayerBranch.setExternalId(itemCategory.getExternalId());
		} else {

			savedTaxpayerBranch = itemCategory;
			savedTaxpayerBranch.setInternalCode(code);

		}

		return itemCategoryRepository.save(savedTaxpayerBranch);

	}

	@Transactional
	public void impotExcelFile(MultipartFile file) throws IOException {
		List<ItemCategory> brands = ItemExcelHelper.excelToItemCategories(file.getInputStream());
		brands.forEach((n) -> {
			n.setInternalCode(sequenceNumberService.getNextItemCategoryCode());
			itemCategoryRepository.save(n);
		});
	}

}

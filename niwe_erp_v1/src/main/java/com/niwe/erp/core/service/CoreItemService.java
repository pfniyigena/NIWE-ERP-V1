package com.niwe.erp.core.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.core.domain.CoreCountry;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.domain.CoreItemClassification;
import com.niwe.erp.core.domain.CoreItemNature;
import com.niwe.erp.core.domain.CoreQuantityUnit;
import com.niwe.erp.core.domain.EItemNature;
import com.niwe.erp.core.dto.CoreItemListDTO;
import com.niwe.erp.core.form.CoreItemForm;
import com.niwe.erp.core.helper.ItemExcelHelper;
import com.niwe.erp.core.repository.CoreCountryRepository;
import com.niwe.erp.core.repository.CoreItemClassificationRepository;
import com.niwe.erp.core.repository.CoreItemRepository;
import com.niwe.erp.core.repository.CoreQuantityUnitRepository;
import com.niwe.erp.core.view.CoreItemListView;
import com.niwe.erp.core.web.util.NiweErpCoreDefaultParameter;
import com.niwe.erp.inventory.domain.MovementType;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.repository.WarehouseRepository;
import com.niwe.erp.inventory.service.StockMovementService;
import com.niwe.erp.invoicing.domain.TaxType;
import com.niwe.erp.invoicing.repository.TaxTypeRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreItemService {
	private final CoreUserService coreUserService;
	private final CoreItemRepository coreItemRepository;
	private final SequenceNumberService sequenceNumberService;
	private final CoreItemNatureService coreItemNatureService;
	private final CoreItemClassificationRepository coreItemClassificationRepository;
	private final CoreQuantityUnitRepository coreQuantityUnitRepository;
	private final CoreCountryRepository coreCountryRepository;
	private final TaxTypeRepository taxTypeRepository;
	private final WarehouseRepository warehouseRepository;
	private final StockMovementService stockMovementService;

	public List<CoreItemForm> findAllAsForm() {
		return coreItemRepository.findAllAsForm();
	}

	public Page<CoreItemForm> findAllAsForm(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("itemName").ascending());
		return coreItemRepository.findAllAsForm(pageable);
	}

	public Page<CoreItemListDTO> findAllAsDto(Pageable pageable) {
		return coreItemRepository.findAllAsDto(pageable);
	}

	public List<CoreItemForm> findAllAsFormByItemNameContainingIgnoreCase(String itemName) {
		return coreItemRepository
				.findAllAsFormByItemNameContainingIgnoreCaseOrItemCodeContainingIgnoreCaseOrBarcodeContainingIgnoreCase(
						itemName, itemName, itemName, itemName);
	}

	public CoreItem saveNew(CoreItem coreItem) {
		String code = sequenceNumberService.getNextItemCode();
		coreItem.setInternalCode(code);
		if (coreItem.getBarcode() == null || coreItem.getBarcode().isEmpty())
			coreItem.setBarcode(code);
		if (coreItem.getExternalItemCode() == null || coreItem.getExternalItemCode().isEmpty())
			coreItem.setExternalItemCode(code);
		if (coreItem.getItemCode() == null || coreItem.getItemCode().isEmpty())
			coreItem.setItemCode(code);
		return coreItemRepository.save(coreItem);

	}

	public List<CoreItem> findAll() {

		return coreItemRepository.findAll();
	}

	public Page<CoreItem> findAll(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("itemName").ascending());
		return coreItemRepository.findAll(pageable);
	}

	public CoreItem save(CoreItem item) {
		if (item.getInternalCode() == null || item.getInternalCode().isEmpty()) {
			item.setInternalCode(sequenceNumberService.getNextItemCode());
		}
		CoreItem saved = null;
		if (item.getId() != null) {
			saved = coreItemRepository.getReferenceById(item.getId());
			saved.setItemName(item.getItemName());
			saved.setBarcode(item.getBarcode());
			saved.setExternalItemCode(item.getExternalItemCode());
			saved.setItemCode(item.getItemCode());
			saved.setUnitPrice(item.getUnitPrice());
			saved.setUnitCost(item.getUnitCost());
			saved.setCountry(item.getCountry());
			saved.setClassification(item.getClassification());
			saved.setBrand(item.getBrand());
			saved.setCategory(item.getCategory());
			saved.setUnit(item.getUnit());
			saved.setNature(item.getNature());
			saved.setTax(item.getTax());
			saved.setTaxpayer(item.getTaxpayer());
			saved.setInternalCode(item.getInternalCode());
			saved.setStockLevel(item.getStockLevel());
			saved.setTaxpayer(coreUserService.getCurrentUserEntity().getTaxpayer());
		} else {
			item.setTaxpayer(coreUserService.getCurrentUserEntity().getTaxpayer());
			saved = item;

		}
		coreItemRepository.save(saved);
		createWarehouseInventory(saved);
		return saved;

	}

	private void createWarehouseInventory(CoreItem saved) {

		for (Warehouse warehouse : warehouseRepository.findAll()) {
			stockMovementService.logReceive(warehouse, saved, saved.getQuanityInitial(), saved.getInternalCode(),
					MovementType.STOCK_INITIAL);
		}

	}

	public CoreItem findById(String id) {
		return coreItemRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("Item not found with id " + id));

	}

	public CoreItem findByInternalCode(String internalCode) {
		return coreItemRepository.findByInternalCode(internalCode).orElseThrow(
				() -> new ResourceNotFoundException("Product not found with internalCode: " + internalCode));

	}

	public void initItems() {

		if (coreItemNatureService.findAll().isEmpty()) {
			List<CoreItemNature> natures = new ArrayList<>();
			for (EItemNature nature : EItemNature.values()) {
				Boolean isDefault = Boolean.FALSE;
				if (nature.equals(EItemNature.ITEM_NATURE_GOOD)) {
					isDefault = Boolean.TRUE;
				}
				natures.add(
						CoreItemNature.builder().code(nature.name()).name(nature.name()).isDefault(isDefault).build());

			}
			coreItemNatureService.saveAll(natures);
		}

		if (coreItemClassificationRepository.findAll().isEmpty()) {
			coreItemClassificationRepository.save(CoreItemClassification.builder()
					.code(NiweErpCoreDefaultParameter.CLASSIFICAION_COE)
					.category(NiweErpCoreDefaultParameter.CLASSIFICAION_CATEGORY)
					.description(NiweErpCoreDefaultParameter.CLASSIFICAION_NAME)
					.displayName(NiweErpCoreDefaultParameter.CLASSIFICAION_NAME)
					.englishName(NiweErpCoreDefaultParameter.CLASSIFICAION_NAME)
					.frenchName(NiweErpCoreDefaultParameter.CLASSIFICAION_NAME).isDefault(Boolean.TRUE).build());

		}
		if (coreQuantityUnitRepository.findAll().isEmpty()) {
			coreQuantityUnitRepository.save(CoreQuantityUnit.builder().code(NiweErpCoreDefaultParameter.PACKAGING_CODE)
					.name(NiweErpCoreDefaultParameter.PACKAGING_NAME).isDefault(Boolean.TRUE).build());
		}

	}

	@Transactional
	public void impotExcelFile(MultipartFile file) {
		try {
			Warehouse defaultWarehouse = warehouseRepository.findByIsMain(true)
					.orElseThrow(() -> new ResourceNotFoundException("Main Warehouse not found"));
			List<CoreItem> products = ItemExcelHelper.excelToProducts(file.getInputStream());
			CoreCountry country = coreCountryRepository.findByIsDefault(Boolean.TRUE).get(0);
			CoreItemClassification classification = coreItemClassificationRepository.findByIsDefault(Boolean.TRUE)
					.get(0);
			CoreQuantityUnit packaging = coreQuantityUnitRepository.findByIsDefault(Boolean.TRUE).get(0);
			CoreItemNature nature = coreItemNatureService.findByIsDefault(Boolean.TRUE).get(0);
			TaxType tax = taxTypeRepository.findByIsDefault(Boolean.TRUE).get(0);

			List<CoreItem> enrichedProducts = products.stream().peek(p -> {

				String code = p.getInternalCode();
				if (code == null || code.isEmpty()) {
					code = sequenceNumberService.getNextItemCode();
				}
				p.setInternalCode(code);
				if (p.getItemCode() == null || p.getItemCode().isEmpty())
					p.setItemCode(code);
				if (p.getBarcode() == null || p.getBarcode().isEmpty())
					p.setBarcode(code);
				if (p.getExternalItemCode() == null || p.getExternalItemCode().isEmpty())
					p.setExternalItemCode(code);
				if (p.getUnitPrice() == null)
					p.setUnitPrice(new BigDecimal("0.00"));
				if (p.getUnitCost() == null)
					p.setUnitCost(new BigDecimal("0.00"));
				p.setCountry(country);
				p.setClassification(classification);
				p.setNature(nature);
				p.setUnit(packaging);
				p.setTax(tax);
				p.setTaxpayer(coreUserService.getCurrentUserEntity().getTaxpayer());
			}).toList();
			coreItemRepository.saveAll(enrichedProducts);
			logMovement(defaultWarehouse, enrichedProducts);
		} catch (Exception e) {
			throw new RuntimeException("Could not store Excel data: " + e.getMessage(), e);
		}
	}

	private void logMovement(Warehouse defaultWarehouse, List<CoreItem> enrichedProducts) {
		enrichedProducts.forEach((n) -> {
			stockMovementService.logReceive(defaultWarehouse, n, n.getQuanityInitial(), n.getInternalCode(),
					MovementType.STOCK_INITIAL);

		});

	}

	public CoreItem duplicate(String id) {

		CoreItem original = findById(id);

		CoreItem copy = new CoreItem(original);
		log.info("original:{} and Copy:{}", original, copy);

		return saveNew(copy);
	}

	public Page<CoreItem> listItems(String searchTerm, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("itemName").ascending());

		if (searchTerm == null || searchTerm.isBlank()) {
			return coreItemRepository.findAll(pageable);
		}
		return coreItemRepository.searchItems(searchTerm, pageable);
	}

	public void updateUnitPriceOrUnitCost(String id, String type, BigDecimal value) {
		CoreItem item = findById(id);
		if ("sale".equalsIgnoreCase(type)) {
			item.setUnitPrice(value);
		} else if ("cost".equalsIgnoreCase(type)) {
			item.setUnitCost(value);
		}
		coreItemRepository.save(item);

	}

	public Page<CoreItemListView> getItems(int page, int size, String sortBy, String sortDir, String name,
			String code) {

		Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);

		return coreItemRepository.findAllFiltered(name, code, pageable);
	}

	public Page<CoreItemListView> getItems(Pageable pageable, String name, String code) {

		return coreItemRepository.findAllFiltered(name, code, pageable);
	}

	public Page<CoreItemListDTO> findByLastUpdatedAfter(LocalDateTime lastSyn, Pageable pageable) {
		return coreItemRepository.findByLastUpdatedAfter(lastSyn, pageable);
	}

	@Transactional
	public void updates(List<CoreItemListDTO> list) {

		List<CoreItem> enrichedProducts = list.stream().map(p -> {
			CoreItem item = findByInternalCode(p.internalCode());
			item.setLastUpdated(LocalDateTime.now());
			return item;

		}).toList();

		coreItemRepository.saveAll(enrichedProducts);

	}

	public long countAll() {
		return coreItemRepository.count();
	}
}

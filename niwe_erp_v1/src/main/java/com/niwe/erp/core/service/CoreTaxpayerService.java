package com.niwe.erp.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.common.util.NiweErpCommonConstants;
import com.niwe.erp.core.domain.CoreBranch;
import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.core.repository.CoreBranchRepository;
import com.niwe.erp.core.repository.CoreTaxpayerRepository;
import com.niwe.erp.inventory.service.WarehouseService;
import com.niwe.erp.invoicing.service.TaxTypeService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreTaxpayerService {

	private final CoreTaxpayerRepository coreTaxpayerRepository;
	private final SequenceNumberService sequenceNumberService;
	private final CoreBranchRepository coreBranchRepository;
	private final TaxTypeService taxTypeService;
	private final WarehouseService warehouseService;

	public CoreTaxpayer initTaxpayer() {
		CoreTaxpayer coreTaxpayer = null;
		if (coreTaxpayerRepository.findAll().size() < 1) {

			coreTaxpayer = save(CoreTaxpayer.builder()

					.tinNumber(NiweErpCommonConstants.NIKA_DEFAULT_TIN_NUMBER)
					.taxpayerName(NiweErpCommonConstants.NIKA_DEFAULT_TAXPAYER_NAME)
					.taxpayerPhoneNumber(NiweErpCommonConstants.NIKA_DEFAULT_TAXPAYER_PHONE_NUMBER)
					.taxpayerEmail(NiweErpCommonConstants.NIKA_DEFAULT_TAXPAYER_EMAIL)
					.taxpayerAddress(NiweErpCommonConstants.NIKA_DEFAULT_TAXPAYER_ADDRESS)
					.allowedToExport(NiweErpCommonConstants.NIKA_DEFAULT_ALLOWED_TO_EXPORT)
					.communalTaxRegistered(NiweErpCommonConstants.NIKA_DEFAULT_COMMUNAL_TAX_REGISTERED)
					.deboursRegistered(NiweErpCommonConstants.NIKA_DEFAULT_DEBOURS_REGISTERED)
					.vatRegistered(NiweErpCommonConstants.NIKA_DEFAULT_VAT_REGISTERED).build());

			coreBranchRepository.save(CoreBranch.builder().taxpayer(coreTaxpayer)
					.internalCode(sequenceNumberService.getNextTaxpayerBranchCode())
					.branchName(NiweErpCommonConstants.NIKA_DEFAULT_TAXPAYER_BRANCH_NAME).build());

			
		}else {
			coreTaxpayer=coreTaxpayerRepository.findAll().get(0);
		}
		return coreTaxpayer;
	}

	public CoreTaxpayer save(CoreTaxpayer taxpayer) {
		CoreTaxpayer savedTaxpayer = null;
		String code = taxpayer.getInternalCode();
		if (code == null || code.isEmpty()) {
			code = sequenceNumberService.getNextTaxpayerCode();
		}

		if (taxpayer.getId() != null) {
			CoreTaxpayer exist = coreTaxpayerRepository.getReferenceById(taxpayer.getId());
			exist.setTaxpayerName(taxpayer.getTaxpayerName());
			exist.setTinNumber(taxpayer.getTinNumber());
			exist.setTaxpayerEmail(taxpayer.getTaxpayerEmail());
			exist.setTaxpayerPhoneNumber(taxpayer.getTaxpayerPhoneNumber());
			exist.setTaxpayerAddress(taxpayer.getTaxpayerAddress());
			exist.setFiscalYear(taxpayer.getFiscalYear());
			exist.setInternalCode(code);
			exist.setUseInventory(taxpayer.isUseInventory());
			savedTaxpayer = coreTaxpayerRepository.save(exist);

		} else {
			taxpayer.setInternalCode(code);
			savedTaxpayer = coreTaxpayerRepository.save(taxpayer);
		}
		taxTypeService.initializeTaxes(savedTaxpayer.getFiscalYear());
		if (savedTaxpayer.isUseInventory()) {
			warehouseService.initiateInventory(savedTaxpayer);
		}
		return taxpayer;

	}

	public List<CoreTaxpayer> findAll() {

		return coreTaxpayerRepository.findAll();

	}

	public CoreTaxpayer findById(String id) {
		return coreTaxpayerRepository.getReferenceById(UUID.fromString(id));
	}

}

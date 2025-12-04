package com.niwe.erp.invoicing.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.invoicing.domain.ChargeType;
import com.niwe.erp.invoicing.repository.ChargeTypeRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ChargeTypeService {

	private ChargeTypeRepository chargeTypeRepository;
	private final SequenceNumberService sequenceNumberService;

	public List<ChargeType> findAll() {
		return chargeTypeRepository.findAll();
	}

	public ChargeType save(ChargeType chargeType) {

		String code = chargeType.getChargeCode();
		if (code == null || code.isEmpty()) {
			code = sequenceNumberService.getNextChargeCode();
		}

		if (chargeType.getId() != null) {
			ChargeType exist = chargeTypeRepository.getReferenceById(chargeType.getId());
			exist.setChargeName(chargeType.getChargeName());
			exist.setChargeCode(code);
			exist.setChargeValue(chargeType.getChargeValue());
			exist.setDescription(chargeType.getDescription());
			exist.setDisplayName(chargeType.getDisplayName());
			return chargeTypeRepository.save(exist);
		}
		chargeType.setChargeCode(code);
		return chargeTypeRepository.save(chargeType);

	}

	public ChargeType findById(String id) {

		return chargeTypeRepository.getReferenceById(UUID.fromString(id));
	}

	public ChargeType findByCode(String chargeCode) {

		return chargeTypeRepository.findByChargeCode(chargeCode);
	}

}

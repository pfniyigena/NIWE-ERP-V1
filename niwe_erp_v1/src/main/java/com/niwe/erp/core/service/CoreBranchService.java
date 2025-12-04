package com.niwe.erp.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.core.domain.CoreBranch;
import com.niwe.erp.core.repository.CoreBranchRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreBranchService {

	private final CoreBranchRepository coreBranchRepository;
	private final SequenceNumberService sequenceNumberService;

	public CoreBranch save(CoreBranch coreBranch) {
		CoreBranch savedTaxpayerBranch = null;
		String code = coreBranch.getInternalCode();
		if (code == null || code.isEmpty()) {
			code = sequenceNumberService.getNextTaxpayerBranchCode();
		}

		if (coreBranch.getId() != null) {

			CoreBranch exist = coreBranchRepository.getReferenceById(coreBranch.getId());
			exist.setBranchAddress(coreBranch.getBranchAddress());
			exist.setInternalCode(code);
			exist.setBranchName(coreBranch.getBranchName());
			exist.setTaxpayer(coreBranch.getTaxpayer());
			savedTaxpayerBranch=coreBranchRepository.save(exist);
		} else {
			coreBranch.setInternalCode(code);
			savedTaxpayerBranch = coreBranchRepository.save(coreBranch);
		}

		return savedTaxpayerBranch;

	}

	public List<CoreBranch> findAll() {

		return coreBranchRepository.findAll();
	}

	public CoreBranch findById(String id) {

		return coreBranchRepository.getReferenceById(UUID.fromString(id));
	}
}

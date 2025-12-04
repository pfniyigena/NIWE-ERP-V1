package com.niwe.erp.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.core.domain.CoreQuantityUnit;
import com.niwe.erp.core.repository.CoreQuantityUnitRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreQuantityUnitService {
	
	private final CoreQuantityUnitRepository coreQuantityUnitRepository;

	public List<CoreQuantityUnit> findAll() {
		return coreQuantityUnitRepository.findAll();
	}

	public CoreQuantityUnit  findById(String id) {
		return coreQuantityUnitRepository.getReferenceById(UUID.fromString(id));
	}

	public CoreQuantityUnit save(CoreQuantityUnit coreQuantityUnit) {
		if(coreQuantityUnit.getId()!=null) {
			CoreQuantityUnit  exist=coreQuantityUnitRepository.getReferenceById(coreQuantityUnit.getId());
			exist.setCode(coreQuantityUnit.getCode());
			exist.setName(coreQuantityUnit.getName());
			return coreQuantityUnitRepository.save(exist);
		}
		
		return coreQuantityUnitRepository.save(coreQuantityUnit);
		
	}

}

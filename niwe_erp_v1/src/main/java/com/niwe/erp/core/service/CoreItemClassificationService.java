package com.niwe.erp.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.core.domain.CoreItemClassification;
import com.niwe.erp.core.repository.CoreItemClassificationRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreItemClassificationService {
	
	private final CoreItemClassificationRepository coreItemClassificationRepository;
	
	/**
	 * @return
	 */
	public List<CoreItemClassification>  findAll() {
		return coreItemClassificationRepository.findAll();
		
	}

	/**
	 * @param fromString
	 * @return
	 */
	public CoreItemClassification findById(UUID id) {
		 
		return coreItemClassificationRepository.getReferenceById(id);
	}
	public CoreItemClassification findByStringId(String fromString) {
		 
		return coreItemClassificationRepository.getReferenceById(UUID.fromString(fromString));
	}

	/**
	 * @param classification
	 * @return
	 */
	public CoreItemClassification save(CoreItemClassification classification) {
		
		if(classification.getId()!=null) {
			CoreItemClassification  exist=coreItemClassificationRepository.getReferenceById(classification.getId());
			exist.setCategory(classification.getCategory());
			exist.setCode(classification.getCode());
			exist.setDescription(classification.getDescription());
			exist.setParent(classification.getParent());
			exist.setDisplayName(classification.getDisplayName());
			exist.setEnglishName(classification.getEnglishName());
			exist.setFrenchName(classification.getFrenchName());
			return coreItemClassificationRepository.save(exist);
		}
		return coreItemClassificationRepository.save(classification);
		
	}
	

}

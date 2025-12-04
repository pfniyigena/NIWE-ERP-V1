package com.niwe.erp.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.core.domain.CoreCountry;
import com.niwe.erp.core.repository.CoreCountryRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreCountryService {
	
	private final CoreCountryRepository coreCountryRepository;

	public List<CoreCountry> findAll() {
		return coreCountryRepository.findAll();
	}

	public CoreCountry findById(String id) {
		return coreCountryRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("Country not found with id " + id));
	}
	public CoreCountry findById(UUID id) {
		 
		return coreCountryRepository.getReferenceById(id);
	}
	public CoreCountry save(CoreCountry country) {
		if(country.getId()!=null) {
			CoreCountry  exist=coreCountryRepository.getReferenceById(country.getId());
			exist.setCode(country.getCode());
			exist.setDisplayName(country.getDisplayName());
			exist.setEnglishName(country.getEnglishName());
			exist.setFrenchName(country.getFrenchName());
			return coreCountryRepository.save(exist);
		}
		
		return coreCountryRepository.save(country);
		
	}

}

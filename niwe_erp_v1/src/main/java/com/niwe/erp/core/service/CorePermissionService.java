package com.niwe.erp.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.core.domain.CorePermission;
import com.niwe.erp.core.repository.CorePermissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CorePermissionService {
	private final CorePermissionRepository corePermissionRepository;

	public CorePermission save(CorePermission corePermission) {
		CorePermission newPermission = null;
		if (corePermission.getId() != null) {
			newPermission = corePermissionRepository.getReferenceById(corePermission.getId());
			newPermission.setName(corePermission.getName());
			newPermission.setDescription(corePermission.getDescription());

		} else {

			newPermission = corePermission;

		}
		return corePermissionRepository.save(newPermission);

	}

	public CorePermission findById(String id) {
		return corePermissionRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("Permission not found with id " + id));
	}

	public List<CorePermission> findAll() {

		return corePermissionRepository.findAll();
	}

}

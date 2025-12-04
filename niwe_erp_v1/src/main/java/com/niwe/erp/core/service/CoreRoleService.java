package com.niwe.erp.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.core.domain.CoreRole;
import com.niwe.erp.core.repository.CoreRoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoreRoleService {

	private final CoreRoleRepository coreRoleRepository;

	public CoreRole findById(String id) {

		return coreRoleRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + id));
	}

	public List<CoreRole> findAll() {
		return coreRoleRepository.findAll();
	}

	public CoreRole save(CoreRole coreRole) {
		
		CoreRole newRole = null;
		if (coreRole.getId() != null) {
			newRole = coreRoleRepository.getReferenceById(coreRole.getId());
			newRole.setName(coreRole.getName());
			newRole.setDescription(coreRole.getDescription());
			newRole.setPermissions(coreRole.getPermissions());

		} else {

			newRole = coreRole;

		}
		return coreRoleRepository.save(newRole);

	}

}

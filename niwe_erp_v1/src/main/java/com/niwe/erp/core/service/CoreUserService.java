package com.niwe.erp.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.core.domain.CoreUser;
import com.niwe.erp.core.repository.CorePermissionRepository;
import com.niwe.erp.core.repository.CoreRoleRepository;
import com.niwe.erp.core.repository.CoreUserRepository;
import com.niwe.erp.security.config.AuthenticationFacade;

@Service
public class CoreUserService {
	private final CoreUserRepository coreUserRepository;
	private final AuthenticationFacade auth;

	public CoreUserService(CorePermissionRepository corePermissionRepository, CoreUserRepository coreUserRepository,
			CoreRoleRepository coreRoleRepository, PasswordEncoder passwordEncoder, AuthenticationFacade auth) {
		this.coreUserRepository = coreUserRepository;
		this.auth = auth;
	}

	public Page<CoreUser> listUsers(Pageable p) {
		return coreUserRepository.findAll(p);
	}

	public CoreUser findById(String id) {

		return coreUserRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
	}

	public CoreUser getCurrentUserEntity() {
		String username = auth.getUsername();
		return username == null ? null : coreUserRepository.findByUsername(username).orElse(null);
	}

	public List<CoreUser> findAll() {
		return coreUserRepository.findAll();
	}

	public CoreUser save(CoreUser user) {
		CoreUser newUser = null;
		if (user.getId() != null) {
			newUser = coreUserRepository.getReferenceById(user.getId());
			newUser.setFullname(user.getFullname());
			newUser.setUsername(user.getUsername());
			newUser.setEnabled(user.isEnabled());
			newUser.setRole(user.getRole());

		} else {

			newUser = user;
			newUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
			newUser.setTaxpayer(getCurrentUserEntity().getTaxpayer());

		}
		return coreUserRepository.save(newUser);
	}

	public void changePassword(String userId, String newPassword) {
		CoreUser user = findById(userId);
		user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
		coreUserRepository.save(user);

	}

}

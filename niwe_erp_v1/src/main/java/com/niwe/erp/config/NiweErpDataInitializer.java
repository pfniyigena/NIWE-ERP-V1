package com.niwe.erp.config;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.niwe.erp.core.domain.CorePermission;
import com.niwe.erp.core.domain.CoreRole;
import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.core.domain.CoreUser;
import com.niwe.erp.core.repository.CorePermissionRepository;
import com.niwe.erp.core.repository.CoreRoleRepository;
import com.niwe.erp.core.repository.CoreUserRepository;
import com.niwe.erp.core.service.CoreTaxpayerService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class NiweErpDataInitializer implements ApplicationRunner {

	private final EntityManager entityManager;
	private final CorePermissionRepository permissionRepository;
	private final CoreRoleRepository roleRepository;
	private final CoreUserRepository coreUserRepository;
	private final CoreTaxpayerService coreTaxpayerService;

	private static final List<String> CRUD_ACTIONS = List.of("CREATE", "READ", "UPDATE", "DELETE");
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	private static final String ROLE_USER = "ROLE_USER";

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		CoreTaxpayer coreTaxpayer = coreTaxpayerService.initTaxpayer();
		// 1) discover entity names from JPA metamodel
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

		// Normalize entity names (simple names)
		Set<String> entityNames = entities.stream().map(et -> et.getJavaType().getSimpleName()).filter(Objects::nonNull)
				.collect(Collectors.toSet());

		// 2) build expected permission names
		Set<String> expectedPermissionNames = entityNames.stream()
				.flatMap(name -> CRUD_ACTIONS.stream().map(action -> name.toUpperCase() + "_" + action))
				.collect(Collectors.toSet());

		// 3) create missing permissions in DB
		// Load existing ones to avoid duplicates
		List<CorePermission> existing = permissionRepository.findAllByNameIn(expectedPermissionNames);
		Set<String> existingNames = existing.stream().map(CorePermission::getName).collect(Collectors.toSet());

		List<CorePermission> toCreate = expectedPermissionNames.stream().filter(name -> !existingNames.contains(name))
				.map(name -> new CorePermission(name, "Auto-generated permission: " + name)).toList();

		if (!toCreate.isEmpty()) {
			permissionRepository.saveAll(toCreate);
		}

		// reload all relevant permissions
		List<CorePermission> allPermissions = permissionRepository.findAllByNameIn(expectedPermissionNames);
		Map<String, CorePermission> permissionMap = allPermissions.stream()
				.collect(Collectors.toMap(CorePermission::getName, Function.identity()));

		// ADMIN -> all permissions
		Set<CorePermission> adminPerms = new HashSet<>(permissionMap.values());
		// USER -> only READ permissions
		Set<CorePermission> userPerms = permissionMap.keySet().stream().filter(n -> n.endsWith("_READ"))
				.map(permissionMap::get).collect(Collectors.toSet());
		// 4) ensure default roles exist
		CoreRole admin = roleRepository.findByName(ROLE_ADMIN).orElseGet(() -> roleRepository
				.save(CoreRole.builder().name(ROLE_ADMIN).description(ROLE_ADMIN).permissions(adminPerms).build()));
		CoreRole user = roleRepository.findByName(ROLE_USER).orElseGet(() -> roleRepository
				.save(CoreRole.builder().name(ROLE_USER).description(ROLE_USER).permissions(userPerms).build()));

		if (coreUserRepository.findByUsername("admin").isEmpty()) {
			CoreUser u = new CoreUser();
			u.setUsername("admin");
			u.setEmail("pfniyigena@gmail.com");
			u.setPassword(new BCryptPasswordEncoder().encode("admin"));
			u.setFullname("System Administrator");
			u.setEnabled(true);
			u.setTaxpayer(coreTaxpayer);
			roleRepository.findByName("ROLE_ADMIN").ifPresent(r -> u.setRole(r));
			coreUserRepository.save(u);
		}
		if (coreUserRepository.findByUsername("super").isEmpty()) {
			CoreUser u = new CoreUser();
			u.setUsername("super");
			u.setEmail("niwe2026@gmail.com");
			u.setPassword(new BCryptPasswordEncoder().encode("supper"));
			u.setFullname("Super Administrator");
			u.setEnabled(true);
			u.setTaxpayer(coreTaxpayer);
			roleRepository.findByName("ROLE_ADMIN").ifPresent(r -> u.setRole(r));
			coreUserRepository.save(u);
		}
		if (coreUserRepository.findByUsername("user").isEmpty()) {
			CoreUser u = new CoreUser();
			u.setUsername("user");
			u.setEmail("mangatek2020@gmail.com");
			u.setPassword(new BCryptPasswordEncoder().encode("user"));
			u.setFullname("System User");
			u.setEnabled(true);
			u.setTaxpayer(coreTaxpayer);
			roleRepository.findByName("ROLE_USER").ifPresent(r -> u.setRole(r));
			coreUserRepository.save(u);
		}
		// Log summary
		log.info("Seeded {} permissions.", permissionMap.size());
		log.info("Role {} has {} permissions.", ROLE_ADMIN, admin.getPermissions().size());
		log.info("Role {} has {} permissions.", ROLE_USER, user.getPermissions().size());

	}
}

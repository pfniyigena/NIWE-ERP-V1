package com.niwe.erp.core.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.core.domain.CoreRole;
import com.niwe.erp.core.repository.CorePermissionRepository;
import com.niwe.erp.core.service.CoreRoleService;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.ROLES_URL)
@AllArgsConstructor
public class RoleController {
	private final CoreRoleService coreRoleService;
	private final CorePermissionRepository corePermissionRepository;

	@GetMapping(path = "/list")
	public String listPermissions(Model model) {
		model.addAttribute("lists", coreRoleService.findAll());
		return NiweErpCoreUrlConstants.ROLES_LIST_PAGE;

	}

	@GetMapping(path = "/new")
	public String newRole(Model model) {
		setData(model);
		model.addAttribute("coreRole", CoreRole.builder().name("ROLE_").build());
		return NiweErpCoreUrlConstants.ROLES_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveRoles(@Valid CoreRole coreRole, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		log.debug(String.format("------calling saveRoles:{%s}", coreRole.getPermissions().size()));
		if (bindingResult.hasErrors()) {
			model.addAttribute("coreRole", coreRole);
			setData(model);
			return NiweErpCoreUrlConstants.ROLES_ADD_FORM_PAGE;
		}
		coreRoleService.save(coreRole);
		redirectAttributes.addFlashAttribute("success", "Success.");
		return NiweErpCoreUrlConstants.ROLES_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		CoreRole coreRole = coreRoleService.findById(id);
		model.addAttribute("coreRole", coreRole);
		setData(model);
		return NiweErpCoreUrlConstants.ROLES_ADD_FORM_PAGE;
	}

	private void setData(Model model) {
		model.addAttribute("permissions", corePermissionRepository.findAll());

	}

}

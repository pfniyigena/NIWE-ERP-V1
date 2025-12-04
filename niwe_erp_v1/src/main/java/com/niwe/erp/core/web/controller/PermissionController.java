package com.niwe.erp.core.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.core.domain.CorePermission;
import com.niwe.erp.core.service.CorePermissionService;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.PERMISSIONS_URL)
@AllArgsConstructor
public class PermissionController {
	private final CorePermissionService corePermissionService;

    @GetMapping(path = "/list")
    public String listPermissions(Model model) {
        model.addAttribute("lists", corePermissionService.findAll());
        return NiweErpCoreUrlConstants.PERMISSIONS_LIST_PAGE; 
         
    }
    @GetMapping(path = "/new")
	public String newPermission(Model model) {
		model.addAttribute("corePermission", CorePermission.builder().name("ROLE_").build());
		return NiweErpCoreUrlConstants.PERMISSIONS_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String savePermission(@Valid CorePermission corePermission, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("corePermission", corePermission);
			return NiweErpCoreUrlConstants.PERMISSIONS_ADD_FORM_PAGE;
		}
		corePermissionService.save(corePermission);
		redirectAttributes.addFlashAttribute("success", "Success.");
		return NiweErpCoreUrlConstants.PERMISSIONS_LIST_REDITECT_URL;
	}
	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		CorePermission corePermission = corePermissionService.findById(id);
		model.addAttribute("corePermission", corePermission);
		return NiweErpCoreUrlConstants.PERMISSIONS_ADD_FORM_PAGE;
	}
}

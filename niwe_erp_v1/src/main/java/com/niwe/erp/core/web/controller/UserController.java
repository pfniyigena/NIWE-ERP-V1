package com.niwe.erp.core.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.core.domain.CoreUser;
import com.niwe.erp.core.repository.CoreRoleRepository;
import com.niwe.erp.core.service.CoreUserService;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.USERS_URL)
@AllArgsConstructor
public class UserController {
	private final CoreUserService coreUserService;
	private final CoreRoleRepository coreRoleRepository;

	@GetMapping(path = "/list")
	public String listPermissions(Model model) {
		model.addAttribute("lists", coreUserService.findAll());
		return NiweErpCoreUrlConstants.USERS_LIST_PAGE;

	}

	@GetMapping(path = "/new")
	public String newUser(Model model) {
		setData(model);
		model.addAttribute("user", CoreUser.builder().build());
		return NiweErpCoreUrlConstants.USERS_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveUser(@Valid @ModelAttribute CoreUser user, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		log.debug(String.format("------calling saveUser:{%s}", user));
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", user);
			setData(model);
			return NiweErpCoreUrlConstants.ROLES_ADD_FORM_PAGE;
		}
		coreUserService.save(user);
		redirectAttributes.addFlashAttribute("success", "Success.");
		return NiweErpCoreUrlConstants.USERS_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		CoreUser user = coreUserService.findById(id);
		model.addAttribute("user", user);
		setData(model);
		return NiweErpCoreUrlConstants.USERS_EDIT_FORM_PAGE;
	}

	private void setData(Model model) {
		model.addAttribute("roles", coreRoleRepository.findAll());

	}

	@PostMapping("/changePassword")
	public String updateItemValue(@RequestParam String id, @RequestParam String newPassword,
			RedirectAttributes redirectAttributes) {

		coreUserService.changePassword(id, newPassword);
		redirectAttributes.addFlashAttribute("success", "Success.");
		return NiweErpCoreUrlConstants.USERS_LIST_REDITECT_URL;
	}
}

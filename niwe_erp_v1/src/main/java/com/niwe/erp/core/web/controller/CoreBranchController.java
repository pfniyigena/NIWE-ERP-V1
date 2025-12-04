package com.niwe.erp.core.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.core.domain.CoreBranch;
import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.core.service.CoreBranchService;
import com.niwe.erp.core.service.CoreTaxpayerService;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.BRANCHES_URL)
@AllArgsConstructor
public class CoreBranchController {

	private final CoreTaxpayerService coreTaxpayerService;
	private final CoreBranchService coreBranchService;
	@GetMapping(path = "/list")
	public String listBranches(Model model) {

		List<CoreBranch> list = coreBranchService.findAll();
		log.debug("--------------Calling listBranches-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpCoreUrlConstants.BRANCHES_LIST_URL;
	}
	@GetMapping(path = "/new")
	public String newTBranch(Model model) {
		setData(model);
		model.addAttribute("coreBranch", CoreBranch.builder().build());
		return NiweErpCoreUrlConstants.BRANCHES_ADD_FORM_PAGE;
	}
	@PostMapping(path = "/new")
	public String saveBranch(@Valid  CoreBranch coreBranch,BindingResult bindingResult, RedirectAttributes redirectAttributes,
			 Model model) {

		log.debug(String.format("------calling saveBranch:{%s}", coreBranch));
		if (bindingResult.hasErrors()) {
			model.addAttribute("coreBranch", coreBranch);
			setData(model);
			return NiweErpCoreUrlConstants.BRANCHES_ADD_FORM_PAGE;
		}
		redirectAttributes.addFlashAttribute("success", "Success.");
		coreBranchService.save(coreBranch);
		return NiweErpCoreUrlConstants.BRANCHES_LIST_REDITECT_URL;
	}
	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		CoreBranch coreBranch = coreBranchService.findById(id);

		model.addAttribute("coreBranch", coreBranch);
		setData(model);
		return NiweErpCoreUrlConstants.BRANCHES_ADD_FORM_PAGE;
	}

	private void setData(Model model) {
		List<CoreTaxpayer> taxpayers = coreTaxpayerService.findAll();
		model.addAttribute("taxpayers", taxpayers);

	}
}

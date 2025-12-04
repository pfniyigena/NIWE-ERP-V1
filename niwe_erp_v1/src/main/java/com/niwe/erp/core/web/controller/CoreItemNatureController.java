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

import com.niwe.erp.core.domain.CoreItemNature;
import com.niwe.erp.core.service.CoreItemNatureService;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.ITEM_NATURES_URL)
@AllArgsConstructor
public class CoreItemNatureController {

	private final CoreItemNatureService coreItemNatureService;

	@GetMapping(path = "/list")
	public String listItemNatures(Model model) {

		List<CoreItemNature> list = coreItemNatureService.findAll();
		log.debug("--------------Calling listItemNatures-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpCoreUrlConstants.ITEM_NATURES_LIST_PAGE;
	}

	@GetMapping(path = "/new")
	public String newItemNature(Model model) {
		model.addAttribute("itemNature", CoreItemNature.builder().build());
		return NiweErpCoreUrlConstants.ITEM_NATURES_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveItemNature(CoreItemNature itemNature, RedirectAttributes redirectAttrs,
			BindingResult bindingResult, Model model) {

		log.debug(String.format("------calling saveItemNature:{%s}", itemNature));
		coreItemNatureService.save(itemNature);
		return NiweErpCoreUrlConstants.ITEM_NATURES_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		CoreItemNature itemNature = coreItemNatureService.findById(id);

		model.addAttribute("itemNature", itemNature);

		return NiweErpCoreUrlConstants.ITEM_NATURES_ADD_FORM_PAGE;
	}

}

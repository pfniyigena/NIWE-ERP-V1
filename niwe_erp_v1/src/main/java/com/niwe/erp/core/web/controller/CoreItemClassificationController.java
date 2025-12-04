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

import com.niwe.erp.core.domain.CoreItemClassification;
import com.niwe.erp.core.service.CoreItemClassificationService;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.ITEM_CLASSIFICATIONS_URL)
@AllArgsConstructor
public class CoreItemClassificationController {

	private final CoreItemClassificationService coreItemClassificationService;

	@GetMapping(path = "/list")
	public String listClassifications(Model model) {

		List<CoreItemClassification> list = coreItemClassificationService.findAll();
		log.debug("--------------Calling listClassifications-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpCoreUrlConstants.ITEM_CLASSIFICATIONS_LIST_PAGE;
	}

	@GetMapping(path = "/new")
	public String newClassification(Model model) {
		model.addAttribute("classification", CoreItemClassification.builder().build());
		model.addAttribute("parents", coreItemClassificationService.findAll());
		return NiweErpCoreUrlConstants.ITEM_CLASSIFICATIONS_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveClassification(CoreItemClassification classification, RedirectAttributes redirectAttrs,
			BindingResult bindingResult, Model model) {

		log.debug(String.format("------calling saveClassification:{%s}", classification));
		coreItemClassificationService.save(classification);
		return NiweErpCoreUrlConstants.ITEM_CLASSIFICATIONS_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		CoreItemClassification classification = coreItemClassificationService.findByStringId(id);

		model.addAttribute("classification", classification);
		model.addAttribute("parents", coreItemClassificationService.findAll());
		return NiweErpCoreUrlConstants.ITEM_CLASSIFICATIONS_ADD_FORM_PAGE;
	}
}

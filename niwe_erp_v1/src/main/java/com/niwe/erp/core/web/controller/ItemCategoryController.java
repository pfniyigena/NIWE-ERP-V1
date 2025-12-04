package com.niwe.erp.core.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.core.domain.ItemCategory;
import com.niwe.erp.core.service.CategoryService;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.CATEGOTIES_URL)
@AllArgsConstructor
public class ItemCategoryController {
	private final CategoryService categoryService;

	@GetMapping(path = "/list")
	public String listBranches(Model model) {

		List<ItemCategory> list = categoryService.findAll();
		log.debug("--------------Calling listBranches-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpCoreUrlConstants.CATEGORIES_LIST_URL;
	}

	@GetMapping(path = "/new")
	public String newTBranch(Model model) {
		model.addAttribute("itemCategory", ItemCategory.builder().build());
		return NiweErpCoreUrlConstants.CATEGORIES_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveitemCategory(@Valid ItemCategory itemCategory, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		log.debug(String.format("------calling saveitemCategory:{%s}", itemCategory));
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemCategory", itemCategory);
			return NiweErpCoreUrlConstants.CATEGORIES_ADD_FORM_PAGE;
		}
		redirectAttributes.addFlashAttribute("success", "Success.");
		categoryService.save(itemCategory);
		return NiweErpCoreUrlConstants.CATEGORIES_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		ItemCategory itemCategory = categoryService.findById(id);

		model.addAttribute("itemCategory", itemCategory);

		return NiweErpCoreUrlConstants.CATEGORIES_ADD_FORM_PAGE;
	}
	@PostMapping("/upload")
	public String uploadFile(@RequestParam MultipartFile file, RedirectAttributes redirectAttributes, Model model)
			throws IOException {
		String contentType = file.getContentType();
		if (!Objects.equals(contentType, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			model.addAttribute("error", "Please upload an Excel file (.xlsx)");
			return NiweErpCoreUrlConstants.ITEMS_ADD_FORM_PAGE;
		}
		categoryService.impotExcelFile(file);
		redirectAttributes.addFlashAttribute("success", "Success.");
		return NiweErpCoreUrlConstants.CATEGORIES_LIST_REDITECT_URL;
	}
}

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

import com.niwe.erp.core.domain.ItemBrand;
import com.niwe.erp.core.service.BrandService;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.BRANDS_URL)
@AllArgsConstructor
public class ItemBrandController {
	private final BrandService brandService;

	@GetMapping(path = "/list")
	public String listBrands(Model model) {

		List<ItemBrand> list = brandService.findAll();
		log.debug("--------------Calling listBrands-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpCoreUrlConstants.BRANDS_LIST_URL;
	}

	@GetMapping(path = "/new")
	public String newTBranch(Model model) {
		model.addAttribute("itemBrand", ItemBrand.builder().build());
		return NiweErpCoreUrlConstants.BRANDS_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveitemBrand(@Valid ItemBrand itemBrand, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		log.debug(String.format("------calling saveitemBrand:{%s}", itemBrand));
		if (bindingResult.hasErrors()) {
			model.addAttribute("itemBrand", itemBrand);
			return NiweErpCoreUrlConstants.BRANDS_ADD_FORM_PAGE;
		}
		redirectAttributes.addFlashAttribute("success", "Success.");
		brandService.save(itemBrand);
		return NiweErpCoreUrlConstants.BRANDS_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		ItemBrand itemBrand = brandService.findById(id);
		model.addAttribute("itemBrand", itemBrand);
		return NiweErpCoreUrlConstants.BRANDS_ADD_FORM_PAGE;
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam MultipartFile file, RedirectAttributes redirectAttributes, Model model)
			throws IOException {
		String contentType = file.getContentType();
		if (!Objects.equals(contentType, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			model.addAttribute("error", "Please upload an Excel file (.xlsx)");
			return NiweErpCoreUrlConstants.ITEMS_ADD_FORM_PAGE;
		}
		brandService.impotExcelFile(file);
		redirectAttributes.addFlashAttribute("success", "Success.");
		return NiweErpCoreUrlConstants.BRANDS_LIST_REDITECT_URL;
	}

}

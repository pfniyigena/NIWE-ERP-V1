package com.niwe.erp.sale.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.sale.domain.Customer;
import com.niwe.erp.sale.service.CustomerService;
import com.niwe.erp.sale.web.util.NiweErpSaleUrlConstants;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = NiweErpSaleUrlConstants.CUSTOMERS_URL)
@AllArgsConstructor
@Slf4j
public class CustomerController {

	private final CustomerService customerService;

	@GetMapping(path = "/list")
	public String listCustomers(Model model) {

		List<Customer> list = customerService.findAll();
		log.debug("--------------Calling listCustomers-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpSaleUrlConstants.CUSTOMERS_LIST_PAGE;
	}

	@GetMapping(path = "/new")
	public String newCustomer(Model model) {
		model.addAttribute("customer", Customer.builder().build());
		return NiweErpSaleUrlConstants.CUSTOMERS_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveCustomer(@Valid Customer customer, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("customer", customer);
			return NiweErpSaleUrlConstants.CUSTOMERS_ADD_FORM_PAGE;
		}
		customerService.save(customer);
		redirectAttributes.addFlashAttribute("success", "Success.");
		return NiweErpSaleUrlConstants.CUSTOMERS_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		Customer customer = customerService.findById(id);
		model.addAttribute("customer", customer);
		return NiweErpSaleUrlConstants.CUSTOMERS_ADD_FORM_PAGE;
	}
}

package com.niwe.erp.sale.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.service.WarehouseService;
import com.niwe.erp.sale.domain.Shelf;
import com.niwe.erp.sale.service.CustomerService;
import com.niwe.erp.sale.service.ShelfService;
import com.niwe.erp.sale.web.form.ShelfForm;
import com.niwe.erp.sale.web.form.ShelfLineForm;
import com.niwe.erp.sale.web.util.NiweErpSaleUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpSaleUrlConstants.SHELVES_URL)
@AllArgsConstructor
public class ShelfController {
	private final ShelfService shelfService;
	private final WarehouseService warehouseService;
	private final  CoreItemService coreItemService;
	private final CustomerService customerService;
	@GetMapping(path = "/list")
	public String listShelves(Model model) {

		List<Shelf> list = shelfService.findAll();
		log.debug("--------------Calling listShelves-------------------:{}", list.size());
		model.addAttribute("lists", list);
		return NiweErpSaleUrlConstants.SHELVE_LIST_PAGE;
	}

	@GetMapping(path = "/new")
	public String warehouseForm(Model model) {
		model.addAttribute("shelf", Shelf.builder().build());
		setData(model);

		return NiweErpSaleUrlConstants.SHELVE_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveShelf(Shelf shelf, RedirectAttributes redirectAttrs, BindingResult bindingResult, Model model) {

		log.debug("------calling saveShelf:{}", shelf);
		shelfService.save(shelf);
		return NiweErpSaleUrlConstants.SHELVES_LIST_REDITECT_URL;
	}
	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		Shelf shelf =shelfService.findById(id);
		model.addAttribute("shelf", shelf);
		setData(model);
		return NiweErpSaleUrlConstants.SHELVE_ADD_FORM_PAGE;
	}
	@GetMapping(path = "/open/{id}")
	public String newInvoice(@PathVariable String id, Model model) {
		Shelf shelf =shelfService.findById(id);
		ShelfForm shelfForm = new ShelfForm();
		shelfForm.setShelfId(shelf.getId().toString());
		shelfForm.setShelCode(shelf.getInternalCode());
		shelfForm.setShelName(shelf.getName());
		shelfForm.setWarehouseCode(shelf.getWarehouse().getInternalCode());
		shelfForm.setWarehouseName(shelf.getWarehouse().getWarehouseName());
		shelfForm.setShelfLines(List.of(new ShelfLineForm()));
		model.addAttribute("shelfForm", shelfForm);
		return NiweErpSaleUrlConstants.SHELVE_SALE_FORM_PAGE;
	}
	@PostMapping(path = "/post")
	public String savePost(ShelfForm shelfForm, RedirectAttributes redirectAttributes, BindingResult bindingResult, Model model) {

		log.debug("------calling savePost:{}", shelfForm);
		shelfService.savePost(shelfForm);
		redirectAttributes.addFlashAttribute("success", "Post Sale Success.");
		return NiweErpSaleUrlConstants.SHELVES_OPEN_REDITECT_URL+shelfForm.getShelfId();
		
	}
	@GetMapping("/pos_sale")
	public String showSaleFormOdoo(Model model) {
	    model.addAttribute("saleForm", new ShelfForm());
	    //model.addAttribute("customers", customerService.findAll());
	    model.addAttribute("products", coreItemService.findAll());
	    return NiweErpSaleUrlConstants.SHELVE_POS_SALE_PAGE;
	}
	@PostMapping("/checkout")
    @ResponseBody
    public ResponseEntity<?> checkout(@RequestBody String saleDto) {
       log.debug("checkout:{}",saleDto);
        return ResponseEntity.ok("success");
    }
	@GetMapping("/pos")
	public String showSaleFormOdoor(Model model) {
	    model.addAttribute("saleForm", new ShelfForm());
	    model.addAttribute("customers", customerService.findAll());
	    model.addAttribute("products", coreItemService.findAllAsForm());
	    return NiweErpSaleUrlConstants.SHELVE_POS_PAGE;
	}
	@PostMapping("/complete-sale")
    @ResponseBody
    public ResponseEntity<?> completeSale(@RequestBody String saleDto) {
       log.debug("checkout:{}",saleDto);
        return ResponseEntity.ok("success");
    }
	private void setData(Model model) {
		List<Warehouse> warehouses = warehouseService.findAll();
		model.addAttribute("warehouses", warehouses);

	}
}

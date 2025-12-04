package com.niwe.erp.purchase.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.inventory.repository.WarehouseRepository;
import com.niwe.erp.purchase.domain.Purchase;
import com.niwe.erp.purchase.service.PurchaseService;
import com.niwe.erp.purchase.web.form.PurchaseForm;
import com.niwe.erp.purchase.web.form.PurchaseLineForm;
import com.niwe.erp.purchase.web.util.NiweErpPurchaseUrlConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Controller
@RequestMapping(value = NiweErpPurchaseUrlConstants.PURCHASES_URL)
@RequiredArgsConstructor
@Slf4j
public class PurchaseController {
	
	private final PurchaseService purchaseService;
	private final WarehouseRepository warehouseRepository;
	
	@GetMapping(path = "/list")
	public String listWarehouses(Model model) {

		List<Purchase> list = purchaseService.findAll();
		log.debug("--------------Calling listWarehouses-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpPurchaseUrlConstants.PURCHASES_LIST_PAGE;
	}
	
	@GetMapping(path = "/new")
	public String newInvoice(Model model) {
		PurchaseForm purchase = new PurchaseForm();
		purchase.setPurchaseLines(List.of(new PurchaseLineForm())); // Start with 1 empty line
		model.addAttribute("purchase", purchase);
		return NiweErpPurchaseUrlConstants.PURCHASE_ADD_FORM;
	}
	@PostMapping("/new")
	public String saveDraftPurchase(@ModelAttribute PurchaseForm purchaseForm) {
		
		log.info("saveDraftPurchase:{}",purchaseForm.getPurchaseLines().size());
		
		purchaseService.saveDraftPurchaseForm(purchaseForm);
		
	    return NiweErpPurchaseUrlConstants.PURCHASES_LIST_REDITECT_URL;
	}

	@GetMapping("/confirm/{id}")
    public String confirmPurchase(@PathVariable String id, Model model) {
        Purchase purchase = purchaseService.confirm(id);
        log.info("confirmInvoice:{}",purchase);
        model.addAttribute("purchase", purchase);
        return NiweErpPurchaseUrlConstants.PURCHASE_VIEW_FORM;  
    }
	@PostMapping("/confirm-receive")
	public String confirmReceive(@RequestParam String purchaseId,
	                             @RequestParam String warehouseId,Model model,
	                             RedirectAttributes redirectAttributes) {
		log.info("purchaseId:{},warehouseId:{}",purchaseId,warehouseId);
        Purchase purchase = purchaseService.confirmAndReceive(purchaseId,warehouseId);
        model.addAttribute("purchase", purchase);
	    redirectAttributes.addFlashAttribute("success", "Purchase confirmed and goods received.");
	    return NiweErpPurchaseUrlConstants.PURCHASES_LIST_REDITECT_URL;
	}

    @GetMapping("/view/{id}")
    public String viewInvoice(@PathVariable String id, Model model) {
        Purchase purchase = purchaseService.findById(id);
        log.info("viewInvoice:{}",purchase);
        model.addAttribute("purchase", purchase);
        setData(model);
        return NiweErpPurchaseUrlConstants.PURCHASE_VIEW_FORM;  
    }
    
   private  void setData( Model model) {
	   model.addAttribute("warehouses", warehouseRepository.findAll());
   }
}

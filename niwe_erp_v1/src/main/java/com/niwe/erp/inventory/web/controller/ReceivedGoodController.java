package com.niwe.erp.inventory.web.controller;

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

import com.niwe.erp.inventory.domain.GoodReceivedNote;
import com.niwe.erp.inventory.repository.WarehouseRepository;
import com.niwe.erp.inventory.service.ReceivedGoodService;
import com.niwe.erp.inventory.web.form.GoodForm;
import com.niwe.erp.inventory.web.form.GoodLineForm;
import com.niwe.erp.inventory.web.util.NikaErpInventoryUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInventoryUrlConstants.STOCKS_RECEIVED_URL)
@AllArgsConstructor
public class ReceivedGoodController {

	private final ReceivedGoodService receivedGoodService;
	private final WarehouseRepository warehouseRepository;
	@GetMapping(path = "/list")
	public String listReceivedGoods(Model model) {

		List<GoodReceivedNote> list = receivedGoodService.findAll();
		log.debug("--------------Calling listReceivedGoods-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.STOCKS_RECEIVED_LIST_PAGE;
	}

	@GetMapping("/view/{id}")
	public String viewReceivedGood(@PathVariable String id, Model model) {
		GoodReceivedNote good = receivedGoodService.findById(id);
		log.info("viewReceivedGood:{}", good);
		model.addAttribute("good", good);
		setData(model);
		return NikaErpInventoryUrlConstants.STOCK_RECEIVED_VIEW_FORM;
	}

	@GetMapping(path = "/new")
	public String newReceivedGood(Model model) {
		GoodForm goodForm= new GoodForm();
		goodForm.setGoodLines(List.of(new GoodLineForm()));
		model.addAttribute("goodForm", goodForm);
		return NikaErpInventoryUrlConstants.STOCK_RECEIVED_ADD_FORM;
	}

	@PostMapping("/new")
	public String saveDraftReceivedGood(@ModelAttribute GoodForm goodForm) {
		log.info("saveDraftReceivedGood:{}", goodForm.getGoodLines().size());
		GoodReceivedNote good = receivedGoodService.saveDraftReceivedGood(goodForm);
		return NikaErpInventoryUrlConstants.STOCK_RECEIVED_VIEW_FORM_REDIRECT_URL + good.getId();
	}
	@PostMapping("/confirm-receive")
	public String confirmReceive(@RequestParam String goodId,
	                             @RequestParam String warehouseId,Model model,
	                             RedirectAttributes redirectAttributes) {
		log.info("goodId:{},warehouseId:{}",goodId,warehouseId);
		GoodReceivedNote good = receivedGoodService.confirmAndReceive(goodId,warehouseId);
	    redirectAttributes.addFlashAttribute("success", "Purchase confirmed and goods received.");
	    return NikaErpInventoryUrlConstants.STOCK_RECEIVED_VIEW_FORM_REDIRECT_URL + good.getId();
	}
	
	 private  void setData( Model model) {
		   model.addAttribute("warehouses", warehouseRepository.findAll());
	   }
}

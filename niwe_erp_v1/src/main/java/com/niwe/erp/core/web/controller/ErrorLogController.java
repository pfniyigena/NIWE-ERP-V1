package com.niwe.erp.core.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.core.domain.ErrorLog;
import com.niwe.erp.core.service.ErrorLogService;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.LOGS_URL)
@AllArgsConstructor
public class ErrorLogController {
	private final ErrorLogService errorLogService;

	@GetMapping(path = "/list")
	public String lisErrorLogs(Model model) {

		List<ErrorLog> list = errorLogService.findAll();
		log.debug("--------------Calling lisErrorLogs-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpCoreUrlConstants.LOGS_LIST_URL;
	}
	@GetMapping(path = "/view/{id}")
	public String viewItemInfo(@PathVariable String id, Model model) {
		ErrorLog log = errorLogService.findById(id);
		model.addAttribute("log", log);
		return NiweErpCoreUrlConstants.LOGS_VIEW_URL;
	}
	@PostMapping("/delete")
	public String deleteItem(@RequestParam String logId, RedirectAttributes redirectAttributes) {
		log.info("----deleteItem id:{}", logId);
		errorLogService.deleteItemById(logId);
		redirectAttributes.addFlashAttribute("success", "Delete Success.");
		return NiweErpCoreUrlConstants.LOGS_LIST_REDITECT_URL;
	}
	
}

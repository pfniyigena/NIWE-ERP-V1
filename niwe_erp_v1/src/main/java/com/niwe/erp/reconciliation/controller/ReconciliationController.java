package com.niwe.erp.reconciliation.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.reconciliation.service.ReconciliationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "reconciliations")
@RequiredArgsConstructor
@Slf4j
public class ReconciliationController {
	private final ReconciliationService reconciliationService;

	@GetMapping(path = "/list")
	public String list(Model model) {
		return "reconciliations/list";
	}

	@PostMapping("/upload/generated-sales")
	public String uploadClient(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
			Model model) throws IOException {
		reconciliationService.saveSaleGeneratedExcel(file);
		redirectAttributes.addFlashAttribute("success", "Client file saved");
		return "redirect:/reconciliations/list";
	}

	@PostMapping("/upload/declared-sales")
	public String uploadServer(@RequestParam("file2") MultipartFile file2, RedirectAttributes redirectAttributes,
			Model model) throws IOException {
		log.info("uploadServer:{}", file2.getOriginalFilename());
		reconciliationService.saveSaleDeclaredExcel(file2);
		redirectAttributes.addFlashAttribute("success", "Server file saved");
		return "redirect:/reconciliations/list";
	}

	@GetMapping("/export/missing/generated")
	public ResponseEntity<Resource> exportMissingGenerated() throws IOException {
		ByteArrayInputStream stream = reconciliationService.exportMissingGenerated();

		InputStreamResource file = new InputStreamResource(stream);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=missing_records_generated.xlsx")
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@GetMapping("/export/missing/declared")
	public ResponseEntity<Resource> exportMissingDeclared() throws IOException {
		ByteArrayInputStream stream = reconciliationService.exportMissingDeclared();

		InputStreamResource file = new InputStreamResource(stream);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=missing_records_declared.xlsx")
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

}

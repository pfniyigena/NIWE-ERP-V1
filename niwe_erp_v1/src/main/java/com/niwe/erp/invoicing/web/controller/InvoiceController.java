package com.niwe.erp.invoicing.web.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niwe.erp.invoicing.domain.Invoice;
import com.niwe.erp.invoicing.domain.InvoiceTaxSummary;
import com.niwe.erp.invoicing.service.InvoicePdfService;
import com.niwe.erp.invoicing.service.InvoiceService;
import com.niwe.erp.invoicing.service.InvoiceTaxSummaryService;
import com.niwe.erp.invoicing.web.form.InvoiceForm;
import com.niwe.erp.invoicing.web.form.InvoiceLineForm;
import com.niwe.erp.invoicing.web.util.NikaErpInvoicingUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping(value = NikaErpInvoicingUrlConstants.INVOICES_URL)
@AllArgsConstructor
public class InvoiceController {
	private final InvoiceTaxSummaryService invoiceTaxSummaryService;
	private final InvoiceService invoiceService;
	private final InvoicePdfService invoicePdfService;
	@GetMapping(path = "/list")
	public String listInvoices(Model model) {

		List<Invoice> list = invoiceService.findAll();
		log.debug("--------------Calling listInvoices-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInvoicingUrlConstants.INVOICE_LIST_PAGE;
	}
	@GetMapping(path = "/new")
	public String newInvoice(Model model) {
		InvoiceForm invoice = new InvoiceForm();
		invoice.setInvoiceLines(List.of(new InvoiceLineForm())); // Start with 1 empty line
		model.addAttribute("invoice", invoice);
		return NikaErpInvoicingUrlConstants.INVOICE_ADD_FORM;
	}
	@PostMapping("/new")
	public String saveDraftInvoice(@ModelAttribute InvoiceForm invoiceForm) {
		
		log.info("saveDraftInvoice:{}",invoiceForm.getInvoiceLines().size());
		
		invoiceService.saveDraftInvoiceForm(invoiceForm);
	    return NikaErpInvoicingUrlConstants.INVOICES_LIST_REDITECT_URL;
	}
	 
    @GetMapping("/view/{id}")
    public String viewInvoice(@PathVariable String id, Model model) {
        Invoice invoice = invoiceService.findById(id);
        List<InvoiceTaxSummary> taxes=invoiceTaxSummaryService.findByInvoice(invoice);
        log.info("viewInvoice:{},Taxes:{}",invoice,taxes);
        model.addAttribute("invoice", invoice);
        model.addAttribute("taxes", taxes);
        return NikaErpInvoicingUrlConstants.INVOICE_VIEW_FORM;  // ✅ Print view
    }
    @GetMapping("/view-v2/{id}")
    public String viewInvoiceV2(@PathVariable String id, Model model) {
        Invoice invoice = invoiceService.findById(id);
        List<InvoiceTaxSummary> taxes=invoiceTaxSummaryService.findByInvoice(invoice);
        log.info("viewInvoice:{},Taxes:{}",invoice,taxes);
        model.addAttribute("invoice", invoice);
        model.addAttribute("taxes", taxes);
        return NikaErpInvoicingUrlConstants.INVOICE_VIEW_FORM_V2;  // ✅ Print view
    }
    // Edit mode
    @GetMapping("/edit/{id}")
    public String editInvoice(@PathVariable String id, Model model) {
    	  InvoiceForm invoice = invoiceService.findInvoiceFormById(id);
          log.info("editInvoice:{}",invoice);
          model.addAttribute("invoice", invoice);

        return NikaErpInvoicingUrlConstants.INVOICE_EDIT_FORM;  // ✅ Edit view
    }
    @GetMapping("/confirm/{id}")
    public String confirmInvoice(@PathVariable String id, Model model) {
        Invoice invoice = invoiceService.confirm(id);
        log.info("confirmInvoice:{}",invoice);
        model.addAttribute("invoice", invoice);
        return NikaErpInvoicingUrlConstants.INVOICE_VIEW_FORM;  // ✅ Print view
    }
    
    @GetMapping("/print/{id}")
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable String id) {
    	Invoice invoice = invoiceService.findById(id);

        byte[] pdfBytes = invoicePdfService.generateInvoicePdf("INV-" + invoice.getInternalCode(), "Pierre Fourier Niyigena", invoice);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}

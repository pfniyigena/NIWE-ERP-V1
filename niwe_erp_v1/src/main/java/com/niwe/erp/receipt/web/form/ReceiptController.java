package com.niwe.erp.receipt.web.form;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/receipt")
@RequiredArgsConstructor
public class ReceiptController {

    private final PdfReceiptService pdfReceiptService;
private final ThermalPrinterService thermalPrinterService;
    @PostMapping(value = "/generate", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateReceipt(@RequestBody ReceiptDto receiptDto) {
        try {
            byte[] pdfBytes = pdfReceiptService.generateReceiptPdf(receiptDto);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "receipt_" + receiptDto.orderId() + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping(value = "/generate-fancy", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateFancyReceipt(@RequestBody ReceiptDto receiptDto) throws Exception {
        String qrLink = "https://mypos.com/receipt/" + receiptDto.orderId(); // or UPI payment link, etc.

        byte[] pdf = pdfReceiptService.generateReceiptWithLogoAndQr(receiptDto, qrLink);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "receipt_" + receiptDto.orderId() + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
    @PostMapping("/pos/sale")
    public ResponseEntity<?> completeSale(@RequestBody ReceiptDto receiptDto) {
 
        String qrLink = "https://yourshop.com/receipt/" + receiptDto.orderId();

        try {
            thermalPrinterService.printReceipt(receiptDto, qrLink);
            return ResponseEntity.ok("Printed!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Print failed: " + e.getMessage());
        }
    }
}
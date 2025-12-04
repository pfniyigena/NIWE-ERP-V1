package com.niwe.erp.invoicing.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.niwe.erp.invoicing.domain.Invoice;
import com.niwe.erp.invoicing.domain.InvoiceLine;
import com.niwe.erp.invoicing.web.form.InvoiceForm;
import com.niwe.erp.invoicing.web.form.InvoiceLineForm;

public class InvoiceManualMapper {

	public static InvoiceLineForm toLineDto(InvoiceLine line) {
		return InvoiceLineForm.builder()
				.itemName(line.getItemName())
				.quantity(line.getQuantity())
				.unitPrice(line.getUnitPrice())
				.lineTotal(line.getGrossAmount())
				.build();
	}

	public static InvoiceForm toDto(Invoice invoice) {
		List<InvoiceLineForm> lineDtos = invoice.getLines().stream().map(InvoiceManualMapper::toLineDto).toList();

		return InvoiceForm.builder().invoiceLines(lineDtos).build();
	}

	public static List<InvoiceForm> toDtoList(List<Invoice> invoices) {
		return invoices.stream().map(InvoiceManualMapper::toDto).toList();
	}

	// ---------- DTO → ENTITY (Manual Inverse) ----------

    public static InvoiceLine toLineEntity(InvoiceLineForm dto, Invoice invoice) {
        InvoiceLine line = new InvoiceLine();
        line.setId(UUID.fromString(dto.getId()));
        line.setItemName(dto.getItemName());
        line.setUnitPrice(dto.getUnitPrice());
        line.setQuantity(dto.getQuantity());
        line.setInvoice(invoice); // important: set parent reference
        return line;
    }

    public static Invoice toEntity(InvoiceForm dto) {
        Invoice invoice = new Invoice();
        invoice.setId(dto.getId());

        List<InvoiceLine> lines = dto.getInvoiceLines() != null ?
                dto.getInvoiceLines().stream()
                        .map(lineDto -> toLineEntity(lineDto, invoice))
                        .collect(Collectors.toList()) :
                List.of();

        invoice.setLines(lines);
        return invoice;
    }
}

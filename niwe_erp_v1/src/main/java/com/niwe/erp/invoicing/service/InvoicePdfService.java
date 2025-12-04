package com.niwe.erp.invoicing.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.niwe.erp.invoicing.domain.Invoice;
import com.niwe.erp.invoicing.domain.InvoiceLine;

@Service
public class InvoicePdfService {

    public byte[] generateInvoicePdf(String invoiceNo, String customerName, Invoice invoice) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf, PageSize.A4);
            doc.setMargins(20, 20, 20, 20);

            // 🧾 HEADER TABLE (Logo + Company Info)
         // ✅ Create a table with 2 columns (logo + company info)
            float[] columnWidths = {120F, 380F}; // Adjust column width ratios
            Table headerTable = new Table(UnitValue.createPercentArray(columnWidths));
            headerTable.setWidth(UnitValue.createPercentValue(100));

            // Company Logo
            try {
                ClassPathResource logoResource = new ClassPathResource("static/logo.png");
                Image logo = new Image(ImageDataFactory.create(logoResource.getURL()));
                         
                logo.scaleToFit(60, 60); // 👈 reduce logo size (width, height)
                logo.setAutoScale(false); // prevents further scaling
                Cell logoCell = new Cell()
                        .add(logo)
                        .setBorder(Border.NO_BORDER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE);
                headerTable.addCell(logoCell);
            } catch (IOException e) {
                // fallback: blank cell if logo missing
                headerTable.addCell(new Cell().setBorder(Border.NO_BORDER));
                System.out.println("⚠️ Logo not found in /src/main/resources/static/logo.png");
            }

            // Company Info (Right aligned)
            Paragraph companyInfo = new Paragraph("AFRITECH SOLUTIONS\n")
                    .setBold()
                    .setFontSize(12)
                    .add("123 Business Street, N'Djamena, Chad\n")
                    .add("Phone: +235 90000000\n")
                    .add("Email: info@afritech.td");

            Cell infoCell = new Cell()
                    .add(companyInfo)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);
            headerTable.addCell(infoCell);

            doc.add(headerTable);
            //doc.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(10));

            // INVOICE TITLE
            doc.add(new Paragraph("INVOICE")
                    .setFontSize(12)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            doc.add(new Paragraph("\n"));

            // Invoice Info
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                    .useAllAvailableWidth();
            infoTable.addCell(new Cell().add(new Paragraph("Invoice No: " + invoiceNo)).setBorder(Border.NO_BORDER));
            infoTable.addCell(new Cell().add(new Paragraph("Date: " + LocalDate.now())).setBorder(Border.NO_BORDER));
            infoTable.addCell(new Cell().add(new Paragraph("Billed To:\n" + customerName)).setBorder(Border.NO_BORDER));
            infoTable.addCell(new Cell().add(new Paragraph("Due Date: " + LocalDate.now().plusDays(7))).setBorder(Border.NO_BORDER));
            doc.add(infoTable);
            doc.add(new Paragraph("\n"));

            // ITEMS TABLE
            Table table = new Table(UnitValue.createPercentArray(new float[]{50, 15, 15, 20}))
                    .useAllAvailableWidth();

            table.addHeaderCell(new Cell().add(new Paragraph("Description").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Qty").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Unit Price").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Total").setBold()));

            for (InvoiceLine item : invoice.getLines()) {
                table.addCell(item.getItemName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(String.format("$ %.2f", item.getUnitPrice()));
                table.addCell(String.format("$ %.2f", item.getTotalAPayer()));
            }

            doc.add(table);
            doc.add(new Paragraph("\n"));

            // TOTAL SECTION
            Table totalTable = new Table(UnitValue.createPercentArray(new float[]{70, 30}))
                    .useAllAvailableWidth();

            totalTable.addCell(new Cell().add(new Paragraph("Subtotal")).setBorder(Border.NO_BORDER));
            totalTable.addCell(new Cell().add(new Paragraph(String.format("$ %.2f", invoice.getAmountWithoutTax())))
                    .setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

            totalTable.addCell(new Cell().add(new Paragraph("Tax (18%)")).setBorder(Border.NO_BORDER));
            totalTable.addCell(new Cell().add(new Paragraph(String.format("$ %.2f", invoice.getTotalTaxAmount())))
                    .setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

            totalTable.addCell(new Cell().add(new Paragraph("Total Due").setBold()).setBorder(Border.NO_BORDER));
            totalTable.addCell(new Cell().add(new Paragraph(String.format("$ %.2f", invoice.getTotalAPayer())))
                    .setBold().setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

            doc.add(totalTable);
            doc.add(new Paragraph("\n"));

            // QR Code
            String qrText = "Invoice: " + invoiceNo + "\nCustomer: " + customerName + "\nAmount: $" +
                    String.format("%.2f", invoice.getTotalAPayer());
            BarcodeQRCode qrCode = new BarcodeQRCode(qrText);
            Image qrImage = new Image(qrCode.createFormXObject(pdf))
                    .setWidth(100)
                    .setHeight(100)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            doc.add(qrImage);
            doc.add(new Paragraph("Scan to Pay or Verify Invoice")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10)
                    .setFontColor(ColorConstants.DARK_GRAY));

            // Footer
            doc.add(new LineSeparator(new SolidLine()));
            doc.add(new Paragraph("Thank you for your business!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10));
            doc.add(new Paragraph("This is a system-generated invoice — no signature required.")
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER));

            doc.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}

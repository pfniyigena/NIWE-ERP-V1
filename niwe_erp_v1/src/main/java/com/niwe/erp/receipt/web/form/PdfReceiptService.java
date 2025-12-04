package com.niwe.erp.receipt.web.form;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class PdfReceiptService {

    public byte[] generateReceiptPdf(ReceiptDto receipt) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        
        // Set page size to 80mm thermal printer (common POS size)
        pdf.setDefaultPageSize(new PageSize(226.77f, 1000)); // 80mm width, height auto

        Document document = new Document(pdf);
        document.setMargins(10, 10, 10, 10);

        // Fonts
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Header
        document.add(new Paragraph(receipt.storeName())
                .setFont(bold).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(receipt.storeAddress())
                .setFont(regular).setFontSize(9).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Tel: " + receipt.storePhone())
                .setFont(regular).setFontSize(9).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Cashier: " + receipt.cashierName())
                .setFont(regular).setFontSize(9));
        document.add(new Paragraph("Date: " + receipt.dateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .setFont(regular).setFontSize(9));
        document.add(new Paragraph("Order ID: " + receipt.orderId())
                .setFont(regular).setFontSize(9));

        document.add(new Paragraph("----------------------------------------")
                .setFont(regular).setFontSize(9));

        // Items Table
        Table table = new Table(new float[]{3, 1, 1.5f, 2});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Cell().add(new Paragraph("Item").setFont(bold).setFontSize(9)));
        table.addHeaderCell(new Cell().add(new Paragraph("Qty").setFont(bold).setFontSize(9)));
        table.addHeaderCell(new Cell().add(new Paragraph("Price").setFont(bold).setFontSize(9)));
        table.addHeaderCell(new Cell().add(new Paragraph("Total").setFont(bold).setFontSize(9)));

        for (ReceiptItem item : receipt.items()) {
            table.addCell(new Cell().add(new Paragraph(item.description()).setFont(regular).setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(item.qty())).setFont(regular).setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", item.unitPrice())).setFont(regular).setFontSize(9)));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", item.total())).setFont(regular).setFontSize(9)));
        }

        document.add(table);

        document.add(new Paragraph("----------------------------------------")
                .setFont(regular).setFontSize(9));

        // Totals
        document.add(new Paragraph(String.format("Sub Total      : %.2f", receipt.subTotal()))
                .setFont(regular).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph(String.format("Discount       : %.2f", receipt.discount()))
                .setFont(regular).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph(String.format("TOTAL          : %.2f", receipt.total()))
                .setFont(bold).setFontSize(12).setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph(String.format("Cash Received  : %.2f", receipt.cashReceived()))
                .setFont(regular).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph(String.format("Change         : %.2f", receipt.change()))
                .setFont(regular).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));

        document.add(new Paragraph("\nThank you for your purchase!\nCome again :)")
                .setFont(regular).setFontSize(10).setTextAlignment(TextAlignment.CENTER));

        document.close();
        return baos.toByteArray();
    }
    // Put your logo in src/main/resources/static/logo.png (or .jpg)
    private static final String LOGO_PATH = "src/main/resources/static/logo.png"; // or use ClassPathResource in production

    public byte[] generateReceiptWithLogoAndQr(ReceiptDto receipt, String qrContent) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        pdf.setDefaultPageSize(new PageSize(226.77f, 1200)); // ~80mm width

        Document document = new Document(pdf);
        document.setMargins(10, 10, 10, 10);

        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // ==================== 1. LOGO ====================
        if (Files.exists(Path.of(LOGO_PATH))) {
            Image logo = new Image(ImageDataFactory.create(LOGO_PATH));
            logo.setWidth(UnitValue.createPercentValue(70));
            logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(logo);
            document.add(new Paragraph("\n"));
        } else {
            // Fallback text if logo missing
            document.add(new Paragraph(receipt.storeName())
                .setFont(bold).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        }

        // ==================== HEADER INFO ====================
        document.add(new Paragraph(receipt.storeAddress())
            .setFont(regular).setFontSize(9).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Tel: " + receipt.storePhone())
            .setFont(regular).setFontSize(9).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Cashier: " + receipt.cashierName())
            .setFont(regular).setFontSize(9));
        document.add(new Paragraph("Date: " + receipt.dateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
            .setFont(regular).setFontSize(9));
        document.add(new Paragraph("Bill No: " + receipt.orderId())
            .setFont(regular).setFontSize(9));

        document.add(new Paragraph("----------------------------------------")
            .setFont(regular).setFontSize(9));

        // ==================== ITEMS TABLE ====================
        Table table = new Table(UnitValue.createPercentArray(new float[]{3.5f, 1, 1.5f, 2}));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(cell("Item", bold));
        table.addHeaderCell(cell("Qty", bold));
        table.addHeaderCell(cell("Price", bold));
        table.addHeaderCell(cell("Total", bold));

        for (ReceiptItem item : receipt.items()) {
            table.addCell(cell(item.description(), regular));
            table.addCell(cell(String.valueOf(item.qty()), regular));
            table.addCell(cell(String.format("%.2f", item.unitPrice()), regular));
            table.addCell(cell(String.format("%.2f", item.total()), regular));
        }
        document.add(table);

        document.add(new Paragraph("----------------------------------------")
            .setFont(regular).setFontSize(9));

        // ==================== TOTALS ====================
        addRightAlignedLine(document, regular, "Sub Total    : ", String.format("%.2f", receipt.subTotal()));
        addRightAlignedLine(document, regular, "Discount     : ", String.format("%.2f", receipt.discount()));
        addRightAlignedLine(document, bold,   "TOTAL        : ", String.format("%.2f", receipt.total()));
        addRightAlignedLine(document, regular, "Cash         : ", String.format("%.2f", receipt.cashReceived()));
        addRightAlignedLine(document, regular, "Change       : ", String.format("%.2f", receipt.change()));

        document.add(new Paragraph("\n"));

        // ==================== QR CODE ====================
        if (qrContent != null && !qrContent.isBlank()) {
            ByteArrayOutputStream qrStream = new ByteArrayOutputStream();
            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrWriter.encode(qrContent, BarcodeFormat.QR_CODE, 150, 150);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", qrStream);

            Image qrImage = new Image(ImageDataFactory.create(qrStream.toByteArray()));
            qrImage.setWidth(120);
            qrImage.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(qrImage);

            document.add(new Paragraph("Scan for digital receipt")
                .setFont(regular).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
        }

        // ==================== FOOTER ====================
        document.add(new Paragraph("\n*** Thank You! Come Again ***")
            .setFont(bold).setFontSize(10).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Powered by MyPOS")
            .setFont(regular).setFontSize(8).setTextAlignment(TextAlignment.CENTER));

        document.close();
        return baos.toByteArray();
    }

    private Cell cell(String content, PdfFont font) {
        return new Cell().add(new Paragraph(content).setFont(font).setFontSize(9)).setPadding(2);
    }

    private void addRightAlignedLine(Document doc, PdfFont font, String label, String value) {
        Paragraph p = new Paragraph(label + value)
            .setFont(font)
            .setFontSize(10)
            .setTextAlignment(TextAlignment.RIGHT);
        doc.add(p);
    }
}
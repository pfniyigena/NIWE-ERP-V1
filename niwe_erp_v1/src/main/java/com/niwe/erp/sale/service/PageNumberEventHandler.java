package com.niwe.erp.sale.service;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

public class PageNumberEventHandler implements IEventHandler {

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfCanvas pdfCanvas = new PdfCanvas(docEvent.getPage());

        Rectangle pageSize = docEvent.getPage().getPageSize();
        int pageNumber = docEvent.getDocument().getPageNumber(docEvent.getPage());

        // Only PdfCanvas + Rectangle for Canvas
        Canvas canvas = new Canvas(pdfCanvas, pageSize);

        canvas.showTextAligned(
                new Paragraph("Page " + pageNumber),
                pageSize.getWidth() / 2,
                20, // 20 points from bottom
                TextAlignment.CENTER
        );

        canvas.close();
    }
}

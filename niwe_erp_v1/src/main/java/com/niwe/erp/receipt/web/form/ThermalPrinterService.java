package com.niwe.erp.receipt.web.form;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "printer")
@Data   // Lombok – create getters/setters
public class ThermalPrinterService {

    private String ip = "192.168.1.100";   // set in application.yml
    private int port = 9100;
    private boolean is58mm = false;        // true = 58mm, false = 80mm
    private boolean hasLogo = true;        // set false if you didn't upload logo yet
    private boolean kickDrawer = true;

    public void printReceipt(ReceiptDto receipt, String qrContent) throws IOException {
        Socket socket = new Socket(ip, port);
        OutputStream out = socket.getOutputStream();

        out.write(ESC_POS_V2.INIT);

        // 1. Print stored NV logo (if exists)
        if (hasLogo) {
            out.write(ESC_POS_V2.PRINT_NV_LOGO);
            out.write(ESC_POS_V2.LINE_FEED);
            out.write(ESC_POS_V2.LINE_FEED);
        }

        // 2. Header
        out.write(ESC_POS_V2.ALIGN_CENTER);
        out.write(ESC_POS_V2.TEXT_BOLD_ON);
        out.write(ESC_POS_V2.TEXT_DOUBLE_HEIGHT);
        out.write((receipt.storeName() + "\n").getBytes());
        out.write(ESC_POS_V2.TEXT_NORMAL);
        out.write(ESC_POS_V2.TEXT_BOLD_OFF);

        out.write((receipt.storeAddress() + "\n").getBytes());
        out.write(("Tel: " + receipt.storePhone() + "\n\n").getBytes());
        out.write(ESC_POS_V2.ALIGN_LEFT);

        printLeftRight(out, "Cashier: " + receipt.cashierName(),
                            receipt.dateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        printLeftRight(out, "Bill No: " + receipt.orderId(), "");

        out.write(getSeparator().getBytes());

        // 3. Items
        for (ReceiptItem item : receipt.items()) {
            String desc = truncate(item.description(), is58mm ? 26 : 32);
            String line1 = String.format("%s %dx%8.2f\n", desc, item.qty(), item.unitPrice());
            out.write(line1.getBytes());

            String amount = String.format("%" + (is58mm ? 38 : 44) + ".2f\n", item.total());
            out.write(amount.getBytes());
        }

        out.write(getSeparator().getBytes());

        // 4. Totals
        out.write(ESC_POS_V2.TEXT_BOLD_ON);
        printLeftRight(out, "SUBTOTAL", String.format("%.2f", receipt.subTotal()));
        printLeftRight(out, "DISCOUNT", String.format("%.2f", receipt.discount()));
        out.write(ESC_POS_V2.TEXT_DOUBLE_HEIGHT);
        printLeftRight(out, "TOTAL", String.format("%.2f", receipt.total()));
        out.write(ESC_POS_V2.TEXT_NORMAL);
        out.write(ESC_POS_V2.TEXT_BOLD_OFF);

        printLeftRight(out, "CASH", String.format("%.2f", receipt.cashReceived()));
        printLeftRight(out, "CHANGE", String.format("%.2f", receipt.change()));

        out.write("\n".getBytes());

        // 5. QR Code
        if (qrContent != null && !qrContent.isBlank()) {
            out.write(ESC_POS_V2.ALIGN_CENTER);
            out.write(ESC_POS_V2.QR_MODEL_2);
            out.write(qrContent.getBytes("UTF-8"));
            out.write(new byte[]{0});
            out.write(ESC_POS_V2.QR_SIZE);
            out.write(ESC_POS_V2.QR_ERROR_L);
            out.write(ESC_POS_V2.QR_PRINT);
            out.write("\nScan for digital receipt\n\n".getBytes());
            out.write(ESC_POS_V2.ALIGN_LEFT);
        }

        // 6. Footer & cut
        out.write(ESC_POS_V2.ALIGN_CENTER);
        out.write("*** Thank You! Come Again ***\n\n\n".getBytes());

        out.write(ESC_POS_V2.CUT_PAPER);

        // 7. Kick cash drawer (ding!)
        if (kickDrawer) {
            out.write(ESC_POS_V2.DRAWER_KICK_PIN2);  // Most common
            // out.write(ESC_POS_V2.DRAWER_KICK_PIN5); // Uncomment if your drawer is on pin 5
        }

        out.flush();
        out.close();
        socket.close();
    }

    private String getSeparator() {
        return is58mm ? "------------------------------\n" : "----------------------------------------\n";
    }

    private void printLeftRight(OutputStream out, String left, String right) throws IOException {
        int width = is58mm ? 38 : 44;
        String line = String.format("%-" + (width - right.length()) + "s%s\n", left, right);
        out.write(line.getBytes());
    }

    private String truncate(String s, int max) {
        return s.length() > max ? s.substring(0, max - 3) + "..." : s;
    }
}
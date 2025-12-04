package com.niwe.erp.receipt.web.form;
public final class ESC_POS {
    // Basic
    public static final byte[] INIT              = {27, 64};
    public static final byte[] LINE_FEED         = {10};
    public static final byte[] CUT_PAPER         = {29, 86, 65};      // Full cut
    public static final byte[] PARTIAL_CUT       = {29, 86, 66, 0};

    // Text
    public static final byte[] ALIGN_LEFT        = {27, 97, 0};
    public static final byte[] ALIGN_CENTER      = {27, 97, 1};
    public static final byte[] ALIGN_RIGHT       = {27, 97, 2};
    public static final byte[] TEXT_BOLD_ON      = {27, 69, 1};
    public static final byte[] TEXT_BOLD_OFF     = {27, 69, 0};
    public static final byte[] TEXT_DOUBLE_HEIGHT= {27, 33, 16};
    public static final byte[] TEXT_DOUBLE_WIDTH = {27, 33, 32};
    public static final byte[] TEXT_NORMAL       = {27, 33, 0};

    // Cash drawer kick (most printers support pin 2 & pin 5)
    //public static final byte[] DRAWER_KICK_PIN2 = {27, 112, 0, 25, 250};   // Pin 2 (most common)
    //public static final byte[] DRAWER_KICK_PIN5 = {27, 112, 1, 25, 250};   // Pin 5 (some Chinese models)

    // NV Logo (stored in printer memory as logo #1)
    public static final byte[] PRINT_NV_LOGO     = {28, 112, 1, 48}; // Logo #1, normal mode

    // QR Code (GS ( k)
    public static final byte[] QR_MODEL_2        = {29, 40, 107, 4, 0, 49, 65, 50, 0};
    public static final byte[] QR_SIZE           = {29, 40, 107, 3, 0, 49, 67, 8};   // size 8
    public static final byte[] QR_ERROR_L        = {29, 40, 107, 3, 0, 49, 69, 48}; // 7% error correction
    public static final byte[] QR_PRINT          = {29, 40, 107, 3, 0, 49, 81, 48};
}
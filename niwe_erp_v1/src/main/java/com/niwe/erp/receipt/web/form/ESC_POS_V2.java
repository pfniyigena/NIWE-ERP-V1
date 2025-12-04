package com.niwe.erp.receipt.web.form;
public final class ESC_POS_V2 {

    private static byte b(int i) { return (byte) i; }

    public static final byte[] INIT              = {b(27), b(64)};
    public static final byte[] LINE_FEED         = {10};
    public static final byte[] CUT_PAPER         = {b(29), b(86), b(65)};
    public static final byte[] PARTIAL_CUT      = {b(29), b(86), b(66), b(0)};

    public static final byte[] ALIGN_LEFT        = {b(27), b(97), b(0)};
    public static final byte[] ALIGN_CENTER      = {b(27), b(97), b(1)};
    public static final byte[] ALIGN_RIGHT       = {b(27), b(97), b(2)};

    public static final byte[] TEXT_BOLD_ON      = {b(27), b(69), b(1)};
    public static final byte[] TEXT_BOLD_OFF     = {b(27), b(69), b(0)};
    public static final byte[] TEXT_DOUBLE_HEIGHT= {b(27), b(33), b(16)};
    public static final byte[] TEXT_DOUBLE_WIDTH = {b(27), b(33), b(32)};
    public static final byte[] TEXT_NORMAL       = {b(27), b(33), b(0)};

    public static final byte[] DRAWER_KICK_PIN2  = {b(27), b(112), b(0), b(25), b(250)};  // ← Fixed!
    public static final byte[] DRAWER_KICK_PIN5  = {b(27), b(112), b(1), b(25), b(250)};  // ← Fixed!

    public static final byte[] PRINT_NV_LOGO     = {b(28), b(112), b(1), b(48)};

    // QR Code commands
    public static final byte[] QR_MODEL_2        = {b(29), b(40), b(107), b(4), b(0), b(49), b(65), b(50), b(0)};
    public static final byte[] QR_SIZE           = {b(29), b(40), b(107), b(3), b(0), b(49), b(67), b(8)};
    public static final byte[] QR_ERROR_L        = {b(29), b(40), b(107), b(3), b(0), b(49), b(69), b(48)};
    public static final byte[] QR_PRINT          = {b(29), b(40), b(107), b(3), b(0), b(49), b(81), b(48)};
}
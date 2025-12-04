package com.niwe.erp.ebm.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class EbmUtil {
	private EbmUtil() {
		throw new IllegalStateException("Utility class SdcUtil");
	}

	public static String padString(String inputString, int strLen) {
		inputString = inputString.trim();
		int dataLen = inputString.length();
		if (dataLen == strLen || strLen == 0)
			return inputString;
		if (dataLen < strLen) {
			for (int i = 0; i < strLen - dataLen; i++)
				inputString = " " + inputString;
		} else {
			return "ERROR";
		}
		return inputString;
	}

	public static String padAmount(String inputAmount, int strLen) {
		inputAmount = inputAmount.replace(".", ",");
		return padString(inputAmount, strLen);
	}

	public static String getDecimalComma(String s) {
		if (s == "") {
			s = "0,00";
		}
		s = s.replace(".", ",");
		if (!s.contains(",")) {
			s = s + ",00";
		}
		s = s + "0";
		int dec = s.indexOf(",");
		s = s.substring(0, dec + 3);
		return s;
	}

	public static String getDecimalFromDouble(Double value) {
		return String.format("%.2f", value);

	}

	public static String getDecimalFromBigDecimal(BigDecimal value) {
		return String.format("%.2f", value);

	}

	public static String padToLength(String input, int length) {
		char paddingChar = ' ';
		if (input == null) {
			input = "";
		}
		input = input.trim();
		int num = length - input.length();
		if (num > 0) {
			for (int i = 0; i < num; i++) {
				input = paddingChar + input;
			}
		}
		return input;
	}

	/**
	 * @param registeredTinNumber
	 * @param sdcNumber
	 * @param machineRegistrationCode
	 * @param invoiceNumber
	 * @param customerTinNumber
	 * @param invoiceType
	 * @param invoiceTypeCounter
	 * @param totalInvoiceCounter
	 * @param transactionType
	 * @param taxRateA
	 * @param taxRateB1
	 * @param taxRateB2
	 * @param taxRateC
	 * @param taxRateD
	 * @param totalAmountIncludingTaxA
	 * @param totalAmountIncludingTaxB1
	 * @param totalAmountIncludingTaxB2
	 * @param totalAmountIncludingTaxC
	 * @param totalAmountIncludingTaxD
	 * @param totalTaxA
	 * @param totalTaxB1
	 * @param totalTaxB2
	 * @param totalTaxC
	 * @param totalTaxD
	 * @param cisDate
	 * @param sdcDate
	 * @return
	 */
	public static String getSignatureInputString(String registeredTinNumber, String sdcNumber,
			String machineRegistrationCode, Long invoiceNumber, String customerTinNumber, String invoiceType,
			Long invoiceTypeCounter, Long totalInvoiceCounter, String transactionType, BigDecimal taxRateA,
			BigDecimal taxRateB1, BigDecimal taxRateB2, BigDecimal taxRateC, BigDecimal taxRateD,
			BigDecimal totalAmountIncludingTaxA, BigDecimal totalAmountIncludingTaxB1,
			BigDecimal totalAmountIncludingTaxB2, BigDecimal totalAmountIncludingTaxC,
			BigDecimal totalAmountIncludingTaxD, BigDecimal totalTaxA, BigDecimal totalTaxB1, BigDecimal totalTaxB2,
			BigDecimal totalTaxC, BigDecimal totalTaxD, LocalDateTime cisDate, LocalDateTime sdcDate) {
		try {
			String ret = "";
			ret += padToLength(formatDateToSdcFormat(cisDate), 14);
			ret += padToLength(registeredTinNumber, 9);
			ret += padToLength(customerTinNumber, 9);
			ret += padToLength(machineRegistrationCode, 11);
			ret += padToLength(invoiceNumber.toString(), 10);

			// Tax 1
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(taxRateA)), 5);
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(totalAmountIncludingTaxA)), 15);
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(totalTaxA)), 15);
			// Tax 2
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(taxRateB1)), 5);
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(totalAmountIncludingTaxB1)), 15);
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(totalTaxB1)), 15);
			// Tax 3
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(taxRateB2)), 5);
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(totalAmountIncludingTaxB2)), 15);
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(totalTaxB2)), 15);
			// Tax 4
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(taxRateC)), 5);
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(totalAmountIncludingTaxC)), 15);
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(totalTaxC)), 15);
			// Tax 5
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(taxRateD)), 5);
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(totalAmountIncludingTaxD)), 15);
			ret += padToLength(getDecimalComma(getDecimalFromBigDecimal(totalTaxD)), 15);
			ret += padToLength(transactionType, 1);
			ret += padToLength(invoiceType, 1);
			ret += padToLength(sdcNumber, 12);
			ret += padToLength(formatDateToSdcFormat(sdcDate), 14);
			ret += padToLength(invoiceTypeCounter.toString(), 10);
			ret += padToLength(totalInvoiceCounter.toString(), 10);
			return ret;
		} catch (Exception e) {
			return null;
		}
	}

	public static byte[] getReceiptInternalDataByteLong(long totalSaleTaxAmountExcludingDecimalPart, long RTA,
			long ZCNT, long SDCTC2) {
		return new byte[] { (byte) (totalSaleTaxAmountExcludingDecimalPart >> 32),
				(byte) (totalSaleTaxAmountExcludingDecimalPart >> 24),
				(byte) (totalSaleTaxAmountExcludingDecimalPart >> 16),
				(byte) (totalSaleTaxAmountExcludingDecimalPart >> 8), (byte) totalSaleTaxAmountExcludingDecimalPart,
				(byte) (RTA >> 32), (byte) (RTA >> 24), (byte) (RTA >> 16), (byte) (RTA >> 8), (byte) RTA,
				(byte) (ZCNT >> 8), (byte) ZCNT, (byte) (SDCTC2 >> 24), (byte) (SDCTC2 >> 16), (byte) (SDCTC2 >> 8),
				(byte) SDCTC2 };

	}

	public static byte[] getInternalDataInput(BigDecimal totalSaleTaxAmount, BigDecimal totalReturnTaxAmount,
			Integer numberOfCopyInvoice, Integer dailyReportNumber, Integer totalInvoiceCounter) {
		// int STA, int RTA, int ZCNT, int SDCTC
		return getReceiptInternalDataByteLong(totalSaleTaxAmount.longValue(), totalReturnTaxAmount.longValue(),
				numberOfCopyInvoice.longValue(), totalInvoiceCounter.longValue());
	}

	static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public static String formatDateToSdcFormat(LocalDateTime date) {

		return date.format(CUSTOM_FORMATTER);
	}
}

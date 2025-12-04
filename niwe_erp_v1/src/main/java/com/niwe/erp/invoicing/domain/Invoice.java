package com.niwe.erp.invoicing.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.ebm.domain.SdcDailyReport;
import com.niwe.erp.ebm.domain.SdcInformation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "INVOICING_INVOICE")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Invoice extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", nullable = false, unique = true)
	private String internalCode;
	/**
	 * The registeredTinNumber
	 */
	@Column(name = "REGISTERED_TIN_NUMBER", nullable = false)
	private String registeredTinNumber;
	/**
	 * The taxPayerName
	 */
	@Column(name = "TAXPAYER_NAME", nullable = false)
	private String taxPayerName;
	/**
	 * The sdcNumber
	 */
	@Column(name = "SDC_NUMBER", nullable = false)
	private String sdcNumber;
	/**
	 * The machineRegistrationCode
	 */
	@Column(name = "MACHINE_REGISTRATION_CODE", nullable = false)
	private String machineRegistrationCode;
	/**
	 * The customerTinNumber
	 */
	@Column(name = "CUSTOMER_TIN_NUMBER", nullable = true)
	private String customerTinNumber;
	/**
	 * The customerName
	 */
	@Column(name = "CUSTOMER_NAME", nullable = true)
	private String customerName;
	/**
	 * The invoiceNumber
	 */
	@Column(name = "INVOICE_NUMBER", nullable = false)
	@Builder.Default
	private Long invoiceNumber = 0L;
	/**
	 * The refundNumber
	 */
	@Column(name = "REFUND_NUMBER", nullable = true)
	private Long refundNumber;
	/**
	 * The totalInvoiceCounter
	 */
	@Column(name = "TOTAL_INVOICE_COUNTER", nullable = false)
	@Builder.Default
	private Long totalInvoiceCounter = 0L;
	/**
	 * The totalInvoicePerTypeCounter
	 */
	@Column(name = "TOTAL_INVOICE_PER_TYPE_COUNTER", nullable = false)
	@Builder.Default
	private Long totalInvoicePerTypeCounter = 0L;
	/**
	 * The invoiceType
	 */
	@Column(name = "INVOICE_TYPE", nullable = false)
	@lombok.ToString.Include
	@Builder.Default
	private EInvoiceType invoiceType = EInvoiceType.INVOICE_TYPE_NORMAL;
	/**
	 * The transactionType
	 */
	@Column(name = "TRANSACTION_TYPE", nullable = false)
	@lombok.ToString.Include
	@Builder.Default
	private ETransactionType transactionType = ETransactionType.TRANSACTION_TYPE_SALE;
	/**
	 * The sdcDate
	 */
	@Column(name = "SDC_DATE", nullable = false)
	@Builder.Default
	private LocalDateTime sdcDate = LocalDateTime.now();
	/**
	 * The cisDate
	 */
	@Column(name = "CIS_DATE", nullable = false)
	@Builder.Default
	private LocalDateTime cisDate = LocalDateTime.now();
	/**
	 * The signature
	 */
	@Column(name = "SIGNATURE", nullable = true)
	private String signature;
	/**
	 * The internalData
	 */
	@Column(name = "INTERNAL_DATA", nullable = true)
	private String internalData;

	/**
	 * The itemNumber
	 */
	@Column(name = "ITEM_NUMBER", nullable = false)
	private Integer itemNumber;
	/**
	 * The totalTaxAmount
	 */
	@Column(name = "TOTAL_TAX_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal totalTaxAmount = BigDecimal.ZERO;
	/**
	 * The totalAmountIncludingTax
	 */
	@Column(name = "TOTAL_AMOUNT_INCLUDING_TAX", nullable = true)
	@Builder.Default
	private BigDecimal totalAmountIncludingTax = BigDecimal.ZERO;
	/**
	 * The amountWithoutTax
	 */
	
	@Column(name = "TOTAL_AMOUNT_WITHOU_TAX", nullable = true)
	@Builder.Default
	private BigDecimal amountWithoutTax = BigDecimal.ZERO;
	
	
	/**
	 * The totalAPayer
	 */
	@Column(name = "TOTAL_A_PAYER", nullable = true)
	@Builder.Default
	private BigDecimal totalAPayer = BigDecimal.ZERO;
	/**
	 * The totalDiscountAmount
	 */
	@Column(name = "TOTAL_DISCOUNT_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal totalDiscountAmount = BigDecimal.ZERO;
	/**
	 * The grossAmount
	 */
	@Column(name = "GROSS_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal grossAmount = BigDecimal.ZERO;
	/**
	 * The paymentMethod
	 */
	@Column(name = "PAYMENT_METHOD", nullable = false)
	@lombok.ToString.Include
	@Builder.Default
	private EPaymentMethod paymentMethod = EPaymentMethod.CASH;
	/**
	 * The refounded
	 */
	@Column(name = "REFUNDED", nullable = true)
	@Builder.Default
	private Boolean refounded = Boolean.FALSE;
	/**
	 * The refundReason
	 */
	@Column(name = "REFUND_REASON", nullable = true)
	private String refundReason;
	/**
	 * The originalCreationDate
	 */
	@Column(name = "ORIGINAL_CREATION_DATE", columnDefinition = "TIMESTAMP", nullable = true)
	@Builder.Default
	private LocalDateTime originalCreationDate = LocalDateTime.now();
	/**
	 * The qrCode
	 */
	@Column(name = "QR_CODE", nullable = true)
	private String qrCode;
	/**
	 * The printed
	 */
	@Column(name = "PRINTED", nullable = true)
	@Builder.Default
	private Boolean printed = Boolean.FALSE;
	/**
	 * The pushedToFen
	 */
	@Column(name = "PUSHED_TO_FEN", nullable = false)
	@Builder.Default
	private Boolean pushedToFen = Boolean.FALSE;
	/**
	 * The isExport
	 */
	@Column(name = "IS_EXPORT", nullable = false)
	@Builder.Default
	private Boolean isExport = Boolean.FALSE;
	/**
	 * The externalReferenceNumber
	 */
	@Column(name = "CIS_VERSION", nullable = false)
	private String cisVersion;
	/**
	 * The externalReferenceNumber
	 */
	@Column(name = "EXTERNAL_REFERENCE_NUMBER", nullable = true)
	private String externalReferenceNumber;
	/**
	 * The description
	 */
	@Column(name = "DESCRIPTION", nullable = true)
	private String description;
	/**
	 * The totalExtraAmount
	 */
	@Column(name = "TOTAL_EXTRA_AMOUNT", nullable = true)
	@Builder.Default
	private BigDecimal totalExtraAmount = BigDecimal.ZERO;
	/**
	 * The lines
	 */
	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@ToString.Exclude
    private List<InvoiceLine> lines;
	/**
	 * The sdcDailyReport
	 */
	@ManyToOne
	@JoinColumn(name = "SDC_DAILY_REPORT_ID", nullable = true)
	@ToString.Exclude
	private SdcDailyReport sdcDailyReport;
	/**
	 * The sdcInformation
	 */
	@ManyToOne
	@JoinColumn(name = "SDC_INFORMATION_ID", nullable = true)
	@ToString.Exclude
	private SdcInformation sdcInformation;

	/**
	 * The Taxpayer
	 */
	@ManyToOne
	@JoinColumn(name = "TAXPAYER_ID", nullable = true)
	private CoreTaxpayer taxpayer;
	/**
	 * The status
	 */
	@Column(name = "INVOICE_STATUS", nullable = true)
	@Builder.Default
	private InvoiceStatus status = InvoiceStatus.DRAFT;
	public String getTransactionTypeCode() {

		String invoiceNumberPerType = "";

		switch (this.transactionType) {
		case TRANSACTION_TYPE_SALE:

			invoiceNumberPerType = "V";
			break;
		case TRANSACTION_TYPE_REFUND:

			invoiceNumberPerType = "A";
			break;
		default:
			break;
		}

		return invoiceNumberPerType;
	}

	public String getInvoiceTypeCode() {
		String invoiceNumberPerType = "";

		switch (this.invoiceType) {
		case INVOICE_TYPE_NORMAL:

			invoiceNumberPerType = "N";
			break;
		case INVOICE_TYPE_TRAINING:

			invoiceNumberPerType = "T";
			break;
		case INVOICE_TYPE_COPY:

			invoiceNumberPerType = "C";
			break;
		case INVOICE_TYPE_PROFORMA:

			invoiceNumberPerType = "P";
			break;
		default:
			break;
		}

		return invoiceNumberPerType;
	}
}

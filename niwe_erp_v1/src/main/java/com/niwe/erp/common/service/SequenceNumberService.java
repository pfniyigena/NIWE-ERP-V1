package com.niwe.erp.common.service;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.domain.ESequenceType;
import com.niwe.erp.common.domain.SequenceNumber;
import com.niwe.erp.common.repository.SequenceNumberRepository;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class SequenceNumberService {

	/**
	 * The sequenceNumberRepository
	 */
	private final SequenceNumberRepository sequenceNumberRepository;

	/**
	 * @param type
	 * @return
	 */
	private String getNextSequenceNumber(final ESequenceType type) {

		SequenceNumber result = sequenceNumberRepository.findByType(type);

		if (result == null) {
			// That means we do not have any sequence, we need to create one
			SequenceNumber sequence = new SequenceNumber();
			sequence.setType(type);
			sequence.setSequence("1");
			// create the sequence
			sequenceNumberRepository.save(sequence);
			return sequence.getSequence();
		}

		Long nextSequence = Long.parseLong(result.getSequence()) + 1;
		result.setSequence(nextSequence.toString());
		sequenceNumberRepository.save(result);
		return result.getSequence();
	}

	/**
	 * @return
	 */
	public String getNextTaxpayerCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.TAXPAYER);
		return "COMP" + StringUtils.leftPad(sequence, 3, "0");
	}

	/**
	 * @return
	 */
	public String getNextTaxpayerBranchCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.BRANCH);
		return "BRA" + StringUtils.leftPad(sequence, 3, "0");
	}
	public String getNextItemCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.ITEM);
		return "ITEM" + StringUtils.leftPad(sequence, 9, "0");
	}
	public String getNextInvoiceCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.INVOICE);
		return "INV" + StringUtils.leftPad(sequence, 9, "0");
	}
	public String getNextChargeCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.CHARGE);
		return "CHG" + StringUtils.leftPad(sequence, 3, "0");
	}
	public String getNextWarehouseCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.WAREHOUSE);
		return "WH" + StringUtils.leftPad(sequence, 3, "0");
	}
	public String getNextShelfCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.SHELF);
		return "POS" + StringUtils.leftPad(sequence, 3, "0");
	}
	public String getNextPurchaseCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.PURCHASE);
		return "PU" + StringUtils.leftPad(sequence, 9, "0");
	}
	public String getNextGoodReceivedCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.GOOD_RECEIVE_NOTE);
		return "GRN" + StringUtils.leftPad(sequence, 9, "0");
	}
	public String getNextPaymentMethodCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.PAYMENT_METHOD);
		return "PM" + StringUtils.leftPad(sequence, 2, "0");
	}
	public String getNextStockTransferCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.STOCK_TRANSFER);
		return "ST" + StringUtils.leftPad(sequence, 9, "0");
	}
	public String getNextSaleCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.SALE);
		return "SA" + StringUtils.leftPad(sequence, 9, "0");
	}
	/**
	 * @return
	 */
	public String getNextStandCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.STAND);
		return "STA" + StringUtils.leftPad(sequence, 3, "0");
	}
	/**
	 * @return
	 */
	public String getNextItemCategoryCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.ITEM_CATEGOTY);
		return "IC" + StringUtils.leftPad(sequence, 3, "0");
	}
	/**
	 * @return
	 */
	public String getNextItemBrandCode() {
		String sequence = this.getNextSequenceNumber(ESequenceType.ITEM_BRAND);
		return "IB" + StringUtils.leftPad(sequence, 3, "0");
	}

	public String getNextCustomerCode() {
		String sequence = this.getNextSequenceNumber(ESequenceType.CUSTOMER);
		return "CL" + StringUtils.leftPad(sequence, 4, "0");
	}

	public String getNextSupplierCode() {
		String sequence = this.getNextSequenceNumber(ESequenceType.SUPPLIER);
		return "SP" + StringUtils.leftPad(sequence, 4, "0");
	}
}

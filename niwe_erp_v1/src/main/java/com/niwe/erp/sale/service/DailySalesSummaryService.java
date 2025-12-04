package com.niwe.erp.sale.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.sale.domain.DailySalesSummary;
import com.niwe.erp.sale.domain.DailySalesSummaryPayment;
import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.domain.TransactionType;
import com.niwe.erp.sale.repository.DailySalesSummaryPaymentRepository;
import com.niwe.erp.sale.repository.DailySalesSummaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DailySalesSummaryService {

	private final DailySalesSummaryRepository dailySalesSummaryRepository;
	private final DailySalesSummaryPaymentRepository dailySalesSummaryPaymentRepository;

	public DailySalesSummary save(Sale sale) {

		LocalDate saleDate = LocalDate.ofInstant(sale.getSaleDate(), ZoneId.systemDefault());

		DailySalesSummary summary = dailySalesSummaryRepository.findBySummaryDate(saleDate).orElseGet(() -> {
			DailySalesSummary s = new DailySalesSummary();
			s.setSummaryDate(saleDate);
			s.setTaxpayer(sale.getTaxpayer());
			return dailySalesSummaryRepository.save(s);
		});

		// 3. Get or create payment-method daily summary
		DailySalesSummaryPayment paymentSummary = dailySalesSummaryPaymentRepository
				.findBySummaryIdAndPaymentMethodId(summary.getId(), sale.getPaymentMethod().getId()).orElseGet(() -> {
					DailySalesSummaryPayment p = new DailySalesSummaryPayment();
					p.setSummary(summary);
					p.setPaymentMethod(sale.getPaymentMethod());
					return dailySalesSummaryPaymentRepository.save(p);
				});

		if (sale.getTransactionType().equals(TransactionType.SALE)) {
			summary.setTotalAmountToPay(summary.getTotalAmountToPay().add(sale.getTotalAmountToPay()));
			summary.setTotalTaxAmount(summary.getTotalTaxAmount().add(sale.getTotalTaxAmount()));
			summary.setTotalAmountHorsTax(summary.getTotalAmountHorsTax().add(sale.getTotalAmountHorsTax()));
			summary.setTotalAmountInclusiveTax(
					summary.getTotalAmountInclusiveTax().add(sale.getTotalAmountInclusiveTax()));
			summary.setTotalDiscountAmount(summary.getTotalDiscountAmount().add(sale.getTotalDiscountAmount()));
			summary.setTotalGrossAmount(summary.getTotalGrossAmount().add(sale.getTotalGrossAmount()));
			summary.setTotalCost(summary.getTotalCost().add(sale.getTotalCost()));
		} else {
			summary.setTotalAmountToPay(summary.getTotalAmountToPay().subtract(sale.getTotalAmountToPay()));
			summary.setTotalTaxAmount(summary.getTotalTaxAmount().subtract(sale.getTotalTaxAmount()));
			summary.setTotalAmountHorsTax(summary.getTotalAmountHorsTax().subtract(sale.getTotalAmountHorsTax()));
			summary.setTotalAmountInclusiveTax(
					summary.getTotalAmountInclusiveTax().subtract(sale.getTotalAmountInclusiveTax()));
			summary.setTotalDiscountAmount(summary.getTotalDiscountAmount().subtract(sale.getTotalDiscountAmount()));
			summary.setTotalGrossAmount(summary.getTotalGrossAmount().subtract(sale.getTotalGrossAmount()));
			summary.setTotalCost(summary.getTotalCost().subtract(sale.getTotalCost()));
		}
		summary.setTotalProfit(summary.getTotalAmountHorsTax().subtract(summary.getTotalCost()));
		summary.setNumberOfReceipts(summary.getNumberOfReceipts() + 1);
		dailySalesSummaryRepository.save(summary);

		if (sale.getTransactionType().equals(TransactionType.SALE)) {
			paymentSummary.setTotalPaidAmount(paymentSummary.getTotalPaidAmount().add(sale.getTotalAmountToPay()));
		} else {
			paymentSummary.setTotalPaidAmount(paymentSummary.getTotalPaidAmount().subtract(sale.getTotalAmountToPay()));
		}

		paymentSummary.setNumberOfTransactions(paymentSummary.getNumberOfTransactions() + 1);

		dailySalesSummaryPaymentRepository.save(paymentSummary);
		return summary;
	}

	public List<DailySalesSummary> findAll() {
		return dailySalesSummaryRepository.findAll(Sort.by(Sort.Direction.DESC, "summaryDate"));
	}

	public DailySalesSummary findById(String id) {
		return dailySalesSummaryRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("DailySalesSummary not found with id " + id));
	}

	public DailySalesSummary findBySummaryDate(LocalDate summaryDate) {
		return dailySalesSummaryRepository.findBySummaryDate(summaryDate).orElseThrow(
				() -> new ResourceNotFoundException("DailySalesSummary not found with Date " + summaryDate));
	}

}

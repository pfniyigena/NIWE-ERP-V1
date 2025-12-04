package com.niwe.erp.core.web.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.niwe.erp.sale.domain.DailySalesSummaryPayment;
import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.domain.TransactionType;
import com.niwe.erp.sale.repository.DailySalesSummaryRepository;
import com.niwe.erp.sale.service.SaleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {
	private final DailySalesSummaryRepository dailySalesSummaryRepository;
	private final SaleService saleService;
	private BigDecimal cashAmount = BigDecimal.ZERO;
	private BigDecimal momoAmount = BigDecimal.ZERO;
	private BigDecimal vubaAmount = BigDecimal.ZERO;
	private BigDecimal visaAmount= BigDecimal.ZERO;

	@GetMapping("/")
	public String inde(Model model) {
		log.info("---------INDEX----------------------------------");
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal totalTax = BigDecimal.ZERO;
		BigDecimal totalSales = BigDecimal.ZERO;
		BigDecimal totalRefunds = BigDecimal.ZERO;

		List<Sale> sales = saleService.getSalesByMonth(YearMonth.now());
		for (Sale sale : sales) {
			BigDecimal amount = sale.getTotalAmountToPay() != null ? sale.getTotalAmountToPay() : BigDecimal.ZERO;
			BigDecimal tax = sale.getTotalTaxAmount() != null ? sale.getTotalTaxAmount() : BigDecimal.ZERO;

			if (sale.getTransactionType() == TransactionType.REFUND) {
				amount = amount.negate();
				tax = tax.negate();
				totalRefunds = totalRefunds.add(sale.getTotalAmountToPay());
			} else {
				totalSales = totalSales.add(sale.getTotalAmountToPay());
			}
			totalAmount = totalAmount.add(amount);
			totalTax = totalTax.add(tax);
		}
		model.addAttribute("totalTax", totalTax);
		model.addAttribute("totalAmount", totalAmount);
		model.addAttribute("totalRefunds", totalRefunds);
		model.addAttribute("totalSales", totalSales);
		dailySalesSummaryRepository.findBySummaryDate(LocalDate.now()).ifPresent(s -> {
			List<DailySalesSummaryPayment> payments = s.getPayments();
			for (DailySalesSummaryPayment payment : payments) {
				if (payment.getPaymentMethod().getName().toLowerCase().contains(("Cash").toLowerCase())) {

					this.cashAmount = payment.getTotalPaidAmount();
				}
				if (payment.getPaymentMethod().getName().toLowerCase().contains(("mtn").toLowerCase())) {
					this.momoAmount = momoAmount.add(payment.getTotalPaidAmount());

				}
				if (payment.getPaymentMethod().getName().toLowerCase().contains(("airtel").toLowerCase())) {
					this.momoAmount = momoAmount.add(payment.getTotalPaidAmount());

				}
				if (payment.getPaymentMethod().getName().toLowerCase().contains(("vuba").toLowerCase())) {

					this.vubaAmount = payment.getTotalPaidAmount();
				}
				if (payment.getPaymentMethod().getName().toLowerCase().contains(("visa").toLowerCase())) {

					this.visaAmount = payment.getTotalPaidAmount();
				}

			}
		});
		model.addAttribute("cash", cashAmount);
		model.addAttribute("momo", momoAmount);
		model.addAttribute("visa", visaAmount);
		model.addAttribute("vuba", vubaAmount);
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/v2")
	public String index() {
		return "index-2";
	}
}

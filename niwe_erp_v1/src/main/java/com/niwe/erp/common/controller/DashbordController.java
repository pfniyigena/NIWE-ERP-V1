package com.niwe.erp.common.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niwe.erp.common.util.NiweErpCommonUrlConstants;
import com.niwe.erp.core.repository.CoreItemRepository;
import com.niwe.erp.core.view.BrandItemView;
import com.niwe.erp.core.view.CategoryItemView;
import com.niwe.erp.core.view.DeadStockItem;
import com.niwe.erp.core.view.FastMoveItem;
import com.niwe.erp.inventory.repository.LocationStockRepository;
import com.niwe.erp.inventory.repository.StockMovementRepository;
import com.niwe.erp.inventory.repository.WarehouseStockRepository;
import com.niwe.erp.inventory.web.view.InflowOutflowSaleView;
import com.niwe.erp.sale.domain.DailySalesSummaryPayment;
import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.domain.TransactionType;
import com.niwe.erp.sale.repository.DailySalesSummaryRepository;
import com.niwe.erp.sale.service.SaleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = NiweErpCommonUrlConstants.DASHBOARDS_URL)
@Slf4j
@RequiredArgsConstructor
public class DashbordController {
	private final DailySalesSummaryRepository dailySalesSummaryRepository;
	private final SaleService saleService;
	private final CoreItemRepository coreItemRepository;
	private final WarehouseStockRepository warehouseStockRepository;
	private final LocationStockRepository locationStockRepository;
	private final StockMovementRepository stockMovementRepository;
	private BigDecimal cashAmount = BigDecimal.ZERO;
	private BigDecimal momoAmount = BigDecimal.ZERO;
	private BigDecimal vubaAmount = BigDecimal.ZERO;
	private BigDecimal visaAmount = BigDecimal.ZERO;
	@GetMapping("/")
	public String index(Model model) {
		log.info("---------INDEX----------------------------------");
		cashAmount = BigDecimal.ZERO;
		momoAmount = BigDecimal.ZERO;
		vubaAmount = BigDecimal.ZERO;
		visaAmount = BigDecimal.ZERO;
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

	@GetMapping("/sales")
	public String sales(Model model) {

		log.info("---------DASHBOARD SALES----------------------------------");
		cashAmount = BigDecimal.ZERO;
		momoAmount = BigDecimal.ZERO;
		vubaAmount = BigDecimal.ZERO;
		visaAmount = BigDecimal.ZERO;
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
				if (payment.getPaymentMethod().getName().toLowerCase().contains(("mtn").toLowerCase())
						|| payment.getPaymentMethod().getName().toLowerCase().contains(("momo").toLowerCase())) {
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
		List<FastMoveItem> fastItems = coreItemRepository.findFastMoveItems();
		List<DeadStockItem> deadItems =coreItemRepository.findDeadStockItems(); 
		model.addAttribute("cash", cashAmount);
		model.addAttribute("momo", momoAmount);
		model.addAttribute("visa", visaAmount);
		model.addAttribute("vuba", vubaAmount);
		model.addAttribute("fastItems", fastItems);
		model.addAttribute("deadItems", deadItems);
		return NiweErpCommonUrlConstants.DASHBOARDS_SALE_PAGE;
	}

	@GetMapping("/inventory")
	public String inventory(Model model) {
		BigDecimal stockValue = warehouseStockRepository.findStockValue();
		BigDecimal standValue = locationStockRepository.findStandsValue();
		List<CategoryItemView> categories=warehouseStockRepository.stockByCategory();
		List<BrandItemView> brands=warehouseStockRepository.stockByBrand();
		List<InflowOutflowSaleView> histories=stockMovementRepository.findInflowOutflowSaleView();
		log.info("histories:{}",histories.size());
		model.addAttribute("categories", categories);
		model.addAttribute("brands", brands);
		model.addAttribute("stockValue", stockValue);
		model.addAttribute("standValue", standValue);
		model.addAttribute("histories", histories);
		return NiweErpCommonUrlConstants.DASHBOARDS_INVENTORY_PAGE;
	}

}

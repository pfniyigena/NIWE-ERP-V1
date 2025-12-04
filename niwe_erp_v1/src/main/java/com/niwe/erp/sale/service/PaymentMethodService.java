package com.niwe.erp.sale.service;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.sale.domain.PaymentMethod;
import com.niwe.erp.sale.repository.PaymentMethodRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

	private final PaymentMethodRepository paymentMethodRepository;
	private final SequenceNumberService sequenceNumberService;

	public PaymentMethod save(String name) {

		PaymentMethod paymentMethod = paymentMethodRepository.findByNameContainingIgnoreCase(name).orElseGet(() -> {
			return paymentMethodRepository.save(PaymentMethod.builder().name(name)
					.internalCode(sequenceNumberService.getNextPaymentMethodCode()).build());
		});
		return paymentMethod;
	}

}

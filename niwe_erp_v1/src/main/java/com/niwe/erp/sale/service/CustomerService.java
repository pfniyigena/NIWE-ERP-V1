package com.niwe.erp.sale.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.sale.domain.Customer;
import com.niwe.erp.sale.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
	private final CustomerRepository customerRepository;
	private final SequenceNumberService sequenceNumberService;

	public Customer save(Customer customer) {
		if (customerRepository.findByCustomerTin(customer.getCustomerTin()).isPresent()) {
			return update(customer);
		}
		log.info("=============customer:{},customerId:{}", customer, customer.getId());
		if (customer.getId() != null) {
			return update(customer);
		} else {
			if (customer.getInternalCode() == null || customer.getInternalCode().isBlank())
				customer.setInternalCode(sequenceNumberService.getNextCustomerCode());
			return customerRepository.save(customer);
		}

	}

	public Customer update(Customer customer) {
		Customer exist = customerRepository.findByCustomerTin(customer.getCustomerTin()).get();
		exist.setCustomerTin(customer.getCustomerTin());
		exist.setCustomerPhone(customer.getCustomerPhone());
		exist.setCustomerName(customer.getCustomerName());
		return customerRepository.save(exist);

	}

	public List<Customer> findAll() {

		return customerRepository.findAll();
	}

	public Customer findById(String id) {
		return customerRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
	}

}

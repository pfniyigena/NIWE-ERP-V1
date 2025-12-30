package com.niwe.erp.sale.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.core.dto.CustomerDTO;
import com.niwe.erp.core.helper.ItemExcelHelper;
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

	public Page<Customer> findAllPageable(Pageable pageable) {

		return customerRepository.findAll(pageable);
	}

	public Page<CustomerDTO> findAllAsDto(Pageable pageable) {
		return customerRepository.findAllAsDto(pageable);
	}

	public Customer findById(String id) {
		return customerRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
	}

	@Transactional
	public void impotExcelFile(MultipartFile file) throws IOException {
		List<Customer> brands = ItemExcelHelper.excelToCustomers(file.getInputStream());
		brands.forEach((n) -> {
			save(n);
		});
	}

	@Transactional
	public void createCustomers(List<CustomerDTO> customers) {
		customers.forEach((n) -> {
			log.info("Incomming customer:{}", n);
			Customer customer = new Customer();
			customer.setCustomerEmail(n.customerEmail());
			customer.setCustomerName(n.customerName());
			customer.setCustomerTin(n.tinNumber());
			customer.setCustomerPhone(n.customerPhone());
			save(customer);
		});

	}

	public Page<CustomerDTO> findByLastUpdatedAfter(LocalDateTime lastSyn, Pageable pageable) {
		return customerRepository.findByLastUpdatedAfter(lastSyn, pageable);
	}

}

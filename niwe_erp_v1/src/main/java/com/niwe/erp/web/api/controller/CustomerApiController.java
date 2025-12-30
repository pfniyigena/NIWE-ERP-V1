package com.niwe.erp.web.api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niwe.erp.core.dto.CustomerDTO;
import com.niwe.erp.sale.domain.Shelf;
import com.niwe.erp.sale.service.CustomerService;
import com.niwe.erp.sale.service.ShelfService;
import com.niwe.erp.web.api.dto.NiweCommonDataRequest;
import com.niwe.erp.web.api.dto.NiweCommonRequest;
import com.niwe.erp.web.api.dto.NiweCommonResponse;
import com.niwe.erp.web.util.NiweErpApiUrlConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = NiweErpApiUrlConstants.NIWE_API + NiweErpApiUrlConstants.API_CUSTOMERS_URL)
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Customers", description = "Customer API for Niwe ERP")
public class CustomerApiController {
	private final CustomerService customerService;
	private final ShelfService shelfService;

	@Operation(summary = "Get Items", description = "Retrieve  item records")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Items found"),
			@ApiResponse(responseCode = "404", description = "Items not found") })
	@PostMapping(headers = NiweErpApiUrlConstants.NIWE_API_V1)
	public ResponseEntity<NiweCommonResponse> getItems(@RequestBody NiweCommonRequest niweCommonRequest) {
		log.info("Get Items from request: {}", niweCommonRequest);
		Sort sort = Sort.by("modifiedAt").ascending();
		Pageable pageable = PageRequest.of(niweCommonRequest.pageSize(), niweCommonRequest.recordSize(), sort);
		Page<CustomerDTO> page = customerService.findAllAsDto(pageable);
		List<CustomerDTO> list = page.getContent();
		NiweCommonResponse niweCommonResponse = new NiweCommonResponse(list, "000", "SUCCESS", page.getTotalPages());
		log.info("Get Items from response: {}", niweCommonResponse);
		return ResponseEntity.ok(niweCommonResponse);
	}

	@Operation(summary = "Get Customers Updates", description = "Retrieve  updated Customers records")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Items found"),
			@ApiResponse(responseCode = "404", description = "Items not found") })
	@PostMapping(value = "/updates", headers = NiweErpApiUrlConstants.NIWE_API_V1)
	public ResponseEntity<NiweCommonResponse> getUpdatedItems(@RequestBody NiweCommonRequest niweCommonRequest) {
		log.info("Get updates Customers from: {}", niweCommonRequest);
		Sort sort = Sort.by("lastUpdated").ascending();
		Shelf shelf = shelfService.findByInternalCode(niweCommonRequest.shelfCode());
		log.info("Get updates Customers from: {}", shelf);
		Pageable pageable = PageRequest.of(niweCommonRequest.pageSize(), niweCommonRequest.recordSize(), sort);
		Page<CustomerDTO> page = customerService.findByLastUpdatedAfter(shelf.getLastSynCustomer(), pageable);
		List<CustomerDTO> list = page.getContent();
		log.info("Get updates Customers : {}", list.size());
		shelf.setLastSynCustomer(LocalDateTime.now());
		shelfService.save(shelf);
		NiweCommonResponse niweCommonResponse = new NiweCommonResponse(list, "000", "SUCCESS", page.getTotalPages());
		log.info("Get updates Items from response: {}", niweCommonResponse);
		return ResponseEntity.ok(niweCommonResponse);
	}
	@Operation(summary = "Get Items Updates", description = "Retrieve  updated item records")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Items found"),
			@ApiResponse(responseCode = "404", description = "Items not found") })
	@PostMapping(value = "/push", headers = NiweErpApiUrlConstants.NIWE_API_V1)
	public ResponseEntity<NiweCommonResponse> push(@RequestBody NiweCommonDataRequest<List<CustomerDTO>> niweCommonRequest) {
		//log.info("Post push customers from: {}", niweCommonRequest);
		Shelf shelf = shelfService.findByInternalCode(niweCommonRequest.shelfCode());
		List<CustomerDTO> customers=(List<CustomerDTO>) niweCommonRequest.data();
		log.info("Push from: {},Customers:{}", shelf,customers.size());
		customerService.createCustomers(customers);
		NiweCommonResponse niweCommonResponse = new NiweCommonResponse(null, "000", "SUCCESS",0);
		log.info("Push from response: {}", niweCommonResponse);
		return ResponseEntity.ok(niweCommonResponse);
	}
}

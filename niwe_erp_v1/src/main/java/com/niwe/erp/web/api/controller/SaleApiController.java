package com.niwe.erp.web.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niwe.erp.sale.service.ShelfService;
import com.niwe.erp.web.api.dto.NiweCommonResponse;
import com.niwe.erp.web.api.dto.SaleRequest;
import com.niwe.erp.web.util.NiweErpApiUrlConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = NiweErpApiUrlConstants.NIWE_API + NiweErpApiUrlConstants.API_SALES_URL)
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Sales", description = "Sales API for Niwe ERP")
public class SaleApiController {
	private final ShelfService shelfService;

	@Operation(summary = "Receive Sales Data", description = "Receive sales  data records")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Items found"),
			@ApiResponse(responseCode = "404", description = "Items not found") })
	@PostMapping(headers = NiweErpApiUrlConstants.NIWE_API_V1)
	public ResponseEntity<NiweCommonResponse> receiveSale(@RequestBody SaleRequest request) {
		log.info("ReceiveSale from request: {}", request);
		shelfService.receiveSaleFromExternalShelf(request);
		NiweCommonResponse niweCommonResponse = new NiweCommonResponse(null, "000", "SUCCESS", 0);
		log.info("ReceiveSale from response: {}", niweCommonResponse);
		return ResponseEntity.ok(niweCommonResponse);
	}

	@PostMapping(headers = NiweErpApiUrlConstants.NIWE_API_V2)
	public ResponseEntity<NiweCommonResponse> receiveSaleV2(@RequestBody SaleRequest request) {
		log.info("ReceiveSale from request: {}", request);
		shelfService.receiveSaleFromExternalShelf(request);
		NiweCommonResponse niweCommonResponse = new NiweCommonResponse(null, "000", "SUCCESS", 0);
		log.info("ReceiveSale from response: {}", niweCommonResponse);
		return ResponseEntity.ok(niweCommonResponse);
	}
}

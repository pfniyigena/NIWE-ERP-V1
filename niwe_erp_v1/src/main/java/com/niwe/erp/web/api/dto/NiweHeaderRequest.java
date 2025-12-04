package com.niwe.erp.web.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NiweHeaderRequest(@JsonProperty("tin_number") String tinNumber,
		@JsonProperty("shelf_code") String shelfCode) {

}

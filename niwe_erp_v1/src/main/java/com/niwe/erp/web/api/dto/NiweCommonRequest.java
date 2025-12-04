package com.niwe.erp.web.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NiweCommonRequest(@JsonProperty("tin_number") String tinNumber,
		@JsonProperty("shelf_code") String shelfCode, @JsonProperty("page_size") int pageSize,
		@JsonProperty("record_size") int recordSize) {

}

package com.niwe.erp.web.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NiweCommonResponse(@JsonProperty("data") Object data, @JsonProperty("result_code") String resultCode,
		@JsonProperty("result_message") String resultMessage,@JsonProperty("total_record")int totalRecord) {

}

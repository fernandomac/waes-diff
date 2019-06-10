package com.waes.model;

import io.swagger.annotations.ApiModelProperty;

public class DiffResponse {

	@ApiModelProperty(
			value = "Result of comparison between left and right Base64 content sides",
			example = "DIFFERENT_SIZE"
	)
	private DiffResult result;
	
	@ApiModelProperty(
			value = "Offsets and content length regarding comparison if both sides had the same size but different content",
			example = "Offsets: 3084 3085 3086 - [ Content length: 3088 ]"
	)
	private String details;
	
	public DiffResult getResult() {
		return result;
	}
	
	public void setResult(DiffResult result) {
		this.result = result;
	}
	
	public String getDetails() {
		return details;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "DiffResponse [result=" + result + ", details=" + details + "]";
	}
}

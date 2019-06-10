package com.waes.model;

import io.swagger.annotations.ApiModelProperty;

public class DiffRequest {

	@ApiModelProperty(
			value = "Base64 encoded content to be compared to another",
			example = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPH", 
			required=true
			
	)
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	
}

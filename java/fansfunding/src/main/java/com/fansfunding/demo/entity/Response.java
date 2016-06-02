package com.fansfunding.demo.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
	@JsonProperty
	public boolean result;
	@JsonProperty
	public int errCode;
	@JsonProperty
	public T data;
}

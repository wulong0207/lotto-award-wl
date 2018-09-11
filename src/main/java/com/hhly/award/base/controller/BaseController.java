package com.hhly.award.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {
 
	protected  HttpServletRequest request;
	
	protected  HttpServletResponse response;

	public HttpServletRequest getRequest() {
		return request; 
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	

	
}

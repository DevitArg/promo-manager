package com.devit.promomanager.exception;

import com.devit.promomanager.api.model.ErrorResponse;

import javax.ws.rs.core.Response;

public class NotFoundException extends RestAPIException {

	public NotFoundException(String message) {
		super();
		errorResponse = new ErrorResponse();
		errorResponse.setHttpStatus(Response.Status.NOT_FOUND.getStatusCode());
		errorResponse.setMessage(message);
	}
}

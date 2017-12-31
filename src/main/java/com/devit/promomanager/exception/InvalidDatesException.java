package com.devit.promomanager.exception;

import com.devit.promomanager.api.model.ErrorResponse;

import javax.ws.rs.core.Response;

public class InvalidDatesException extends RestAPIException {

	public InvalidDatesException(String message) {
		super();
		errorResponse = new ErrorResponse();
		errorResponse.setHttpStatus(Response.Status.BAD_REQUEST.getStatusCode());
		errorResponse.setMessage(message);
	}
}

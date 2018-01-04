package com.devit.promomanager.exception;

import com.devit.promomanager.api.model.ErrorResponse;

import javax.ws.rs.core.Response;

public class InvalidBusinessException extends RestAPIException {

	public InvalidBusinessException(String businessId) {
		super();
		errorResponse = new ErrorResponse();
		errorResponse.setHttpStatus(Response.Status.BAD_REQUEST.getStatusCode());
		errorResponse.setMessage(String.format("Business with id %s may not exist", businessId));
	}
}

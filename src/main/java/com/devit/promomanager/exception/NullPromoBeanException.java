package com.devit.promomanager.exception;

import com.devit.promomanager.api.model.ErrorResponse;

import javax.ws.rs.core.Response;

public class NullPromoBeanException extends RestAPIException {

	public NullPromoBeanException() {
		super();
		errorResponse = new ErrorResponse();
		errorResponse.setHttpStatus(Response.Status.BAD_REQUEST.getStatusCode());
		errorResponse.setMessage("PromoBean must not be null");
	}
}

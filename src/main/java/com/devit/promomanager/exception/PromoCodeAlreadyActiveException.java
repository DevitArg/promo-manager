package com.devit.promomanager.exception;

import com.devit.promomanager.api.model.ErrorResponse;

import javax.ws.rs.core.Response;

public class PromoCodeAlreadyActiveException extends RestAPIException {

	public PromoCodeAlreadyActiveException(String existingPromoCode) {
		super();
		errorResponse = new ErrorResponse();
		errorResponse.setHttpStatus(Response.Status.CONFLICT.getStatusCode());
		errorResponse.setMessage(String.format("The promo with promoCode: %s is already with an ACTIVE status", existingPromoCode));
	}

}

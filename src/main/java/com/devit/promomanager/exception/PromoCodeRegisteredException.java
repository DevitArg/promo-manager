package com.devit.promomanager.exception;

import com.devit.promomanager.api.model.ErrorResponse;

import javax.ws.rs.core.Response;

/**
 * @author Lucas.Godoy on 21/11/17.
 */
public class PromoCodeRegisteredException extends RestAPIException {

	public PromoCodeRegisteredException(String existingPromoCode) {
		super();
		errorResponse = new ErrorResponse();
		errorResponse.setHttpStatus(Response.Status.CONFLICT.getStatusCode());
		errorResponse.setMessage(String.format("The promo with promoCode: %s already exists", existingPromoCode));
	}

}

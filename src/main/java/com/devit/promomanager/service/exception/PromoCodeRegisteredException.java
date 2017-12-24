package com.devit.promomanager.service.exception;

import com.devit.promomanager.api.handler.ApiError;
import com.devit.promomanager.api.handler.CustomApiException;

/**
 * @author Lucas.Godoy on 21/11/17.
 */
public class PromoCodeRegisteredException extends CustomApiException {

	public PromoCodeRegisteredException(ApiError apiError) {
		super(apiError);
	}

}

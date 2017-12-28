package com.devit.promomanager.api.exception.mappers.helper;


import com.devit.promomanager.api.model.ErrorResponse;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * @author Lucas.Godoy on 12/12/17.
 */
public class ExceptionMapperResponseHelper {

	public static Response getResponse(Response.Status errorStatus, String messages, Throwable exception) {
		return getResponse(errorStatus, messages, exception, null);
	}

	public static Response getResponse(Response.Status errorStatus, String messages, Throwable exception, HttpHeaders headers) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setHttpStatus(errorStatus.getStatusCode());
		errorResponse.setMessage(messages);
		errorResponse.setDeveloperMessage(exception.getMessage() != null ? exception.getMessage() : messages);

		Response.ResponseBuilder responseBuilder = Response.status(errorStatus).entity(errorResponse);

		if (headers != null) {
			responseBuilder.type(headers.getMediaType());
		}

		return responseBuilder.build();
	}

}

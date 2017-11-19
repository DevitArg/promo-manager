package com.devit.promomanager.api.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author Lucas.Godoy on 13/11/17.
 */
@Data
@AllArgsConstructor
public class ApiError {

	private HttpStatus httpStatus;
	private String message;

}

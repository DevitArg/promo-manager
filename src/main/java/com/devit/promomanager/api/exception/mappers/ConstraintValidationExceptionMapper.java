package com.devit.promomanager.api.exception.mappers;

import com.devit.promomanager.api.exception.mappers.helper.ExceptionMapperResponseHelper;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

/**
 * @author Lucas.Godoy on 12/12/17.
 */
@Provider
@Component
public class ConstraintValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException exception) {
		String messages = buildMessage(exception);
		return ExceptionMapperResponseHelper.getResponse(Response.Status.BAD_REQUEST, messages, exception);
	}

	private String buildMessage(ConstraintViolationException exception) {
		return exception.getConstraintViolations()
				.stream()
				.map(this::processViolations)
				.collect(Collectors.toList())
				.stream()
				.reduce((m1, m2) -> m1 + ", " + m2)
				.orElse("No errors");
	}

	private String processViolations(ConstraintViolation<?> violation) {
		return "Value "
				+ (violation.getInvalidValue() != null ? "'" + violation.getInvalidValue().toString() + "'" : "(null)")
				+ " of " + violation.getRootBeanClass().getSimpleName()
				+ "." + violation.getPropertyPath()
				+ ": " + violation.getMessage();
	}

}

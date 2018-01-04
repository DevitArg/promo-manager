package com.devit.promomanager.api.impl;

import com.devit.promomanager.api.PromoCrudApi;
import com.devit.promomanager.api.model.ActivatePromoBean;
import com.devit.promomanager.api.model.PromoBean;
import com.devit.promomanager.exception.*;
import com.devit.promomanager.service.PromoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

/**
 * @author Lucas.Godoy on 19/11/17.
 */
@Component
public class PromoCrudApiImpl implements PromoCrudApi {

	@Autowired
	private PromoService promoService;

	@Override
	public Response activatePromotion(ActivatePromoBean activatePromoBean) {
		try {
			promoService.activatePromotion(activatePromoBean);
			return Response.noContent().build();
		} catch (NotFoundException | PromoCodeAlreadyActiveException | InvalidDatesException e) {
			throw e.throwRestException();
		}
	}

	@Override
	public Response createPromotion(PromoBean promoBean) {
		try {
			return Response
					.status(Response.Status.CREATED)
					.entity(promoService.createPromotion(promoBean))
					.build();
		} catch (InvalidDatesException | InvalidBusinessException | NullPromoBeanException | PromoCodeRegisteredException e) {
			throw e.throwRestException();
		}
	}
}

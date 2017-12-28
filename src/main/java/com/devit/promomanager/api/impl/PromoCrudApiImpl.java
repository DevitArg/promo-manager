package com.devit.promomanager.api.impl;

import com.devit.promomanager.api.PromoCrudApi;
import com.devit.promomanager.api.model.PromoBean;
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
	public Response createPromotion(PromoBean promoBean) {
		PromoBean responseBean = promoService.createPromotion(promoBean);
		return Response
				.status(Response.Status.CREATED)
				.entity(responseBean)
				.build();
	}
}

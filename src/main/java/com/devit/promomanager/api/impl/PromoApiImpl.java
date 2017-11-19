package com.devit.promomanager.api.impl;

import com.devit.checkmein.api.PromoApi;
import com.devit.checkmein.api.model.PromoBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author Lucas.Godoy on 19/11/17.
 */
public class PromoApiImpl implements PromoApi {
	@Override
	public ResponseEntity<Void> createPromotion(@Valid @RequestBody PromoBean checkInBean) {
		return null;
	}
}

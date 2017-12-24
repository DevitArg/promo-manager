package com.devit.promomanager.api.impl;

import com.devit.promomanager.api.PromoApi;
import com.devit.promomanager.api.model.PromoBean;
import com.devit.promomanager.service.PromoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Lucas.Godoy on 19/11/17.
 */
@RestController
public class PromoApiImpl implements PromoApi {

	@Autowired
	private PromoService promoService;

	@Override
	public ResponseEntity<PromoBean> createPromotion(@Valid @RequestBody PromoBean promoBean) {
		return new ResponseEntity<PromoBean>(promoService.createPromotion(promoBean), HttpStatus.CREATED);
	}

}

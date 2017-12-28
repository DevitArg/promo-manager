package com.devit.promomanager.service;

import com.devit.promomanager.api.model.PromoBean;
import com.devit.promomanager.exception.InvalidDatesException;
import com.devit.promomanager.exception.NullPromoBeanException;
import com.devit.promomanager.exception.PromoCodeRegisteredException;

/**
 * @author Lucas.Godoy on 21/11/17.
 */
public interface PromoService {

	PromoBean createPromotion(PromoBean promoBean) throws InvalidDatesException, NullPromoBeanException, PromoCodeRegisteredException;

}

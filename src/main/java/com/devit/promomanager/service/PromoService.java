package com.devit.promomanager.service;

import com.devit.promomanager.api.model.ActivatePromoBean;
import com.devit.promomanager.api.model.PromoBean;
import com.devit.promomanager.exception.*;

/**
 * @author Lucas.Godoy on 21/11/17.
 */
public interface PromoService {

	PromoBean createPromotion(PromoBean promoBean) throws InvalidDatesException, NullPromoBeanException, PromoCodeRegisteredException, InvalidBusinessException;

	void activatePromotion(ActivatePromoBean activatePromoBean) throws NotFoundException, PromoCodeAlreadyActiveException, InvalidDatesException;
}

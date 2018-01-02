package com.devit.promomanager.service.activation;

import com.devit.promomanager.api.model.ActivatePromoBean;
import com.devit.promomanager.api.model.PromoStatus;
import com.devit.promomanager.exception.InvalidDatesException;
import com.devit.promomanager.persistense.document.PromoDocument;

public interface PromoActivationStrategy {

	boolean supports(PromoStatus oldStatus);

	PromoDocument activatePromo(PromoDocument promoDocument, ActivatePromoBean activatePromoBean) throws InvalidDatesException;

}

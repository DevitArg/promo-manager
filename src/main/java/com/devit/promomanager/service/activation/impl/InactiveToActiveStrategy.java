package com.devit.promomanager.service.activation.impl;

import com.devit.promomanager.api.model.ActivatePromoBean;
import com.devit.promomanager.api.model.PromoStatus;
import com.devit.promomanager.exception.InvalidDatesException;
import com.devit.promomanager.persistense.document.PromoDocument;
import com.devit.promomanager.persistense.repository.PromoRepository;
import com.devit.promomanager.service.activation.PromoActivationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class InactiveToActiveStrategy implements PromoActivationStrategy {

	@Autowired
	private PromoRepository promoRepository;

	@Autowired
	private RetryTemplate retryTemplate;

	@Override
	public boolean supports(PromoStatus oldStatus) {
		return PromoStatus.INACTIVE.equals(oldStatus);
	}

	@Override
	public PromoDocument activatePromo(PromoDocument promoDocument, ActivatePromoBean activatePromoBean) throws InvalidDatesException {
		if (!(activatePromoBean.getBegins() != null
				&& activatePromoBean.getExpires() != null
				&& activatePromoBean.getBegins().isBefore(activatePromoBean.getExpires()))) {
			throw new InvalidDatesException("Dates must be specified and beginning date before expiry date.");
		}

		promoDocument.setBegins(activatePromoBean.getBegins());
		promoDocument.setExpires(activatePromoBean.getExpires());
		promoDocument.setStatus(PromoStatus.ACTIVE);

		return saveOrUpdatePromo(promoDocument);
	}

	private PromoDocument saveOrUpdatePromo(PromoDocument promoDocument) {
		RetryCallback<PromoDocument, RuntimeException> retryCallback = context -> promoRepository.save(promoDocument);
		return retryTemplate.execute(retryCallback);
	}

}

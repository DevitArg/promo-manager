package com.devit.promomanager.service.activation;

import com.devit.promomanager.api.model.ActivatePromoBean;
import com.devit.promomanager.exception.InvalidDatesException;
import com.devit.promomanager.exception.NotFoundException;
import com.devit.promomanager.persistense.document.PromoDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromoActivationStrategyProvider {

	@Autowired
	private List<PromoActivationStrategy> activationStrategyList;

	public PromoDocument activatePromo(PromoDocument promoDocument, ActivatePromoBean activatePromoBean) throws NotFoundException, InvalidDatesException {
		PromoActivationStrategy activationStrategy = activationStrategyList
				.parallelStream()
				.filter(strategy -> strategy.supports(promoDocument.getStatus()))
				.findAny()
				.orElseThrow(() -> new NotFoundException(String.format("Could not change status %s to ACTIVE", promoDocument.getStatus())));

		return activationStrategy.activatePromo(promoDocument, activatePromoBean);
	}

}

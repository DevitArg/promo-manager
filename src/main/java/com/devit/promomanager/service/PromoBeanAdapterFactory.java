package com.devit.promomanager.service;

import com.devit.promomanager.api.model.PromoBean;
import com.devit.promomanager.api.model.PromoBeanAdapter;
import com.devit.promomanager.exception.InvalidBusinessException;
import com.devit.promomanager.exception.InvalidDatesException;
import com.devit.promomanager.exception.NullPromoBeanException;
import com.devit.promomanager.exception.PromoCodeRegisteredException;
import com.devit.promomanager.persistense.repository.PromoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PromoBeanAdapterFactory {

	@Autowired
	private PromoRepository promoRepository;

	public PromoBeanAdapter getPromoBeanAdapter(PromoBean promoBean) throws NullPromoBeanException,
			PromoCodeRegisteredException, InvalidDatesException, InvalidBusinessException {
		if (promoBean == null) {
			throw new NullPromoBeanException();
		} else if (isPromoCodeRegistered(promoBean.getPromoCode(), promoBean.getBusinessId())) {
			throw new PromoCodeRegisteredException(promoBean.getPromoCode());
		} else if (!isBusinessValid(promoBean.getBusinessId())){
			throw new InvalidBusinessException(promoBean.getBusinessId());
		}

		return new PromoBeanAdapter(promoBean);
	}

	private boolean isBusinessValid(String buisinessId) {
		// TODO: add business validation against business management service (?)
		return buisinessId != null;
	}

	private boolean isPromoCodeRegistered(String promoCode, String businessId) {
		return promoRepository.findByPromoCodeAndBusinessId(promoCode, businessId).isPresent();
	}

}

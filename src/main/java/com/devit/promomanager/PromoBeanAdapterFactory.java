package com.devit.promomanager;

import com.devit.promomanager.api.model.PromoBean;
import com.devit.promomanager.api.model.PromoBeanAdapter;
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

	public PromoBeanAdapter getPromoBeanAdapter(PromoBean promoBean) throws NullPromoBeanException, PromoCodeRegisteredException, InvalidDatesException {
		if (promoBean == null) {
			throw new NullPromoBeanException();
		}
		if (isPromoCodeRegistered(promoBean.getPromoCode())) {
			throw new PromoCodeRegisteredException(promoBean.getPromoCode());
		}
		return new PromoBeanAdapter(promoBean);
	}

	private boolean isPromoCodeRegistered(String promoCode) {
		return promoRepository.findByPromoCode(promoCode).isPresent();
	}

}

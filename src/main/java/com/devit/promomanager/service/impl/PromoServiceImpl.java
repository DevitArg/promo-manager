package com.devit.promomanager.service.impl;

import com.devit.promomanager.api.model.PromoBean;
import com.devit.promomanager.exception.InvalidDatesException;
import com.devit.promomanager.exception.NullPromoBeanException;
import com.devit.promomanager.exception.PromoCodeRegisteredException;
import com.devit.promomanager.persistense.document.PromoDocument;
import com.devit.promomanager.persistense.repository.PromoRepository;
import com.devit.promomanager.service.PromoService;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Lucas.Godoy on 21/11/17.
 */
@Service
public class PromoServiceImpl implements PromoService {

	@Autowired
	private DozerBeanMapper dozerBeanMapper;

	@Autowired
	private PromoRepository promoRepository;

	@Override
	public PromoBean createPromotion(PromoBean promoBean) throws InvalidDatesException, NullPromoBeanException, PromoCodeRegisteredException {
		validatePromoBean(promoBean);
		PromoDocument promoDocument = dozerBeanMapper.map(promoBean, PromoDocument.class);
		promoDocument = promoRepository.save(promoDocument);

		return dozerBeanMapper.map(promoDocument, PromoBean.class);
	}

	private void validatePromoBean(PromoBean promoBean) throws InvalidDatesException, NullPromoBeanException, PromoCodeRegisteredException {
		if (promoBean == null) {
			throw new NullPromoBeanException();
		}
		if (isPromoCodeRegistered(promoBean.getPromoCode())) {
			throw new PromoCodeRegisteredException(promoBean.getPromoCode());
		}
		if (!areValidDates(promoBean.getBegins(), promoBean.getExpires())) {
			throw new InvalidDatesException();
		}
	}

	private boolean isPromoCodeRegistered(String promoCode) {
		return promoRepository.findByPromoCode(promoCode).isPresent();
	}

	private boolean areValidDates(LocalDateTime begins, LocalDateTime expires) {
		boolean areDefined = begins != null && expires != null;

		boolean isValidInactive = begins == null && expires == null;
		boolean isValidForever = begins != null && expires == null;
		boolean isValidInAPeriodOfTime = areDefined && begins.isBefore(expires);

		boolean areValid = isValidInactive || isValidForever || isValidInAPeriodOfTime;

		boolean isNotValidBeginAfterExpire = areDefined && begins.isAfter(expires);
		boolean isNotValidNullBeginWithExpire = begins == null && expires != null;

		boolean isInvalid = isNotValidBeginAfterExpire || isNotValidNullBeginWithExpire;

		return areValid && !isInvalid;
	}

}

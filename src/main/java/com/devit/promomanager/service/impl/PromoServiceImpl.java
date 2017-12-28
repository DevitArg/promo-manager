package com.devit.promomanager.service.impl;

import com.devit.promomanager.api.model.PromoBean;
import com.devit.promomanager.persistense.document.PromoDocument;
import com.devit.promomanager.persistense.repository.PromoRepository;
import com.devit.promomanager.service.PromoService;
import com.devit.promomanager.exception.PromoCodeRegisteredException;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
	public PromoBean createPromotion(PromoBean promoBean) {
		if (promoBean == null) {
			throw new IllegalArgumentException("PromoBean must not be null");
		}
		if (isPromoCodeRegistered(promoBean.getPromoCode())) {
			throw (new PromoCodeRegisteredException(promoBean.getPromoCode())).throwRestException();
		}
		PromoDocument promoDocument = dozerBeanMapper.map(promoBean, PromoDocument.class);
		promoDocument = promoRepository.save(promoDocument);

		return dozerBeanMapper.map(promoDocument, PromoBean.class);
	}

	private boolean isPromoCodeRegistered(String promoCode) {
		return promoRepository.findByPromoCode(promoCode).isPresent();
	}

}

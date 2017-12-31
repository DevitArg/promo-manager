package com.devit.promomanager.service.impl;

import com.devit.promomanager.PromoBeanAdapterFactory;
import com.devit.promomanager.api.model.PromoBean;
import com.devit.promomanager.api.model.PromoBeanAdapter;
import com.devit.promomanager.exception.InvalidDatesException;
import com.devit.promomanager.exception.NullPromoBeanException;
import com.devit.promomanager.exception.PromoCodeRegisteredException;
import com.devit.promomanager.persistense.document.PromoDocument;
import com.devit.promomanager.persistense.repository.PromoRepository;
import com.devit.promomanager.service.PromoService;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private PromoBeanAdapterFactory promoBeanAdapterFactory;

	@Override
	public PromoBean createPromotion(PromoBean promoBean) throws InvalidDatesException, NullPromoBeanException, PromoCodeRegisteredException {
		PromoBeanAdapter promoBeanAdapter = promoBeanAdapterFactory.getPromoBeanAdapter(promoBean);
		PromoDocument promoDocument = dozerBeanMapper.map(promoBeanAdapter.getPromoBean(), PromoDocument.class);
		promoDocument = promoRepository.save(promoDocument);

		return dozerBeanMapper.map(promoDocument, PromoBean.class);
	}

}

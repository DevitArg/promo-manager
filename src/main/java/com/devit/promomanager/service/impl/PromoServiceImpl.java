package com.devit.promomanager.service.impl;

import com.devit.promomanager.service.PromoBeanAdapterFactory;
import com.devit.promomanager.api.model.ActivatePromoBean;
import com.devit.promomanager.api.model.PromoBean;
import com.devit.promomanager.api.model.PromoBeanAdapter;
import com.devit.promomanager.api.model.PromoStatus;
import com.devit.promomanager.exception.*;
import com.devit.promomanager.kafka.KafkaTopicProperties;
import com.devit.promomanager.persistense.document.PromoDocument;
import com.devit.promomanager.persistense.repository.PromoRepository;
import com.devit.promomanager.service.PromoService;
import com.devit.promomanager.service.activation.PromoActivationStrategyProvider;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
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

	@Autowired
	private PromoActivationStrategyProvider activationStrategyProvider;

	@Autowired
	private KafkaTemplate kafkaTemplate;

	@Autowired
	private KafkaTopicProperties kafkaTopicProperties;

	@Autowired
	private RetryTemplate retryTemplate;

	@Override
	public PromoBean createPromotion(PromoBean promoBean) throws InvalidDatesException, NullPromoBeanException, PromoCodeRegisteredException {
		PromoBeanAdapter promoBeanAdapter = promoBeanAdapterFactory.getPromoBeanAdapter(promoBean);
		PromoDocument promoDocument = dozerBeanMapper.map(promoBeanAdapter.getPromoBean(), PromoDocument.class);

		promoDocument = saveOrUpdatePromo(promoDocument);

		if (PromoStatus.ACTIVE.equals(promoDocument.getStatus())) {
			kafkaTemplate.send(kafkaTopicProperties.getTopic(), promoDocument);
		}

		return dozerBeanMapper.map(promoDocument, PromoBean.class);
	}

	@Override
	public void activatePromotion(ActivatePromoBean activatePromoBean) throws NotFoundException, PromoCodeAlreadyActiveException, InvalidDatesException {
		PromoDocument promoDocument = promoRepository.findByPromoCode(activatePromoBean.getPromoCode())
				.orElseThrow(() -> new NotFoundException("Provided promoCode has not been found it may not exist"));

		if (!promoDocument.getStatus().equals(PromoStatus.ACTIVE)) {
			activationStrategyProvider.activatePromo(promoDocument, activatePromoBean);
		} else {
			throw new PromoCodeAlreadyActiveException(promoDocument.getPromoCode());
		}

		kafkaTemplate.send(kafkaTopicProperties.getTopic(), promoDocument);
	}

	private PromoDocument saveOrUpdatePromo(PromoDocument promoDocument) {
		RetryCallback<PromoDocument, RuntimeException> retryCallback = context -> promoRepository.save(promoDocument);
		return retryTemplate.execute(retryCallback);
	}
}

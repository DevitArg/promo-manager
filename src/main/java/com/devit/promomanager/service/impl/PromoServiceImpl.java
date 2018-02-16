package com.devit.promomanager.service.impl;

import com.devit.promomanager.api.clients.checkmein.api.WhoAreThereApi;
import com.devit.promomanager.api.clients.checkmein.model.CheckInBean;
import com.devit.promomanager.api.model.ActivatePromoBean;
import com.devit.promomanager.api.model.PromoBean;
import com.devit.promomanager.api.model.PromoBeanAdapter;
import com.devit.promomanager.api.model.PromoStatus;
import com.devit.promomanager.exception.*;
import com.devit.promomanager.kafka.KafkaTopicProperties;
import com.devit.promomanager.persistense.document.PromoDocument;
import com.devit.promomanager.persistense.repository.PromoRepository;
import com.devit.promomanager.service.PromoBeanAdapterFactory;
import com.devit.promomanager.service.PromoService;
import com.devit.promomanager.service.activation.PromoActivationStrategyProvider;
import com.devit.promomanager.servicetoservice.WebClientFactory;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

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

	@Autowired
	private WebClientFactory webClientFactory;

	@Override
	public PromoBean createPromotion(PromoBean promoBean) throws InvalidDatesException, NullPromoBeanException,
			PromoCodeRegisteredException, InvalidBusinessException {
		PromoBeanAdapter promoBeanAdapter = promoBeanAdapterFactory.getPromoBeanAdapter(promoBean);
		PromoDocument promoDocument = dozerBeanMapper.map(promoBeanAdapter.getPromoBean(), PromoDocument.class);

		promoDocument = saveOrUpdatePromo(promoDocument);

		notifyActivePromotion(promoDocument);

		return dozerBeanMapper.map(promoDocument, PromoBean.class);
	}

	@Override
	public void activatePromotion(ActivatePromoBean activatePromoBean) throws NotFoundException, PromoCodeAlreadyActiveException, InvalidDatesException {
		// TODO: add business validation against business management service (?)
		PromoDocument promoDocument = promoRepository.findByPromoCodeAndBusinessId(activatePromoBean.getPromoCode(), activatePromoBean.getBusinessId())
				.orElseThrow(() -> new NotFoundException("Either the provided promoCode or businessId have not been found they may not exist"));

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

	private void notifyActivePromotion(PromoDocument promoDocument) {
		if (PromoStatus.ACTIVE.equals(promoDocument.getStatus())) {
			List<CheckInBean> users = new ArrayList<>();
			WhoAreThereApi apiClient = (WhoAreThereApi) webClientFactory.get(WhoAreThereApi.class, "check-me-in");
			Response response = apiClient.whoAreThere(promoDocument.getBusinessId());
			if (response != null) {
				users = response.readEntity(new GenericType<List<CheckInBean>>() {
				});
			}
			users.forEach(u -> kafkaTemplate.send("sms-to-send", u));
			kafkaTemplate.send(kafkaTopicProperties.getTopic(), promoDocument);
		}
	}
}

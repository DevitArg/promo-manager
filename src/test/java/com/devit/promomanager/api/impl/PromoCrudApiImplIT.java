package com.devit.promomanager.api.impl;

import com.devit.promomanager.AbstractIT;
import com.devit.promomanager.api.model.ActivatePromoBean;
import com.devit.promomanager.api.model.ErrorResponse;
import com.devit.promomanager.api.model.PromoBean;
import com.devit.promomanager.api.model.PromoStatus;
import com.devit.promomanager.persistense.document.PromoDocument;
import com.devit.promomanager.persistense.repository.PromoRepository;
import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.jayway.restassured.RestAssured.given;
import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Lucas.Godoy on 21/11/17.
 */
public class PromoCrudApiImplIT extends AbstractIT {

	private static final String BASE_PATH = "/promoManager/v1";
	private static final String PROMO_API_PATH = BASE_PATH + "/promo";

	@Autowired
	private PromoRepository promoRepository;

	@Autowired
	private DozerBeanMapper dozerBeanMapper;

	@Test
	public void createPromotionWithValidFieldsNonNullDates_test() {
		createActivePromotionWithValidFieldsNonNullDates();
	}

	private PromoBean createActivePromotionWithValidFieldsNonNullDates() {
		PromoBean requestBody = buildValidBrandNewPromoBean();
		return requestAndVerifySuccessfulResponse(requestBody, PromoStatus.ACTIVE);
	}

	@Test
	public void createPromotionWithValidFieldsNullDates_test() {
		createInactivePromotionWithValidFieldsNullDates();
	}

	public PromoBean createInactivePromotionWithValidFieldsNullDates() {
		PromoBean requestBody = buildBrandNewPromoBeanFlexDates(null, null);
		return requestAndVerifySuccessfulResponse(requestBody, PromoStatus.INACTIVE);
	}

		@Test
	public void createPromotionNullNameFailure_test() {
		requestNoNullableFieldValidation("name");
	}

	@Test
	public void createPromotionNullDescriptionFailure_test() {
		requestNoNullableFieldValidation("description");
	}

	@Test
	public void createPromotionNullPromoCodeFailure_test() {
		requestNoNullableFieldValidation("promoCode");
	}

	@Test
	public void createPromoWithNullBodyFailure_test() {
		given()
				.body("")
				.when()
				.post(PROMO_API_PATH)
				.then().log().body(true)
				.statusCode(equalTo(HttpStatus.BAD_REQUEST.value()));
	}

	@Test
	public void createPromotionAlreadyCreatedFailure_test() {
		PromoBean requestBody = buildValidBrandNewPromoBean();
		promoRepository.save(dozerBeanMapper.map(requestBody, PromoDocument.class));

		ErrorResponse errorResponse =
				given()
						.body(requestBody)
						.when()
						.post(PROMO_API_PATH)
						.then()
						.statusCode(equalTo(HttpStatus.CONFLICT.value()))
						.extract().body().as(ErrorResponse.class);

		assertThat(errorResponse).isNotNull();
		assertThat(errorResponse.getMessage()).containsIgnoringCase(String.format("The promo with promoCode: %s already exists"
				, requestBody.getPromoCode()));
	}

	@Test
	public void createPromotionWithInvalidDatesExpiryBeforeBeginningFailure_test() {
		LocalDateTime begins = LocalDateTime.now();
		LocalDateTime expires = begins.minusSeconds(1);
		createPromotionWithInvalidDates(begins, expires, "Beginning date must be greater than expiry date");
	}

	@Test
	public void createPromotionWithInvalidDatesNullExpiryFailure_test() {
		LocalDateTime begins = LocalDateTime.now();
		LocalDateTime expires = null;
		createPromotionWithInvalidDates(begins, expires, "Either expiry date should be defined or beginning date should be blank");
	}

	@Test
	public void createPromotionWithInvalidDatesNullBeginningFailure_test() {
		LocalDateTime begins = null;
		LocalDateTime expires = LocalDateTime.now();
		createPromotionWithInvalidDates(begins, expires, "Either beginning date should be defined or expiry date should be blank");
	}

	@Test
	public void activatePromotionWithInactiveStatus_test() throws Exception {
		PromoBean inactivePromo = createInactivePromotionWithValidFieldsNullDates();

		ActivatePromoBean activatePromoBean = new ActivatePromoBean();
		activatePromoBean.setPromoCode(inactivePromo.getPromoCode());
		activatePromoBean.setBegins(LocalDateTime.now());
		activatePromoBean.setExpires(LocalDateTime.now().plusDays(1));

		given()
				.body(activatePromoBean)
				.when()
				.put(PROMO_API_PATH)
				.then()
				.statusCode(equalTo(HttpStatus.NO_CONTENT.value()));

		PromoDocument activatedPromo = promoRepository.findByPromoCode(activatePromoBean.getPromoCode())
				.orElseThrow(() -> new Exception("wrong promoCode"));

		assertThat(activatedPromo.getBegins()).isEqualTo(activatePromoBean.getBegins());
		assertThat(activatedPromo.getExpires()).isEqualTo(activatePromoBean.getExpires());
		assertThat(activatedPromo.getStatus()).isEqualTo(PromoStatus.ACTIVE);
	}

	@Test
	public void activatePromotionWithActiveStatusFailure_test() throws Exception {
		PromoBean activePromo = createActivePromotionWithValidFieldsNonNullDates();

		ActivatePromoBean activatePromoBean = new ActivatePromoBean();
		activatePromoBean.setPromoCode(activePromo.getPromoCode());
		activatePromoBean.setBegins(LocalDateTime.now());
		activatePromoBean.setExpires(LocalDateTime.now().plusDays(1));

		ErrorResponse errorResponse = given()
				.body(activatePromoBean)
				.when()
				.put(PROMO_API_PATH)
				.then()
				.statusCode(equalTo(HttpStatus.CONFLICT.value()))
				.extract().body().as(ErrorResponse.class);

		assertThat(errorResponse.getMessage())
				.containsIgnoringCase(String.format("The promo with promoCode: %s is already with an ACTIVE status", activatePromoBean.getPromoCode()));
	}

	@Test
	public void activatePromotionWithInactiveStatusNullBeginningDateFailure_test() {
		PromoBean inactivePromo = createInactivePromotionWithValidFieldsNullDates();

		ActivatePromoBean activatePromoBean = new ActivatePromoBean();
		activatePromoBean.setPromoCode(inactivePromo.getPromoCode());
		activatePromoBean.setBegins(null);
		activatePromoBean.setExpires(LocalDateTime.now());

		activatePromotionBadRequest(activatePromoBean, "Dates must be specified and beginning date before expiry date");
	}

	@Test
	public void activatePromotionWithInactiveStatusNullExpiryDateFailure_test() {
		PromoBean inactivePromo = createInactivePromotionWithValidFieldsNullDates();

		ActivatePromoBean activatePromoBean = new ActivatePromoBean();
		activatePromoBean.setPromoCode(inactivePromo.getPromoCode());
		activatePromoBean.setBegins(LocalDateTime.now());
		activatePromoBean.setExpires(null);

		activatePromotionBadRequest(activatePromoBean, "Dates must be specified and beginning date before expiry date");
	}

	@Test
	public void activatePromotionWithInactiveStatusNullDatesFailure_test() {
		PromoBean inactivePromo = createInactivePromotionWithValidFieldsNullDates();

		ActivatePromoBean activatePromoBean = new ActivatePromoBean();
		activatePromoBean.setPromoCode(inactivePromo.getPromoCode());
		activatePromoBean.setBegins(null);
		activatePromoBean.setExpires(null);

		activatePromotionBadRequest(activatePromoBean, "Dates must be specified and beginning date before expiry date");
	}

	@Test
	public void activatePromotionWithInactiveStatusBeginningAfterExpiryDateFailure_test() {
		PromoBean inactivePromo = createInactivePromotionWithValidFieldsNullDates();

		ActivatePromoBean activatePromoBean = new ActivatePromoBean();
		activatePromoBean.setPromoCode(inactivePromo.getPromoCode());
		activatePromoBean.setBegins(LocalDateTime.now());
		activatePromoBean.setExpires(LocalDateTime.now().minusSeconds(1));

		activatePromotionBadRequest(activatePromoBean, "Dates must be specified and beginning date before expiry date");
	}

	private void activatePromotionBadRequest(ActivatePromoBean wrongActivatePromoBean, String errorMessage) {
		ErrorResponse errorResponse = given()
				.body(wrongActivatePromoBean)
				.when()
				.put(PROMO_API_PATH)
				.then()
				.statusCode(equalTo(HttpStatus.BAD_REQUEST.value()))
				.extract().body().as(ErrorResponse.class);

		assertThat(errorResponse.getMessage()).containsIgnoringCase(errorMessage);
	}

	private void createPromotionWithInvalidDates(LocalDateTime begins, LocalDateTime expires, String errorMessage) {
		PromoBean requestBody = buildBrandNewPromoBeanFlexDates(begins, expires);
		requestAndVerifyFailedBadRequest(requestBody, errorMessage);
	}

	private PromoBean requestAndVerifySuccessfulResponse(PromoBean requestBody, PromoStatus status) {
		PromoBean responseBody = given()
				.body(requestBody)
				.when()
				.post(PROMO_API_PATH)
				.then()
				.statusCode(equalTo(HttpStatus.CREATED.value()))
				.extract().body().as(PromoBean.class);

		PromoDocument promoDocument = promoRepository.findOne(responseBody.getId());

		assertThat(responseBody)
				.isNotNull()
				.isEqualToComparingOnlyGivenFields(requestBody,
						"name", "description", "promoCode", "begins", "expires");

		assertThat(responseBody)
				.isNotNull()
				.isEqualToComparingOnlyGivenFields(promoDocument,
						"id", "name", "description", "promoCode", "begins", "expires", "status");

		assertThat(promoDocument.getStatus()).isEqualTo(status);

		return responseBody;
	}

	private void requestNoNullableFieldValidation(String fieldToBeNull) {
		PromoBean requestBody = buildPromoBean(fieldToBeNull);
		requestAndVerifyFailedBadRequest(requestBody, String.format("(PromoBean).%s: may not be null", fieldToBeNull));
	}

	private void requestAndVerifyFailedBadRequest(PromoBean wrongRequestBody, String errorMessage) {
		ErrorResponse errorResponse = given()
				.body(wrongRequestBody)
				.when()
				.post(PROMO_API_PATH)
				.then().log().body(true)
				.statusCode(equalTo(HttpStatus.BAD_REQUEST.value()))
				.extract().body().as(ErrorResponse.class);

		assertThat(errorResponse).isNotNull();
		assertThat(errorResponse.getMessage()).containsIgnoringCase(errorMessage);
	}

	private PromoBean buildBrandNewPromoBeanFlexDates(LocalDateTime begins, LocalDateTime expires) {
		PromoBean promoBean = buildValidBrandNewPromoBean();
		promoBean.setBegins(begins);
		promoBean.setExpires(expires);
		return promoBean;
	}

	private PromoBean buildValidBrandNewPromoBean() {
		return buildPromoBean("id", "status");
	}

	private PromoBean buildPromoBean(String... excludedFields) {
		PromoBean random = random(PromoBean.class, excludedFields);

		if (random.getBegins() != null) {
			random.setExpires(random.getBegins().plusDays(10));
		}

		return random;
	}

}

package com.devit.promomanager.api.impl;

import com.devit.promomanager.AbstractIT;
import com.devit.promomanager.api.model.ErrorResponse;
import com.devit.promomanager.api.model.PromoBean;
import com.devit.promomanager.persistense.document.PromoDocument;
import com.devit.promomanager.persistense.repository.PromoRepository;
import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

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
	public void createPromotionSuccessfully_test() {
		PromoBean requestBody = buildValidBrandNewPromoBean();

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
						"id", "name", "description", "promoCode", "begins", "expires");
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
	public void createPromoWithNullBody() {
		 given()
				.body("")
				.when()
				.post(PROMO_API_PATH)
				.then().log().body(true)
				.statusCode(equalTo(HttpStatus.BAD_REQUEST.value()));
	}

	private void requestNoNullableFieldValidation(String fieldToBeNull) {
		PromoBean requestBody = buildPromoBean(fieldToBeNull);
		ErrorResponse errorResponse = createPromoWithNotNullableField(requestBody);

		assertThat(errorResponse).isNotNull();
		assertThat(errorResponse.getMessage()).containsIgnoringCase(
				String.format("(PromoBean).%s: may not be null", fieldToBeNull));
	}

	private ErrorResponse createPromoWithNotNullableField(PromoBean wrongBean) {
		return given()
				.body(wrongBean)
				.when()
				.post(PROMO_API_PATH)
				.then().log().body(true)
				.statusCode(equalTo(HttpStatus.BAD_REQUEST.value()))
				.extract().body().as(ErrorResponse.class);
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

	private PromoBean buildValidBrandNewPromoBean() {
		return buildPromoBean("id");
	}

	private PromoBean buildPromoBean(String... excludedFields) {
		PromoBean random = random(PromoBean.class, excludedFields);

		if (random.getBegins() != null) {
			random.setExpires(random.getBegins().plusDays(10));
		}

		return random;
	}

}

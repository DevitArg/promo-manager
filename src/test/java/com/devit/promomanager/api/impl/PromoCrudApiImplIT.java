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
	public void createPromotionSuccessfully_test() {
		PromoBean requestBody = buildValidBrandNewPromoBean();

		PromoBean responseBody = given()
				.body(requestBody)
				.when()
				.post(PROMO_API_PATH)
				.then()
				.statusCode(equalTo(HttpStatus.CREATED.value()))
				.extract().body().as(PromoBean.class);

		assertThat(responseBody)
				.isNotNull()
				.isEqualToComparingOnlyGivenFields(requestBody,
						"name", "description", "promoCode", "begins", "expires");
	}

	@Test
	public void createPromotionNullNameFailure_test() {
		PromoBean requestBody = buildPromoBean("id", "name", "begins", "expires");
		given()
				.body(requestBody)
				.when()
				.post(PROMO_API_PATH)
				.then()
				.statusCode(equalTo(HttpStatus.BAD_REQUEST.value()));
	}

	@Test
	public void createPromotionAlreadyCreatedFailure_test() {
		PromoBean requestBody = buildValidBrandNewPromoBean();
		promoRepository.save(dozerBeanMapper.map(requestBody, PromoDocument.class));

		ErrorResponse errorResponse =
				given().log().all()
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
		return buildPromoBean("id", "begins", "expires");
	}

	private PromoBean buildPromoBean(String... excludedFields) {
		PromoBean random = random(PromoBean.class, excludedFields);
		LocalDateTime startDate = LocalDateTime.now();
		random.setBegins(startDate);
		random.setExpires(startDate.plusDays(10));
		return random;
	}

}

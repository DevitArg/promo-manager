package com.devit.promomanager.api.model;

import com.devit.promomanager.exception.InvalidDatesException;

import java.time.LocalDateTime;

public class PromoBeanAdapter {

	private PromoBean promoBean;

	public PromoBeanAdapter(PromoBean promoBean) throws InvalidDatesException {
		this.promoBean = promoBean;
		initStatusBasedOnDates();
	}

	private void initStatusBasedOnDates() throws InvalidDatesException {
		LocalDateTime begins = this.promoBean.getBegins();
		LocalDateTime expires = this.promoBean.getExpires();

		boolean areDefined = begins != null && expires != null;

		if (areDefined) {
			if (begins.isBefore(expires)) {
				promoBean.setStatus(PromoStatus.ACTIVE);
			} else {
				throw new InvalidDatesException("Beginning date must be greater than expiry date");
			}
		} else {
			// in the case one of them are not null throw exception
			if (begins != null) {
				throw new InvalidDatesException("Either expiry date should be defined or beginning date should be blank");
			} else if (expires != null) {
				throw new InvalidDatesException("Either beginning date should be defined or expiry date should be blank");
			}
			// but if both null the status is valid
			promoBean.setStatus(PromoStatus.INACTIVE);
		}
	}

	public PromoBean getPromoBean() {
		return promoBean;
	}

	public void setPromoBean(PromoBean promoBean) {
		this.promoBean = promoBean;
	}
}

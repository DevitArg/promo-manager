package com.devit.promomanager.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * PromoBean
 */
public class PromoBean {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("promoCode")
	private String promoCode = null;

	@JsonProperty("begins")
	private LocalDate begins = null;

	@JsonProperty("expires")
	private LocalDate expires = null;

	public PromoBean id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Get id
	 *
	 * @return id
	 **/
	@ApiModelProperty(value = "")


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PromoBean name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Get name
	 *
	 * @return name
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PromoBean description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Get description
	 *
	 * @return description
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PromoBean promoCode(String promoCode) {
		this.promoCode = promoCode;
		return this;
	}

	/**
	 * Get promoCode
	 *
	 * @return promoCode
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull


	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public PromoBean startDate(LocalDate startDate) {
		this.begins = startDate;
		return this;
	}

	/**
	 * Get begins
	 *
	 * @return begins
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public LocalDate getBegins() {
		return begins;
	}

	public void setBegins(LocalDate begins) {
		this.begins = begins;
	}

	public PromoBean endDate(LocalDate endDate) {
		this.expires = endDate;
		return this;
	}

	/**
	 * Get expires
	 *
	 * @return expires
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public LocalDate getExpires() {
		return expires;
	}

	public void setExpires(LocalDate expires) {
		this.expires = expires;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PromoBean promoBean = (PromoBean) o;
		return Objects.equals(this.id, promoBean.id) &&
				Objects.equals(this.name, promoBean.name) &&
				Objects.equals(this.description, promoBean.description) &&
				Objects.equals(this.promoCode, promoBean.promoCode) &&
				Objects.equals(this.begins, promoBean.begins) &&
				Objects.equals(this.expires, promoBean.expires);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description, promoCode, begins, expires);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PromoBean {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    promoCode: ").append(toIndentedString(promoCode)).append("\n");
		sb.append("    begins: ").append(toIndentedString(begins)).append("\n");
		sb.append("    expires: ").append(toIndentedString(expires)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}


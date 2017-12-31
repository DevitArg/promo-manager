package com.devit.promomanager.persistense.document;

import com.devit.promomanager.api.model.PromoStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


/**
 * @author Lucas.Godoy on 21/11/17.
 */
@Getter
@Setter
@Document(collection = "PromoDocumentV1")
public class PromoDocument {

	private String id;
	private String name;
	private String description;
	@Indexed
	private String promoCode;
	@Indexed
	private LocalDateTime begins;
	@Indexed
	private LocalDateTime expires;
	@Indexed
	private PromoStatus status;

}

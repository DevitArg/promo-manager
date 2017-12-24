package com.devit.promomanager.persistense.document;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


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
	private LocalDate begins;
	@Indexed
	private LocalDate expires;

}

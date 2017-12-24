package com.devit.promomanager.persistense.repository;

import com.devit.promomanager.persistense.document.PromoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author Lucas.Godoy on 21/11/17.
 */
public interface PromoRepository extends MongoRepository<PromoDocument, String> {

	Optional<PromoDocument> findByPromoCode(String promoCode);

}

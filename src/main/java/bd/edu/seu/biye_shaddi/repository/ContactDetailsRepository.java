package bd.edu.seu.biye_shaddi.repository;

import bd.edu.seu.biye_shaddi.model.ContactDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ContactDetailsRepository extends MongoRepository<ContactDetails, String> {
    Optional<ContactDetails> findByEmailId(String emailId);
}


package bd.edu.seu.biye_shaddi.repository;

import bd.edu.seu.biye_shaddi.model.Registration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegistrationRepository extends MongoRepository<Registration, String> {

}

package bd.edu.seu.biye_shaddi.repository;

import bd.edu.seu.biye_shaddi.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmailId(String emailId);


}

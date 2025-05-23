package bd.edu.seu.biye_shaddi.repository;

import bd.edu.seu.biye_shaddi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}

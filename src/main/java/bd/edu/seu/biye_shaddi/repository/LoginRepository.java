package bd.edu.seu.biye_shaddi.repository;


import bd.edu.seu.biye_shaddi.model.Login;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends MongoRepository<Login, String> {



}

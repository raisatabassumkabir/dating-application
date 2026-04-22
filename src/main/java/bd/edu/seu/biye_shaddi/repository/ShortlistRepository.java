package bd.edu.seu.biye_shaddi.repository;

import bd.edu.seu.biye_shaddi.model.Shortlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ShortlistRepository extends MongoRepository<Shortlist, String> {
    List<Shortlist> findByUserId(String userId);

    Optional<Shortlist> findByUserIdAndShortlistedUserId(String userId, String shortlistedUserId);

    void deleteByUserIdAndShortlistedUserId(String userId, String shortlistedUserId);
}

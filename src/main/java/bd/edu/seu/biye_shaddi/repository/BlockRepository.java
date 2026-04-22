package bd.edu.seu.biye_shaddi.repository;

import bd.edu.seu.biye_shaddi.model.Block;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface BlockRepository extends MongoRepository<Block, String> {
    List<Block> findByUserId(String userId);

    Optional<Block> findByUserIdAndBlockedUserId(String userId, String blockedUserId);
}

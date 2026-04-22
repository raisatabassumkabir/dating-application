package bd.edu.seu.biye_shaddi.repository;

import bd.edu.seu.biye_shaddi.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends MongoRepository<Like, String> {
    List<Like> findByUserId(String userId);

    List<Like> findByLikedUserId(String likedUserId);

    Optional<Like> findByUserIdAndLikedUserId(String userId, String likedUserId);

    void deleteByUserIdAndLikedUserId(String userId, String likedUserId);
}

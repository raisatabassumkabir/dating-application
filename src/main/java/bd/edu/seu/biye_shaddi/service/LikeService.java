package bd.edu.seu.biye_shaddi.service;

import bd.edu.seu.biye_shaddi.model.Like;
import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.repository.LikeRepository;
import bd.edu.seu.biye_shaddi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    public Like likeUser(String fromEmailId, String toEmailId) {
        if (fromEmailId.equals(toEmailId)) {
            throw new IllegalArgumentException("Cannot like yourself");
        }

        Optional<Like> existing = likeRepository.findByUserIdAndLikedUserId(fromEmailId, toEmailId);
        if (existing.isPresent()) {
            return existing.get(); // Already liked
        }

        Like like = new Like(fromEmailId, toEmailId);
        return likeRepository.save(like);
    }

    @Transactional
    public void unlikeUser(String fromEmailId, String toEmailId) {
        likeRepository.deleteByUserIdAndLikedUserId(fromEmailId, toEmailId);
    }

    public boolean isLiked(String fromEmailId, String toEmailId) {
        return likeRepository.findByUserIdAndLikedUserId(fromEmailId, toEmailId).isPresent();
    }

    public boolean isMutualLike(String emailId1, String emailId2) {
        boolean user1LikesUser2 = likeRepository.findByUserIdAndLikedUserId(emailId1, emailId2).isPresent();
        boolean user2LikesUser1 = likeRepository.findByUserIdAndLikedUserId(emailId2, emailId1).isPresent();
        return user1LikesUser2 && user2LikesUser1;
    }

    public List<User> getMutualLikes(String emailId) {
        // Get all users this person liked
        List<Like> likesGiven = likeRepository.findByUserId(emailId);

        List<User> mutualMatches = new ArrayList<>();

        for (Like like : likesGiven) {
            String likedUserId = like.getLikedUserId();
            // Check if the other person also liked back
            if (likeRepository.findByUserIdAndLikedUserId(likedUserId, emailId).isPresent()) {
                userRepository.findByEmailId(likedUserId).ifPresent(mutualMatches::add);
            }
        }

        return mutualMatches;
    }

    public List<Like> getLikesGiven(String emailId) {
        return likeRepository.findByUserId(emailId);
    }

    public List<Like> getLikesReceived(String emailId) {
        return likeRepository.findByLikedUserId(emailId);
    }

    public List<User> getLikedUsers(String emailId) {
        List<Like> likes = likeRepository.findByUserId(emailId);
        List<User> likedUsers = new ArrayList<>();
        for (Like like : likes) {
            userRepository.findByEmailId(like.getLikedUserId()).ifPresent(likedUsers::add);
        }
        return likedUsers;
    }
}

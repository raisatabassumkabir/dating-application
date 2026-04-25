package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.Like;
import bd.edu.seu.biye_shaddi.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> likeUser(@RequestParam String userId, @RequestParam String likedUserId) {
        try {
            Like like = likeService.likeUser(userId, likedUserId);
            return ResponseEntity.ok(like);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<?> unlikeUser(@RequestParam String userId, @RequestParam String likedUserId) {
        likeService.unlikeUser(userId, likedUserId);
        return ResponseEntity.ok("Unliked");
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isLiked(@RequestParam String userId, @RequestParam String likedUserId) {
        return ResponseEntity.ok(likeService.isLiked(userId, likedUserId));
    }

    @GetMapping("/mutual")
    public ResponseEntity<Boolean> isMutual(@RequestParam String userId, @RequestParam String otherUserId) {
        return ResponseEntity.ok(likeService.isMutualLike(userId, otherUserId));
    }
}

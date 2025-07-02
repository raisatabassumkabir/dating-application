package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.TalkRequest;
import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.MatchingService;

import bd.edu.seu.biye_shaddi.service.TalkRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matches")
public class MatchingController {



    private final MatchingService matchingService;
    private final TalkRequestService talkRequestService;

    public MatchingController(MatchingService matchingService, TalkRequestService talkRequestService) {
        this.matchingService = matchingService;
        this.talkRequestService = talkRequestService;
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTopMatches(@RequestParam String emailId, @RequestParam(defaultValue = "10") int limit) {
        try {
            List<User> matches = matchingService.findTopMatchesByEmailId(emailId, limit);
            if (matches.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No matches found for the given criteria");
            }
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching matches: " + e.getMessage());
        }
    }

    @GetMapping("/compatibility/{emailId1}/{emailId2}")
    public ResponseEntity<Double> getCompatibilityScore(
            @PathVariable String emailId1,
            @PathVariable String emailId2) {
        try {
            Optional<User> user1 = matchingService.getUserByEmailId(emailId1);
            Optional<User> user2 = matchingService.getUserByEmailId(emailId2);

            if (!user1.isPresent() || !user2.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            double score = matchingService.calculateCompatibilityScore(user1.get(), user2.get());
            return ResponseEntity.ok(score);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/preferences/{emailId}")
    public ResponseEntity<?> getUserPreferences(@PathVariable String emailId) {
        try {
            Optional<User> user = matchingService.getUserByEmailId(emailId);
            if (!user.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }

            return ResponseEntity.ok(user.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching user preferences: " + e.getMessage());
        }
    }

    @PutMapping("/preferences/{emailId}")
    public ResponseEntity<?> updateUserPreferences(
            @PathVariable String emailId,
            @RequestBody User preferences) {
        try {
            User updatedUser = matchingService.updateUserPreferencesByEmailId(emailId, preferences);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating preferences: " + e.getMessage());
        }
    }

    @GetMapping("/count/{emailId}")
    public ResponseEntity<Long> getPotentialMatchesCount(@PathVariable String emailId) {
        try {
            long count = matchingService.countPotentialMatchesByEmailId(emailId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


}
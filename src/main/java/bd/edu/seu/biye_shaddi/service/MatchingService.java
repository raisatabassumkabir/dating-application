package bd.edu.seu.biye_shaddi.service;

import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public MatchingService(UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public double calculateCompatibilityScore(User user1, User user2) {
        System.out.println("\n=== Calculating compatibility score between:");
        System.out.println("User1: " + user1.getEmailId());
        System.out.println("User2: " + user2.getEmailId());
        
        double score = 0.0;
        int maxScore = 7; // Gender: 2, Height: 2, Interests: 2, Age: 1

        // Age compatibility
        if (user2.getAge() >= user1.getPreferredAgeMin() && user2.getAge() <= user1.getPreferredAgeMax()) {
            score += 1;
            System.out.println("✓ Age match: " + user2.getEmailId() + " age " + user2.getAge() + " in [" + user1.getPreferredAgeMin() + ", " + user1.getPreferredAgeMax() + "]");
        } else {
            System.out.println("✗ Age mismatch: " + user2.getEmailId() + " age " + user2.getAge() + " not in [" + user1.getPreferredAgeMin() + ", " + user1.getPreferredAgeMax() + "]");
        }

        // Gender compatibility
        if (user1.getPreferredGender() != null && user1.getPreferredGender().equalsIgnoreCase(user2.getGender())) {
            score += 2;
            System.out.println("✓ Gender match: " + user2.getEmailId() + " gender " + user2.getGender() + " = " + user1.getPreferredGender());
        } else {
            System.out.println("✗ Gender mismatch: " + user2.getEmailId() + " gender " + user2.getGender() + " != " + user1.getPreferredGender());
        }

        // Height compatibility
        if (user2.getHeight() != null && user1.getPreferredHeightMin() != null && user1.getPreferredHeightMax() != null) {
            try {
                double height2 = parseHeight(user2.getHeight());
                double min = parseHeight(user1.getPreferredHeightMin());
                double max = parseHeight(user1.getPreferredHeightMax());
                System.out.println("Height check for " + user2.getEmailId() + ": " + height2 + " in [" + min + ", " + max + "]");
                if (height2 >= min && height2 <= max) {
                    score += 2;
                    System.out.println("✓ Height match");
                } else {
                    System.out.println("✗ Height mismatch");
                }
            } catch (NumberFormatException e) {
                System.out.println("✗ Height parsing failed for " + user2.getEmailId() + ": " + user2.getHeight());
            }
        } else {
            System.out.println("✗ Height data missing for " + user2.getEmailId());
        }

        // Interests similarity
        if (user1.getInterests() != null && user2.getInterests() != null) {
            System.out.println("User1 (" + user1.getEmailId() + ") interests: " + user1.getInterests());
            System.out.println("User2 (" + user2.getEmailId() + ") interests: " + user2.getInterests());
            if (!user1.getInterests().isEmpty() && !user2.getInterests().isEmpty()) {
                Set<String> commonInterests = new HashSet<>(user1.getInterests());
                commonInterests.retainAll(user2.getInterests());
                double interestScore = commonInterests.isEmpty() ? 1.0 : (2.0 * commonInterests.size()) / (user1.getInterests().size() + user2.getInterests().size());
                score += interestScore;
                System.out.println("✓ Interest score for " + user2.getEmailId() + ": " + interestScore + ", common: " + commonInterests);
            } else {
                score += 1.0; // Encourage matches with any interests
                System.out.println("✓ Interest score for " + user2.getEmailId() + ": 1.0 (no overlap or empty)");
            }
        } else {
            System.out.println("✗ Interests missing for " + user2.getEmailId());
        }

        double percentage = (score / maxScore) * 100;
        System.out.println("Final score for " + user2.getEmailId() + ": " + score + "/" + maxScore + " = " + percentage + "%");
        return percentage;
    }

    private double parseHeight(String height) {
        if (height == null || height.trim().isEmpty()) {
            System.out.println("Height is null or empty: " + height);
            return 0.0;
        }
        String numericPart = height.replaceAll("[^0-9.]", "");
        if (numericPart.isEmpty()) {
            System.out.println("Height parsing failed: " + height);
            return 0.0;
        }
        try {
            double value = Double.parseDouble(numericPart);
            System.out.println("Parsed height: " + height + " -> " + value);
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Height parsing exception: " + height);
            return 0.0;
        }
    }


    public List<User> findTopMatchesByEmailId(String emailId, int limit) {
        User currentUser = userRepository.findByEmailId(emailId).orElse(null);
        if (currentUser == null) {
            System.out.println("User not found: " + emailId);
            return Collections.emptyList();
        }
        System.out.println("Finding matches for: " + emailId + ", preferredGender: " + currentUser.getPreferredGender());

        Query query = new Query();
        query.addCriteria(Criteria.where("emailId").ne(emailId));
        query.addCriteria(Criteria.where("relationshipStatus").in(Arrays.asList("single", "divorced")));

        if (currentUser.getPreferredGender() != null && !currentUser.getPreferredGender().isEmpty()) {
            query.addCriteria(Criteria.where("gender").is(currentUser.getPreferredGender()));
            System.out.println("Applied gender filter: " + currentUser.getPreferredGender());
        }

        // Relax age filter to avoid premature exclusion
        // Age will be checked in scoring
        System.out.println("No age filter applied, checking in scoring");

        List<User> allPotentialMatches = mongoTemplate.find(query, User.class);
        System.out.println("Potential matches found: " + allPotentialMatches.size() + " users");

        Map<User, Double> matchScores = new HashMap<>();
        for (User match : allPotentialMatches) {
            System.out.println("\nEvaluating match: " + match.getEmailId());
            double score = calculateCompatibilityScore(currentUser, match);
            double reverseScore = calculateCompatibilityScore(match, currentUser);
            if (score >= 40 && reverseScore >= 40) { // Enforce 40% mutual similarity
                match.setCompatibilityScore(score);
                matchScores.put(match, score);
                System.out.println("Match accepted: " + match.getEmailId() + ", score: " + score + "%, reverse: " + reverseScore + "%");
            } else {
                System.out.println("Match rejected: " + match.getEmailId() + ", score: " + score + "%, reverse: " + reverseScore + "%");
            }
        }

        List<User> sortedMatches = matchScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
        System.out.println("Final sorted matches: " + sortedMatches.stream().map(User::getEmailId).collect(Collectors.toList()));
        return sortedMatches;
    }

    public Optional<User> getUserByEmailId(String emailId) {
        return userRepository.findByEmailId(emailId);
    }

    public User updateUserPreferencesByEmailId(String emailId, User preferences) {
        User existingUser = userRepository.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setPreferredAgeMin(preferences.getPreferredAgeMin());
        existingUser.setPreferredAgeMax(preferences.getPreferredAgeMax());
        existingUser.setPreferredGender(preferences.getPreferredGender());
        existingUser.setPreferredHeightMin(preferences.getPreferredHeightMin());
        existingUser.setPreferredHeightMax(preferences.getPreferredHeightMax());
        existingUser.setInterests(preferences.getInterests());
        return userRepository.save(existingUser);
    }

    public long countPotentialMatchesByEmailId(String emailId) {
        User currentUser = userRepository.findByEmailId(emailId).orElse(null);
        if (currentUser == null) {
            return 0;
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("emailId").ne(emailId));
        query.addCriteria(Criteria.where("relationshipStatus").in(Arrays.asList("single", "divorced")));

        if (currentUser.getPreferredGender() != null && !currentUser.getPreferredGender().isEmpty()) {
            query.addCriteria(Criteria.where("gender").is(currentUser.getPreferredGender()));
        }

        return mongoTemplate.count(query, User.class);
    }



    public User updateUserPreferences(String userId, User preferences) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return updateUserPreferencesByEmailId(existingUser.getEmailId(), preferences);
    }

    public long countPotentialMatches(String userId) {
        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            return 0;
        }
        return countPotentialMatchesByEmailId(currentUser.getEmailId());
    }


}
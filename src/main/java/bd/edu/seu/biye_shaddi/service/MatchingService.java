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
    private final TalkRequestService talkRequestService;

    @Autowired
    public MatchingService(UserRepository userRepository, MongoTemplate mongoTemplate,
            TalkRequestService talkRequestService) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
        this.talkRequestService = talkRequestService;
    }

    /**
     * Derive the opposite gender for matching.
     * If user is "male" -> preferred is "female" and vice versa.
     * Falls back to the user's explicit preferredGender if set.
     */
    private String derivePreferredGender(User user) {
        String pg = user.getPreferredGender();
        if (pg != null && !pg.trim().isEmpty()) {
            return pg.trim();
        }
        // Auto-derive from own gender
        String gender = user.getGender();
        if (gender == null)
            return null;
        if (gender.trim().equalsIgnoreCase("male"))
            return "female";
        if (gender.trim().equalsIgnoreCase("female"))
            return "male";
        return null;
    }

    public double calculateCompatibilityScore(User user1, User user2) {
        System.out.println("\n=== Compatibility: " + user1.getEmailId() + " -> " + user2.getEmailId());

        double score = 0.0;
        int maxScore = 7; // Gender: 2, Height: 2, Interests: 2, Age: 1

        // Age compatibility
        int age2 = user2.getAge();
        int prefAgeMin = user1.getPreferredAgeMin();
        int prefAgeMax = user1.getPreferredAgeMax();
        if (prefAgeMax == 0)
            prefAgeMax = 100;
        if (age2 >= prefAgeMin && age2 <= prefAgeMax) {
            score += 1;
            System.out.println("  ✓ Age match: " + age2 + " in [" + prefAgeMin + ", " + prefAgeMax + "]");
        } else {
            System.out.println("  ✗ Age mismatch: " + age2 + " not in [" + prefAgeMin + ", " + prefAgeMax + "]");
        }

        // Gender compatibility (auto-derived)
        String preferredGender = derivePreferredGender(user1);
        if (preferredGender != null && preferredGender.equalsIgnoreCase(user2.getGender())) {
            score += 2;
            System.out.println("  ✓ Gender match: " + user2.getGender());
        } else if (preferredGender == null) {
            score += 1;
            System.out.println("  ~ Gender unknown, partial credit");
        } else {
            System.out.println("  ✗ Gender mismatch: wanted " + preferredGender + ", got " + user2.getGender());
        }

        // Height compatibility
        try {
            double height2 = parseHeight(user2.getHeight());
            double hMin = parseHeight(user1.getPreferredHeightMin());
            double hMax = parseHeight(user1.getPreferredHeightMax());
            if (hMax == 0.0)
                hMax = Double.MAX_VALUE;

            if (height2 > 0 && hMin > 0) {
                if (height2 >= hMin && height2 <= hMax) {
                    score += 2;
                    System.out.println("  ✓ Height match: " + height2 + " in [" + hMin + ", " + hMax + "]");
                } else {
                    System.out.println("  ✗ Height mismatch: " + height2 + " not in [" + hMin + ", " + hMax + "]");
                }
            } else {
                score += 1;
                System.out.println("  ~ Height data incomplete, partial credit");
            }
        } catch (Exception e) {
            score += 1;
            System.out.println("  ~ Height parse error, partial credit: " + e.getMessage());
        }

        // Interests similarity
        List<String> int1 = user1.getInterests();
        List<String> int2 = user2.getInterests();
        if (int1 != null && int2 != null && !int1.isEmpty() && !int2.isEmpty()) {
            Set<String> s1 = int1.stream().map(String::toLowerCase).collect(Collectors.toSet());
            Set<String> s2 = int2.stream().map(String::toLowerCase).collect(Collectors.toSet());
            Set<String> common = new HashSet<>(s1);
            common.retainAll(s2);
            double interestScore = common.isEmpty() ? 0.5
                    : (2.0 * common.size()) / (s1.size() + s2.size());
            score += interestScore;
            System.out.println("  ✓ Interest score: " + interestScore + " (common: " + common + ")");
        } else {
            score += 1.0;
            System.out.println("  ~ Interests not filled, partial credit");
        }

        double percentage = (score / maxScore) * 100;
        System.out.println("  TOTAL: " + String.format("%.1f", score) + "/" + maxScore
                + " = " + String.format("%.1f", percentage) + "%");
        return percentage;
    }

    /**
     * Parse height string like "5'6''" into total inches.
     * "5'6''" -> strips quotes -> "5 6" -> 5*12 + 6 = 66 inches
     */
    private double parseHeight(String height) {
        if (height == null || height.trim().isEmpty()) {
            return 0.0;
        }
        try {
            String clean = height.replaceAll("['\u2018\u2019\u201C\u201D\"` ]", " ").replaceAll("\\s+", " ").trim();
            String[] parts = clean.split(" ");

            if (parts.length >= 2) {
                double feet = Double.parseDouble(parts[0]);
                double inches = Double.parseDouble(parts[1]);
                double total = (feet * 12) + inches;
                System.out.println("    parseHeight(\"" + height + "\") -> " + total + " in");
                return total;
            } else if (parts.length == 1 && !parts[0].isEmpty()) {
                double val = Double.parseDouble(parts[0]);
                double total = (val < 10) ? val * 12 : val;
                System.out.println("    parseHeight(\"" + height + "\") -> " + total + " in");
                return total;
            }
        } catch (Exception e) {
            System.out.println("    parseHeight ERROR: \"" + height + "\" -> " + e.getMessage());
        }
        return 0.0;
    }

    public List<User> findTopMatchesByEmailId(String emailId, int limit) {
        User currentUser = userRepository.findByEmailId(emailId).orElse(null);
        if (currentUser == null) {
            System.out.println("User not found: " + emailId);
            return Collections.emptyList();
        }

        String preferredGender = derivePreferredGender(currentUser);
        System.out.println("\n========================================");
        System.out.println("DISCOVER for: " + emailId);
        System.out.println("  own gender: " + currentUser.getGender());
        System.out.println("  derived preferredGender: " + preferredGender);
        System.out.println("========================================");

        Query query = new Query();
        query.addCriteria(Criteria.where("emailId").ne(emailId));

        // Filter: only single or divorced
        query.addCriteria(Criteria.where("maritalStatus")
                .in("Single", "single", "SINGLE", "Divorced", "divorced", "DIVORCED"));

        // Filter: opposite gender
        if (preferredGender != null) {
            query.addCriteria(Criteria.where("gender").regex("^" + preferredGender + "$", "i"));
            System.out.println("  Gender filter applied: " + preferredGender);
        }

        List<User> allPotentialMatches = mongoTemplate.find(query, User.class);
        System.out.println("  DB returned " + allPotentialMatches.size() + " potential matches");
        for (User u : allPotentialMatches) {
            System.out.println("    - " + u.getEmailId() + " | gender=" + u.getGender()
                    + " | maritalStatus=" + u.getMaritalStatus() + " | age=" + u.getAge());
        }

        Map<User, Double> matchScores = new HashMap<>();
        for (User match : allPotentialMatches) {
            double score = calculateCompatibilityScore(currentUser, match);
            double reverseScore = calculateCompatibilityScore(match, currentUser);
            double avgScore = (score + reverseScore) / 2.0;

            System.out.println("  >> " + match.getEmailId()
                    + ": fwd=" + String.format("%.1f", score) + "%"
                    + ", rev=" + String.format("%.1f", reverseScore) + "%"
                    + ", avg=" + String.format("%.1f", avgScore) + "%");

            // Very lenient threshold to ensure people see recommendations
            if (avgScore >= 20) {
                match.setCompatibilityScore((double) Math.round(avgScore));
                matchScores.put(match, avgScore);
                System.out.println("     -> ACCEPTED");
            } else {
                System.out.println("     -> REJECTED (below 20%)");
            }
        }

        List<User> sortedMatches = matchScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());

        System.out.println("\nFinal recommendations: "
                + sortedMatches.stream().map(u -> u.getEmailId() + "(" + u.getCompatibilityScore() + "%)")
                        .collect(Collectors.toList()));
        System.out.println("========================================\n");
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
        query.addCriteria(Criteria.where("maritalStatus")
                .in("Single", "single", "SINGLE", "Divorced", "divorced", "DIVORCED"));

        String preferredGender = derivePreferredGender(currentUser);
        if (preferredGender != null) {
            query.addCriteria(Criteria.where("gender").regex("^" + preferredGender + "$", "i"));
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
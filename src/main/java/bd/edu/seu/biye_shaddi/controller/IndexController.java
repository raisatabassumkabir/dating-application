package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.ContactDetails;
import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.ContactDetailsService;
import bd.edu.seu.biye_shaddi.service.LikeService;
import bd.edu.seu.biye_shaddi.service.MatchingService;
import bd.edu.seu.biye_shaddi.service.TalkRequestService;
import bd.edu.seu.biye_shaddi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {
    private final MatchingService matchingService;
    private final UserService userService;
    private final TalkRequestService talkRequestService;
    private final ContactDetailsService contactDetailsService;
    private final LikeService likeService;

    public IndexController(MatchingService matchingService, UserService userService,
            TalkRequestService talkRequestService, ContactDetailsService contactDetailsService,
            LikeService likeService) {
        this.matchingService = matchingService;
        this.userService = userService;
        this.talkRequestService = talkRequestService;
        this.contactDetailsService = contactDetailsService;
        this.likeService = likeService;
    }

    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/matches")
    public String getMatchesPage(@RequestParam(required = false) String emailId, Model model, Principal principal) {
        String userEmail = emailId != null ? emailId : (principal != null ? principal.getName() : null);
        if (userEmail == null || userEmail.isEmpty()) {
            return "redirect:/login";
        }
        Optional<User> user = matchingService.getUserByEmailId(userEmail);
        if (!user.isPresent()) {
            return "redirect:/login";
        }

        List<bd.edu.seu.biye_shaddi.dto.MatchDTO> matchDTOs = new java.util.ArrayList<>();

        // 1. Mutual Likes
        List<User> mutualMatches = likeService.getMutualLikes(userEmail);
        for (User u : mutualMatches) {
            matchDTOs.add(new bd.edu.seu.biye_shaddi.dto.MatchDTO(u, "MUTUAL"));
        }

        // 2. Liked Users (Exclude mutuals to avoid duplicates)
        List<User> likedUsers = likeService.getLikedUsers(userEmail);
        for (User u : likedUsers) {
            if (u.getEmailId() == null)
                continue; // Skip if email is null
            boolean isMutual = mutualMatches.stream()
                    .filter(m -> m.getEmailId() != null)
                    .anyMatch(m -> m.getEmailId().equals(u.getEmailId()));

            if (!isMutual) {
                matchDTOs.add(new bd.edu.seu.biye_shaddi.dto.MatchDTO(u, "LIKED"));
            }
        }

        // 3. Sent Requests (Exclude if already in mutual or liked - though usually
        // request follows like)
        List<bd.edu.seu.biye_shaddi.model.TalkRequest> sentRequests = talkRequestService.getSentRequests(userEmail);
        for (bd.edu.seu.biye_shaddi.model.TalkRequest req : sentRequests) {
            String targetEmail = req.getToEmailId();
            if (targetEmail == null)
                continue; // Skip if target email is null

            // Check if already added as MUTUAL or LIKED
            boolean alreadyAdded = matchDTOs.stream()
                    .filter(m -> m.getUser() != null && m.getUser().getEmailId() != null)
                    .anyMatch(m -> m.getUser().getEmailId().equals(targetEmail));

            if (!alreadyAdded) {
                Optional<User> targetUser = userService.getUserByEmail(targetEmail);
                if (targetUser.isPresent()) {
                    matchDTOs.add(new bd.edu.seu.biye_shaddi.dto.MatchDTO(targetUser.get(), "SENT"));
                }
            } else {
                // If it was LIKED, upgrade status to SENT if appropriate
                matchDTOs.stream()
                        .filter(m -> m.getUser() != null && m.getUser().getEmailId() != null)
                        .filter(m -> m.getUser().getEmailId().equals(targetEmail) && "LIKED".equals(m.getStatus()))
                        .findFirst()
                        .ifPresent(m -> m.setStatus("SENT"));
            }
        }

        model.addAttribute("matches", matchDTOs); // Note: This now contains MatchDTO objects, not User objects
        model.addAttribute("emailId", userEmail);
        return "matches";
    }

    @GetMapping("/discover")
    public String getDiscoverPage(@RequestParam(required = false) String emailId, Model model, Principal principal) {
        String userEmail = emailId != null ? emailId : (principal != null ? principal.getName() : null);
        if (userEmail == null || userEmail.isEmpty()) {
            return "redirect:/login";
        }
        Optional<User> user = matchingService.getUserByEmailId(userEmail);
        if (!user.isPresent()) {
            return "redirect:/login";
        }
        List<User> recommendations = matchingService.findTopMatchesByEmailId(userEmail, 20);
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("emailId", userEmail);
        return "discover";
    }

    @GetMapping("/matches-contact-info")
    public String getMatchesContactInfoPage(@RequestParam String emailId, @RequestParam String viewerEmailId,
            Model model) {
        System.out.println("Fetching contact details: viewer=" + viewerEmailId + ", target=" + emailId);

        // Validate emailId format
        if (emailId == null || emailId.trim().isEmpty()) {
            System.out.println("Invalid emailId: " + emailId);
            model.addAttribute("error", "Invalid user ID");
            return "matches-contact-info";
        }

        // Validate viewerEmailId format
        if (viewerEmailId == null || viewerEmailId.trim().isEmpty()) {
            System.out.println("Invalid viewerEmailId: " + viewerEmailId);
            model.addAttribute("error", "Invalid viewer ID");
            return "matches-contact-info";
        }

        // Fetch user
        Optional<User> user = matchingService.getUserByEmailId(emailId);
        if (!user.isPresent()) {
            System.out.println("Target user not found: " + emailId);
            model.addAttribute("error", "User not found");
            return "matches-contact-info";
        }

        // Fetch contact details
        Optional<ContactDetails> contactDetails = contactDetailsService.getContactDetailsByEmail(emailId);
        if (!contactDetails.isPresent()) {
            System.out.println("Contact details not found for: " + emailId);
            model.addAttribute("error", "Contact details not found");
            model.addAttribute("user", user.get());
            return "matches-contact-info";
        }

        // Check authorization
        boolean canView = talkRequestService.canViewContactDetails(viewerEmailId, emailId);
        if (!canView) {
            System.out.println("Unauthorized to view contact details: viewer=" + viewerEmailId + ", target=" + emailId);
            model.addAttribute("error", "You are not authorized to view this user's contact details");
            model.addAttribute("user", user.get());
            return "matches-contact-info";
        }

        // Add to model
        model.addAttribute("user", user.get());
        model.addAttribute("contactDetails", contactDetails.get());
        model.addAttribute("emailId", viewerEmailId);
        System.out.println("Contact details rendered for: " + emailId);
        return "matches-contact-info";
    }

    // @GetMapping("/matches-contact-info")
    // public String getMatchesContactInfoPage(@RequestParam String emailId,
    // @RequestParam(required = false) String viewerEmailId, Model model) {
    // System.out.println("Fetching contact details: viewer=" + viewerEmailId + ",
    // target=" + emailId);
    // Optional<User>user=userService.getUserByEmail(emailId);
    // Optional<ContactDetails> contactDetails =
    // contactDetailsService.getContactDetailsByEmail(emailId);
    // if (user == null || contactDetails.isEmpty()) {
    // System.out.println("Contact details not found for: " + emailId);
    // model.addAttribute("error", "Contact details not found");
    // model.addAttribute("viewerEmailId", viewerEmailId);
    // return "contact_info";
    // }
    // model.addAttribute("user", user);
    // model.addAttribute("contactDetails", contactDetails);
    // model.addAttribute("viewerEmailId", viewerEmailId);
    // return "contact_info";
    // }
    //
    // @GetMapping("/matches")
    // public String getMatchesPage(@RequestParam String emailId, Model model,
    // Principal principal) {
    // System.out.println("Accessing /matches with emailId: " + emailId);
    // Optional<User>user=userService.getUserByEmail(emailId);
    // if (user == null) {
    // System.out.println("User not found for emailId: " + emailId);
    // return "redirect:/dashboard";
    // }
    // List<User> matches = matchingService.findTopMatchesByEmailId(emailId,10);
    // model.addAttribute("emailId", emailId);
    // model.addAttribute("matches", matches);
    // return "matches";
    // }
}

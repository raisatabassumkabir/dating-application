package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.ContactDetails;
import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.ContactDetailsService;
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

    public IndexController(MatchingService matchingService, UserService userService, TalkRequestService talkRequestService, ContactDetailsService contactDetailsService) {
        this.matchingService = matchingService;
        this.userService = userService;
        this.talkRequestService = talkRequestService;
        this.contactDetailsService = contactDetailsService;
    }

    @GetMapping("/index")
    public String indexPage(){
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
        List<User> matches = matchingService.findTopMatchesByEmailId(userEmail, 10);
        model.addAttribute("matches", matches);
        model.addAttribute("emailId", userEmail);
        return "matches";
    }

    @GetMapping("/matches-contact-info")
    public String getMatchesContactInfoPage(@RequestParam String emailId, @RequestParam String viewerEmailId, Model model) {
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
        System.out.println("Contact details rendered for: " + emailId);
        return "matches-contact-info";
    }

//    @GetMapping("/matches-contact-info")
//    public String getMatchesContactInfoPage(@RequestParam String emailId, @RequestParam(required = false) String viewerEmailId, Model model) {
//        System.out.println("Fetching contact details: viewer=" + viewerEmailId + ", target=" + emailId);
//       Optional<User>user=userService.getUserByEmail(emailId);
//        Optional<ContactDetails> contactDetails = contactDetailsService.getContactDetailsByEmail(emailId);
//        if (user == null || contactDetails.isEmpty()) {
//            System.out.println("Contact details not found for: " + emailId);
//            model.addAttribute("error", "Contact details not found");
//            model.addAttribute("viewerEmailId", viewerEmailId);
//            return "contact_info";
//        }
//        model.addAttribute("user", user);
//        model.addAttribute("contactDetails", contactDetails);
//        model.addAttribute("viewerEmailId", viewerEmailId);
//        return "contact_info";
//    }
//
//    @GetMapping("/matches")
//    public String getMatchesPage(@RequestParam String emailId, Model model, Principal principal) {
//        System.out.println("Accessing /matches with emailId: " + emailId);
//        Optional<User>user=userService.getUserByEmail(emailId);
//        if (user == null) {
//            System.out.println("User not found for emailId: " + emailId);
//            return "redirect:/dashboard";
//        }
//        List<User> matches = matchingService.findTopMatchesByEmailId(emailId,10);
//        model.addAttribute("emailId", emailId);
//        model.addAttribute("matches", matches);
//        return "matches";
//    }
}

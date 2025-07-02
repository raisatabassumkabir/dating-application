package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.TalkRequest;
import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.MatchingService;
import bd.edu.seu.biye_shaddi.service.TalkRequestService;
import bd.edu.seu.biye_shaddi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class TalkRequestViewController {

    private  final MatchingService matchingService;
    private final TalkRequestService talkRequestService;
    private final UserService userService;

    public TalkRequestViewController(MatchingService matchingService, TalkRequestService talkRequestService, UserService userService) {
        this.matchingService = matchingService;
        this.talkRequestService = talkRequestService;
        this.userService = userService;
    }
    @GetMapping("/talkrequest")
    public String getTalkRequestPage(@RequestParam(required = false) String emailId, Model model, Principal principal) {
        // CHANGED: Handle emailId from Principal or request param
        String userEmail = emailId != null ? emailId : (principal != null ? principal.getName() : null);
        if (userEmail == null || userEmail.isEmpty()) {
            System.out.println("Invalid emailId: null or empty");
            return "redirect:/login";
        }
        try {
            // CHANGED: Fetch pending requests and senders
            List<TalkRequest> pendingRequests = talkRequestService.getPendingRequests(userEmail);
            List<User> senders = pendingRequests.stream()
                    .map(req -> userService.getUserByEmail(req.getFromEmailId()).orElse(null))
                    .filter(user -> user != null)
                    .collect(Collectors.toList());
            System.out.println("Fetched " + pendingRequests.size() + " pending requests for " + userEmail);
            model.addAttribute("pendingRequests", pendingRequests);
            model.addAttribute("senders", senders);
            model.addAttribute("emailId", userEmail);
            return "talkrequest";
        } catch (Exception e) {
            // CHANGED: Handle errors gracefully
            System.out.println("Error fetching talk requests for " + userEmail + ": " + e.getMessage());
            model.addAttribute("error", "Error loading talk requests: " + e.getMessage());
            model.addAttribute("pendingRequests", List.of());
            model.addAttribute("senders", List.of());
            model.addAttribute("emailId", userEmail);
            return "talkrequest";
        }
    }
}

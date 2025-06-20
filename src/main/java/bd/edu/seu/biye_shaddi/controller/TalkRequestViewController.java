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
    public String getTalkRequests(@RequestParam(required = false) String emailId, Model model, Principal principal) {
        try {
            String userEmail = emailId != null ? emailId : (principal != null ? principal.getName() : null);
            System.out.println("Processing talk requests for emailId: " + userEmail);
            if (userEmail == null || userEmail.isEmpty()) {
                System.out.println("Invalid emailId: null or empty");
                return "redirect:/login";
            }
            Optional<User> user = userService.getUserByEmail(userEmail);
            if (!user.isPresent()) {
                System.out.println("User not found in MongoDB: " + userEmail);
                return "redirect:/login";
            }
            System.out.println("User found: " + user.get().getName() + " (" + userEmail + ")");
            List<TalkRequest> pendingRequests = talkRequestService.getPendingRequests(userEmail);
            System.out.println("Found " + pendingRequests.size() + " pending requests for " + userEmail + ":");
            pendingRequests.forEach(req -> System.out.println(" - id=" + req.getId() + ", from=" + req.getFromEmailId() + ", status=" + req.getStatus()));
            List<User> senders = pendingRequests.stream()
                    .map(request -> {
                        Optional<User> sender = userService.getUserByEmail(request.getFromEmailId());
                        System.out.println("Looking up sender for request id=" + request.getId() + ", fromEmailId=" + request.getFromEmailId() + ": " + (sender.isPresent() ? "found" : "not found"));
                        return sender.orElse(null);
                    })
                    .filter(userObj -> userObj != null)
                    .collect(Collectors.toList());

            System.out.println("Loaded " + senders.size() + " senders and " + pendingRequests.size() + " pending requests for " + userEmail);
            model.addAttribute("senders", senders);
            model.addAttribute("pendingRequests", pendingRequests);
            model.addAttribute("emailId", userEmail);
            return "talkrequest";
        } catch (Exception e) {
            System.err.println("Error fetching talk requests for " + emailId + ": " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading talk requests: " + e.getMessage());
            model.addAttribute("emailId", emailId);
            model.addAttribute("senders", List.of());
            model.addAttribute("pendingRequests", List.of());
            return "talkrequest";
        }
    }
}

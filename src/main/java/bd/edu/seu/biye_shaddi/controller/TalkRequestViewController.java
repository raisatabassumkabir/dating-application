package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.TalkRequest;
import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.TalkRequestService;
import bd.edu.seu.biye_shaddi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TalkRequestViewController {

    private final TalkRequestService talkRequestService;
    private final UserService userService;

    public TalkRequestViewController(TalkRequestService talkRequestService, UserService userService) {
        this.talkRequestService = talkRequestService;
        this.userService = userService;
    }

    @GetMapping("/talkrequest")
    public String getTalkRequestPage(@RequestParam(required = false) String emailId, Model model, Principal principal) {
        String userEmail = emailId != null ? emailId : (principal != null ? principal.getName() : null);
        if (userEmail == null || userEmail.isEmpty()) {
            return "redirect:/login";
        }
        try {
            // 1. Pending received requests
            List<TalkRequest> pendingRequests = talkRequestService.getPendingRequests(userEmail);
            List<User> senders = pendingRequests.stream()
                    .map(req -> userService.getUserByEmail(req.getFromEmailId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // 2. Sent requests (enriched with recipient user details)
            List<TalkRequest> sentRequests = talkRequestService.getSentRequests(userEmail);
            List<TalkRequest> enrichedSentRequests = sentRequests.stream().map(request -> {
                userService.getUserByEmail(request.getToEmailId()).ifPresent(request::setToUser);
                return request;
            }).collect(Collectors.toList());

            // 3. Chat partners = all accepted conversations (both sent and received)
            Set<String> acceptedEmails = new LinkedHashSet<>();

            for (TalkRequest req : talkRequestService.getAcceptedSentRequests(userEmail)) {
                acceptedEmails.add(req.getToEmailId());
            }
            for (TalkRequest req : talkRequestService.getAcceptedReceivedRequests(userEmail)) {
                acceptedEmails.add(req.getFromEmailId());
            }

            List<User> chatPartners = acceptedEmails.stream()
                    .map(email -> userService.getUserByEmail(email).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            model.addAttribute("pendingRequests", pendingRequests);
            model.addAttribute("senders", senders);
            model.addAttribute("sentRequests", enrichedSentRequests);
            model.addAttribute("chatPartners", chatPartners);
            model.addAttribute("emailId", userEmail);
            return "talkrequest";
        } catch (Exception e) {
            System.out.println("Error fetching talk requests for " + userEmail + ": " + e.getMessage());
            model.addAttribute("error", "Error loading messages: " + e.getMessage());
            model.addAttribute("pendingRequests", List.of());
            model.addAttribute("senders", List.of());
            model.addAttribute("sentRequests", List.of());
            model.addAttribute("chatPartners", List.of());
            model.addAttribute("emailId", userEmail);
            return "talkrequest";
        }
    }
}
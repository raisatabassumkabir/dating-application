package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.TalkRequest;
import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.MatchingService;
import bd.edu.seu.biye_shaddi.service.TalkRequestService;
import bd.edu.seu.biye_shaddi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/talk-requests")
public class TalkRequestController {
    private final TalkRequestService talkRequestService;
    private final UserService userService;
    private final MatchingService matchingService;

    public TalkRequestController(TalkRequestService talkRequestService, UserService userService, MatchingService matchingService) {
        this.talkRequestService = talkRequestService;
        this.userService = userService;
        this.matchingService = matchingService;
    }

    @PostMapping("/talk-request")
    public ResponseEntity<?> sendTalkRequest(@RequestParam String fromEmailId, @RequestParam String toEmailId) {
        try {
            TalkRequest request = talkRequestService.sendTalkRequest(fromEmailId, toEmailId);
            System.out.println("Talk request sent: from=" + fromEmailId + " to=" + toEmailId + ", id=" + request.getId());
            return ResponseEntity.ok(request);
        } catch (IllegalArgumentException e) {
            System.out.println("Bad request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error sending talk request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending talk request: " + e.getMessage());
        }
    }

    @PostMapping("/accept/{requestId}")
    public ResponseEntity<?> acceptTalkRequest(@PathVariable String requestId, @RequestParam String emailId) {
        try {
            talkRequestService.acceptTalkRequest(requestId, emailId);
            System.out.println("Talk request accepted: id=" + requestId + " by=" + emailId);
            return ResponseEntity.ok("Talk request accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("Bad request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error accepting talk request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error accepting talk request: " + e.getMessage());
        }
    }

    @PostMapping("/reject/{requestId}")
    public ResponseEntity<?> rejectTalkRequest(@PathVariable String requestId, @RequestParam String emailId) {
        try {
            talkRequestService.rejectTalkRequest(requestId, emailId);
            System.out.println("Talk request rejected: id=" + requestId + " by=" + emailId);
            return ResponseEntity.ok("Talk request rejected");
        } catch (IllegalArgumentException e) {
            System.out.println("Bad request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error rejecting talk request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error rejecting talk request: " + e.getMessage());
        }
    }

    @GetMapping("/pending/{emailId}")
    public ResponseEntity<?> getPendingTalkRequests(@PathVariable String emailId) {
        try {
            List<TalkRequest> pendingRequests = talkRequestService.getPendingRequests(emailId);
            System.out.println("Fetched " + pendingRequests.size() + " pending requests for " + emailId);
            return ResponseEntity.ok(pendingRequests);
        } catch (IllegalArgumentException e) {
            System.out.println("Bad request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error fetching pending requests: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching pending requests: " + e.getMessage());
        }
    }

    @GetMapping("/sent/{emailId}")
    public ResponseEntity<?> getSentTalkRequests(@PathVariable String emailId) {
        try {
            List<TalkRequest> sentRequests = talkRequestService.getSentRequests(emailId);
            System.out.println("Fetched " + sentRequests.size() + " sent requests for " + emailId);
            return ResponseEntity.ok(sentRequests);
        } catch (IllegalArgumentException e) {
            System.out.println("Bad request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error fetching sent requests: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching sent requests: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getTalkRequestStatus(@RequestParam String fromEmailId, @RequestParam String toEmailId) {
        try {
            List<TalkRequest> requests = talkRequestService.findByFromEmailIdAndToEmailId(fromEmailId, toEmailId);
            System.out.println("Found " + requests.size() + " requests from " + fromEmailId + " to " + toEmailId);
            if (requests.isEmpty()) {
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.ok(requests.get(0));
        } catch (Exception e) {
            System.out.println("Error fetching talk request status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching talk request status: " + e.getMessage());
        }
    }

    @GetMapping("/can-view-contact")
    public ResponseEntity<Boolean> canViewContactDetails(@RequestParam String viewerEmailId, @RequestParam String targetEmailId) {
        try {
            boolean canView = talkRequestService.canViewContactDetails(viewerEmailId, targetEmailId);
            System.out.println("Can view contact: viewer=" + viewerEmailId + ", target=" + targetEmailId + ", result=" + canView);
            return ResponseEntity.ok(canView);
        } catch (Exception e) {
            System.out.println("Error checking contact view: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
package bd.edu.seu.biye_shaddi.service;

import bd.edu.seu.biye_shaddi.model.TalkRequest;
import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.repository.TalkRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TalkRequestService {
    private final TalkRequestRepository talkRequestRepository;
    private final UserService userService;

    public TalkRequestService(TalkRequestRepository talkRequestRepository, UserService userService) {
        this.talkRequestRepository = talkRequestRepository;
        this.userService = userService;
    }

    public TalkRequest sendTalkRequest(String fromEmailId, String toEmailId) {
        Optional<User> fromUser = userService.getUserByEmail(fromEmailId);
        Optional<User> toUser = userService.getUserByEmail(toEmailId);
        if (!fromUser.isPresent()) {
            System.out.println("Sender not found: " + fromEmailId);
            throw new IllegalArgumentException("Sender not found: " + fromEmailId);
        }
        if (!toUser.isPresent()) {
            System.out.println("Recipient not found: " + toEmailId);
            throw new IllegalArgumentException("Recipient not found: " + toEmailId);
        }
        System.out.println("✅ Both users exist");

        List<TalkRequest> existingRequests = talkRequestRepository.findByFromEmailIdAndToEmailId(fromEmailId, toEmailId);
        if (!existingRequests.isEmpty()) {
            System.out.println("Talk request already exists: id=" + existingRequests.get(0).getId() + ", status=" + existingRequests.get(0).getStatus());
            return existingRequests.get(0);
        }
        TalkRequest request = new TalkRequest(fromEmailId, toEmailId, "PENDING");
        try {
            TalkRequest savedRequest = talkRequestRepository.save(request);
            System.out.println("Talk request saved: id=" + savedRequest.getId() + ", from=" + fromEmailId + ", to=" + toEmailId + ", status=" + savedRequest.getStatus());
            return savedRequest;
        } catch (Exception e) {
            System.out.println("Failed to save talk request: from=" + fromEmailId + ", to=" + toEmailId + ", error=" + e.getMessage());
            throw new RuntimeException("Failed to save talk request", e);
        }
    }

    public void acceptTalkRequest(String requestId, String emailId) {
        Optional<User> user = userService.getUserByEmail(emailId);
        if (!user.isPresent()) {
            System.out.println("Invalid user emailId: " + emailId);
            throw new IllegalArgumentException("Invalid user emailId: " + emailId);
        }
        Optional<TalkRequest> requestOptional = talkRequestRepository.findById(requestId);
        if (!requestOptional.isPresent()) {
            System.out.println("Request not found: " + requestId);
            throw new IllegalArgumentException("Request not found: " + requestId);
        }
        TalkRequest request = requestOptional.get();
        if (!request.getToEmailId().equals(emailId)) {
            System.out.println("Unauthorized to accept request " + requestId + " by " + emailId);
            throw new IllegalArgumentException("Unauthorized to accept this request");
        }
        request.setStatus("ACCEPTED");
        try {
            talkRequestRepository.save(request);
            System.out.println("Talk request " + requestId + " accepted by " + emailId);
        } catch (Exception e) {
            System.out.println("Failed to accept talk request " + requestId + " by " + emailId + ": " + e.getMessage());
            throw new RuntimeException("Failed to accept talk request", e);
        }
    }

    public void rejectTalkRequest(String requestId, String emailId) {
        Optional<User> user = userService.getUserByEmail(emailId);
        if (!user.isPresent()) {
            System.out.println("Invalid user emailId: " + emailId);
            throw new IllegalArgumentException("Invalid user emailId: " + emailId);
        }
        Optional<TalkRequest> requestOptional = talkRequestRepository.findById(requestId);
        if (!requestOptional.isPresent()) {
            System.out.println("Request not found: " + requestId);
            throw new IllegalArgumentException("Request not found: " + requestId);
        }
        TalkRequest request = requestOptional.get();
        if (!request.getToEmailId().equals(emailId)) {
            System.out.println("Unauthorized to reject request " + requestId + " by " + emailId);
            throw new IllegalArgumentException("Unauthorized to reject this request");
        }
        request.setStatus("REJECTED");
        try {
            talkRequestRepository.save(request);
            System.out.println("Talk request " + requestId + " rejected by " + emailId);
        } catch (Exception e) {
            System.out.println("Failed to reject talk request " + requestId + " by " + emailId + ": " + e.getMessage());
            throw new RuntimeException("Failed to reject talk request", e);
        }
    }

    public List<TalkRequest> getPendingRequests(String toEmailId) {
        Optional<User> user = userService.getUserByEmail(toEmailId);
        if (!user.isPresent()) {
            System.out.println("Invalid user emailId: " + toEmailId);
            throw new IllegalArgumentException("Invalid user emailId: " + toEmailId);
        }
        try {
            List<TalkRequest> requests = talkRequestRepository.findByToEmailIdAndStatus(toEmailId, "PENDING");
            System.out.println("Found " + requests.size() + " pending requests for " + toEmailId);
            return requests;
        } catch (Exception e) {
            System.out.println("Failed to fetch pending requests for " + toEmailId + ": " + e.getMessage());
            throw new RuntimeException("Failed to fetch pending requests", e);
        }
    }
//public List<TalkRequest> getPendingRequests(String toEmailId) {
//    System.out.println("Fetching pending requests for: " + toEmailId);
//    Optional<User> user = userService.getUserByEmail(toEmailId);
//    if (!user.isPresent()) {
//        System.out.println("User not found: " + toEmailId);
//        throw new IllegalArgumentException("Invalid user emailId: " + toEmailId);
//    }
//    try {
//        List<TalkRequest> requests = talkRequestRepository.findByToEmailIdAndStatus(toEmailId, "PENDING");
//        System.out.println("Found " + requests.size() + " pending requests:");
//        requests.forEach(req -> System.out.println(" - From: " + req.getFromEmailId() + ", Status: " + req.getStatus()));
//        return requests;
//    } catch (Exception e) {
//        System.out.println("Error fetching pending requests: " + e.getMessage());
//        throw new RuntimeException("Failed to fetch pending requests", e);
//    }
//}
    public boolean canViewContactDetails(String viewerEmailId, String targetEmailId) {
        Optional<User> viewer = userService.getUserByEmail(viewerEmailId);
        Optional<User> target = userService.getUserByEmail(targetEmailId);
        if (!viewer.isPresent() || !target.isPresent()) {
            System.out.println("Invalid user emailId: viewer=" + viewerEmailId + ", target=" + targetEmailId);
            return false;
        }
        List<TalkRequest> forward = talkRequestRepository.findByFromEmailIdAndToEmailIdAndStatus(viewerEmailId, targetEmailId, "ACCEPTED");
        List<TalkRequest> reverse = talkRequestRepository.findByFromEmailIdAndToEmailIdAndStatus(targetEmailId, viewerEmailId, "ACCEPTED");
        boolean canView = !forward.isEmpty() || !reverse.isEmpty();
        System.out.println("Can " + viewerEmailId + " view " + targetEmailId + "'s contact details? " + canView);
        return canView;
    }

    public List<TalkRequest> findByFromEmailIdAndToEmailId(String fromEmailId, String toEmailId) {
        try {
            List<TalkRequest> requests = talkRequestRepository.findByFromEmailIdAndToEmailId(fromEmailId, toEmailId);
            System.out.println("Found " + requests.size() + " requests from " + fromEmailId + " to " + toEmailId);
            return requests;
        } catch (Exception e) {
            System.out.println("Failed to find talk requests from " + fromEmailId + " to " + toEmailId + ": " + e.getMessage());
            throw new RuntimeException("Failed to find talk requests", e);
        }
    }
}
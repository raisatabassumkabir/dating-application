package bd.edu.seu.biye_shaddi.service;

import bd.edu.seu.biye_shaddi.model.TalkRequest;
import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.repository.TalkRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class TalkRequestService {
    private final TalkRequestRepository talkRequestRepository;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public TalkRequestService(TalkRequestRepository talkRequestRepository, UserService userService, MongoTemplate mongoTemplate) {
        this.talkRequestRepository = talkRequestRepository;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;
    }

    public TalkRequest sendTalkRequest(String fromEmailId, String toEmailId) {
        if (fromEmailId == null || fromEmailId.isEmpty()) {
            System.out.println("Invalid fromEmailId: " + fromEmailId);
            throw new IllegalArgumentException("Invalid fromEmailId");
        }
        if (toEmailId == null || toEmailId.isEmpty()) {
            System.out.println("Invalid toEmailId: " + toEmailId);
            throw new IllegalArgumentException("Invalid toEmailId");
        }
        try {
            mongoTemplate.getDb().getCollection("talkRequests").find().first();
            System.out.println("✅ Database connection verified for talkRequests collection");
        } catch (Exception e) {
            System.out.println("❌ Database connection error: " + e.getMessage());
            throw new RuntimeException("Database connection error", e);
        }
        System.out.println("Attempting to send talk request: from=" + fromEmailId + ", to=" + toEmailId);
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
        System.out.println("✅ Both users exist: sender=" + fromUser.get().getEmailId() + ", recipient=" + toUser.get().getEmailId());
        List<TalkRequest> existingRequests = talkRequestRepository.findByFromEmailIdAndToEmailId(fromEmailId, toEmailId);
        if (!existingRequests.isEmpty()) {
            System.out.println("Talk request already exists: id=" + existingRequests.get(0).getId() + ", status=" + existingRequests.get(0).getStatus());
            return existingRequests.get(0);
        }
        TalkRequest request = new TalkRequest(fromEmailId, toEmailId, "PENDING");
        System.out.println("Saving talk request: " + request.toString());
        try {
            if (request.getFromEmailId() == null || request.getToEmailId() == null || request.getStatus() == null) {
                System.out.println("Invalid TalkRequest data: " + request);
                throw new IllegalArgumentException("Invalid TalkRequest data");
            }
            TalkRequest savedRequest = talkRequestRepository.save(request);
            Optional<TalkRequest> verified = talkRequestRepository.findById(savedRequest.getId());
            if (verified.isPresent()) {
                System.out.println("✅ Talk request saved and verified: id=" + savedRequest.getId() + ", from=" + fromEmailId + ", to=" + toEmailId + ", status=" + savedRequest.getStatus());
            } else {
                System.out.println("❌ Talk request saved but not found in database: id=" + savedRequest.getId());
                throw new RuntimeException("Talk request not found after save");
            }
            return savedRequest;
        } catch (Exception e) {
            System.out.println("❌ Failed to save talk request: from=" + fromEmailId + ", to=" + toEmailId + ", error=" + e.getMessage());
            throw new RuntimeException("Failed to save talk request", e);
        }
    }

    public void acceptTalkRequest(String requestId, String emailId) {
        System.out.println("Attempting to accept talk request: id=" + requestId + ", by=" + emailId);
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
        System.out.println("Attempting to reject talk request: id=" + requestId + ", by=" + emailId);
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
        System.out.println("Fetching for: " + toEmailId);
        Optional<User> user = userService.getUserByEmail(toEmailId);
        if (!user.isPresent()) {
            System.out.println("Invalid user emailId: " + toEmailId);
            throw new IllegalArgumentException("Invalid user emailId: " + toEmailId);
        }
        try {
            List<TalkRequest> requests = talkRequestRepository.findByToEmailIdAndStatus(toEmailId, "PENDING");
            System.out.println("Found " + requests.size() + " pending requests for " + toEmailId + ":");
            requests.forEach(req -> System.out.println(" - From: " + req.getFromEmailId() + ", ID: " + req.getId() + ", Status: " + req.getStatus()));
            return requests;
        } catch (Exception e) {
            System.out.println("Failed to fetch pending requests for " + toEmailId + ": " + e.getMessage());
            throw new RuntimeException("Failed to fetch pending requests", e);
        }
    }

    public List<TalkRequest> getSentRequests(String fromEmailId) {
        System.out.println("Fetching sent requests for: " + fromEmailId);
        Optional<User> user = userService.getUserByEmail(fromEmailId);
        if (!user.isPresent()) {
            System.out.println("Invalid user emailId: " + fromEmailId);
            throw new IllegalArgumentException("Invalid user emailId: " + fromEmailId);
        }
        try {
            List<TalkRequest> requests = talkRequestRepository.findByFromEmailId(fromEmailId);
            System.out.println("Found " + requests.size() + " sent requests for " + fromEmailId + ":");
            requests.forEach(req -> System.out.println(" - To: " + req.getToEmailId() + ", ID: " + req.getId() + ", Status: " + req.getStatus()));
            return requests;
        } catch (Exception e) {
            System.out.println("Failed to fetch sent requests for " + fromEmailId + ": " + e.getMessage());
            throw new RuntimeException("Failed to fetch sent requests", e);
        }
    }

    public boolean canViewContactDetails(String viewerEmailId, String targetEmailId) {
        System.out.println("Checking if " + viewerEmailId + " can view contact details of " + targetEmailId);
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
        System.out.println("Finding talk requests: from=" + fromEmailId + ", to=" + toEmailId);
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
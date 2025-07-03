package bd.edu.seu.biye_shaddi.service;

import bd.edu.seu.biye_shaddi.model.TalkRequest;
import bd.edu.seu.biye_shaddi.repository.TalkRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TalkRequestService {

    private final TalkRequestRepository talkRequestRepository;

    public TalkRequestService(TalkRequestRepository talkRequestRepository) {
        this.talkRequestRepository = talkRequestRepository;
    }

    public TalkRequest sendTalkRequest(String fromEmailId, String toEmailId) {
        if (fromEmailId.equals(toEmailId)) {
            throw new IllegalArgumentException("Cannot send talk request to self");
        }
        List<TalkRequest> existingRequests = talkRequestRepository.findByFromEmailIdAndToEmailIdAndStatus(fromEmailId, toEmailId, "PENDING");
        if (!existingRequests.isEmpty()) {
            throw new IllegalStateException("Talk request already sent");
        }
        TalkRequest request = new TalkRequest(fromEmailId, toEmailId, "PENDING");
        request.setId(UUID.randomUUID().toString());
        return talkRequestRepository.save(request);
    }

    public TalkRequest acceptTalkRequest(String requestId, String emailId) {
        Optional<TalkRequest> optionalRequest = talkRequestRepository.findById(requestId);
        if (optionalRequest.isEmpty()) {
            throw new IllegalArgumentException("Talk request not found");
        }
        TalkRequest request = optionalRequest.get();
        if (!request.getToEmailId().equals(emailId)) {
            throw new IllegalArgumentException("Unauthorized to accept this request");
        }
        if (!request.getStatus().equals("PENDING")) {
            throw new IllegalStateException("Request is not pending");
        }
        request.setStatus("ACCEPTED");
        return talkRequestRepository.save(request);
    }

    public TalkRequest rejectTalkRequest(String requestId, String emailId) {
        Optional<TalkRequest> optionalRequest = talkRequestRepository.findById(requestId);
        if (optionalRequest.isEmpty()) {
            throw new IllegalArgumentException("Talk request not found");
        }
        TalkRequest request = optionalRequest.get();
        if (!request.getToEmailId().equals(emailId)) {
            throw new IllegalArgumentException("Unauthorized to reject this request");
        }
        if (!request.getStatus().equals("PENDING")) {
            throw new IllegalStateException("Request is not pending");
        }
        request.setStatus("REJECTED");
        return talkRequestRepository.save(request);
    }

    public List<TalkRequest> getPendingRequests(String emailId) {
        return talkRequestRepository.findByToEmailIdAndStatus(emailId, "PENDING");
    }

    public List<TalkRequest> getSentRequests(String emailId) {
        return talkRequestRepository.findByFromEmailId(emailId);
    }

    public boolean canViewContactDetails(String viewerEmailId, String targetEmailId) {
        List<TalkRequest> requests = talkRequestRepository.findByFromEmailIdAndToEmailId(viewerEmailId, targetEmailId);
        return requests.stream().anyMatch(req -> "ACCEPTED".equals(req.getStatus()));
    }

    public List<TalkRequest> findByFromEmailIdAndToEmailId(String fromEmailId, String toEmailId) {
        return talkRequestRepository.findByFromEmailIdAndToEmailId(fromEmailId, toEmailId);
    }

    public List<TalkRequest> findByToEmailIdAndFromEmailId(String toEmailId, String fromEmailId) {
        return talkRequestRepository.findByToEmailIdAndFromEmailId(toEmailId, fromEmailId);
    }
}
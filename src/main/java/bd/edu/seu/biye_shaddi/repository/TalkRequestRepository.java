package bd.edu.seu.biye_shaddi.repository;

import bd.edu.seu.biye_shaddi.model.TalkRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TalkRequestRepository extends MongoRepository<TalkRequest, String> {

    List<TalkRequest> findByFromEmailIdAndToEmailId(String fromEmailId, String toEmailId);
    List<TalkRequest> findByToEmailIdAndStatus(String toEmailId, String status);
    List<TalkRequest> findByFromEmailIdAndToEmailIdAndStatus(String fromEmailId, String toEmailId, String status);
    List<TalkRequest> findByFromEmailId(String fromEmailId);
    List<TalkRequest> findByToEmailIdAndFromEmailId(String toEmailId, String fromEmailId);
}
package bd.edu.seu.biye_shaddi.repository;

import bd.edu.seu.biye_shaddi.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findBySenderIdAndReceiverId(String senderId, String receiverId);

    List<ChatMessage> findBySenderIdAndReceiverIdAndIsRead(String senderId, String receiverId, boolean isRead);

    long countBySenderIdAndReceiverIdAndIsRead(String senderId, String receiverId, boolean isRead);

    @Query(value = "{ $or: [ { 'senderId': ?0, 'receiverId': ?1 }, { 'senderId': ?1, 'receiverId': ?0 } ] }", sort = "{ 'timestamp': 1 }")
    List<ChatMessage> findConversation(String userId1, String userId2);
}

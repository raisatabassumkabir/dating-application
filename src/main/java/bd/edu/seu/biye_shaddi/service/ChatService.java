package bd.edu.seu.biye_shaddi.service;

import bd.edu.seu.biye_shaddi.model.ChatMessage;
import bd.edu.seu.biye_shaddi.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage sendMessage(String senderId, String receiverId, String content) {
        ChatMessage message = new ChatMessage(senderId, receiverId, content);
        return chatMessageRepository.save(message);
    }
    
    public List<ChatMessage> getConversation(String userId1, String userId2) {
        return chatMessageRepository.findConversation(userId1, userId2);
    }

    public void markAsRead(String senderEmail, String receiverEmail) {
        List<ChatMessage> unread = chatMessageRepository.findBySenderIdAndReceiverIdAndIsRead(senderEmail,
                receiverEmail, false);
        for (ChatMessage msg : unread) {
            msg.setRead(true);
        }
        chatMessageRepository.saveAll(unread);
    }

    public long getUnreadCount(String senderEmail, String receiverEmail) {
        return chatMessageRepository.countBySenderIdAndReceiverIdAndIsRead(senderEmail, receiverEmail, false);
    }
}

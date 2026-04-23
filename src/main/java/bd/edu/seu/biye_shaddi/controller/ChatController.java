package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.ChatMessage;
import bd.edu.seu.biye_shaddi.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    // WebSocket handler — receives message from sender, saves, and forwards to
    // recipient
    @MessageMapping("/chat.send")
    public void handleWebSocketMessage(@Payload Map<String, String> payload) {
        String senderId = payload.get("senderId");
        String receiverId = payload.get("receiverId");
        String content = payload.get("content");

        if (senderId == null || receiverId == null || content == null || content.trim().isEmpty()) {
            return;
        }

        ChatMessage message = chatService.sendMessage(senderId, receiverId, content.trim());

        // Send to receiver's personal queue
        messagingTemplate.convertAndSendToUser(receiverId, "/queue/messages", message);
        // Also echo back to sender so they get the saved message with ID/timestamp
        messagingTemplate.convertAndSendToUser(senderId, "/queue/messages", message);
    }

    // REST: Send a message (fallback for non-WebSocket)
    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestParam String senderId, @RequestParam String receiverId,
            @RequestParam String content) {
        ChatMessage message = chatService.sendMessage(senderId, receiverId, content);
        // Also broadcast via WebSocket
        messagingTemplate.convertAndSendToUser(receiverId, "/queue/messages", message);
        messagingTemplate.convertAndSendToUser(senderId, "/queue/messages", message);
        return ResponseEntity.ok(message);
    }

    // REST: Get conversation history between two users
    @GetMapping("/history")
    public ResponseEntity<List<ChatMessage>> getHistory(@RequestParam String userId1, @RequestParam String userId2) {
        return ResponseEntity.ok(chatService.getConversation(userId1, userId2));
    }

    // REST: Mark all messages from sender as read by receiver
    @PostMapping("/mark-read")
    public ResponseEntity<Void> markAsRead(@RequestParam String senderEmail, @RequestParam String receiverEmail) {
        chatService.markAsRead(senderEmail, receiverEmail);
        return ResponseEntity.ok().build();
    }

    // REST: Get unread count from a specific sender
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(@RequestParam String senderEmail, @RequestParam String receiverEmail) {
        return ResponseEntity.ok(chatService.getUnreadCount(senderEmail, receiverEmail));
    }
}

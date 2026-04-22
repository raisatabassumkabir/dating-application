package bd.edu.seu.biye_shaddi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blocks")
public class Block {
    @Id
    private String id;
    private String userId; // The user who is blocking
    private String blockedUserId; // The user being blocked
    private String reason; // Optional reason

    public Block() {
    }

    public Block(String userId, String blockedUserId, String reason) {
        this.userId = userId;
        this.blockedUserId = blockedUserId;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBlockedUserId() {
        return blockedUserId;
    }

    public void setBlockedUserId(String blockedUserId) {
        this.blockedUserId = blockedUserId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

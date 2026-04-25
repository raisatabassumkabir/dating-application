package bd.edu.seu.biye_shaddi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "likes")
public class Like {
    @Id
    private String id;
    private String userId;
    private String likedUserId;
    private LocalDateTime timestamp;

    public Like() {
        this.timestamp = LocalDateTime.now();
    }

    public Like(String userId, String likedUserId) {
        this.userId = userId;
        this.likedUserId = likedUserId;
        this.timestamp = LocalDateTime.now();
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

    public String getLikedUserId() {
        return likedUserId;
    }

    public void setLikedUserId(String likedUserId) {
        this.likedUserId = likedUserId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

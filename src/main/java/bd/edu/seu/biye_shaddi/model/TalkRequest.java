package bd.edu.seu.biye_shaddi.model;
import org.springframework.data.annotation.Id;


import java.time.LocalDateTime;

public class TalkRequest {
    @Id
    private String id;
    private String fromEmailId;
    private String toEmailId;
    private String status; // PENDING, ACCEPTED, REJECTED
    private LocalDateTime timestamp;

    // Constructors
    public TalkRequest() {
        this.timestamp = LocalDateTime.now();
    }

    public TalkRequest(String fromEmailId, String toEmailId, String status) {
        this.fromEmailId = fromEmailId;
        this.toEmailId = toEmailId;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromEmailId() {
        return fromEmailId;
    }

    public void setFromEmailId(String fromEmailId) {
        this.fromEmailId = fromEmailId;
    }

    public String getToEmailId() {
        return toEmailId;
    }


    public void setToEmailId(String toEmailId) {
        this.toEmailId = toEmailId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
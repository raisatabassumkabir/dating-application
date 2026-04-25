package bd.edu.seu.biye_shaddi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shortlists")
public class Shortlist {
    @Id
    private String id;
    private String userId; // The user who is shortlisting
    private String shortlistedUserId; // The user being shortlisted

    public Shortlist() {
    }

    public Shortlist(String userId, String shortlistedUserId) {
        this.userId = userId;
        this.shortlistedUserId = shortlistedUserId;
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

    public String getShortlistedUserId() {
        return shortlistedUserId;
    }

    public void setShortlistedUserId(String shortlistedUserId) {
        this.shortlistedUserId = shortlistedUserId;
    }
}

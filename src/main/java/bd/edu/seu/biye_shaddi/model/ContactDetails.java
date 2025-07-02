package bd.edu.seu.biye_shaddi.model;

import org.springframework.data.annotation.Id;

public class ContactDetails {
    @Id
    private String id;
    private String emailId;
    private String mobile;
    private String whatsApp;
    private String instagram;
    private String facebook;

    public ContactDetails(String facebook, String instagram, String mobile, String whatsApp, String emailId) {
        this.facebook = facebook;
        this.instagram = instagram;
        this.mobile = mobile;
        this.whatsApp = whatsApp;
        this.emailId = emailId;
    }

    public ContactDetails() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }


    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWhatsApp() {
        return whatsApp;
    }

    public void setWhatsApp(String whatsApp) {
        this.whatsApp = whatsApp;
    }
}

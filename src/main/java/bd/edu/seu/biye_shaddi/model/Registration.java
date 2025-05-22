package bd.edu.seu.biye_shaddi.model;

import org.springframework.data.annotation.Id;

public class Registration {
    @Id
    private String email;
    private String password;
    private String nid;
    private String passport;

    public Registration(String email, String nid, String passport, String password) {
        this.email = email;
        this.nid = nid;
        this.passport = passport;
        this.password = password;
    }
 public Registration() {

 }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

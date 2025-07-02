package bd.edu.seu.biye_shaddi.model;

import org.springframework.data.annotation.Id;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Registration {
    @Id
    private String email;
    private String password;
    private String nidOrPassport;


    public Registration() {

    }

    public Registration(String email, String nidOrPassport, String password) {
        this.email = email;
        this.nidOrPassport = nidOrPassport;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNidOrPassport() {
        return nidOrPassport;
    }

    public void setNidOrPassport(String nidOrPassport) {
        this.nidOrPassport = nidOrPassport;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}
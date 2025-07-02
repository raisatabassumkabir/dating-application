package bd.edu.seu.biye_shaddi.model;

import org.springframework.data.annotation.Id;

public class Login {
    @Id
    private String email;
    private String password;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public Login() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

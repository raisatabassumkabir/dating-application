package bd.edu.seu.biye_shaddi.service;


import bd.edu.seu.biye_shaddi.model.Login;
import bd.edu.seu.biye_shaddi.repository.LoginRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

   private final LoginRepository loginRepository;

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public Login saveLogin(Login login) {
        return loginRepository.save(login);

    }
}

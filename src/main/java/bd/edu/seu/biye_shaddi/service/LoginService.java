package bd.edu.seu.biye_shaddi.service;


import bd.edu.seu.biye_shaddi.model.Login;
import bd.edu.seu.biye_shaddi.model.Registration;
import bd.edu.seu.biye_shaddi.repository.LoginRepository;
import bd.edu.seu.biye_shaddi.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

   private final LoginRepository loginRepository;
   private final RegistrationRepository registrationRepository;

    public LoginService(LoginRepository loginRepository, RegistrationRepository registrationRepository) {
        this.loginRepository = loginRepository;

        this.registrationRepository = registrationRepository;
    }

    public Login saveLogin(Login login) {
        return loginRepository.save(login);

    }
    public boolean userAuthenticated(String email, String password) {
     Optional<Registration> optionalRegistration =registrationRepository.findByEmail(email);
     if (optionalRegistration.isPresent()) {
         if (optionalRegistration.get().getPassword().equals(password)) {
             return true;
         }
     }
        return false;
    }

}

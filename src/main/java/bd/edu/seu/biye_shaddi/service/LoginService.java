package bd.edu.seu.biye_shaddi.service;


import bd.edu.seu.biye_shaddi.model.Login;
import bd.edu.seu.biye_shaddi.model.Registration;
import bd.edu.seu.biye_shaddi.repository.LoginRepository;
import bd.edu.seu.biye_shaddi.repository.RegistrationRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        Optional<Registration> optionalRegistration = registrationRepository.findByEmail(email);
        if (optionalRegistration.isPresent()) {
            if ( new BCryptPasswordEncoder().matches(password, optionalRegistration.get().getPassword())) {
                return true;
            } else {
                System.out.println("Password does not match."); // Debug log
            }
        } else {
            System.out.println("User  not found."); // Debug log
        }
        return false;
    }

}



//}

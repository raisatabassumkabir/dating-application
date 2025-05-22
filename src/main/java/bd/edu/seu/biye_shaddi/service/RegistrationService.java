package bd.edu.seu.biye_shaddi.service;


import bd.edu.seu.biye_shaddi.model.Registration;
import bd.edu.seu.biye_shaddi.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;


    public RegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public Registration save(Registration registration) {
        return  registrationRepository.save(registration);
    }
}

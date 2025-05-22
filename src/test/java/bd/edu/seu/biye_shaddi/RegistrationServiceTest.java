package bd.edu.seu.biye_shaddi;

import bd.edu.seu.biye_shaddi.model.Registration;
import bd.edu.seu.biye_shaddi.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;

    @Test
    public void saveRegistration() {
        Registration registration = new Registration();

        registration.setEmail("test@test.com");
        registration.setPassword("password");
        registration.setNidOrPassport("88888888");

        registrationService.save(registration);

    }
}

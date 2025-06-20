package bd.edu.seu.biye_shaddi.springSecurity;

import bd.edu.seu.biye_shaddi.model.Registration;
import bd.edu.seu.biye_shaddi.repository.RegistrationRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final RegistrationRepository registrationRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(RegistrationRepository registrationRepository, PasswordEncoder passwordEncoder) {
        this.registrationRepository = registrationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        Registration registration = registrationRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        if (passwordEncoder.matches(password, registration.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    email,
                    password,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } else {
            throw new BadCredentialsException("Invalid password");
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}

package bd.edu.seu.biye_shaddi.springSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(request-> request
                .requestMatchers("/","/registration","/index","/registration-form","/login","/login-form","/user-info-form","/user_dashboard","/user_info","/contact_details","/saveContactDetails","/contact_info","/matches","/talkrequest").permitAll()
                        .requestMatchers("/picture/**","/css/**","/uploads/**","/user/**").permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(form->form.loginPage("/login")
                        .usernameParameter("email")
                       .passwordParameter("password")
                       //.successForwardUrl("/user_dashboard")
                        .failureForwardUrl("/login?error=true")
                        .permitAll())
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

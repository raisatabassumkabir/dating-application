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
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http.authorizeHttpRequests(request -> request
                                .requestMatchers("/", "/registration", "/index", "/registration-form", "/login",
                                                "/login-form",
                                                "/user_dashboard", "/user-info-form", "/user_info", "/contact_details",
                                                "/saveContactDetails",
                                                "/contact_info", "/matches", "/talkrequest", "/matches-contact-info",
                                                "/error", "/discover")
                                .permitAll()
                                .requestMatchers("/picture/**", "/css/**", "/js/**", "/uploads/**", "/user/**",
                                                "/api/talk-requests/**",
                                                "/api/like/**", "/api/shortlist/**", "/api/block/**",
                                                "/api/chat/**", "/api/save-personal", "/api/save-family",
                                                "/api/save-partner", "/api/save-contact", "/ws/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                                .formLogin(form -> form.loginPage("/login")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/discover", true)
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/user-info-form", "/saveContactDetails",
                                                                "/api/save-personal", "/api/save-family",
                                                                "/api/save-partner", "/api/save-contact"))
                                .build();

        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

}

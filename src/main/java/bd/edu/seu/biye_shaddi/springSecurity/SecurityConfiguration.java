package bd.edu.seu.biye_shaddi.springSecurity;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(request-> request
                .requestMatchers("/","/registration","/index","/registration-form","/login","/login-form","/user-info-form","/user_dashboard","/user_info").permitAll()
                        .requestMatchers("/picture/**","/css/**","/uploads/**").permitAll()
                        .anyRequest()
                        .authenticated())
//                .formLogin(form->form.loginPage("/login")
//                        .usernameParameter("email")
//                        .passwordParameter("password")
//                        .successForwardUrl("/user_dashboard")
//                        .failureForwardUrl("/login?error=true")
//                        .permitAll())
                .build();

    }


}

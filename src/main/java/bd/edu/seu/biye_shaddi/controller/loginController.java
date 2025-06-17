package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.Login;
import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.repository.UserRepository;
import bd.edu.seu.biye_shaddi.service.LoginService;
import bd.edu.seu.biye_shaddi.service.RegistrationService;
import bd.edu.seu.biye_shaddi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class loginController {
    private final LoginService loginService;

    private final UserRepository userRepository;
    private final UserService userService;


    public loginController(LoginService loginService, UserRepository userRepository, UserService userService) {
        this.loginService = loginService;

        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("login", new Login());
        return "login";
    }




    @PostMapping("/login-form")
    public String loginForm(@RequestParam String email, @RequestParam String password, Model model) {
        boolean isUserAuthenticated = loginService.userAuthenticated(email, password);
        if (isUserAuthenticated) {
            // Check if user-info exists and is filled
            Optional<User> userOptional = userRepository.findByEmailId(email);
            if (userOptional.isPresent() && userOptional.get().getName() != null && !userOptional.get().getName().isEmpty()) {
                // Profile is filled, redirect to dashboard
                 return "redirect:/user_info?emailId=" + email;

            } else {
                // Profile not filled, redirect to user_info
                return "redirect:/user_dashboard?emailId=" + email;
            }
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }//working one

}

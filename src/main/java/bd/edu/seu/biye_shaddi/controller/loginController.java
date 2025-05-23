package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.Login;
import bd.edu.seu.biye_shaddi.service.LoginService;
import bd.edu.seu.biye_shaddi.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class loginController {
    private final LoginService loginService;


    public loginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("login", new Login());
        return "login";
    }


    @PostMapping("/login-form")
    public String loginForm(@RequestParam String email, @RequestParam String password, Model model) {

        boolean isUserAuthenticated=loginService.userAuthenticated(email,password);
        if (isUserAuthenticated) {
            return "user_dashboard";

        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";

        }
    }
}
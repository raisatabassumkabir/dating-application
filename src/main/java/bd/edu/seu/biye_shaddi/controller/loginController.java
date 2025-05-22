package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.Login;
import bd.edu.seu.biye_shaddi.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class loginController {
    private final  LoginService loginService;

    public loginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    public String loginForm(@ModelAttribute Login login) {
        loginService.saveLogin(login);
        return "login";

    }
}

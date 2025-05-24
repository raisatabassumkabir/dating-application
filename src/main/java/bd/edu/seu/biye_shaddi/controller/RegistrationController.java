package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.Registration;
import bd.edu.seu.biye_shaddi.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final   RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/registration")
     public String registrationPage(Model model) {
        model.addAttribute("registration", new Registration());
        return "registration";
    }

    @PostMapping("/registration-form")
    public String registration  (@ModelAttribute Registration registration , @RequestParam String email , @RequestParam String password,@RequestParam String nidOrPassport ){
        registrationService.save(registration);
        return "login";
    }
}

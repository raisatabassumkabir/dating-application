package bd.edu.seu.biye_shaddi.controller;


import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user_dashboard")
    public String userDashboardPage(Model model){
        return "user_dashboard";
    }
    @PostMapping("/user-info-form")
    public String userInfoForm(@ModelAttribute User user){
        userService.saveUser(user);
        return "user_dashboard";
    }
}

package bd.edu.seu.biye_shaddi.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user_dashboard")
    public String userDashboardPage(){
        return "user_dashboard";
    }
}

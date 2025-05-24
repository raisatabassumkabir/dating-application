package bd.edu.seu.biye_shaddi.controller;


import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    public String userInfoForm(@ModelAttribute User user,@RequestParam String name,@RequestParam int age,@RequestParam String gender,@RequestParam String location, @RequestParam String education, @RequestParam String religion, @RequestParam String height, @RequestParam String relationshipStatus, @RequestParam String bio,@RequestParam String  profession,@RequestParam String interests,@RequestParam String profilePictureUrl, @RequestParam int preferredAgeMin ,@RequestParam int preferredAgeMax ,@RequestParam String preferredGender,@RequestParam String preferredHeightMin,@RequestParam String preferredHeightMax){
        userService.saveUser(user);
        return "user_info";
    }

    @GetMapping("/user_info")
    public String seeInfoPage(Model model){
        return "user_info";
    }
}

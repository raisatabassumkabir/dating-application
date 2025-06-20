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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserController {


    private final UserService userService;

    public UserController( UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user_dashboard")
    public String userDashboardPage(Model model, @RequestParam("emailId") String emailId) {
        if (emailId!= null && !emailId.isEmpty()) {
            Optional<User> userOptional = userService.getUserByEmail(emailId);
            if (userOptional.isPresent()) {
                return "redirect:/user_info?emailId=" + emailId;
            }
            User user = new User();
            user.setEmailId(emailId);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }
        return "user_dashboard";
    }//the working one


    @PostMapping("/user-info-form")
    public String userInfoForm(@ModelAttribute User user,@RequestParam("profilePicture")MultipartFile file) throws IOException  {
        //  profile picture upload

        if (!file.isEmpty()) {
            String uploadDir = "uploads/";
            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalFileName;

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);

            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // store the file path in the user model
            user.setProfilePictureUrl("/" + uploadDir + fileName); // assuming profilePictureUrl is a String

        }
        userService.saveUser(user);
        System.out.println("User: " + user);
       //return "redirect:/contact_details?emailId=" + user.getEmailId();
        return "redirect:/contact_details?emailId=" + user.getEmailId();

    }



    @GetMapping("/user_info")
    public String seeProfilePage(@RequestParam String emailId, Model model) {
        Optional<User> userOptional = userService.getUserByEmail(emailId);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
        } else {
            model.addAttribute("error", "User not found");
            return "error";
        }
        return "user_info";
    }//the working one


}

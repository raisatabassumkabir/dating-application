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
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserController {


    private final UserService userService;

    public UserController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user_dashboard")
    public String userDashboardPage(Model model, @RequestParam String id) {
        if (id!= null && !id.isEmpty()) {
            Optional<User> userOptional = userService.getUserById(id);
            if (userOptional.isPresent()) {
                return "redirect:/user_info?id=" + id;
            }
            User user = new User();
            // Optionally set the id if needed
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }
        return "user_dashboard";
    }


    @PostMapping("/user-info-form")
    public String userInfoForm(@ModelAttribute User user, @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture) {
        // Handle profile picture upload
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                String uploadDir = "uploads/";
                java.io.File uploadDirFile = new java.io.File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }
                String fileExtension = org.springframework.util.StringUtils.getFilenameExtension(profilePicture.getOriginalFilename());
                String newFileName = java.util.UUID.randomUUID().toString() + (fileExtension != null ? "." + fileExtension : "");
                java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir, newFileName);
                java.nio.file.Files.copy(profilePicture.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                user.setProfilePictureUrl("/" + uploadDir + newFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        userService.saveUser(user);
        return "redirect:/user_info?id=" + user.getId();
    }

    @GetMapping("/user_info")
    public String seeProfilePage(@RequestParam("id") String id, Model model) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
        } else {
            model.addAttribute("error", "User not found");
            return "error";
        }
        return "user_info";
    }



}

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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user_dashboard")
    public String userDashboardPage(Model model, @RequestParam(value = "emailId", required = false) String emailId) {
        User user;
        if (emailId != null && !emailId.isEmpty()) {
            Optional<User> userOptional = userService.getUserByEmail(emailId);
            user = userOptional.orElseGet(() -> {
                User newUser = new User();
                newUser.setEmailId(emailId);
                return newUser;
            });
        } else {
            user = new User();
        }
        model.addAttribute("user", user);
        model.addAttribute("emailId", emailId);
        System.out.println("Accessing user_dashboard with emailId: " + emailId + ", Profile Picture: "
                + user.getProfilePictureUrl());
        return "user_dashboard";
    }

    @PostMapping("/user-info-form")
    public String userInfoForm(@ModelAttribute User user, @RequestParam("profilePicture") MultipartFile file)
            throws IOException {
        System.out.println("Received user data: " + user);
        System.out.println("Profile picture is empty: " + file.isEmpty());

        // Preserve existing profile picture if no new file is uploaded
        if (!file.isEmpty()) {
            String uploadDir = "Uploads/";
            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalFileName;

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePictureUrl("/" + uploadDir + fileName);
            System.out.println("Uploaded new profile picture: " + user.getProfilePictureUrl());
        } else {
            // If no new file is uploaded, retain the existing profile picture URL
            if (user.getId() != null) {
                Optional<User> existingUser = userService.getUserById(user.getId());
                if (existingUser.isPresent() && existingUser.get().getProfilePictureUrl() != null) {
                    user.setProfilePictureUrl(existingUser.get().getProfilePictureUrl());
                    System.out.println("Retained existing profile picture: " + user.getProfilePictureUrl());
                } else {
                    user.setProfilePictureUrl(null);
                    System.out.println("No existing profile picture found for user ID: " + user.getId());
                }
            } else {
                user.setProfilePictureUrl(null);
                System.out.println("No profile picture set for new user");
            }
        }

        userService.saveUser(user);
        System.out.println("Saved user: " + user);
        return "redirect:/contact_details?emailId=" + user.getEmailId();
    }

    @GetMapping("/user_info")
    public String seeProfilePage(@RequestParam String emailId, Model model) {
        Optional<User> userOptional = userService.getUserByEmail(emailId);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            model.addAttribute("emailId", emailId);
            System.out.println("Displaying user_info for emailId: " + emailId + ", Profile Picture: "
                    + userOptional.get().getProfilePictureUrl());
        } else {
            model.addAttribute("error", "User not found");
            System.out.println("User not found for emailId: " + emailId);
            return "error";
        }
        return "user_info";
    }
}
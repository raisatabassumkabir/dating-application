package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
public class UserController {

    private final UserService userService;
    private final bd.edu.seu.biye_shaddi.repository.RegistrationRepository registrationRepository;

    public UserController(UserService userService,
            bd.edu.seu.biye_shaddi.repository.RegistrationRepository registrationRepository) {
        this.userService = userService;
        this.registrationRepository = registrationRepository;
    }

    @GetMapping("/user_dashboard")
    public String userDashboardPage(Model model, @RequestParam(value = "emailId", required = false) String emailId) {
        User user;
        if (emailId != null && !emailId.isEmpty()) {
            Optional<User> userOptional = userService.getUserByEmail(emailId);
            user = userOptional.orElseGet(() -> {
                User newUser = new User();
                newUser.setEmailId(emailId);
                syncRegistrationData(newUser, emailId);
                return newUser;
            });
        } else {
            // New user scenario or user not found in DB yet
            user = new User();
            if (emailId != null && !emailId.isEmpty()) {
                user.setEmailId(emailId);
                syncRegistrationData(user, emailId);
            }
        }
        if (user.getContactDetails() == null) {
            user.setContactDetails(new bd.edu.seu.biye_shaddi.model.ContactDetails());
        }
        model.addAttribute("user", user);
        model.addAttribute("emailId", emailId);
        System.out.println("Accessing user_dashboard with emailId: " + emailId + ", Profile Picture: "
                + user.getProfilePictureUrl());
        return "user_dashboard";
    }

    // ==================== AJAX per-section save endpoints ====================

    @PostMapping("/api/save-personal")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> savePersonal(
            @RequestParam String emailId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(required = false) String height,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String maritalStatus,
            @RequestParam(required = false) String religion,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String monthlyIncome,
            @RequestParam(required = false) String bio,
            @RequestParam(value = "interests", required = false) List<String> interests,
            @RequestParam(value = "profilePicture", required = false) MultipartFile file) {
        try {
            User user = getOrCreateUser(emailId);

            if (name != null)
                user.setName(name);
            if (height != null)
                user.setHeight(height);
            if (gender != null)
                user.setGender(gender);
            if (maritalStatus != null)
                user.setMaritalStatus(maritalStatus);
            if (religion != null)
                user.setReligion(religion);
            if (education != null)
                user.setEducation(education);
            if (profession != null)
                user.setProfession(profession);
            if (location != null)
                user.setLocation(location);
            if (monthlyIncome != null)
                user.setMonthlyIncome(monthlyIncome);
            if (bio != null)
                user.setBio(bio);
            if (interests != null)
                user.setInterests(interests);

            // Parse DOB
            if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    user.setDateOfBirth(sdf.parse(dateOfBirth));
                    long ageInMillis = new java.util.Date().getTime() - user.getDateOfBirth().getTime();
                    long ageInYears = ageInMillis / (1000L * 60 * 60 * 24 * 365);
                    user.setAge((int) ageInYears);
                } catch (Exception e) {
                    System.out.println("Error parsing DOB: " + e.getMessage());
                }
            }

            // Handle profile picture
            if (file != null && !file.isEmpty()) {
                String uploadDir = "Uploads/";
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath))
                    Files.createDirectories(uploadPath);
                Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                user.setProfilePictureUrl("/" + uploadDir + fileName);
            }

            userService.saveUser(user);
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("message", "Personal info saved successfully!");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            Map<String, Object> res = new HashMap<>();
            res.put("success", false);
            res.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(res);
        }
    }

    @PostMapping("/api/save-family")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveFamily(
            @RequestParam String emailId,
            @RequestParam(required = false) String familyStatus,
            @RequestParam(required = false) String familyType,
            @RequestParam(required = false) String fatherStatus,
            @RequestParam(required = false) String motherStatus,
            @RequestParam(required = false, defaultValue = "0") int numberOfSiblings) {
        try {
            User user = getOrCreateUser(emailId);

            if (familyStatus != null)
                user.setFamilyStatus(familyStatus);
            if (familyType != null)
                user.setFamilyType(familyType);
            if (fatherStatus != null)
                user.setFatherStatus(fatherStatus);
            if (motherStatus != null)
                user.setMotherStatus(motherStatus);
            user.setNumberOfSiblings(numberOfSiblings);

            userService.saveUser(user);
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("message", "Family info saved successfully!");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            Map<String, Object> res = new HashMap<>();
            res.put("success", false);
            res.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(res);
        }
    }

    @PostMapping("/api/save-partner")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> savePartner(
            @RequestParam String emailId,
            @RequestParam(required = false, defaultValue = "0") int preferredAgeMin,
            @RequestParam(required = false, defaultValue = "0") int preferredAgeMax,
            @RequestParam(required = false) String preferredHeightMin,
            @RequestParam(required = false) String preferredHeightMax,
            @RequestParam(required = false) String preferredReligion,
            @RequestParam(required = false) String preferredMaritalStatus,
            @RequestParam(value = "preferredProfession", required = false) List<String> preferredProfession,
            @RequestParam(required = false) String preferredLocation) {
        try {
            User user = getOrCreateUser(emailId);

            user.setPreferredAgeMin(preferredAgeMin);
            user.setPreferredAgeMax(preferredAgeMax);
            if (preferredHeightMin != null)
                user.setPreferredHeightMin(preferredHeightMin);
            if (preferredHeightMax != null)
                user.setPreferredHeightMax(preferredHeightMax);
            if (preferredReligion != null)
                user.setPreferredReligion(preferredReligion);
            if (preferredMaritalStatus != null)
                user.setPreferredMaritalStatus(preferredMaritalStatus);
            if (preferredProfession != null)
                user.setPreferredProfession(preferredProfession);
            if (preferredLocation != null)
                user.setPreferredLocation(preferredLocation);

            userService.saveUser(user);
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("message", "Partner preferences saved successfully!");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            Map<String, Object> res = new HashMap<>();
            res.put("success", false);
            res.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(res);
        }
    }

    @PostMapping("/api/save-contact")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveContact(
            @RequestParam String emailId,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String facebook,
            @RequestParam(required = false) String instagram) {
        try {
            User user = getOrCreateUser(emailId);

            if (user.getContactDetails() == null) {
                user.setContactDetails(new bd.edu.seu.biye_shaddi.model.ContactDetails());
            }
            if (mobile != null)
                user.getContactDetails().setMobile(mobile);
            if (facebook != null)
                user.getContactDetails().setFacebook(facebook);
            if (instagram != null)
                user.getContactDetails().setInstagram(instagram);
            user.getContactDetails().setEmailId(emailId);

            userService.saveUser(user);
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("message", "Contact info saved successfully!");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            Map<String, Object> res = new HashMap<>();
            res.put("success", false);
            res.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(res);
        }
    }

    /** Helper: fetch existing user by email or create a new one */
    private User getOrCreateUser(String emailId) {
        Optional<User> opt = userService.getUserByEmail(emailId.trim());
        if (opt.isPresent())
            return opt.get();
        User newUser = new User();
        newUser.setEmailId(emailId.trim());
        syncRegistrationData(newUser, emailId.trim());
        return newUser;
    }

    // ==================== Original endpoints (kept for compatibility)
    // ====================

    @PostMapping("/user-info-form")
    public String userInfoForm(@ModelAttribute User user, org.springframework.validation.BindingResult bindingResult,
            @RequestParam("profilePicture") MultipartFile file)
            throws IOException {
        System.out.println("Received user data: " + user);
        if (user.getContactDetails() != null) {
            System.out.println("Contact Details - Mobile: " + user.getContactDetails().getMobile() +
                    ", FB: " + user.getContactDetails().getFacebook() +
                    ", Insta: " + user.getContactDetails().getInstagram());
        } else {
            System.out.println("Contact Details is NULL in received object");
        }

        if (bindingResult.hasErrors()) {
            System.out.println("Binding errors: " + bindingResult.getAllErrors());
        }

        // Normalize email
        if (user.getEmailId() != null) {
            user.setEmailId(user.getEmailId().trim());
        }

        User userToSave = user;

        // Fetch existing user to ensure we don't lose data and handle merging
        if (user.getId() != null && !user.getId().isEmpty()) {
            Optional<User> existingUserOpt = userService.getUserById(user.getId());
            if (existingUserOpt.isPresent()) {
                User existingUser = existingUserOpt.get();
                System.out.println("Found existing user: " + existingUser.getId());

                // Update fields from form
                existingUser.setName(user.getName());
                existingUser.setDateOfBirth(user.getDateOfBirth());
                existingUser.setGender(user.getGender());
                existingUser.setHeight(user.getHeight());
                existingUser.setMaritalStatus(user.getMaritalStatus());
                existingUser.setReligion(user.getReligion());
                existingUser.setEducation(user.getEducation());
                existingUser.setProfession(user.getProfession());
                existingUser.setLocation(user.getLocation());
                existingUser.setMonthlyIncome(user.getMonthlyIncome());
                existingUser.setBio(user.getBio());
                existingUser.setInterests(user.getInterests());

                // Family
                existingUser.setFamilyStatus(user.getFamilyStatus());
                existingUser.setFamilyType(user.getFamilyType());
                existingUser.setFatherStatus(user.getFatherStatus());
                existingUser.setMotherStatus(user.getMotherStatus());
                existingUser.setNumberOfSiblings(user.getNumberOfSiblings());

                // Preferences
                existingUser.setPreferredAgeMin(user.getPreferredAgeMin());
                existingUser.setPreferredAgeMax(user.getPreferredAgeMax());
                existingUser.setPreferredHeightMin(user.getPreferredHeightMin());
                existingUser.setPreferredHeightMax(user.getPreferredHeightMax());
                existingUser.setPreferredReligion(user.getPreferredReligion());
                existingUser.setPreferredMaritalStatus(user.getPreferredMaritalStatus());
                existingUser.setPreferredProfession(user.getPreferredProfession());
                existingUser.setPreferredLocation(user.getPreferredLocation());

                // Contact Details - Crucial Merge
                if (user.getContactDetails() != null) {
                    if (existingUser.getContactDetails() == null) {
                        existingUser.setContactDetails(new bd.edu.seu.biye_shaddi.model.ContactDetails());
                    }
                    existingUser.getContactDetails().setMobile(user.getContactDetails().getMobile());
                    existingUser.getContactDetails().setFacebook(user.getContactDetails().getFacebook());
                    existingUser.getContactDetails().setInstagram(user.getContactDetails().getInstagram());
                    existingUser.getContactDetails().setEmailId(user.getEmailId());
                }

                userToSave = existingUser;
            }
        }

        System.out.println("Profile picture is empty: " + file.isEmpty());

        // Handle Profile Picture
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
            userToSave.setProfilePictureUrl("/" + uploadDir + fileName);
            System.out.println("Uploaded new profile picture: " + userToSave.getProfilePictureUrl());
        }
        // If file is empty, userToSave already has the old URL if it was an existing
        // user

        // Calculate Age
        if (userToSave.getDateOfBirth() != null) {
            long ageInMillis = new java.util.Date().getTime() - userToSave.getDateOfBirth().getTime();
            long ageInYears = ageInMillis / (1000L * 60 * 60 * 24 * 365);
            userToSave.setAge((int) ageInYears);

            if (userToSave.getAge() < 18) {
                System.out.println("Validation Error: Age is less than 18. Age: " + userToSave.getAge());
            }
        }

        // FIX: If ID is empty string (from hidden input), set to null so Mongo
        // generates a new ID
        if (userToSave.getId() != null && userToSave.getId().trim().isEmpty()) {
            userToSave.setId(null);
        }

        userService.saveUser(userToSave);
        System.out.println("Saved user with ID: " + userToSave.getId());
        return "redirect:/user_info?emailId=" + userToSave.getEmailId();
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

    private void syncRegistrationData(User user, String emailId) {
        // Attempt to fetch registration data to pre-fill
        Optional<bd.edu.seu.biye_shaddi.model.Registration> regOpt = registrationRepository.findByEmail(emailId);
        if (regOpt.isPresent()) {
            bd.edu.seu.biye_shaddi.model.Registration reg = regOpt.get();
            user.setName(reg.getFirstName() + " " + reg.getLastName());
            user.setGender(reg.getGender());
            user.setReligion(reg.getReligion());
            user.setEducation(reg.getEducation());
            user.setProfession(reg.getOccupation()); // Occupation -> Profession
            user.setLocation(reg.getCity());

            // Parse DOB
            if (reg.getDateOfBirth() != null && !reg.getDateOfBirth().isEmpty()) {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    user.setDateOfBirth(sdf.parse(reg.getDateOfBirth()));

                    // Calculate Age
                    long ageInMillis = new java.util.Date().getTime() - user.getDateOfBirth().getTime();
                    long ageInYears = ageInMillis / (1000L * 60 * 60 * 24 * 365);
                    user.setAge((int) ageInYears);
                } catch (Exception e) {
                    System.out.println("Error parsing DOB from registration: " + e.getMessage());
                }
            }
            System.out.println("Pre-filled user data from registration for: " + emailId);
        }
    }
}
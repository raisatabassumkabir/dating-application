package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.ContactDetails;
import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.ContactDetailsService;
import bd.edu.seu.biye_shaddi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Controller
public class ContactDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(ContactDetailsController.class);
    private final UserService userService;
    private final ContactDetailsService contactDetailsService;

    public ContactDetailsController(UserService userService, ContactDetailsService contactDetailsService) {
        this.userService = userService;
        this.contactDetailsService = contactDetailsService;
    }


    @GetMapping("/contact_details")
    public String showContactDetailsForm(Model model, @RequestParam("emailId") String emailId) {
        Optional<User> userOptional = userService.getUserByEmail(emailId);
        if (!userOptional.isPresent()) {
            model.addAttribute("error", "User not found");
            logger.error("User not found for emailId: {}", emailId);
            return "error";
        }

        Optional<ContactDetails> contactDetailsOptional = contactDetailsService.getContactDetailsByEmail(emailId);
        ContactDetails contactDetails = contactDetailsOptional.orElse(new ContactDetails());
        contactDetails.setEmailId(emailId);
        model.addAttribute("contactDetails", contactDetails);
        model.addAttribute("user", userOptional.get());
        logger.debug("Loaded contact details for emailId: {}", emailId);

        return "contact_details";
    }

    @PostMapping("/saveContactDetails")
    public String saveContactDetails(@ModelAttribute ContactDetails contactDetails, @RequestParam("emailId") String emailId, Model model) {
        logger.debug("Saving contact details for emailId: {}, ContactDetails: {}", emailId, contactDetails);
        // Clean emailId to prevent duplication
        String cleanedEmailId = emailId.split(",")[0].trim();
        Optional<User> userOptional = userService.getUserByEmail(cleanedEmailId);
        if (!userOptional.isPresent()) {
            logger.error("User not found for emailId: {}", cleanedEmailId);
            model.addAttribute("error", "User not found for email: " + cleanedEmailId);
            return "error";
        }

        contactDetails.setEmailId(cleanedEmailId);
        contactDetailsService.saveContactDetails(contactDetails);
        logger.info("Saved contact details for emailId: {}", cleanedEmailId);

        return "redirect:/contact_info?emailId=" + cleanedEmailId;
    }

    @GetMapping("/contact_info")
    public String showContactInfo(Model model, @RequestParam("emailId") String emailId) {
        Optional<User> userOptional = userService.getUserByEmail(emailId);
        if (!userOptional.isPresent()) {
            model.addAttribute("error", "User not found");
            logger.error("User not found for emailId: {}", emailId);
            return "error";
        }

        Optional<ContactDetails> contactDetailsOptional = contactDetailsService.getContactDetailsByEmail(emailId);
        ContactDetails contactDetails = contactDetailsOptional.orElse(new ContactDetails());
        model.addAttribute("contactDetails", contactDetails);
        model.addAttribute("user", userOptional.get());
        logger.info("Rendering contact_info.html for emailId: {}", emailId);

        return "contact_info";
    }
}
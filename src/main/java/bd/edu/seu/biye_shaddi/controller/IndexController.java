package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.MatchingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class IndexController {
   private final MatchingService matchingService;

    public IndexController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping("/index")
    public String indexPage(){
        return "index";
    }


    @GetMapping("/matches")
    public String getMatches(@RequestParam String emailId, Model model) {
        // Get the user's matches
        List<User> matches = matchingService.findTopMatches(emailId, 10);

        // Add matches to the model
        model.addAttribute("matches", matches);
        model.addAttribute("emailId", emailId);

        return "matches";
    }
}

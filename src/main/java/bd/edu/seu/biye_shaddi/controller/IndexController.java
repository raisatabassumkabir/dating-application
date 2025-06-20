package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.TalkRequest;
import bd.edu.seu.biye_shaddi.model.User;
import bd.edu.seu.biye_shaddi.service.MatchingService;
import bd.edu.seu.biye_shaddi.service.TalkRequestService;
import bd.edu.seu.biye_shaddi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class IndexController {
   private final MatchingService matchingService;
   private final UserService userService;
   private final TalkRequestService talkRequestService;

    public IndexController(MatchingService matchingService, UserService userService, TalkRequestService talkRequestService) {
        this.matchingService = matchingService;
        this.userService = userService;
        this.talkRequestService = talkRequestService;
    }

    @GetMapping("/index")
    public String indexPage(){
        return "index";
    }


    @GetMapping("/matches")
    public String getMatches(@RequestParam String emailId, Model model) {
        // Get the user's matches
        List<User> matches = matchingService.findTopMatchesByEmailId(emailId,20);

        // Add matches to the model
        model.addAttribute("matches", matches);
        model.addAttribute("emailId", emailId);

        return "matches";
    }


}

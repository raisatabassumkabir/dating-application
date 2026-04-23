package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.Shortlist;
import bd.edu.seu.biye_shaddi.service.ShortlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shortlist")
public class ShortlistController {
    private final ShortlistService shortlistService;

    public ShortlistController(ShortlistService shortlistService) {
        this.shortlistService = shortlistService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addShortlist(@RequestParam String userId, @RequestParam String shortlistedUserId) {
        Shortlist shortlist = shortlistService.addShortlist(userId, shortlistedUserId);
        return ResponseEntity.ok(shortlist);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeShortlist(@RequestParam String userId, @RequestParam String shortlistedUserId) {
        shortlistService.removeShortlist(userId, shortlistedUserId);
        return ResponseEntity.ok("Removed");
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isShortlisted(@RequestParam String userId, @RequestParam String shortlistedUserId) {
        return ResponseEntity.ok(shortlistService.isShortlisted(userId, shortlistedUserId));
    }
}

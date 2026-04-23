package bd.edu.seu.biye_shaddi.controller;

import bd.edu.seu.biye_shaddi.model.Block;
import bd.edu.seu.biye_shaddi.service.BlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/block")
public class BlockController {
    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> blockUser(@RequestParam String userId, @RequestParam String blockedUserId,
            @RequestParam(required = false) String reason) {
        Block block = blockService.blockUser(userId, blockedUserId, reason);
        return ResponseEntity.ok(block);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isBlocked(@RequestParam String userId, @RequestParam String blockedUserId) {
        return ResponseEntity.ok(blockService.isBlocked(userId, blockedUserId));
    }
}

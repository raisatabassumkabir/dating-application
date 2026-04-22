package bd.edu.seu.biye_shaddi.service;

import bd.edu.seu.biye_shaddi.model.Block;
import bd.edu.seu.biye_shaddi.repository.BlockRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlockService {
    private final BlockRepository blockRepository;

    public BlockService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    public Block blockUser(String userId, String blockedUserId, String reason) {
        if (isBlocked(userId, blockedUserId)) {
            return null;
        }
        Block block = new Block(userId, blockedUserId, reason);
        return blockRepository.save(block);
    }

    public boolean isBlocked(String userId, String blockedUserId) {
        return blockRepository.findByUserIdAndBlockedUserId(userId, blockedUserId).isPresent();
    }

    public List<String> getBlockedUserIds(String userId) {
        return blockRepository.findByUserId(userId).stream()
                .map(Block::getBlockedUserId)
                .collect(Collectors.toList());
    }
}

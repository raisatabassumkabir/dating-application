package bd.edu.seu.biye_shaddi.service;

import bd.edu.seu.biye_shaddi.model.Shortlist;
import bd.edu.seu.biye_shaddi.repository.ShortlistRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ShortlistService {
    private final ShortlistRepository shortlistRepository;

    public ShortlistService(ShortlistRepository shortlistRepository) {
        this.shortlistRepository = shortlistRepository;
    }

    public Shortlist addShortlist(String userId, String shortlistedUserId) {
        if (isShortlisted(userId, shortlistedUserId)) {
            return null; // Already exists
        }
        Shortlist shortlist = new Shortlist(userId, shortlistedUserId);
        return shortlistRepository.save(shortlist);
    }

    public void removeShortlist(String userId, String shortlistedUserId) {
        shortlistRepository.deleteByUserIdAndShortlistedUserId(userId, shortlistedUserId);
    }

    public boolean isShortlisted(String userId, String shortlistedUserId) {
        return shortlistRepository.findByUserIdAndShortlistedUserId(userId, shortlistedUserId).isPresent();
    }

    public List<Shortlist> getUserShortlist(String userId) {
        return shortlistRepository.findByUserId(userId);
    }
}

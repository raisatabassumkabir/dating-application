package bd.edu.seu.biye_shaddi.service;

import bd.edu.seu.biye_shaddi.model.ContactDetails;
import bd.edu.seu.biye_shaddi.repository.ContactDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactDetailsService {


    private final ContactDetailsRepository contactDetailsRepository;

    public ContactDetailsService(ContactDetailsRepository contactDetailsRepository) {
        this.contactDetailsRepository = contactDetailsRepository;
    }

    public ContactDetails saveContactDetails(ContactDetails contactDetails) {
        return contactDetailsRepository.save(contactDetails);
    }

    public Optional<ContactDetails> getContactDetailsByEmail(String emailId) {
        return contactDetailsRepository.findByEmailId(emailId);
    }

}

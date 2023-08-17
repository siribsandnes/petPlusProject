package no.ntnu.crudrest.service;

import no.ntnu.crudrest.models.User;
import no.ntnu.crudrest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NewsletterService {

    @Autowired
    UserRepository userRepository;

    public String registerForNewsletter(String mail) {
        String responseText = "Du må registrere deg først";
        Optional<User> userOpt = userRepository.findByUsername(mail);
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isNewsletter()) {
                responseText = "Du har fra før";
            } else {
                // Send mail
                responseText = "Velkommen";
                user.setNewsletter(true);
                userRepository.save(user);
            }
        }
        return responseText;
    }
}

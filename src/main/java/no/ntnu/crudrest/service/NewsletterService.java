package no.ntnu.crudrest.service;

import no.ntnu.crudrest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsletterService {

    @Autowired
    UserRepository userRepository;

    public String registerForNewsletter(String mail) {
        return "Tullebukk";
    }
}

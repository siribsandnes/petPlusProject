package no.ntnu.crudrest.service;

import no.ntnu.crudrest.models.User;
import no.ntnu.crudrest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NewsletterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public String registerForNewsletter(String mail) {
        String responseText = "You have to have a user to sign up for the newsletter";
        Optional<User> userOpt = userRepository.findByUsername(mail);
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isNewsletter()) {
                responseText = "You are already signed up for our newsletter";
            } else {
                sendMail(user);
                responseText = "Welcome! Check your inbox :)";
                user.setNewsletter(true);
                userRepository.save(user);
            }
        }
        return responseText;
    }

    private void sendMail(User user){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("petPlusStoreMailer@gmail.com");
        message.setTo(user.getUsername());
        message.setSubject("Welcome");
        message.setText("Welcome to our newsletter!");
        mailSender.send(message);
    }
}

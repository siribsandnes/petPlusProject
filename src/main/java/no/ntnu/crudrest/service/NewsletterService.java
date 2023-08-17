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
        String responseText = "Du må registrere deg først";
        Optional<User> userOpt = userRepository.findByUsername(mail);
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isNewsletter()) {
                responseText = "Du har fra før";
            } else {
                sendMail(user);
                responseText = "Velkommen";
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
        message.setText("Welcome to our newsletter");
        mailSender.send(message);
    }
}

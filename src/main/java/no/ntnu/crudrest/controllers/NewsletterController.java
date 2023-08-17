package no.ntnu.crudrest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import no.ntnu.crudrest.service.NewsletterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

    @PutMapping("/newsletter/{mail}")
    public String registerForNewsletter(@PathVariable String mail) {
        return newsletterService.registerForNewsletter(mail);
    }

}

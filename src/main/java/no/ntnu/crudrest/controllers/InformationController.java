package no.ntnu.crudrest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class InformationController {

    @GetMapping("faq")
    public String faqPage(Model model) {
        return "faq";
    }

    @GetMapping("about")
    public String aboutUsPage() {
        return "about";
    }

    @GetMapping("/values")
    public String ourValuesPage() {
        return "about";
    }
    @GetMapping("/customerservice")
    public String costumerservicePage() {
        return "customer-service";
    }
}

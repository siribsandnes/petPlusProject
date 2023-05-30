package no.ntnu.crudrest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
@Tag(name = "InformationController", description = "Handles information pages")
public class InformationController {

    @GetMapping("faq")
    @Operation(
            summary = "Show FAQ order page",
            description = "Displays the Frequently Asked Questions (FAQ) page for orders"
    )
    public String faqPage(Model model) {
        return "faq";
    }

    @GetMapping("faq/shipping")
    @Operation(
            summary = "Show FAQ shipping page",
            description = "Displays the Frequently Asked Questions (FAQ) page for shipping"
    )
    public String faqShippingPage(Model model) {
        return "faq-shipping";
    }

    @GetMapping("faq/return")
    @Operation(
            summary = "Show FAQ return page",
            description = "Displays the Frequently Asked Questions (FAQ) page for refunds"
    )
    public String faqReturnPage(Model model) {
        return "faq-refund";
    }


    @GetMapping("about")
    @Operation(
            summary = "Show About Us page",
            description = "Displays the About Us page"
    )
    public String aboutUsPage() {
        return "about";
    }

    @GetMapping("/values")
    @Operation(
            summary = "Show Our Values page",
            description = "Displays the Our Values page"
    )
    public String ourValuesPage() {
        return "about";
    }
    @GetMapping("/customerservice")
    @Operation(
            summary = "Show Customer Service page",
            description = "Displays the Contact Us page"
    )
    public String costumerservicePage() {
        return "customer-service";
    }
}

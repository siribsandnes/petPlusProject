package no.ntnu.crudrest.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

/**
 * Creates AuthenticationManager - set up authentication type
 * The @EnableWebSecurity tells that this ia a class for configuring web security
 */
@Configuration
public class SecurityConfiguration {
    /**
     * A service providing our users from the database
     */
    @Autowired
    UserDetailsService userDetailsService;

    /**
     * This method will be called automatically by the framework to find out what authentication to use.
     * Here we tell that we want to load users from a database
     *
     * @param auth Authentication builder
     * @throws Exception
     */
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }



    /**
     * This method will be called automatically by the framework to find out what authentication to use.
     *
     * @param http HttpSecurity setting builder
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain configureAuthorizationFilterChain(HttpSecurity http) throws Exception {

        // Set up the authorization requests, starting from most restrictive at the top, to least restrictive on bottom
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/users/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/").permitAll()
                .requestMatchers("/shoppingCart/**").permitAll()
                .requestMatchers("/shoppingCart/removeProduct/**").permitAll()
                .requestMatchers("/shoppingCart/addProduct/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/products/delete/**").hasRole("ADMIN")
                .requestMatchers("/products").permitAll()
                .requestMatchers("/products/**").permitAll()
                .requestMatchers("/api/products").permitAll()
                .requestMatchers("/api-docs").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/signup").permitAll()
                .requestMatchers("/forgotPassword").permitAll()
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/favicon.ico").permitAll()
                .requestMatchers("/static/favicon.ico").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/img/**").permitAll()
                .requestMatchers("/styles/**").permitAll()
                .requestMatchers("/payment/**").permitAll()
                .requestMatchers("/paymentSuccess/**").permitAll()
                .requestMatchers("/about").permitAll()
                .requestMatchers("/faq/**").permitAll()
                .requestMatchers("/customerservice").permitAll()
                .requestMatchers("/values").permitAll()
                .requestMatchers("/newsletter/**").permitAll()
                // /error is needed or else getting weird redirect on login, found solution here: https://github.com/spring-projects/spring-security/issues/12635#issuecomment-1429055478
                .requestMatchers("/error").permitAll()
                .and().formLogin().loginPage("/login")
                .and().logout().logoutSuccessUrl("/")
        ;
        return http.build();
    }

    /**
     * This method is called to decide what encryption to use for password checking
     *
     * @return The password encryptor
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
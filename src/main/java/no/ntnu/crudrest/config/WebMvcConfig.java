package no.ntnu.crudrest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ShoppingCartInterceptor shoppingCartInterceptor;
    private final UserSessionInterceptor userSessionInterceptor;

    public WebMvcConfig(ShoppingCartInterceptor shoppingCartInterceptor, UserSessionInterceptor userSessionInterceptor) {
        this.shoppingCartInterceptor = shoppingCartInterceptor;
        this.userSessionInterceptor = userSessionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(shoppingCartInterceptor);
        registry.addInterceptor(userSessionInterceptor);
    }
}


package no.ntnu.crudrest.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.ntnu.crudrest.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ShoppingCartInterceptor implements HandlerInterceptor {
    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * If  modelAndView is not null, add the amount of products in the shopping cart to the model.
     * Makes the amount of products in the cart available on every page so
     * the nav bar can always show how many products the user have in their cart across all pages.
     */

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        if (modelAndView != null) {
            modelAndView.addObject("cartProducts", shoppingCartService.getAmountInCart());
        }
    }
}

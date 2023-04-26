package no.ntnu.crudrest.util;

import no.ntnu.crudrest.models.*;
import no.ntnu.crudrest.repositories.CategoryRepository;
import no.ntnu.crudrest.repositories.ProductRepository;
import no.ntnu.crudrest.repositories.RoleRepository;
import no.ntnu.crudrest.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Component
public class DummyDataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DummyDataInitializer.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        makeUsers();
        makeCategories();
        makeProducts();

    }

    private void makeUsers(){
        Optional<User> rootUserExist = userRepository.findByUsername("root@root.com");
        if(rootUserExist.isEmpty()){
            logger.info("Importing user data");
            Address rootAddress = new Address("Root Street Address", "0000", "Root City");
            User root = new User("root@root.com", "$2a$10$oC/uXSY8Y28EjkjeZBGc7.CZILWH3CMSX8dWNIr.CMpTDG.T9ANrC", "Root first name", "Root last name","12345678", rootAddress);

            Role user = new Role("ROLE_USER");
            Role admin = new Role("ROLE_ADMIN");
            roleRepository.save(user);
            roleRepository.save(admin);

            root.addRole(user);
            root.addRole(admin);

            userRepository.save(root);
            logger.info("User data finished importing");
        }
        else {
            logger.info("User data already in database, no need to import");
        }
    }
    private void makeCategories(){
        if(categoryRepository.count() == 0){
            logger.info("Importing Product Categories");
            categoryRepository.save(new ProductCategory("Dog"));
            categoryRepository.save(new ProductCategory("Dog Clothing"));
            categoryRepository.save(new ProductCategory("Dog Treats"));
            categoryRepository.save(new ProductCategory("Dog Food"));
            categoryRepository.save(new ProductCategory("Dog Accessories"));

            categoryRepository.save(new ProductCategory("Cat"));
            categoryRepository.save(new ProductCategory("Cat Clothing"));
            categoryRepository.save(new ProductCategory("Cat Treats"));
            categoryRepository.save(new ProductCategory("Cat Food"));
            categoryRepository.save(new ProductCategory("Cat  Accessories"));
            logger.info("Product Categories finished importing");
        }
        else {
            logger.info("Product Categories already in database, no need to import");
        }
    }
    private void makeProducts(){
        if(productRepository.count() == 0){
            logger.info("Importing Products");

            //Make Products
            productRepository.save(new Product(1,"Dog Winter Wear Set",999,50));
            productRepository.save(new Product(2,"Dog Winter Boots",349,40));
            productRepository.save(new Product(3,"Eco-Friendly Dog Leash",299,30));
            productRepository.save(new Product(4,"Sweater for cats",79,20));

            //Find Categories
            ProductCategory dog = categoryRepository.findByName("Dog");
            ProductCategory dogClothing = categoryRepository.findByName("Dog Clothing");
            ProductCategory dogTreats = categoryRepository.findByName("Dog Treats");
            ProductCategory dogFood = categoryRepository.findByName("Dog Food");
            ProductCategory dogAccessories = categoryRepository.findByName("Dog Accessories");

            ProductCategory cat = categoryRepository.findByName("Cat");
            ProductCategory catClothing = categoryRepository.findByName("Cat Clothing");
            ProductCategory catTreats = categoryRepository.findByName("Cat Treats");
            ProductCategory catFood = categoryRepository.findByName("Cat Food");
            ProductCategory catAccessories = categoryRepository.findByName("Cat Accessories");

            //Apply Categories
            dog.addProduct(productRepository.findProductByProductName("Dog Winter Wear Set"));
            dogClothing.addProduct(productRepository.findProductByProductName("Dog Winter Wear Set"));

            dog.addProduct(productRepository.findProductByProductName("Dog Winter Boots"));
            dogClothing.addProduct(productRepository.findProductByProductName("Dog Winter Boots"));
            dogAccessories.addProduct(productRepository.findProductByProductName("Dog Winter Boots"));

            dog.addProduct(productRepository.findProductByProductName("Eco-Friendly Dog Leash"));
            dogAccessories.addProduct(productRepository.findProductByProductName("Eco-Friendly Dog Leash"));

            cat.addProduct(productRepository.findProductByProductName("Sweater for cats"));
            catClothing.addProduct(productRepository.findProductByProductName("Sweater for cats"));
            logger.info("Products finished importing");
        }
        else {
            logger.info("Products already in database, no need to import");
        }
    }
}

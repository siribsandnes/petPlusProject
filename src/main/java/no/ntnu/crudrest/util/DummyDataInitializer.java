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

import java.util.ArrayList;
import java.util.List;
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
        makeCategoriesAndProducts();
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
    private void makeCategoriesAndProducts(){
        if(categoryRepository.count() == 0){
            logger.info("Importing Product Categories");
            ProductCategory dog = new ProductCategory("Dog");
            ProductCategory dogClothing = new ProductCategory("Dog Clothing");
            ProductCategory dogTreats = new ProductCategory("Dog Treats");
            ProductCategory dogFood = new ProductCategory("Dog Food");
            ProductCategory dogAccessories = new ProductCategory("Dog Accessories");

            ProductCategory cat = new ProductCategory("Cat");
            ProductCategory catClothing = new ProductCategory("Cat Clothing");
            ProductCategory catTreats = new ProductCategory("Cat Treats");
            ProductCategory catFood = new ProductCategory("Cat Food");
            ProductCategory catAccessories = new ProductCategory("Cat  Accessories");
            logger.info("Product Categories finished importing");

            //Make Products
            Product dogWinterWearSet = new Product(1,"Dog Winter Wear Set",999,50);
            Product dogWinterBoots = new Product(2,"Dog Winter Boots",349,40);
            Product ecoFriendlyDogLeash = new Product(3,"Eco-Friendly Dog Leash",299,30);
            Product sweaterForCats = new Product(4,"Sweater for cats",79,20);

            List<Product> productList = new ArrayList<>();
            productList.add(dogWinterWearSet);
            productList.add(dogWinterBoots);
            productList.add(ecoFriendlyDogLeash);
            productList.add(sweaterForCats);

            List<ProductCategory> categoryList = new ArrayList<>();
            categoryList.add(dog);
            categoryList.add(dogClothing);
            categoryList.add(dogAccessories);
            categoryList.add(dogTreats);
            categoryList.add(dogFood);
            categoryList.add(cat);
            categoryList.add(catClothing);
            categoryList.add(catAccessories);
            categoryList.add(catTreats);
            categoryList.add(catFood);

            //Apply Product to Category
            dog.addProduct(dogWinterWearSet);
            dogClothing.addProduct(dogWinterWearSet);

            dog.addProduct(dogWinterBoots);
            dogClothing.addProduct(dogWinterBoots);
            dogAccessories.addProduct(dogWinterBoots);

            dog.addProduct(ecoFriendlyDogLeash);
            dogAccessories.addProduct(ecoFriendlyDogLeash);

            cat.addProduct(sweaterForCats);
            catClothing.addProduct(sweaterForCats);

            //Apply Category to Product
            dogWinterWearSet.addProductCategory(dog);
            dogWinterWearSet.addProductCategory(dogClothing);

            dogWinterBoots.addProductCategory(dog);
            dogWinterBoots.addProductCategory(dogClothing);
            dogWinterBoots.addProductCategory(dogAccessories);

            ecoFriendlyDogLeash.addProductCategory(dog);
            ecoFriendlyDogLeash.addProductCategory(dogAccessories);

            sweaterForCats.addProductCategory(cat);
            sweaterForCats.addProductCategory(catClothing);

            productRepository.saveAll(productList);
            categoryRepository.saveAll(categoryList);

            logger.info("Products and categories finished importing");
        }
        else {
            logger.info("Products and categories already in database, no need to import");
        }
    }
}

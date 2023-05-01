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
            Address rootAddress = new Address("Root Street Address", "0000", "Root City", "Root firstname", "Root last name", "12345678");
            User root = new User("root@root.com", "$2a$10$oC/uXSY8Y28EjkjeZBGc7.CZILWH3CMSX8dWNIr.CMpTDG.T9ANrC",  rootAddress);

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

            ProductCategory bird = new ProductCategory("Bird");
            ProductCategory indoors = new ProductCategory("Indoors");
            ProductCategory winter = new ProductCategory("Winter");


            //Make Products
            Product dogWinterWearSet = new Product(1,"Dog Winter Wear Set","Our Winter Wear Set for Dogs is the perfect addition to your pup's wardrobe. Made \n" +
                    "with soft and warm materials, this set will keep your pup comfortable and cozy all winter long. \n" +
                    "The set includes a black beret and a black turtleneck. The beret has a velcro tie-back closure and \n" +
                    "the turtleneck features a ribbed, stretchy knit material. Both pieces are machine-washable, so \n" +
                    "you can keep them looking fresh all season. Our Winter Wear Set for Dogs is the perfect way to \n" +
                    "keep your pup warm and stylish during the colder months. Get your pup the ultimate winter \n" +
                    "look with our Winter Wear Set today",1000,50);

            Product dogWinterBoots = new Product(2,"Dog Winter Boots","Our Leather Winter Boots are perfect for keeping your pup's paws warm and \n" +
                    "protected during the colder months. The boots are made of soft and durable leather and come \n" +
                    "in a classic brown color. They feature a buckle closure for a secure fit and the soles are non-slip \n" +
                    "for extra traction on slippery surfaces. Our Leather Winter Boots are best for poodles, but could \n" +
                    "also fit other types of dogs. The boots are lined with cozy and comfy fleece, so your pup will stay \n" +
                    "warm and stylish all winter long. Get your pup the perfect winter look with our Leather Winter \n" +
                    "Boots today!",800,40);

            Product ecoFriendlyDogLeash = new Product(3,"Eco-Friendly Dog Leash"," This eco-friendly dog leash is the perfect accessory for your pup! Made from fabric, \n" +
                    "this leash comes with calm ornaments for a stylish and subtle look. Available in three sizes, you \n" +
                    "can find the perfect fit for your pup - from the smallest dogs like Chihuahuas to very large dogs \n" +
                    "such as Great Danes. With this eco-friendly leash, you can show your pup some love while also \n" +
                    "helping the environment.",229,30);

            Product sweaterForCats = new Product(4,"Sweater for cats","This white ceramic bowl is perfect for cats! It has a low profile, making it easy for \n" +
                    "your cat to access the food inside. The bowl is also quite large, allowing you to put in larger \n" +
                    "amounts of food for your pet. With its white ceramic material, it is easy to clean and maintain \n" +
                    "and will look great in any pet-friendly home.",79,20);

            Product birdCage = new Product(5,"Bird Cage","This beautiful small cage for birds is the perfect accessory for any bird lover. It is \n" +
                    "formed in the shape of a sphere, made from gold-coated metal that is sure to stand out in any \n" +
                    "home. This cage is especially well-suited to small, white birds as its design is perfect for their size \n" +
                    "and coloring. It will make a great addition to any bird-lover's home.\n", 2800, 10);

            Product catHoodie = new Product(6, "Hoodie for cats", "This cozy hoodie for cats is the perfect way to keep your furry friend warm and \n" +
                    "stylish. Available in three trendy colors – red, green, and blue – this hoodie features a white \n" +
                    "hood that can be removed if desired. Whether you choose to keep the hood or not, this hoodie \n" +
                    "is sure to make your cat look great and be comfortable.",420, 15);




            List<Product> productList = new ArrayList<>();
            productList.add(dogWinterWearSet);
            productList.add(dogWinterBoots);
            productList.add(ecoFriendlyDogLeash);
            productList.add(sweaterForCats);
            productList.add(birdCage);
            productList.add(catHoodie);

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
            categoryList.add(bird);
            categoryList.add(winter);
            categoryList.add(indoors);

            //Apply Product to Category
            dog.addProduct(dogWinterWearSet);
            dogClothing.addProduct(dogWinterWearSet);
            winter.addProduct(dogWinterWearSet);

            dog.addProduct(dogWinterBoots);
            dogClothing.addProduct(dogWinterBoots);
            dogAccessories.addProduct(dogWinterBoots);
            winter.addProduct(dogWinterBoots);

            dog.addProduct(ecoFriendlyDogLeash);
            dogAccessories.addProduct(ecoFriendlyDogLeash);

            cat.addProduct(sweaterForCats);
            catClothing.addProduct(sweaterForCats);
            indoors.addProduct(sweaterForCats);

            bird.addProduct(birdCage);
            indoors.addProduct(birdCage);

            cat.addProduct(catHoodie);
            catClothing.addProduct(catHoodie);

            //Apply Category to Product
            dogWinterWearSet.addProductCategory(dog);
            dogWinterWearSet.addProductCategory(dogClothing);
            dogWinterWearSet.addProductCategory(winter);

            dogWinterBoots.addProductCategory(dog);
            dogWinterBoots.addProductCategory(dogClothing);
            dogWinterBoots.addProductCategory(dogAccessories);
            dogWinterBoots.addProductCategory(winter);

            ecoFriendlyDogLeash.addProductCategory(dog);
            ecoFriendlyDogLeash.addProductCategory(dogAccessories);

            sweaterForCats.addProductCategory(cat);
            sweaterForCats.addProductCategory(catClothing);
            sweaterForCats.addProductCategory(indoors);

            birdCage.addProductCategory(bird);
            birdCage.addProductCategory(indoors);

            catHoodie.addProductCategory(cat);
            catHoodie.addProductCategory(catClothing);

            productRepository.saveAll(productList);
            categoryRepository.saveAll(categoryList);

            logger.info("Products and categories finished importing");
        }
        else {
            logger.info("Products and categories already in database, no need to import");
        }
    }
}

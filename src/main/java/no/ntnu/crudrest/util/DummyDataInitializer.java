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
import org.springframework.security.crypto.bcrypt.BCrypt;
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

    private String createHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private void makeUsers(){
        Optional<User> rootUserExist = userRepository.findByUsername("root@root.com");
        if(rootUserExist.isEmpty()){
            String pass = System.getenv("PET_PLUS_DB_PASS");
            logger.info("Importing user data");
            Address rootAddress = new Address("Root Street Address", "0000", "Root City", "Root firstname", "Root last name", "12345678");
            User root = new User("root@root.com", createHash(pass),  rootAddress);

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
            ProductCategory dogBeds = new ProductCategory("Dog Beds");

            ProductCategory cat = new ProductCategory("Cat");
            ProductCategory catClothing = new ProductCategory("Cat Clothing");
            ProductCategory catTreats = new ProductCategory("Cat Treats");
            ProductCategory catFood = new ProductCategory("Cat Food");
            ProductCategory catAccessories = new ProductCategory("Cat  Accessories");
            ProductCategory catToys = new ProductCategory("Cat Toys");

            ProductCategory bird = new ProductCategory("Bird");
            ProductCategory indoors = new ProductCategory("Indoors");
            ProductCategory winter = new ProductCategory("Winter");
            ProductCategory small = new ProductCategory("Small Pets");
            ProductCategory hamster = new ProductCategory("Hamster");



            //Make Products
            Product dogWinterWearSet = new Product(1,"Dog Winter Wear Set","Our Winter Wear Set for Dogs is the perfect addition to your pup's wardrobe. Made \n" +
                    "with soft and warm materials, this set will keep your pup comfortable and cozy all winter long. \n" +
                    "The set includes a black beret and a black turtleneck. The beret has a velcro tie-back closure and \n" +
                    "the turtleneck features a ribbed, stretchy knit material. Both pieces are machine-washable, so \n" +
                    "you can keep them looking fresh all season. Our Winter Wear Set for Dogs is the perfect way to \n" +
                    "keep your pup warm and stylish during the colder months. Get your pup the ultimate winter \n" +
                    "look with our Winter Wear Set today",1000,50,"../img/products/1.png", "Dog wearing a black winter suit and black winter hat");

            Product dogWinterBoots = new Product(2,"Dog Winter Boots","Our Leather Winter Boots are perfect for keeping your pup's paws warm and \n" +
                    "protected during the colder months. The boots are made of soft and durable leather and come \n" +
                    "in a classic brown color. They feature a buckle closure for a secure fit and the soles are non-slip \n" +
                    "for extra traction on slippery surfaces. Our Leather Winter Boots are best for poodles, but could \n" +
                    "also fit other types of dogs. The boots are lined with cozy and comfy fleece, so your pup will stay \n" +
                    "warm and stylish all winter long. Get your pup the perfect winter look with our Leather Winter \n" +
                    "Boots today!",800,40,"../img/products/2.png", "White dog wearing leather winter boots");

            Product ecoFriendlyDogLeash = new Product(3,"Eco-Friendly Dog Leash"," This eco-friendly dog leash is the perfect accessory for your pup! Made from fabric, \n" +
                    "this leash comes with calm ornaments for a stylish and subtle look. Available in three sizes, you \n" +
                    "can find the perfect fit for your pup - from the smallest dogs like Chihuahuas to very large dogs \n" +
                    "such as Great Danes. With this eco-friendly leash, you can show your pup some love while also \n" +
                    "helping the environment.",229,30,"../img/products/3.png", "Dog with eco friendly dog leash");

            Product sweaterForCats = new Product(4,"Sweater for cats","This white ceramic bowl is perfect for cats! It has a low profile, making it easy for \n" +
                    "your cat to access the food inside. The bowl is also quite large, allowing you to put in larger \n" +
                    "amounts of food for your pet. With its white ceramic material, it is easy to clean and maintain \n" +
                    "and will look great in any pet-friendly home.",79,20,"../img/products/4.png", "Cat wearing sweater");

            Product birdCage = new Product(5,"Bird Cage","This beautiful small cage for birds is the perfect accessory for any bird lover. It is \n" +
                    "formed in the shape of a sphere, made from gold-coated metal that is sure to stand out in any \n" +
                    "home. This cage is especially well-suited to small, white birds as its design is perfect for their size \n" +
                    "and coloring. It will make a great addition to any bird-lover's home.\n", 2800, 10,"../img/products/5.png","Bird in rounded shaped golden birdcage");

            Product catHoodie = new Product(6, "Hoodie for cats", "This cozy hoodie for cats is the perfect way to keep your furry friend warm and \n" +
                    "stylish. Available in three trendy colors – red, green, and blue – this hoodie features a white \n" +
                    "hood that can be removed if desired. Whether you choose to keep the hood or not, this hoodie \n" +
                    "is sure to make your cat look great and be comfortable.",420, 15,"../img/products/6.png","Cat wearing red hoodie with white hood");

            Product dogBed1 = new Product(7,"Orange Dog Bed", "Nice and comfy orange dog bed that is perfect for small to medium sized dogs.", 349, 50,"../img/products/7.png","Dog in orange dog bed, medium sized");

            Product dogBed2 = new Product(8,"Gray Dog Bed", "Nice and comfy gray dog bed that is perfect for small to medium sized dogs.", 479, 30,"../img/products/8.png", "Dog in gray dog bed, medium sized");


            Product dogTreat1 = new Product(9,"Colorful dog food mix", "A mixture of colorful treats for your furry friend, no matter how big or small, young or old any dog will love this tasty treat.", 99, 100,"../img/products/9.png", "Colorful dog food mix");

            Product dogTreat2 = new Product(10,"Bone Shaped Treats", "We all know dogs and bones goes hand in hand so here are the perfect treat for your dog!", 129, 100,"../img/products/10.png", "Colorful bone shaped dog treats");

            Product bagDogFood = new Product(11, "Grain-Free Dog Foood", "Our Grain-Free Dog Foood is specially formulated to provide optimal nutrition for your beloved pup. Made with high-quality ingredients, this dog food is free from grains, making it suitable for dogs with food sensitivities or allergies. It contains a balanced blend of protein, vegetables, and essential vitamins and minerals to support your dog's overall health and well-being. Give your dog the nourishment they deserve with our Grain-Free Dog Foood.", 350, 100, "../img/products/bagdogfood300x250.png", "bag of dog foood");


            Product bell = new Product(12, "Jingling Bell Toy for Cats",
                    "Entertain your curious feline friend with our Jingling Bell Toy for Cats. This toy is a colorful bell which creates an enticing sound that will capture your cat's attention. The lightweight design allows for easy batting and chasing, providing both mental and physical stimulation. Watch as your cat pounces and swats at the bell, enjoying hours of playful fun.",
                    35, 35, "../img/products/bell300x250.png", "colorful bell toy");

            Product birdFood = new Product(13, "Premium Bird Food Blend",
                    "Treat your feathered friends to our Premium Bird Food Blend. This high-quality blend is specially formulated to meet the nutritional needs of a variety of bird species. It contains a mix of seeds, grains, and dried fruits that provide essential vitamins, minerals, and protein. Our bird food blend attracts a wide range of birds to your yard, offering them a delicious and nourishing meal. Sit back and enjoy the colorful array of birds that will flock to your feeder with our Premium Bird Food Blend.",
                    80, 45, "../img/products/birdfood300x250.png", "bag of bird food");

            Product blueCatToy = new Product(14, "Blue Bouncy Ball Cat Toy",
                    "Keep your feline friend entertained with our Blue Bouncy Ball Cat Toy. This vibrant and lightweight ball is designed to capture your cat's attention and stimulate their hunting instincts. Made from durable materials, it is perfect for batting, chasing, and pouncing. The ball's unpredictable bouncing pattern adds an extra element of excitement to playtime. Watch as your cat enjoys hours of engaging fun with our Blue Bouncy Ball Cat Toy.", 80, 45, "../img/products/bluecattoy300x250.png", "blue cat toy");

            Product boneshapedDogFood = new Product(15, "Bone-Shaped Dog Food",
                    "Treat your canine companion to our Bone-Shaped Dog Food Treats. These delicious and nutritious treats are specially shaped like bones to delight your dog. Made from high-quality ingredients, they provide a tasty and satisfying snack for your furry friend. These treats are perfect for training, rewarding good behavior, or simply as a special treat during playtime. Give your dog the joy of chewing on our Bone-Shaped Dog Food Treats.", 150, 50, "../img/products/bonedogfood300x250.png", "boneshaped dog food");

            Product pinkCatToy = new Product(16, "Pink Bouncy Ball Cat Toy",
                    "Keep your feline friend entertained with our Pink Bouncy Ball Cat Toy. This vibrant and lightweight ball is designed to capture your cat's attention and stimulate their hunting instincts. Made from durable materials, it is perfect for batting, chasing, and pouncing. The ball's unpredictable bouncing pattern adds an extra element of excitement to playtime. Watch as your cat enjoys hours of engaging fun with our Pink Bouncy Ball Cat Toy.", 80, 40, "../img/products/catball300x250.png", "pink cat toy");

            Product hawaiianCatShirt = new Product(17, "Hawaiian Cat Shirt",
                    "Dress your feline friend in style with our Hawaiian Cat Shirt. This adorable shirt features a vibrant Hawaiian print, complete with colorful flowers and palm trees. Made from soft and breathable fabric, it ensures your cat's comfort throughout the day. The shirt is designed with a Velcro closure for easy dressing and removal. Whether it's for a special occasion or simply to add some flair to your cat's wardrobe, our Hawaiian Cat Shirt is the purrfect choice. Let your cat rock the tropical vibes with this fashionable shirt.", 50, 50, "../img/products/catshirt300x250.png", "Hawaiian Cat Shirt");

            Product multiArmCatToy = new Product(18, "Multi-Armed Cat Toy with Face",
                    "Engage your curious cat with our Multi-Armed Cat Toy with Face. This unique toy features multiple arms with enticing textures and dangling attachments that will captivate your feline friend's attention. The toy is designed with a playful face, adding an extra element of charm. Watch as your cat swats, pounces, and interacts with the toy's arms, satisfying their natural hunting instincts. Provide endless entertainment for your beloved pet with our Multi-Armed Cat Toy with Face.", 45, 60, "../img/products/cattoy300x250.png", "Multi-Armed Cat Toy with Face");

            Product blackDogBed = new Product(19, "Black Dog Bed",
                    "Give your furry friend the perfect place to rest and relax with our Black Dog Bed. This stylish and cozy bed is designed with your dog's comfort in mind. The plush cushioning provides a soft and supportive surface for your dog to curl up on. The black color adds a touch of elegance to any room decor. The bed's durable construction ensures long-lasting use, and the removable cover makes it easy to clean. Treat your dog to the ultimate comfort with our Black Dog Bed.", 450, 100, "../img/products/dogbedblack300x250.png", "Black dog bed");

            Product blueDogBed = new Product(20, "Blue Dog Bed",
                    "Give your furry friend the perfect place to rest and relax with our Blue Dog Bed. This stylish and cozy bed is designed with your dog's comfort in mind. The plush cushioning provides a soft and supportive surface for your dog to curl up on. The blue color adds a calming touch to any room decor. The bed's durable construction ensures long-lasting use, and the removable cover makes it easy to clean. Treat your dog to the ultimate comfort with our Blue Dog Bed.", 425, 90, "../img/products/dogbedblue300x250.png", "Blue dog bed");

            Product greenDogBed = new Product(21, "Green Dog Bed",
                    "Give your furry friend the perfect place to rest and relax with our Green Dog Bed. This stylish and cozy bed is designed with your dog's comfort in mind. The plush cushioning provides a soft and supportive surface for your dog to curl up on. The green color adds a fresh and vibrant touch to any room decor. The bed's durable construction ensures long-lasting use, and the removable cover makes it easy to clean. Treat your dog to the ultimate comfort with our Green Dog Bed.", 425, 90, "../img/products/dogbedgreen300x250.png", "Green dog bed");

            Product pinkDogBed = new Product(22, "Pink Dog Bed",
                    "Give your furry friend the perfect place to rest and relax with our Pink Dog Bed. This stylish and cozy bed is designed with your dog's comfort in mind. The plush cushioning provides a soft and supportive surface for your dog to curl up on. The pink color adds a playful and charming touch to any room decor. The bed's durable construction ensures long-lasting use, and the removable cover makes it easy to clean. Treat your dog to the ultimate comfort with our Pink Dog Bed.", 425, 90, "../img/products/dogbedpink300x250.png", "Pink dog bed");

            Product rainbowDogBed = new Product(23, "Rainbow Dog Bed",
                    "Give your furry friend the perfect place to rest and relax with our Rainbow Dog Bed. This vibrant and cheerful bed is designed with your dog's comfort in mind. The plush cushioning provides a soft and supportive surface for your dog to curl up on. The rainbow colors add a playful and eye-catching touch to any room decor. The bed's durable construction ensures long-lasting use, and the removable cover makes it easy to clean. Treat your dog to the ultimate comfort with our Rainbow Dog Bed.", 500, 90, "../img/products/dogbedrainbow300x250.png", "Rainbow dog bed");

            Product whiteDogBed = new Product(24, "White Dog Bed","Give your furry friend the perfect place to rest and relax with our White Dog Bed. This clean and elegant bed is designed with your dog's comfort in mind. The plush cushioning provides a soft and supportive surface for your dog to curl up on. The white color adds a timeless and versatile touch to any room decor. The bed's durable construction ensures long-lasting use, and the removable cover makes it easy to clean. Treat your dog to the ultimate comfort with our White Dog Bed."
                    , 400, 90, "../img/products/dogbedwhite300x250.png", "White dog bed");

            Product dogBoneSnack = new Product(25, "Dog Bone Snack","Treat your beloved canine companion to our Dog Bone Snack. This delicious and nutritious treat is shaped like a bone, perfect for satisfying your dog's natural instinct to chew. Made from high-quality ingredients, it offers a flavorful and satisfying snack for your furry friend. The crunchy texture promotes dental health by helping to reduce plaque and tartar buildup. Whether it's for training, rewarding good behavior, or simply as a tasty treat, our Dog Bone Snack is sure to make your dog's tail wag with joy."
                    , 30, 30, "../img/products/dogbone300x250.png", "Dog Bone Snack");

            Product dogFoodEcon = new Product(26, "Economy Dog Food","Provide your canine companion with nourishing meals without breaking the bank with our Economy Dog Food. This budget-friendly dog food offers a balanced and nutritious diet for your dog. Made with quality ingredients, it provides essential nutrients to support their overall health and well-being. While being cost-effective, it does not compromise on quality and still offers a tasty meal for your furry friend. Keep your dog happy and healthy with our affordable Economy Dog Food."
                    , 80, 70, "../img/products/dogfood300x250.png", "Economy Dog Food");

            Product dogMuffin = new Product(27, "Dog Bone Decorated Dog Muffin","Treat your canine companion to a special indulgence with our Dog Bone Decorated Dog Muffin. This delicious muffin is specially made with dog-friendly ingredients to provide a tasty and safe treat for your furry friend. The muffin itself is moist and flavorful, and it is topped with a decorative dog bone made from dog-safe materials. It's a delightful combination of taste and visual appeal that will surely make your dog's tail wag with excitement. Perfect for special occasions or just to spoil your pup, our Dog Bone Decorated Dog Muffin is a treat they'll love."
                    , 30, 70, "../img/products/dogmuffin300x250.png", "Dog Bone Decorated Dog Muffin");

            Product greenMouse = new Product(28, "Green Mouse Cat Toy","Entertain your feline friend with our Green Mouse Cat Toy. This adorable and interactive toy is designed to engage your cat's hunting instincts. The toy features a realistic mouse shape with a vibrant green color that will capture your cat's attention. Made from durable materials, it can withstand your cat's playful pouncing and swatting. The toy is filled with catnip, adding an irresistible scent that will further entice your cat to play. Watch as your cat chases, bats, and pounces on our Green Mouse Cat Toy for hours of entertainment."
                    , 15, 20, "../img/products/greenmouse300x250.png", "Green Mouse toy");

            Product hamsterWheel = new Product(29, "Hamster Wheel","Provide a comfortable and secure home for your hamster with our Hamster Cage. This spacious cage is designed with the needs of hamsters in mind. It features multiple levels and platforms for your hamster to explore and play on. The cage is made of durable materials and includes a sturdy wire frame to ensure the safety of your pet. It also comes with a removable bottom tray for easy cleaning and maintenance. With ample space and various accessories, our Hamster Cage is the ideal habitat for your furry friend. Create a cozy and stimulating environment for your hamster with our Hamster Cage."
                    , 350, 35, "../img/products/hamsterwheel300x250.png", "Hamster Wheel");

            Product hamsterCage = new Product(30, "Hamster Cage","Provide your hamster with a fun and healthy way to stay active with our Hamster Wheel. This sturdy and durable wheel is designed specifically for hamsters to encourage exercise and stimulate their natural instincts. The wheel features a solid construction with a smooth surface to ensure safe and comfortable running. It operates silently, allowing your hamster to enjoy their exercise without disturbing the peace. The wheel can be easily attached to the cage and provides hours of entertainment and physical activity for your hamster. Keep your furry friend happy and healthy with our Hamster Wheel."
                    , 650, 20, "../img/products/hamstercage300x250.png", "Hamster Cage");

            Product orangeDogBall = new Product(31, "Orange Ball Dog Toy","Engage your canine companion in endless playtime fun with our Orange Ball Dog Toy. This vibrant and durable toy is designed to capture your dog's attention and provide hours of entertainment. Made from high-quality materials, it can withstand chewing, fetching, and bouncing. The ball's bright orange color makes it easy to spot during outdoor play sessions. Its versatile design allows for interactive games of fetch or independent play. Watch as your dog enjoys chasing, pouncing, and retrieving the Orange Ball Dog Toy. Keep your dog active and entertained with this engaging toy."
                    , 45, 120, "../img/products/orangeball300x250.png", "Orange Ball toy");

            Product redCatToy = new Product(32, "Red Animal-Shaped Cat Toy Ball","Treat your playful feline friend to our Red Animal-Shaped Cat Toy Ball. This engaging and interactive toy combines the excitement of a ball with the charm of an animal shape. The toy features a vibrant red color that will capture your cat's attention. Shaped like a small animal, it adds an extra element of fun during playtime. Made from durable materials, it can withstand batting, pouncing, and chasing. The lightweight design allows for easy rolling and bouncing. Watch as your cat enjoys hours of entertainment with our Red Animal-Shaped Cat Toy Ball. Stimulate their hunting instincts and provide endless amusement with this delightful toy."
                    , 25, 75, "../img/products/redcattoy300x250.png", "Red Animal-Shaped Cat Toy Ball");

            Product xlDoDBone= new Product(33, "XL Dog Bone Snack","Give your large-sized canine companion a satisfying treat with our XL Dog Bone Snack. This extra-large bone-shaped treat is designed to provide a long-lasting and enjoyable chewing experience. Made from high-quality ingredients, it offers a delicious and nutritious snack for your furry friend. The size and texture of the treat promote dental health by helping to clean your dog's teeth and reduce plaque buildup. Whether it's for rewarding good behavior or simply as a tasty snack, our XL Dog Bone Snack is perfect for large dogs who love to chew."
                    , 50, 75, "../img/products/xldogbone300x250.png", "XL Dog Bone Snack");

            Product yellowMouse = new Product(34, "Googly-Eyed Yellow Mouse Cat Toy","Bring out the playfulness in your feline companion with our Googly-Eyed Yellow Mouse Cat Toy. This adorable and interactive toy is designed to capture your cat's attention and ignite their hunting instincts. The toy features a yellow mouse design with playful googly eyes that will surely pique your cat's curiosity. Made from durable materials, it can withstand your cat's batting, pouncing, and chasing. The lightweight construction allows for easy tossing and carrying. Watch as your cat enjoys hours of entertainment with our Googly-Eyed Yellow Mouse Cat Toy. Provide them with a fun and engaging playtime experience with this charming toy."
                    , 40, 60, "../img/products/yellowmouse300x250.png", "Googly-Eyed Yellow Mouse Cat Toy");


            List<Product> productList = new ArrayList<>();
            productList.add(dogWinterWearSet);
            productList.add(dogWinterBoots);
            productList.add(ecoFriendlyDogLeash);
            productList.add(sweaterForCats);
            productList.add(birdCage);
            productList.add(catHoodie);
            productList.add(dogBed1);
            productList.add(dogBed2);
            productList.add(dogTreat1);
            productList.add(dogTreat2);
            productList.add(bagDogFood);
            productList.add(bell);
            productList.add(birdFood);
            productList.add(blueCatToy);
            productList.add(boneshapedDogFood);
            productList.add(pinkCatToy);
            productList.add(hawaiianCatShirt);
            productList.add(multiArmCatToy);
            productList.add(blackDogBed);
            productList.add(blueDogBed);
            productList.add(greenDogBed);
            productList.add(pinkDogBed);
            productList.add(rainbowDogBed);
            productList.add(whiteDogBed);
            productList.add(dogBoneSnack);
            productList.add(dogFoodEcon);
            productList.add(dogMuffin);
            productList.add(greenMouse);
            productList.add(hamsterWheel);
            productList.add(hamsterCage);
            productList.add(orangeDogBall);
            productList.add(redCatToy);
            productList.add(xlDoDBone);
            productList.add(yellowMouse);




            List<ProductCategory> categoryList = new ArrayList<>();
            categoryList.add(dog);
            categoryList.add(dogClothing);
            categoryList.add(dogAccessories);
            categoryList.add(dogTreats);
            categoryList.add(dogFood);
            categoryList.add(dogBeds);
            categoryList.add(cat);
            categoryList.add(catClothing);
            categoryList.add(catAccessories);
            categoryList.add(catTreats);
            categoryList.add(catFood);
            categoryList.add(catToys);
            categoryList.add(bird);
            categoryList.add(winter);
            categoryList.add(indoors);
            categoryList.add(small);
            categoryList.add(hamster);

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

            dog.addProduct(dogBed1);
            dogBeds.addProduct(dogBed1);

            dog.addProduct(dogBed2);
            dogBeds.addProduct(dogBed2);

            dog.addProduct(dogTreat1);
            dogTreats.addProduct(dogTreat1);

            dog.addProduct(dogTreat2);
            dogTreats.addProduct(dogTreat2);

            cat.addProduct(sweaterForCats);
            catClothing.addProduct(sweaterForCats);
            indoors.addProduct(sweaterForCats);

            bird.addProduct(birdCage);
            small.addProduct(birdCage);
            indoors.addProduct(birdCage);

            cat.addProduct(catHoodie);
            catClothing.addProduct(catHoodie);

            dog.addProduct(bagDogFood);
            dogFood.addProduct(bagDogFood);

            cat.addProduct(bell);
            catToys.addProduct(bell);

            bird.addProduct(birdFood);
            small.addProduct(birdFood);

            cat.addProduct(blueCatToy);
            catToys.addProduct(blueCatToy);

            dog.addProduct(boneshapedDogFood);
            dogFood.addProduct(boneshapedDogFood);
            dogTreats.addProduct(boneshapedDogFood);

            catToys.addProduct(pinkCatToy);
            cat.addProduct(pinkCatToy);

            cat.addProduct(hawaiianCatShirt);
            catClothing.addProduct(hawaiianCatShirt);

            cat.addProduct(multiArmCatToy);
            catToys.addProduct(multiArmCatToy);

            dog.addProduct(blackDogBed);
            dogBeds.addProduct(blackDogBed);

            dog.addProduct(blueDogBed);
            dogBeds.addProduct(blueDogBed);

            dog.addProduct(greenDogBed);
            dogBeds.addProduct(greenDogBed);

            dog.addProduct(pinkDogBed);
            dogBeds.addProduct(pinkDogBed);

            dog.addProduct(rainbowDogBed);
            dogBeds.addProduct(rainbowDogBed);

            dogBeds.addProduct(whiteDogBed);
            dog.addProduct(whiteDogBed);

            dogFood.addProduct(dogBoneSnack);
            dogTreats.addProduct(dogBoneSnack);
            dog.addProduct(dogBoneSnack);

            dogFood.addProduct(dogFoodEcon);
            dog.addProduct(dogFoodEcon);


            dogFood.addProduct(dogMuffin);
            dogTreats.addProduct(dogMuffin);
            dog.addProduct(dogMuffin);

            cat.addProduct(greenMouse);
            catToys.addProduct(greenMouse);

            small.addProduct(hamsterWheel);
            hamster.addProduct(hamsterWheel);

            small.addProduct(hamsterCage);
            hamster.addProduct(hamsterCage);


            dog.addProduct(orangeDogBall);
            dogAccessories.addProduct(orangeDogBall);

            cat.addProduct(redCatToy);
            catToys.addProduct(redCatToy);

            dog.addProduct(xlDoDBone);
            dogTreats.addProduct(xlDoDBone);

            cat.addProduct(yellowMouse);
            catToys.addProduct(yellowMouse);



            //Apply Category to Product
            dogWinterWearSet.addProductCategory(dog);
            dogWinterWearSet.addProductCategory(dogClothing);
            dogWinterWearSet.addProductCategory(winter);

            dogWinterBoots.addProductCategory(dog);
            dogWinterBoots.addProductCategory(dogClothing);
            dogWinterBoots.addProductCategory(dogAccessories);
            dogWinterBoots.addProductCategory(winter);

            dogBed1.addProductCategory(dog);
            dogBed1.addProductCategory(dogBeds);

            dogBed2.addProductCategory(dog);
            dogBed2.addProductCategory(dogBeds);

            dogTreat1.addProductCategory(dog);
            dogTreat1.addProductCategory(dogTreats);

            dogTreat2.addProductCategory(dog);
            dogTreat2.addProductCategory(dogTreats);

            ecoFriendlyDogLeash.addProductCategory(dog);
            ecoFriendlyDogLeash.addProductCategory(dogAccessories);

            sweaterForCats.addProductCategory(cat);
            sweaterForCats.addProductCategory(catClothing);
            sweaterForCats.addProductCategory(indoors);

            birdCage.addProductCategory(bird);
            birdCage.addProductCategory(indoors);
            birdCage.addProductCategory(small);

            catHoodie.addProductCategory(cat);
            catHoodie.addProductCategory(catClothing);

            bagDogFood.addProductCategory(dog);
            bagDogFood.addProductCategory(dogFood);

            bell.addProductCategory(cat);
            bell.addProductCategory(catToys);

            birdFood.addProductCategory(small);
            birdFood.addProductCategory(bird);

            blueCatToy.addProductCategory(cat);
            blueCatToy.addProductCategory(catToys);

            boneshapedDogFood.addProductCategory(dog);
            boneshapedDogFood.addProductCategory(dogFood);
            boneshapedDogFood.addProductCategory(dogTreats);

            pinkCatToy.addProductCategory(cat);
            pinkCatToy.addProductCategory(catToys);

            hawaiianCatShirt.addProductCategory(catClothing);
            hawaiianCatShirt.addProductCategory(cat);

            multiArmCatToy.addProductCategory(cat);
            multiArmCatToy.addProductCategory(catToys);

            blackDogBed.addProductCategory(dog);
            blackDogBed.addProductCategory(dogBeds);

            blueDogBed.addProductCategory(dog);
            blueDogBed.addProductCategory(dogBeds);

            greenDogBed.addProductCategory(dog);
            greenDogBed.addProductCategory(dogBeds);

            pinkDogBed.addProductCategory(dog);
            pinkDogBed.addProductCategory(dogBeds);

            rainbowDogBed.addProductCategory(dog);
            rainbowDogBed.addProductCategory(dogBeds);

            whiteDogBed.addProductCategory(dog);
            whiteDogBed.addProductCategory(dogBeds);

            dogBoneSnack.addProductCategory(dog);
            dogBoneSnack.addProductCategory(dogFood);
            dogBoneSnack.addProductCategory(dogTreats);

            dogFoodEcon.addProductCategory(dogFood);
            dogFoodEcon.addProductCategory(dog);

            dogMuffin.addProductCategory(dog);
            dogMuffin.addProductCategory(dogFood);
            dogMuffin.addProductCategory(dogTreats);

            greenMouse.addProductCategory(cat);
            greenMouse.addProductCategory(catToys);

            hamsterWheel.addProductCategory(hamster);
            hamsterWheel.addProductCategory(small);

            hamsterCage.addProductCategory(hamster);
            hamsterCage.addProductCategory(small);

            orangeDogBall.addProductCategory(dog);
            orangeDogBall.addProductCategory(dogAccessories);

            redCatToy.addProductCategory(cat);
            redCatToy.addProductCategory(catToys);

            xlDoDBone.addProductCategory(dog);
            xlDoDBone.addProductCategory(dogTreats);

            yellowMouse.addProductCategory(cat);
            yellowMouse.addProductCategory(catToys);


            productRepository.saveAll(productList);
            categoryRepository.saveAll(categoryList);

            logger.info("Products and categories finished importing");
        }
        else {
            logger.info("Products and categories already in database, no need to import");
        }
    }
}

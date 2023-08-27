/**
 LoadDatabase.java:
 The LoadDatabase class is a configuration class that initializes the database with sample data.
 */
package hac.config;

import hac.dto.ItemDto;
import hac.dto.ReviewDto;
import hac.dto.UserDto;
import hac.services.ItemService;
import hac.services.ReviewService;
import hac.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    /**
     * Initializes the database with sample data using the provided services.
     *
     * @param userService   the service for managing users
     * @param itemService   the service for managing items
     * @param reviewService the service for managing reviews
     * @return a CommandLineRunner instance for database initialization
     */
    @Bean
    CommandLineRunner initDatabase(UserService userService, ItemService itemService, ReviewService reviewService) {
        return args -> {
            // Create Users
            UserDto admin = userService.createUser(new UserDto("admin@gmail.com", "admin", "password","password", "ADMIN"));
            UserDto john = userService.createUser(new UserDto("john@gmail.com", "john", "password","password", "USER"));


            ItemDto alamosMalbec = itemService.createItem(new ItemDto( "Alamos malbec",
                    "https://images.vivino.com/thumbs/F6oigGbISNOfMqYEuQ_mlQ_pb_600x600.png",
                    "Rated 90 points by James Suckling and Robert Parker, this round, smooth and rich Malbec offers great value for your money",
                    "wine"));
            ItemDto catenaZapata = itemService.createItem(new ItemDto("Catena Zapata malbec",
                    "https://products1.imgix.drizly.com/ci-zapata-catena-malbec-cf99e599baf6f2b0.jpeg?auto=format%2Ccompress&ch=Width%2CDPR&fm=jpg&q=20",
                    "A Red wine from Mendoza, Argentina. This wine has 1537 mentions of black fruit notes (blackberry, plum, blueberry)",
                    "wine"));
            ItemDto ClaseAzul = itemService.createItem(new ItemDto("CLASE AZUL TEQUILA REPOSADO",
                    "https://www.the-importer.co.il/GoopSitesFiles/83267/User/catalog_570720-l.webp?637723100069800000",
                    "A Red wine from Mendoza, Argentina. This wine has 1537 mentions of black fruit notes (blackberry, plum, blueberry)",
                    "wine"));
            ItemDto MakatoAzomoX18 = itemService.createItem(new ItemDto("AZUMA MAKOTO X 18 YEARS OLD",
                    "https://res.cloudinary.com/shogun-frontend/image/fetch/f_auto,q_auto,c_limit,w_1920/https://f.shgcdn.com/d74aa279-7ab3-4951-8579-35e85aba6bd1/",
                    "Captivated by the scents he found in our 18 Year Old’s bouquet, the floral artist likened our single malt to the wonder of a flower in bloom.",
                    "whiskey "));


            ItemDto brokersGin = itemService.createItem(new ItemDto("Broker's London Dry Gin",
                    "https://brokersgin.com/wp-content/uploads/2021/09/Brokers-Gin-750ML-3-4-View-250x800.png",
                    "Broker’s Gin was launched in the 1990’s. Supposedly created based on an early 18th century recipe— botanically, probably not in terms of base spirit, the base spirit is clean and clearly column distilled— it’s known as one of the bigger juniper-forward classic-style gins on the market today.",
                    "gin"));

            ItemDto fernetBranca = itemService.createItem(new ItemDto("Fernet Branca",
                    "https://www.delivino.co.il/GoopSitesFiles/83809/User/catalog_185570-l.jpg?637837530723600000",
                    "Fernet-Branca is an Italian brand of fernet, a style of amaro or bitters. It was formulated in Milan in 1845, and is manufactured there by Fratelli Branca Distillerie.It's the most common drink in Argentina",
                    "fernet"));
            ItemDto tequilaPatron = itemService.createItem(new ItemDto("tequila patron anejo",
                    "https://sendgiftsinisrael.com/wp-content/uploads/2020/10/Patron-Anejo-Tequila-750ml.jpg",
                    "The history of the tequila industry can almost be divided into before Patrón and after Patrón. Launched in 1989, Patrón bucked the perception of tequila as a lousy-tasting bottom-shelf party drink and almost single-handedly elevated it into the spirits pantheon along with whiskey and cognac. Patrón’s distinctive beehive-shaped bottle and round cork became iconic in bars and liquor stores in the 1990s and 2000s, and the brand remains a symbol of quality and opulence today.",
                    "tequila"));

            ItemDto lagunitasBeer =  itemService.createItem(new ItemDto("Lagunitas Beer",
                    "https://products0.imgix.drizly.com/ci-lagunitas-ipa-af10e1c1b75b70e3.jpeg?auto=format%2Ccompress&ch=Width%2CDPR&fm=jpg&q=20",
                    "he gold standard of our hop-forward approach to brewing. Our original and flagship IPA, Lagunitas IPA sent us down the path of brewing edgy brews. This highly balanced and super drinkable malt beer was dreamed up in California 30-odd years ago amongst the redwoods, rugged coastlines and rolling hills of Sonoma County. It's West Coast IPA all the way. It's also so doggone good that it's now brewed from Petaluma to Chicago and beyond. Yet, somehow, this particular IPA found its way into your reality at this very moment. Whoa. Life is uncertain, don’t sip!",
                    "beer"));

            reviewService.createReview(new ReviewDto("cheap and good", 1, john.getId(), alamosMalbec.getId(), john.getUsername()));
            reviewService.createReview(new ReviewDto("so tasty", 2, john.getId(), alamosMalbec.getId(), john.getUsername()));
            reviewService.createReview(new ReviewDto("good, but too dry for me", 3, admin.getId(), alamosMalbec.getId(), admin.getUsername()));
            reviewService.createReview(new ReviewDto("you just taste the wood, excellent", 3, admin.getId(), alamosMalbec.getId(), admin.getUsername()));
            reviewService.createReview(new ReviewDto("cheap and good", 3, admin.getId(), alamosMalbec.getId(), admin.getUsername()));
            reviewService.createReview(new ReviewDto("stam bodkim hakol", 3, admin.getId(), alamosMalbec.getId(), admin.getUsername()));
            reviewService.createReview(new ReviewDto("smells like wood and tastes like frutal", 3, admin.getId(), alamosMalbec.getId(), admin.getUsername()));
            reviewService.createReview(new ReviewDto("you just taste the wood, excellent", 3, admin.getId(), catenaZapata.getId(), admin.getUsername()));
            reviewService.createReview(new ReviewDto("you just taste the wood, excellent", 5, admin.getId(), ClaseAzul.getId(), admin.getUsername()));
            reviewService.createReview(new ReviewDto("you just taste the wood, excellent", 5, admin.getId(), ClaseAzul.getId(), admin.getUsername()));
            reviewService.createReview(new ReviewDto("bodkim po", 2, admin.getId(), MakatoAzomoX18.getId(), admin.getUsername()));
            reviewService.createReview(new ReviewDto("pretty cool beer", 1, admin.getId(), lagunitasBeer.getId(), admin.getUsername()));
//            reviewService.createReview(new ReviewDto("bdika 2", 1, admin.getId(), catenaZapata1.getId(), admin.getUsername()));
//            reviewService.createReview(new ReviewDto("ahat shtaim", 1, admin.getId(), ClaseAzul1.getId(), admin.getUsername()));
            reviewService.createReview(new ReviewDto("hiedeeeee", 1, admin.getId(), tequilaPatron.getId(), admin.getUsername()));
            reviewService.createReview(new ReviewDto("aiwaaaaa", 1, admin.getId(), catenaZapata.getId(), admin.getUsername()));
            reviewService.createReview(new ReviewDto("goddamn how can u drink this!?", 1, admin.getId(), fernetBranca.getId(), admin.getUsername()));
            reviewService.createReview(new ReviewDto("I think it’s one of those “nearly everywhere” gins but rarely on barback gins. And It’s surprising to me that it’s not more widely drank.", 5, admin.getId(), brokersGin.getId(), admin.getUsername()));
            };
        }
}

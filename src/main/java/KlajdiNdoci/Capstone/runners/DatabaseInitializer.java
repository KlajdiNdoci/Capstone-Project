package KlajdiNdoci.Capstone.runners;

import KlajdiNdoci.Capstone.entities.Game;
import KlajdiNdoci.Capstone.entities.News;
import KlajdiNdoci.Capstone.entities.Review;
import KlajdiNdoci.Capstone.entities.User;
import KlajdiNdoci.Capstone.enums.GameGenre;
import KlajdiNdoci.Capstone.enums.Platform;
import KlajdiNdoci.Capstone.enums.UserRole;
import KlajdiNdoci.Capstone.repositories.GameRepository;
import KlajdiNdoci.Capstone.repositories.NewsRepository;
import KlajdiNdoci.Capstone.repositories.ReviewRepository;
import KlajdiNdoci.Capstone.repositories.UserRepository;
import KlajdiNdoci.Capstone.services.GameService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameService gameService;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    private final Faker faker = new Faker(new Locale("en"));
    @Override
    public void run(String... args) throws Exception {
        populateDatabase();
    }

    private void populateDatabase() {
        for (int i = 0 ; i < 10;  i++ ){
            Game newGame = new Game();
            newGame.setGameCover("https://tritonsubs.com/wp-content/uploads/2020/07/Placeholder-16x9-1.jpg");
            newGame.setTitle(faker.esports().game());
            newGame.setDescription(faker.lorem().paragraph());
            newGame.setReleaseDate(LocalDate.of(faker.number().numberBetween(1990, 2023), faker.number().numberBetween(1, 12), faker.number().numberBetween(1, 28)));
            newGame.setDeveloper(faker.company().name());
            newGame.setPublisher(faker.company().name());
            Set<GameGenre> randomGenresSet = new HashSet<>();
            while (randomGenresSet.size() < 2) {
                GameGenre randomGenre = GameGenre.valueOf(GameGenre.values()[faker.number().numberBetween(0, GameGenre.values().length)].name());
                randomGenresSet.add(randomGenre);
            }
            List<GameGenre> randomGenres = new ArrayList<>(randomGenresSet);

            newGame.setGenres(randomGenres);

            Set<Platform> randomPlatformsSet = new HashSet<>();
            while (randomPlatformsSet.size() < 2) {
                Platform randomPlatform = Platform.valueOf(Platform.values()[faker.number().numberBetween(0, Platform.values().length)].name());
                randomPlatformsSet.add(randomPlatform);
            }
            List<Platform> randomPlatforms = new ArrayList<>(randomPlatformsSet);

            newGame.setPlatforms(randomPlatforms);
            gameRepository.save(newGame);
            for (int j = 0; j < 3; j++) {
                News news = new News();
                news.setTitle(faker.lorem().sentence());
                news.setContent(faker.lorem().paragraph());
                news.setGame(newGame);
                news.setCreatedAt(faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                newsRepository.save(news);

            }    for (int j = 0; j < 5; j++) {
                User user = new User();
                user.setEmail(faker.internet().emailAddress());
                user.setRole(UserRole.USER);
                user.setUsername(faker.name().username());
                user.setAvatar("https://ui-avatars.com/api/?name=" + faker.name().username());
                user.setName(faker.name().firstName());
                user.setSurname(faker.name().firstName());
                user.setPassword(faker.internet().password(3, 20));
                userRepository.save(user);
                for (int y = 0; y < 3; y++) {
                    Review review = new Review();
                    review.setRating(faker.number().numberBetween(1, 6));
                    review.setTitle(faker.lorem().sentence());
                    review.setContent(faker.lorem().paragraph());
                    review.setUser(user);
                    review.setGame(newGame);
                    reviewRepository.save(review);
                    gameService.updateGameAverageRating(newGame.getId());
                }
            }



        }
    }
}

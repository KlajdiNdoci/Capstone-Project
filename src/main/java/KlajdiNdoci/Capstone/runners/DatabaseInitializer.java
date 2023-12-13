package KlajdiNdoci.Capstone.runners;

import KlajdiNdoci.Capstone.entities.Game;
import KlajdiNdoci.Capstone.enums.GameGenre;
import KlajdiNdoci.Capstone.enums.Platform;
import KlajdiNdoci.Capstone.repositories.GameRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private GameRepository gameRepository;
    private final Faker faker = new Faker(new Locale("en"));
    @Override
    public void run(String... args) throws Exception {
//        populateDatabase();
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
            newGame.setGenres(List.of(GameGenre.valueOf(GameGenre.values()[faker.number().numberBetween(0, GameGenre.values().length)].name())));
            newGame.setPlatforms(List.of(Platform.valueOf(Platform.values()[faker.number().numberBetween(0, Platform.values().length)].name())));
            gameRepository.save(newGame);
        }

    }

    private String getRandomLoremPicsumImageUrl() {
        int width = 1280;
        int height = 720;
        return String.format("https://picsum.photos/%d/%d", width, height);
    }
}

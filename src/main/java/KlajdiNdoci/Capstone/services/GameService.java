package KlajdiNdoci.Capstone.services;

import KlajdiNdoci.Capstone.entities.Game;
import KlajdiNdoci.Capstone.enums.GameGenre;
import KlajdiNdoci.Capstone.enums.Platform;
import KlajdiNdoci.Capstone.exceptions.NotFoundException;
import KlajdiNdoci.Capstone.payloads.NewGameDTO;
import KlajdiNdoci.Capstone.repositories.GameRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CloudinaryService cloudinaryService;

    public Game save(NewGameDTO body) {
        Game newGame = new Game();
        newGame.setGameCover("https://tritonsubs.com/wp-content/uploads/2020/07/Placeholder-16x9-1.jpg");
        newGame.setDescription(body.description());
        newGame.setTitle(body.title());
        newGame.setReleaseDate(body.releaseDate());

        List<GameGenre> genres = new ArrayList<>();
        for (String genreName : body.genres()) {
            GameGenre genre = GameGenre.valueOf(genreName.toUpperCase());
            genres.add(genre);
        }
        newGame.setGenres(genres);
        List<Platform> platforms = new ArrayList<>();
        for (String platformName : body.platforms()) {
            Platform platform = Platform.valueOf(platformName.toUpperCase());
            platforms.add(platform);
        }
        newGame.setPlatforms(platforms);
        return gameRepository.save(newGame);
    }

    public Page<Game> getGames(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        Page<Game> gamesPage = gameRepository.findAll(pageable);
        gamesPage.getContent().forEach(Game::calculateAverageRating);
        return gameRepository.findAll(pageable);
    }

    public Game findById(UUID id) {
        return gameRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(UUID id) {
        Game found = gameRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        gameRepository.delete(found);
    }

    public Game uploadTrailer(MultipartFile file, UUID id) throws IOException {
        Game found = gameRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        if (found.getTrailer() != null) {
            cloudinaryService.deleteImageByUrl(found.getTrailer());
        }
        found.setTrailer(url);
        gameRepository.save(found);
        return found;
    }

    public Game uploadCover(MultipartFile file, UUID id) throws IOException {
        Game found = gameRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        if (!found.getGameCover().equals("https://tritonsubs.com/wp-content/uploads/2020/07/Placeholder-16x9-1.jpg")) {
            cloudinaryService.deleteImageByUrl(found.getGameCover());
        }
        found.setGameCover(url);
        gameRepository.save(found);
        return found;
    }

    public Game findByIdAndUpdate(UUID id, NewGameDTO body) {
        Game found = gameRepository.findById(id).orElseThrow(() -> new NotFoundException(id));

        found.setTitle(body.title());
        found.setDescription(body.description());
        found.setReleaseDate(body.releaseDate());

        List<GameGenre> updatedGenres = new ArrayList<>();
        for (String genreName : body.genres()) {
            GameGenre genre = GameGenre.valueOf(genreName.toUpperCase());
            updatedGenres.add(genre);
        }
        found.setGenres(updatedGenres);

        List<Platform> updatedPlatforms = new ArrayList<>();
        for (String platformName : body.platforms()) {
            Platform platform = Platform.valueOf(platformName.toUpperCase());
            updatedPlatforms.add(platform);
        }
        found.setPlatforms(updatedPlatforms);

        return gameRepository.save(found);
    }

    public Game uploadImages(MultipartFile[] images, UUID gameId) throws IOException {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new NotFoundException(gameId));

        for (MultipartFile image : images) {
            String imageUrl = (String) cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap()).get("url");
            game.getGameImages().add(imageUrl);
        }

        return gameRepository.save(game);
    }
}

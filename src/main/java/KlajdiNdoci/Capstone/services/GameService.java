package KlajdiNdoci.Capstone.services;

import KlajdiNdoci.Capstone.entities.Game;
import KlajdiNdoci.Capstone.entities.Review;
import KlajdiNdoci.Capstone.enums.GameGenre;
import KlajdiNdoci.Capstone.enums.Platform;
import KlajdiNdoci.Capstone.exceptions.NotFoundException;
import KlajdiNdoci.Capstone.exceptions.UnsupportedMediaTypeException;
import KlajdiNdoci.Capstone.payloads.NewGameDTO;
import KlajdiNdoci.Capstone.payloads.PlatformDTO;
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
import java.util.*;
import java.util.stream.Collectors;

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
        newGame.setDeveloper(body.developer().toUpperCase());
        newGame.setPublisher(body.publisher().toUpperCase());
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

    public Page<Game> getGames(int page, int size, String orderBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction),orderBy));
        Page<Game> gamesPage = gameRepository.findAll(pageable);
        gamesPage.getContent().forEach(Game::calculateAverageRating);
        return gameRepository.findAll(pageable);
    }

    public Game findById(UUID id) {
        return gameRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(UUID id) {
        Game found = gameRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        if (found.getTrailer() != null) {
            cloudinaryService.deleteImageByUrl(found.getTrailer());
        }
        if (!found.getGameCover().equals("https://tritonsubs.com/wp-content/uploads/2020/07/Placeholder-16x9-1.jpg")) {
            cloudinaryService.deleteImageByUrl(found.getGameCover());
        }
        if (!found.getGameImages().isEmpty()) {
            for (String gameImage: found.getGameImages()) {
                cloudinaryService.deleteImageByUrl(gameImage);
            }
            cloudinaryService.deleteImageByUrl(found.getTrailer());
        }

        gameRepository.delete(found);
    }

    public Game uploadTrailer(MultipartFile file, UUID id) throws IOException {
        Game found = gameRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        if (!Arrays.asList("video/mp4", "video/avi", "video/mov").contains(file.getContentType())) {
            throw new UnsupportedMediaTypeException("The format of the video is not supported");
        }
        Map<String, Object> params = ObjectUtils.asMap("resource_type", "video");
        String url = (String) cloudinary.uploader().upload(file.getBytes(), params).get("url");
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

    public void updateGameAverageRating(UUID gameId) {
        Game game = findById(gameId);
        game.calculateAverageRating();
        gameRepository.save(game);
    }

    public Page<Game> findGamesByPlatforms(int page, int size, PlatformDTO body, String order, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), order));
        List<Platform> platformList = body.platforms().stream()
                .map(String::toUpperCase)
                .map(Platform::valueOf)
                .toList();
        return gameRepository.findByPlatformsIn(platformList, pageable);
    }

    public Page<Game> findGamesByTitleStartsWith(int page, int size, String q, String order, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), order));
        return gameRepository.findByTitleStartsWithIgnoreCase(q, pageable);
    }
}

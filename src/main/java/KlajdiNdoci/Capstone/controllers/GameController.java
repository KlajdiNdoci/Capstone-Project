package KlajdiNdoci.Capstone.controllers;

import KlajdiNdoci.Capstone.entities.Game;
import KlajdiNdoci.Capstone.entities.Review;
import KlajdiNdoci.Capstone.enums.Platform;
import KlajdiNdoci.Capstone.exceptions.BadRequestException;
import KlajdiNdoci.Capstone.payloads.NewGameDTO;
import KlajdiNdoci.Capstone.payloads.PlatformDTO;
import KlajdiNdoci.Capstone.repositories.GameRepository;
import KlajdiNdoci.Capstone.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Game> getGames(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "createdAt") String orderBy,
                               @RequestParam(defaultValue = "desc")String direction) {
        if (!direction.equalsIgnoreCase("desc") && !direction.equalsIgnoreCase("asc")) {
            throw new IllegalArgumentException("The direction has to be 'asc' or 'desc'!");
        }
        return gameService.getGames(page, size > 20 ? 5 : size, orderBy, direction);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Game createGame(@RequestBody @Validated NewGameDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return gameService.save(body);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Game findById(@PathVariable UUID id) {
        return gameService.findById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID id) {
        gameService.findByIdAndDelete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Game findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated NewGameDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return gameService.findByIdAndUpdate(id, body);
        }
    }

    @PutMapping("/{id}/upload/trailer")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Game uploadTrailer(@RequestParam("file") MultipartFile file, @PathVariable UUID id) throws IOException {
        try {
            return gameService.uploadTrailer(file, id);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping("/{id}/upload/cover")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Game uploadCover(@RequestParam("file") MultipartFile file, @PathVariable UUID id) throws IOException {
        try {
            return gameService.uploadCover(file, id);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping("/{id}/upload/images")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Game uploadImages(@RequestParam("file") MultipartFile[] file, @PathVariable UUID id) throws IOException {
        try {
            return gameService.uploadImages(file, id);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PostMapping("/platforms")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Game> getGamesByPlatforms(@RequestBody @Validated PlatformDTO platforms,
                                          BindingResult validation,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "averageRating") String orderBy,
                                          @RequestParam(defaultValue = "desc") String direction) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            if (!direction.equalsIgnoreCase("desc") && !direction.equalsIgnoreCase("asc")) {
                throw new IllegalArgumentException("The direction has to be 'asc' or 'desc'!");
            }
            return gameService.findGamesByPlatforms(page, size > 20 ? 5 : size, platforms, orderBy, direction);
        }
    }
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Game> getGamesByTitle(@RequestParam String q,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size,
                                          @RequestParam(defaultValue = "createdAt") String orderBy,
                                          @RequestParam(defaultValue = "desc") String direction) {
            if (!direction.equalsIgnoreCase("desc") && !direction.equalsIgnoreCase("asc")) {
                throw new IllegalArgumentException("The direction has to be 'asc' or 'desc'!");
            }
            return gameService.findGamesByTitleStartsWith(page, size > 20 ? 5 : size, q, orderBy, direction);
        }
    @GetMapping("/genres")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Game> filterByGenres(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "createdAt") String orderBy,
                               @RequestParam(defaultValue = "") String genre,
                               @RequestParam(defaultValue = "desc")String direction) {
        if (!direction.equalsIgnoreCase("desc") && !direction.equalsIgnoreCase("asc")) {
            throw new IllegalArgumentException("The direction has to be 'asc' or 'desc'!");
        }
        return gameService.filterByGenres(page, size > 20 ? 5 : size, orderBy, direction, genre);
    }
    @GetMapping("/platforms")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Game> filterByPlatforms(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "createdAt") String orderBy,
                               @RequestParam(defaultValue = "") String platform,
                               @RequestParam(defaultValue = "desc")String direction) {
        if (!direction.equalsIgnoreCase("desc") && !direction.equalsIgnoreCase("asc")) {
            throw new IllegalArgumentException("The direction has to be 'asc' or 'desc'!");
        }
        return gameService.filterByPlatforms(page, size > 20 ? 5 : size, orderBy, direction, platform);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Game> getSavedGamesByUserId(@PathVariable UUID userId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size,
                                            @RequestParam(defaultValue = "createdAt") String orderBy,
                                            @RequestParam(defaultValue = "desc") String direction) {
        if (!direction.equalsIgnoreCase("desc") && !direction.equalsIgnoreCase("asc")) {
            throw new IllegalArgumentException("The direction has to be 'asc' or 'desc'!");
        }
        return gameService.findSavedGamesByUserId(page, size > 20 ? 5 : size, userId, orderBy, direction);
    }
}

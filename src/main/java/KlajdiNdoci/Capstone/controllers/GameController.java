package KlajdiNdoci.Capstone.controllers;

import KlajdiNdoci.Capstone.entities.Game;
import KlajdiNdoci.Capstone.entities.User;
import KlajdiNdoci.Capstone.enums.GameGenre;
import KlajdiNdoci.Capstone.enums.Platform;
import KlajdiNdoci.Capstone.exceptions.BadRequestException;
import KlajdiNdoci.Capstone.payloads.NewGameDTO;
import KlajdiNdoci.Capstone.payloads.NewUserDTO;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @GetMapping("")
    public Page<Game> getGames(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String orderBy) {
        return gameService.getGames(page, size > 20 ? 5 : size, orderBy);
    }

    @PostMapping()
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
}

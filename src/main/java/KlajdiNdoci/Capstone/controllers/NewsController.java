package KlajdiNdoci.Capstone.controllers;

import KlajdiNdoci.Capstone.entities.News;
import KlajdiNdoci.Capstone.entities.User;
import KlajdiNdoci.Capstone.exceptions.BadRequestException;
import KlajdiNdoci.Capstone.payloads.NewNewsDTO;
import KlajdiNdoci.Capstone.repositories.NewsRepository;
import KlajdiNdoci.Capstone.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsRepository newsRepository;

    @GetMapping("")
    public Page<News> getNews(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "createdAt") String orderBy,
                              @RequestParam(defaultValue = "desc") String direction) {
        if (!direction.equalsIgnoreCase("desc") && !direction.equalsIgnoreCase("asc")) {
            throw new IllegalArgumentException("The direction has to be 'asc' or 'desc'!");
        }
        return newsService.getNews(page, size > 20 ? 5 : size, orderBy, direction);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public News createNews(@RequestBody @Validated NewNewsDTO body, BindingResult validation, @AuthenticationPrincipal User currentUser) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return newsService.save(body, currentUser.getId());
        }
    }

    @GetMapping("/{id}")
    public News findById(@PathVariable UUID id) {
        return newsService.findById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID id) {
        newsService.findByIdAndDelete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public News findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated NewNewsDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return newsService.findByIdAndUpdate(body , id);
        }
    }
}

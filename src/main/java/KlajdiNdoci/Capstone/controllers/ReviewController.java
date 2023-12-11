package KlajdiNdoci.Capstone.controllers;

import KlajdiNdoci.Capstone.entities.Review;
import KlajdiNdoci.Capstone.entities.User;
import KlajdiNdoci.Capstone.enums.UserRole;
import KlajdiNdoci.Capstone.exceptions.BadRequestException;
import KlajdiNdoci.Capstone.exceptions.UnauthorizedException;
import KlajdiNdoci.Capstone.payloads.NewReviewDTO;
import KlajdiNdoci.Capstone.payloads.UpdateReviewDTO;
import KlajdiNdoci.Capstone.services.ReviewService;
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
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;


    @GetMapping("")
    public Page<Review> getReviews(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "createdAt") String orderBy,
                                   @RequestParam(defaultValue = "desc") String direction) {
        if (!direction.equalsIgnoreCase("desc") && !direction.equalsIgnoreCase("asc")) {
            throw new IllegalArgumentException("The direction has to be 'asc' or 'desc'!");
        }
        return reviewService.getReviews(page, size > 20 ? 5 : size, orderBy, direction);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Review createReview(@RequestBody @Validated NewReviewDTO body, BindingResult validation, @AuthenticationPrincipal User currentUser) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return reviewService.save(body, currentUser.getId());
        }
    }

    @GetMapping("/{id}")
    public Review findById(@PathVariable UUID id) {
        return reviewService.findById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID id, @AuthenticationPrincipal User currentUser) {
        Review review = reviewService.findById(id);
        if (currentUser.getRole().equals(UserRole.ADMIN) || currentUser.getId().equals(review.getUser().getId())) {
            reviewService.findByIdAndDelete(id);
        } else {
            throw new UnauthorizedException("Access Denied");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Review findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated UpdateReviewDTO body, BindingResult validation, @AuthenticationPrincipal User currentUser) {
        Review review = reviewService.findById(id);
        if (currentUser.getRole().equals(UserRole.ADMIN) || currentUser.getId().equals(review.getUser().getId())) {
            if (validation.hasErrors()) {
                throw new BadRequestException(validation.getAllErrors());
            } else {
                return reviewService.findByIdAndUpdate(body, id);
            }
        } else {
            throw new UnauthorizedException("Access Denied");
        }
    }
    @GetMapping("/game/{gameId}")
    public Page<Review> getReviewsByGameId(@PathVariable UUID gameId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "rating") String orderBy,
                                           @RequestParam(defaultValue = "desc") String direction) {
        if (!direction.equalsIgnoreCase("desc") && !direction.equalsIgnoreCase("asc")) {
            throw new IllegalArgumentException("The direction has to be 'asc' or 'desc'!");
        }
        return reviewService.findReviewsByGameId(page, size > 20 ? 5 : size, gameId, orderBy, direction);
    }
    @GetMapping("/game/{gameId}/{minusDays}")
    public Page<Review> getReviewsByGameIdAndLastMonth(
            @PathVariable UUID gameId,
            @PathVariable int minusDays,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String orderBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        if (!direction.equalsIgnoreCase("desc") && !direction.equalsIgnoreCase("asc")) {
            throw new IllegalArgumentException("The direction has to be 'asc' or 'desc'!");
        }
        return reviewService.findReviewsByGameIdAndDate(page, size > 20 ? 5 : size, gameId, orderBy, direction, minusDays);
    }

    @PostMapping("/{reviewId}/like")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Review likeReview(@PathVariable UUID reviewId, @AuthenticationPrincipal User currentUser) {
        return reviewService.likeReview(reviewId, currentUser.getId());
    }
}

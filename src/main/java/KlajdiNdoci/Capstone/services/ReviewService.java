package KlajdiNdoci.Capstone.services;

import KlajdiNdoci.Capstone.entities.Comment;
import KlajdiNdoci.Capstone.entities.Game;
import KlajdiNdoci.Capstone.entities.Review;
import KlajdiNdoci.Capstone.entities.User;
import KlajdiNdoci.Capstone.exceptions.NotFoundException;
import KlajdiNdoci.Capstone.payloads.NewNewsDTO;
import KlajdiNdoci.Capstone.payloads.NewReviewDTO;
import KlajdiNdoci.Capstone.payloads.UpdateReviewDTO;
import KlajdiNdoci.Capstone.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class ReviewService {
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;
    @Autowired
    private ReviewRepository reviewRepository;

    public Review save(NewReviewDTO body, UUID userId) {
        Review newReview =Review.builder()
                .content(body.content())
                .user(userService.findUserById(userId))
                .title(body.title())
                .game(gameService.findById(body.gameId()))
                .rating(body.rating())
                .build();
        Review savedReview = reviewRepository.save(newReview);
        gameService.updateGameAverageRating(savedReview.getGame().getId());
        return savedReview;
    }

    public Page<Review> getReviews(int page, int size, String orderBy, String direction) {
        Pageable pageable = PageRequest.of(page, size,  Sort.by(Sort.Direction.fromString(direction), orderBy));
        return reviewRepository.findAll(pageable);
    }

    public Review findById(UUID id) {
        return reviewRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Review findByIdAndUpdate(UpdateReviewDTO body, UUID reviewId) {
        Review foundReview = this.findById(reviewId);
        foundReview.setContent(body.content());
        foundReview.setTitle(body.title());
        foundReview.setRating(body.rating());
        Review updatedReview = reviewRepository.save(foundReview);
        gameService.updateGameAverageRating(updatedReview.getGame().getId());

        return updatedReview;
    }

    public void findByIdAndDelete(UUID id) {
        Review found = reviewRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        UUID gameId = found.getGame().getId();
        reviewRepository.delete(found);
        gameService.updateGameAverageRating(gameId);
    }

    public Page<Review> findReviewsByGameId(int page, int size, UUID id, String orderBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), orderBy));
        return reviewRepository.findByGameId(id, pageable);
    }
    public Page<Review> findReviewsByGameIdAndDate(int page, int size, UUID id, String orderBy, String direction, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        LocalDateTime endDate = LocalDateTime.now();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), orderBy));
        return reviewRepository.findByGameIdAndCreatedAtBetween(id, startDate, endDate, pageable);
    }

    public Review likeReview(UUID reviewId, UUID userId) {
        Review review = findById(reviewId);
        User user = userService.findUserById(userId);

        if (!review.getLikes().contains(user)) {
            review.getLikes().add(user);
        } else {
            review.getLikes().remove(user);
        }
        return reviewRepository.save(review);
    }
}

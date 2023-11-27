package KlajdiNdoci.Capstone.services;

import KlajdiNdoci.Capstone.entities.Review;
import KlajdiNdoci.Capstone.exceptions.NotFoundException;
import KlajdiNdoci.Capstone.payloads.NewReviewDTO;
import KlajdiNdoci.Capstone.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewService {
    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;
    @Autowired
    private ReviewRepository reviewRepository;

    public Review save(UUID userId,NewReviewDTO body) {
        Review newReview =Review.builder()
                .content(body.content())
                .user(userService.findUserById(userId))
                .game(gameService.findById(body.gameId()))
                .rating(body.rating())
                .build();
        return reviewRepository.save(newReview);
    }

    public Page<Review> getReviews(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return reviewRepository.findAll(pageable);
    }

    public Review findById(UUID id) {
        return reviewRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Review findByIdAndUpdate(UUID userId,NewReviewDTO body) {
        Review newReview =Review.builder()
                .content(body.content())
                .user(userService.findUserById(userId))
                .game(gameService.findById(body.gameId()))
                .rating(body.rating())
                .build();
        return reviewRepository.save(newReview);
    }

    public void findByIdAndDelete(UUID id) {
        Review found = reviewRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        reviewRepository.delete(found);
    }
}

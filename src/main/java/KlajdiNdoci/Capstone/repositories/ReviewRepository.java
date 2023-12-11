package KlajdiNdoci.Capstone.repositories;
import KlajdiNdoci.Capstone.entities.Review;
import KlajdiNdoci.Capstone.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;
@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Page<Review> findByGameId(UUID gameId, Pageable pageable);

    Page<Review> findByGameIdAndCreatedAtBetween(UUID gameId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("SELECT r.likes FROM Review r WHERE r.id = :reviewId")
    Page<User> findLikesById(@Param("reviewId") UUID reviewId, Pageable pageable);
}

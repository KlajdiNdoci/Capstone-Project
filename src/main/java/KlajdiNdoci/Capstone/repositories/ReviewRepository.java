package KlajdiNdoci.Capstone.repositories;

import KlajdiNdoci.Capstone.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;
@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Page<Review> findByGameId(UUID gameId, Pageable pageable);

    Page<Review> findByGameIdAndCreatedAtBetween(UUID gameId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}

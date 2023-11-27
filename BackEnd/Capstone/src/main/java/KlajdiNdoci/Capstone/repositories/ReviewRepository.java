package KlajdiNdoci.Capstone.repositories;

import KlajdiNdoci.Capstone.entities.Review;
import KlajdiNdoci.Capstone.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
}

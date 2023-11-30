package KlajdiNdoci.Capstone.repositories;

        import KlajdiNdoci.Capstone.entities.Game;
        import KlajdiNdoci.Capstone.entities.Review;
        import KlajdiNdoci.Capstone.enums.Platform;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.Pageable;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.stereotype.Repository;

        import java.util.List;
        import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
    Page<Game> findByPlatformsIn(List<Platform> platforms, Pageable pageable);
    Page<Game> findByTitleStartsWithIgnoreCase(String q, Pageable pageable);
}

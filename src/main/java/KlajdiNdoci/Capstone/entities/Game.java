package KlajdiNdoci.Capstone.entities;

import KlajdiNdoci.Capstone.enums.GameGenre;
import KlajdiNdoci.Capstone.enums.Platform;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "games")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String title;
    private String trailer;
    private String gameCover;
    private LocalDate releaseDate;
    private double averageRating;

    @Column(columnDefinition="TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "game_images", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "image_path")
    private List<String> gameImages;

    @ElementCollection(targetClass = GameGenre.class)
    @CollectionTable(name = "game_genres", joinColumns = @JoinColumn(name = "game_id"))
    @Enumerated(EnumType.STRING)
    private List<GameGenre> genres;

    @ElementCollection(targetClass = Platform.class)
    @CollectionTable(name = "game_platforms", joinColumns = @JoinColumn(name = "game_id"))
    @Enumerated(EnumType.STRING)
    private List<Platform> platforms;

    @JsonIgnore
    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(
            name = "user_saved_games",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @ManyToMany
    @JoinTable(
            name = "game_news",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "news_id")
    )
    private List<News> news;

    public void calculateAverageRating() {
        if (reviews != null && !reviews.isEmpty()) {
            double totalRating = 0;
            int numReviews = reviews.size();

            for (Review review : reviews) {
                totalRating += review.getRating();
            }
            averageRating = totalRating / numReviews;
        } else {
            averageRating = 0.0;
        }
    }
}

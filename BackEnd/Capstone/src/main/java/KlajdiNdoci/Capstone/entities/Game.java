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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String title;
    private String trailer;
    private String description;
    private List<String> gameImages;
    private String gameCover;
    private List<GameGenre> genre;
    @Enumerated(EnumType.STRING)
    private List<Platform> platforms;
    private LocalDate releaseDate;
    @JsonIgnore
    @OneToMany(mappedBy = "game")
    private List<Review> reviews;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "news_id")
    private List<News> news;
}

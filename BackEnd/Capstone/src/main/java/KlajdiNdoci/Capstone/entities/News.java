package KlajdiNdoci.Capstone.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "news")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String title;
    private String content;
    private String image;
    @ManyToOne
    @JoinColumn(name= "comment_id")
    private List<Comment> comments;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    protected LocalDate createdAt;
    @OneToMany(mappedBy = "news")
    private Game game;
}

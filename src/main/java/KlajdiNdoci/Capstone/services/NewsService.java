package KlajdiNdoci.Capstone.services;

import KlajdiNdoci.Capstone.entities.Game;
import KlajdiNdoci.Capstone.entities.News;
import KlajdiNdoci.Capstone.exceptions.NotFoundException;
import KlajdiNdoci.Capstone.payloads.NewNewsDTO;
import KlajdiNdoci.Capstone.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NewsService {
    @Autowired
    private UserService userService;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private GameService gameService;


    public News save( NewNewsDTO body, UUID userId) {
        Game foundGame = gameService.findById(body.gameId());
        News newNews =News.builder()
                .content(body.content())
                .title(body.title())
                .creator(userService.findUserById(userId))
                .game(foundGame)
                .image(foundGame.getGameCover())
                .build();
        return newsRepository.save(newNews);
    }

    public Page<News> getNews(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return newsRepository.findAll(pageable);
    }

    public News findById(UUID id) {
        return newsRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public News findByIdAndUpdate(NewNewsDTO body, UUID newsId) {
        News foundNews = this.findById(newsId);
        foundNews.setContent(body.content());
        foundNews.setTitle(body.title());
        return newsRepository.save(foundNews);
    }

    public void findByIdAndDelete(UUID id) {
        News found = newsRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        newsRepository.delete(found);
    }
}

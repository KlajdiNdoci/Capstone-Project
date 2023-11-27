package KlajdiNdoci.Capstone.services;

import KlajdiNdoci.Capstone.entities.Game;
import KlajdiNdoci.Capstone.exceptions.NotFoundException;
import KlajdiNdoci.Capstone.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public Game save(Game body) {
        return gameRepository.save(body);
    }

    public Page<Game> getComuni(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return gameRepository.findAll(pageable);
    }

    public Game findById(UUID id) {
        return gameRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void findByIdAndDelete(UUID id) {
        Game found = gameRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        gameRepository.delete(found);
    }
}

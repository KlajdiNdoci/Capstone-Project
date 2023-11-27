package KlajdiNdoci.Capstone.services;

import KlajdiNdoci.Capstone.entities.Comment;
import KlajdiNdoci.Capstone.exceptions.NotFoundException;
import KlajdiNdoci.Capstone.payloads.NewCommentDTO;
import KlajdiNdoci.Capstone.payloads.NewNewsDTO;
import KlajdiNdoci.Capstone.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;
    @Autowired
    private CommentRepository commentRepository;

    public Comment save(UUID userId, NewCommentDTO body) {
        Comment newComment =Comment.builder()
                .content(body.content())
                .user(userService.findUserById(userId))
                .news(newsService.findById(body.newsId()))
                .build();
        return commentRepository.save(newComment);
    }

    public Page<Comment> getComments(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return commentRepository.findAll(pageable);
    }

    public Comment findById(UUID id) {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Comment findByIdAndUpdate(NewNewsDTO body, UUID commentId) {
        Comment foundComment = this.findById(commentId);
        foundComment.setContent(body.content());
        return commentRepository.save(foundComment);
    }

    public void findByIdAndDelete(UUID id) {
        Comment found = commentRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        commentRepository.delete(found);
    }
}

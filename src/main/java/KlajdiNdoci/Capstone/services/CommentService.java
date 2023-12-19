package KlajdiNdoci.Capstone.services;

import KlajdiNdoci.Capstone.entities.Comment;
import KlajdiNdoci.Capstone.entities.User;
import KlajdiNdoci.Capstone.exceptions.NotFoundException;
import KlajdiNdoci.Capstone.payloads.NewCommentDTO;
import KlajdiNdoci.Capstone.payloads.NewNewsDTO;
import KlajdiNdoci.Capstone.payloads.UpdateCommentDTO;
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

    public Comment save(NewCommentDTO body,UUID userId, UUID newsId) {
        Comment newComment =Comment.builder()
                .content(body.content())
                .user(userService.findUserById(userId))
                .news(newsService.findById(newsId))
                .build();
        return commentRepository.save(newComment);
    }

    public Page<Comment> getComments(int page, int size, String orderBy, String direction) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.fromString(direction), orderBy));
        return commentRepository.findAll(pageable);
    }

    public Comment findById(UUID id) {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Comment findByIdAndUpdate(UpdateCommentDTO body, UUID commentId) {
        Comment foundComment = this.findById(commentId);
        foundComment.setContent(body.content());
        return commentRepository.save(foundComment);
    }

    public void findByIdAndDelete(UUID id) {
        Comment found = commentRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        commentRepository.delete(found);
    }

    public Page<Comment> getUserComments(int page, int size, String orderBy, String direction, UUID userId) {
        User user = userService.findUserById(userId);
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.fromString(direction), orderBy));
        return commentRepository.findByUser(user, pageable);
    }
}

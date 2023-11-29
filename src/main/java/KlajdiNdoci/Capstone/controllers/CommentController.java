package KlajdiNdoci.Capstone.controllers;

import KlajdiNdoci.Capstone.entities.Comment;
import KlajdiNdoci.Capstone.entities.User;
import KlajdiNdoci.Capstone.enums.UserRole;
import KlajdiNdoci.Capstone.exceptions.BadRequestException;
import KlajdiNdoci.Capstone.exceptions.UnauthorizedException;
import KlajdiNdoci.Capstone.payloads.NewCommentDTO;
import KlajdiNdoci.Capstone.payloads.UpdateCommentDTO;
import KlajdiNdoci.Capstone.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/news/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;


    @GetMapping("")
    public Page<Comment> getComments(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "id") String orderBy) {
        return commentService.getComments(page, size > 20 ? 5 : size, orderBy);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public Comment createComment(@RequestBody @Validated NewCommentDTO body, BindingResult validation, @AuthenticationPrincipal User currentUser) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return commentService.save(body, currentUser.getId());
        }
    }

    @GetMapping("/{id}")
    public Comment findById(@PathVariable UUID id) {
        return commentService.findById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID id, @AuthenticationPrincipal User currentUser) {
        Comment comment = commentService.findById(id);
        if (currentUser.getRole().equals(UserRole.ADMIN) || currentUser.getId().equals(comment.getUser().getId())) {
            commentService.findByIdAndDelete(id);
        } else {
            throw new UnauthorizedException("Access Denied");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Comment findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated UpdateCommentDTO body, BindingResult validation, @AuthenticationPrincipal User currentUser) {
        Comment comment = commentService.findById(id);
        if (currentUser.getRole().equals(UserRole.ADMIN) || currentUser.getId().equals(comment.getUser().getId())) {
            if (validation.hasErrors()) {
                throw new BadRequestException(validation.getAllErrors());
            } else {
                return commentService.findByIdAndUpdate(body, id);
            }
        } else {
            throw new UnauthorizedException("Access Denied");
        }
    }
}


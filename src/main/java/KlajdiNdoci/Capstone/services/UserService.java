package KlajdiNdoci.Capstone.services;

import KlajdiNdoci.Capstone.entities.Game;
import KlajdiNdoci.Capstone.entities.Review;
import KlajdiNdoci.Capstone.entities.User;
import KlajdiNdoci.Capstone.exceptions.NotFoundException;
import KlajdiNdoci.Capstone.payloads.NewUserDTO;
import KlajdiNdoci.Capstone.payloads.UpdateUserDTO;
import KlajdiNdoci.Capstone.repositories.GameRepository;
import KlajdiNdoci.Capstone.repositories.UserRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service

public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private GameService gameService;

    @Autowired
    private CloudinaryService cloudinaryService;

    public Page<User> getUsers(int page, int size, String orderBy, String direction) {
        Pageable pageable = PageRequest.of(page, size,  Sort.by(Sort.Direction.fromString(direction), orderBy));
        return userRepository.findAll(pageable);
    }


    public User findUserById(UUID id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByIdAndUpdate(UUID id, UpdateUserDTO u) throws NotFoundException {
        User foundUser = this.findUserById(id);
        foundUser.setUsername(u.username());
        foundUser.setName(u.name());
        foundUser.setSurname(u.surname());
        foundUser.setEmail(u.email());
        return userRepository.save(foundUser);
    }

    public void findByIdAndDelete(UUID id) throws NotFoundException {
        User foundUser = this.findUserById(id);
        if (!foundUser.getAvatar().equals("https://ui-avatars.com/api/?name=" + foundUser.getUsername())) {
            cloudinaryService.deleteImageByUrl(foundUser.getAvatar());
        }
        userRepository.delete(foundUser);
    }

    public User findUserByEmail(String email) throws NotFoundException {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new NotFoundException(email));
    }

    public User uploadImg(MultipartFile file, UUID id) throws IOException {
        User u = userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        if (!u.getAvatar().equals("https://ui-avatars.com/api/?name=" + u.getUsername())) {
            cloudinaryService.deleteImageByUrl(u.getAvatar());
        }
        u.setAvatar(url);
        userRepository.save(u);
        return u;
    }

    public User addOrRemoveGame(UUID gameId, UUID userId) {
        Game game = gameService.findById(gameId);
        User user = findUserById(userId);

        if (!user.getSavedGames().contains(game)) {
            user.getSavedGames().add(game);
        } else {
            user.getSavedGames().remove(game);
        }
        return userRepository.save(user);
    }
    public User addOrRemoveFriend( UUID friendId,UUID currentUserId) {
        User currentUser = findUserById(currentUserId);
        User friend = findUserById(friendId);

        if (!currentUser.getFriends().contains(friend)) {
            currentUser.getFriends().add(friend);
        }

        if (!friend.getFriends().contains(currentUser)) {
            friend.getFriends().add(currentUser);
        } else {
            currentUser.getFriends().remove(friend);
            friend.getFriends().remove(currentUser);
        }
        return userRepository.save(currentUser);
    }

    public Page<User> getUserFriends(UUID userId ,int page, int size, String orderBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), orderBy));
        return userRepository.findFriendsById(userId, pageable);
    }
}
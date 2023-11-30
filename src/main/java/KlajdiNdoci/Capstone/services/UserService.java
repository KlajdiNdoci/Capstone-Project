package KlajdiNdoci.Capstone.services;

import KlajdiNdoci.Capstone.entities.User;
import KlajdiNdoci.Capstone.exceptions.NotFoundException;
import KlajdiNdoci.Capstone.payloads.NewUserDTO;
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
    private UserRepository utenteRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CloudinaryService cloudinaryService;

    public Page<User> findAll(int page, int size, String sortBy, String direction) {
        if (size < 0)
            size = 10;
        if (size > 100)
            size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction),sortBy));
        return utenteRepository.findAll(pageable);
    }


    public User findUserById(UUID id) throws NotFoundException {
        return utenteRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByIdAndUpdate(UUID id, NewUserDTO u) throws NotFoundException {
        User foundUser = this.findUserById(id);
        foundUser.setUsername(u.username());
        foundUser.setName(u.name());
        foundUser.setSurname(u.surname());
        foundUser.setEmail(u.email());
        return utenteRepository.save(foundUser);
    }

    public void findByIdAndDelete(UUID id) throws NotFoundException {
        User foundUser = this.findUserById(id);
        if (!foundUser.getAvatar().equals("https://ui-avatars.com/api/?name=" + foundUser.getName() + "+" + foundUser.getSurname())) {
            cloudinaryService.deleteImageByUrl(foundUser.getAvatar());
        }
        utenteRepository.delete(foundUser);
    }

    public User findUserByEmail(String email) throws NotFoundException {
        return utenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(email));
    }

    public void deleteAllUtenti() {
        utenteRepository.deleteAll();
    }

    public User uploadImg(MultipartFile file, UUID id) throws IOException {
        User u = utenteRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        if (!u.getAvatar().equals("https://ui-avatars.com/api/?name=" + u.getName() + "+" + u.getSurname())) {
            cloudinaryService.deleteImageByUrl(u.getAvatar());
        }
        u.setAvatar(url);
        utenteRepository.save(u);
        return u;
    }
}
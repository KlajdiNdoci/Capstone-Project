package KlajdiNdoci.Capstone.services;

import KlajdiNdoci.Capstone.config.EmailSender;
import KlajdiNdoci.Capstone.entities.User;
import KlajdiNdoci.Capstone.enums.UserRole;
import KlajdiNdoci.Capstone.exceptions.BadRequestException;
import KlajdiNdoci.Capstone.exceptions.UnauthorizedException;
import KlajdiNdoci.Capstone.payloads.NewUserDTO;
import KlajdiNdoci.Capstone.payloads.UserLoginDTO;
import KlajdiNdoci.Capstone.repositories.UserRepository;
import KlajdiNdoci.Capstone.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {
    @Autowired
    PasswordEncoder bcrypt;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    EmailSender emailSender;

    public String authenticateUser(UserLoginDTO body) {
        User user = userService.findUserByEmail(body.email());
        if (bcrypt.matches(body.password(), user.getPassword())) {
            return jwtTools.createToken(user);

        } else {
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    public User saveUser(NewUserDTO body) throws IOException {

        userRepository.findByEmail(body.email()).ifPresent(user -> {
            throw new BadRequestException("The email " + user.getEmail() + " has already been used!");
        });

        userRepository.findByUsername(body.username()).ifPresent(user -> {
            throw new BadRequestException("The username " + user.getUsername() + " has already been used!");
        });

        User newUser = new User();
        newUser.setAvatar("https://ui-avatars.com/api/?name=" + body.username());
        newUser.setUsername(body.username());
        newUser.setName(body.name());
        newUser.setSurname(body.surname());
        newUser.setRole(UserRole.USER);
        newUser.setEmail(body.email());
        newUser.setPassword(bcrypt.encode(body.password()));
        User savedUser = userRepository.save(newUser);
        emailSender.sendRegistrationEmail(body.email());

        return savedUser;
    }
}

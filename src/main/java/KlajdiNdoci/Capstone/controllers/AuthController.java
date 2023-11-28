package KlajdiNdoci.Capstone.controllers;

import KlajdiNdoci.Capstone.entities.User;
import KlajdiNdoci.Capstone.exceptions.BadRequestException;
import KlajdiNdoci.Capstone.payloads.NewUserDTO;
import KlajdiNdoci.Capstone.payloads.UserLoginDTO;
import KlajdiNdoci.Capstone.payloads.UserLoginSuccessDTO;
import KlajdiNdoci.Capstone.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User saveUser(@RequestBody @Validated NewUserDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            try {
                return authService.saveUser(body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @PostMapping("/login")
    public UserLoginSuccessDTO login(@RequestBody UserLoginDTO body) {
        return new UserLoginSuccessDTO(authService.authenticateUser(body));
    }
}

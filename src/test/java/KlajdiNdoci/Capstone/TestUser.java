package KlajdiNdoci.Capstone;

import KlajdiNdoci.Capstone.controllers.AuthController;
import KlajdiNdoci.Capstone.entities.User;
import KlajdiNdoci.Capstone.payloads.NewUserDTO;
import KlajdiNdoci.Capstone.payloads.UserLoginDTO;
import KlajdiNdoci.Capstone.services.AuthService;
import KlajdiNdoci.Capstone.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {AuthController.class})
public class TestUser {
    NewUserDTO newUser;
    UserLoginDTO userLoginDTO;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void init() {
        newUser = new NewUserDTO("username", "nome", "cognome", "email@email.com", "ddfmoikmfdDFFd5445)");
        userLoginDTO = new UserLoginDTO("dssdsdsdds@ds.com", "dsdsffdffddf");
    }


    @Test
    public void createUser() throws Exception {
        given(authService.saveUser(ArgumentMatchers.any())).willReturn(new User());

        ResultActions resp = mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newUser)));

        resp.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void loginUser() throws Exception {
        given(authService.authenticateUser(ArgumentMatchers.any())).willReturn("");

        ResultActions resp = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userLoginDTO)));

        resp.andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
//    public void getAllUsers() throws Exception {
//        List<User> users = Arrays.asList(new User(), new User());
//        Page<User> userPage = new PageImpl<>(users);
//
//        given(userService.getUsers(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).willReturn(userPage);
//
//        ResultActions resp = mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(users)));
//        resp.andExpect(MockMvcResultMatchers.status().isOk());
//    }
}

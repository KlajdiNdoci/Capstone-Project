package KlajdiNdoci.Capstone;

import KlajdiNdoci.Capstone.controllers.AuthController;
import KlajdiNdoci.Capstone.controllers.UserController;
import KlajdiNdoci.Capstone.entities.User;
import KlajdiNdoci.Capstone.enums.UserRole;
import KlajdiNdoci.Capstone.payloads.NewUserDTO;
import KlajdiNdoci.Capstone.payloads.UserLoginDTO;
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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {UserController.class})
public class TestUser {
    NewUserDTO newUser;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        newUser = new NewUserDTO("username", "nome", "cognome", "email@email.com", "ddfmoikmfdDFFd5445)");
    }
        @Test
        public void getAllUsers() throws Exception {
        List<User> users = Arrays.asList(new User(), new User());
        Page<User> userPage = new PageImpl<>(users);

        given(userService.getUsers(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).willReturn(userPage);

        ResultActions resp = mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(users)));
        resp.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteUser() throws Exception {
        UUID uuid = UUID.randomUUID();
        ResultActions resp = mockMvc.perform(delete("/users/"+uuid).contentType(MediaType.APPLICATION_JSON).content(""));
        resp.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void getById() throws Exception {
            UUID uuid = UUID.randomUUID();
            given(userService.findUserById(ArgumentMatchers.eq(uuid))).willReturn(new User());
            ResultActions resp = mockMvc.perform((get("/users/"+ uuid).contentType(MediaType.APPLICATION_JSON)).content(objectMapper.writeValueAsString(new User())));
            resp.andExpect(MockMvcResultMatchers.status().isOk());
    }
}

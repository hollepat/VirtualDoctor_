package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.UserDTO;
import cvut.fel.virtualdoctor.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController implements IUserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new-user")
    public void createUser(@RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
    }
}

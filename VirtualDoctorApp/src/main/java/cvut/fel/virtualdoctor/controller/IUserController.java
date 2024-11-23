package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.UserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUserController {

    @PostMapping("/new-user")
    void createUser(@RequestBody UserDTO userDTO);
}
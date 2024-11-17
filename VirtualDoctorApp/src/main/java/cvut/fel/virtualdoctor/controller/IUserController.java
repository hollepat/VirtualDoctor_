package cvut.fel.virtualdoctor.controller;

import cvut.fel.virtualdoctor.dto.UserMetaDataDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public interface IUserController {

    @PostMapping("/register")
    void registerUser();

    @PostMapping("/login")
    void loginUser();

    @GetMapping("/get-user-metadata")
    UserMetaDataDTO getUserMetadata();
}
package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.dto.UserDTO;
import cvut.fel.virtualdoctor.model.User;
import cvut.fel.virtualdoctor.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    UserRepository userRepository;

    public void createUser(UserDTO userDTO) {
        logger.info("Creating new user...");
        User user = new User(
                userDTO.username(),
                userDTO.age(),
                userDTO.height(),
                userDTO.weight(),
                userDTO.gender(),
                userDTO.location(),
                userDTO.lifestyle()
        );
        userRepository.save(user);
    }

    public User getUser(String username) {
        logger.info("Getting user...");
        return userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found")
        );
    }
}

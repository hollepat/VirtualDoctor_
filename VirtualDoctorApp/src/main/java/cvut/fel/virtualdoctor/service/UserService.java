package cvut.fel.virtualdoctor.service;

import cvut.fel.virtualdoctor.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void createUser(UserDTO userDTO) {
        logger.info("Creating new user...");
    }
}

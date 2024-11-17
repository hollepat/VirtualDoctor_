package cvut.fel.virtualdoctor.repository;

import cvut.fel.virtualdoctor.model.UserInput;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserInputRepository extends MongoRepository<UserInput, String> {
}

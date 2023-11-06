package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.entity.User;
import project.mapper.UserMapper;
import project.model.userModel.UserResponse;
import project.repository.UserRepository;
import project.service.UserService;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public User saveUser(User user) {
        logger.info("saveUser() - Saving user");
        User user1 = userRepository.save(user);
        logger.info("saveUser() - User was saved");
        return user1;
    }

    @Override
    public UserResponse getUserResponseByEmail(String email) {
        logger.info("getUserResponseByEmail() - Finding user for user response by email "+email);
        User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        UserResponse userResponse = UserMapper.userToUserResponse(user);
        logger.info("getUserResponseByEmail() - User for user response was found");
        return userResponse;
    }
}

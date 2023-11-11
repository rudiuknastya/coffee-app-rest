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

    @Override
    public User getUserById(Long id) {
        logger.info("getUserById() - Finding user by id "+id);
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getUserById() - User was found");
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        logger.info("getUserByEmail() - Finding user by email "+email);
        User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        logger.info("getUserByEmail() - User was found");
        return user;
    }

    @Override
    public User getUserWithShoppingCartByEmail(String email) {
        logger.info("getUserWithShoppingCartByEmail() - Finding user with shopping cart by email "+email);
        User user = userRepository.findWithShoppingCartByEmail(email);
        logger.info("getUserWithShoppingCartByEmail() - User was found");
        return user;
    }

    @Override
    public User getUserWithPasswordResetTokenByEmail(String email) {
        logger.info("getUserWithPasswordResetTokenByEmail() - Finding user with password reset token by email "+email);
        User user = userRepository.findWithPasswordResetTokenByEmail(email);
        logger.info("getUserWithPasswordResetTokenByEmail() - User was found");
        return user;
    }
}

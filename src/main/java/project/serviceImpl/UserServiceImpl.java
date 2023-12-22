package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.entity.User;
import project.mapper.UserMapper;
import project.model.userModel.UserProfileRequest;
import project.model.userModel.UserResponse;
import project.repository.UserRepository;
import project.service.UserService;
@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LogManager.getLogger("serviceLogger");
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new EntityNotFoundException("User not found by email "+email));
        UserResponse userResponse = UserMapper.USER_MAPPER.userToUserResponse(user);
        logger.info("getUserResponseByEmail() - User for user response was found");
        return userResponse;
    }

    @Override
    public User getUserById(Long id) {
        logger.info("getUserById() - Finding user by id "+id);
        User user = userRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("User not found by id "+id));
        logger.info("getUserById() - User was found");
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
        User user = userRepository.findWithPasswordResetTokenByEmail(email).orElseThrow(EntityNotFoundException::new);
        logger.info("getUserWithPasswordResetTokenByEmail() - User was found");
        return user;
    }

    @Override
    public void updateUser(UserProfileRequest userProfileRequest) {
        logger.info("updateUser() - Updating user");
        User user = userRepository.findById(userProfileRequest.getId()).orElseThrow(()->new EntityNotFoundException("User not found by id "+userProfileRequest.getId()));
        UserMapper.USER_MAPPER.setUserRequest(user,userProfileRequest);
        if(!userProfileRequest.getOldPassword().equals("") && !userProfileRequest.getNewPassword().equals("") && !userProfileRequest.getConfirmNewPassword().equals("")){
            user.setPassword(passwordEncoder.encode(userProfileRequest.getNewPassword()));
        }
        userRepository.save(user);
        logger.info("updateUser() - User was updated");
    }
}

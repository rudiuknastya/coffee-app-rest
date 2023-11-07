package project.service;

import project.entity.User;
import project.model.userModel.UserResponse;

public interface UserService {
    User saveUser(User user);
    UserResponse getUserResponseByEmail(String email);
    User getUserById(Long id);

}

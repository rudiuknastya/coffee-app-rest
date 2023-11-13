package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import project.entity.Language;
import project.entity.Role;
import project.entity.User;
import project.entity.UserStatus;
import project.model.userModel.UserRequest;
import project.model.userModel.UserResponse;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Named("userRequestToUser")
    static User userRequestToUser(UserRequest userRequest){
        if(userRequest == null){
            return null;
        }
        User user = new User();
        user.setName(userRequest.getName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEmail(userRequest.getEmail());
        user.setRole(Role.USER);
        user.setStatus(UserStatus.NEW);
        user.setLanguage(Language.UKR);
        user.setRegistrationDate(LocalDate.now());
        return user;
    }
    static UserResponse userToUserResponse(User user){
        if(user == null){
            return null;
        }
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setBirthDate(user.getBirthDate());
        userResponse.setLanguage(user.getLanguage().getLanguageName());
        return userResponse;
    }
}

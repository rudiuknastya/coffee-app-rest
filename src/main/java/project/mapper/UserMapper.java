package project.mapper;

import org.mapstruct.Mapper;
import project.entity.User;
import project.model.userModel.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
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

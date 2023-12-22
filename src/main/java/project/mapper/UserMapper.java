package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.entity.Language;
import project.entity.Role;
import project.entity.User;
import project.entity.UserStatus;
import project.model.userModel.LanguageResponse;
import project.model.userModel.UserProfileRequest;
import project.model.userModel.UserRequest;
import project.model.userModel.UserResponse;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);
    @Mapping(target = "registrationDate", expression = "java(getDateNow())")
    @Mapping(target = "deleted", expression = "java(false)")
    @Mapping(target = "role", source = "userRole")
    @Mapping(target = "status", source = "userStatus")
    @Mapping(target = "language", source = "userLanguage")
    User userRequestToUser(UserRequest userRequest, Language userLanguage, UserStatus userStatus, Role userRole);
    default LocalDate getDateNow(){
        return LocalDate.now();
    }
    @Mapping(ignore = true, target = "id")
    void setUserRequest(@MappingTarget User user, UserProfileRequest userProfileRequest);
    @Mapping(target = "language", expression = "java(createLanguageResponse(user))")
    UserResponse userToUserResponse(User user);
    default LanguageResponse createLanguageResponse(User user) {
        LanguageResponse languageResponse = new LanguageResponse();
        languageResponse.setLanguageName(user.getLanguage().getLanguageName());
        languageResponse.setLanguage(user.getLanguage());
        return languageResponse;
    }
}

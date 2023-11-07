package project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import project.entity.Language;
import project.entity.User;
import project.model.userModel.UserProfileRequest;
import project.model.userModel.UserResponse;
import project.service.UserService;

@Tag(name = "User profile")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Operation(summary = "Get user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/profile")
    ResponseEntity<UserResponse> getUserProfile(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        UserResponse userResponse = userService.getUserResponseByEmail(email);
        if(userResponse != null){
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Get languages for select")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/languages")
    String[] getLanguages(){
        Language[] languages = Language.values();
        String[] languageNames = new String[languages.length];
        for(int i = 0; i < languages.length; i++){
            languageNames[i] = languages[i].getLanguageName();
        }
        return languageNames;
    }
    @Operation(summary = "Update profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Failed validation")})
    @PutMapping("/profile")
    ResponseEntity<?> updateUserProfile(@Valid @RequestBody UserProfileRequest userProfileRequest){
        User user = userService.getUserById(userProfileRequest.getId());
        user.setName(userProfileRequest.getName());
        user.setPhoneNumber(userProfileRequest.getPhoneNumber());
        user.setEmail(userProfileRequest.getEmail());
        if(userProfileRequest.getBirthDate() !=null){
            user.setBirthDate(userProfileRequest.getBirthDate());
        }
        user.setLanguage(Language.fromString(userProfileRequest.getLanguage()));
        if(!userProfileRequest.getOldPassword().equals("") && !userProfileRequest.getNewPassword().equals("") && !userProfileRequest.getConfirmNewPassword().equals("")){
            user.setPassword(bCryptPasswordEncoder.encode(userProfileRequest.getNewPassword()));
        }
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

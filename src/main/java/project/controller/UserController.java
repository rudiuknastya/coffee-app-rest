package project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.entity.Language;
import project.model.userModel.LanguageResponse;
import project.model.userModel.UserProfileRequest;
import project.model.userModel.UserResponse;
import project.service.UserService;

@Tag(name = "Profile")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping( "/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get user profile",description = "Getting user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @GetMapping("/profile")
    ResponseEntity<UserResponse> getUserProfile(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        UserResponse userResponse = userService.getUserResponseByEmail(email);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
    @Operation(summary = "Get languages",description = "Get languages for select")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = LanguageResponse.class))}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @GetMapping("/languages")
    LanguageResponse[] getLanguages(){
        Language[] languages = Language.values();
        LanguageResponse[] languageResponses = new LanguageResponse[languages.length];
        for(int i = 0; i < languages.length; i++){
            LanguageResponse languageResponse = new LanguageResponse();
            languageResponse.setLanguageName(languages[i].getLanguageName());
            languageResponse.setLanguage(languages[i]);
            languageResponses[i] = languageResponse;
        }
        return languageResponses;
    }
    @Operation(summary = "Update profile",description = "Updating user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "User not found",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Failed validation",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @PutMapping("/profile")
    ResponseEntity<?> updateUserProfile(@Valid @RequestBody UserProfileRequest userProfileRequest){
        userService.updateUser(userProfileRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

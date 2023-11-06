package project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.entity.Language;
import project.model.userModel.UserResponse;
import project.service.UserService;


@Tag(name = "User profile")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
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
}

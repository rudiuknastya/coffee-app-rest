package project.controller;

import com.sendgrid.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import project.entity.PasswordResetToken;
import project.entity.User;
import project.model.additiveModel.AdditiveDTO;
import project.model.authenticationModel.*;
import project.model.userModel.UserRequest;
import project.service.AuthenticationService;
import project.service.MailService;
import project.service.PasswordResetTokenService;
import project.service.UserService;

import java.util.UUID;
@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final UserService userService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationService authenticationService, PasswordResetTokenService passwordResetTokenService, UserService userService, MailService mailService, PasswordEncoder passwordEncoder) {
        this.authenticationService = authenticationService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.userService = userService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Register user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Failed validation",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @PostMapping("/register")
    ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody UserRequest userRequest){
        return new ResponseEntity<>(authenticationService.register(userRequest),HttpStatus.CREATED);
    }
    @Operation(summary = "Authenticate user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Wrong email or password",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Failed validation",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
    @Operation(summary = "Get new access token by refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found user by refresh token",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Wrong refresh token",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @PostMapping("/refreshToken")
    ResponseEntity<?> refreshToken(@RequestBody RefreshToken refreshToken){
        if(refreshToken.getRefreshToken().equals("")){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(refreshToken);
        if (authenticationResponse != null) {
            return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Sending email to user to change password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "User with such email not found",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Failed validation",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @PostMapping("/forgotPassword")
    ResponseEntity<?> forgotPassword(HttpServletRequest httpRequest,@Valid @RequestBody EmailRequest emailRequest){
        User user = userService.getUserWithPasswordResetTokenByEmail(emailRequest.getEmail());
        String token = UUID.randomUUID().toString();
        if(user.getPasswordResetToken() != null){
            user.getPasswordResetToken().setToken(token);
            user.getPasswordResetToken().setExpirationDate();
            userService.saveUser(user);
        } else {
            PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
            passwordResetTokenService.savePasswordResetToken(passwordResetToken);
        }
        return new ResponseEntity<>(mailService.sendToken(token,emailRequest.getEmail(),httpRequest),HttpStatus.OK);
    }
    @Operation(summary = "Change password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Failed password validation",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "Password reset token expired",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @PostMapping("/changePassword")
    ResponseEntity<?> changePassword( @RequestParam("token") String token,@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        if(passwordResetTokenService.validatePasswordResetToken(token)){
            String encodedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
            PasswordResetToken passwordResetToken = passwordResetTokenService.getPasswordResetToken(token);
            passwordResetToken.getUser().setPassword(encodedPassword);
            passwordResetTokenService.savePasswordResetToken(passwordResetToken);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}

package project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import project.entity.PasswordResetToken;
import project.entity.User;
import project.model.authenticationModel.*;
import project.model.userModel.UserRequest;
import project.service.AuthenticationService;
import project.service.MailService;
import project.service.PasswordResetTokenService;
import project.service.UserService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Tag(name = "Security")
@RestController
public class SecurityController {
    private final AuthenticationService authenticationService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final UserService userService;
    private final MailService mailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityController(AuthenticationService authenticationService, PasswordResetTokenService passwordResetTokenService, UserService userService, MailService mailService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationService = authenticationService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.userService = userService;
        this.mailService = mailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Operation(summary = "Register user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Failed validation")})
    @PostMapping("/register")
    ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody UserRequest userRequest){
        return new ResponseEntity<>(authenticationService.register(userRequest),HttpStatus.CREATED);
    }
    @Operation(summary = "Authenticate user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Failed validation")})
    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
    @Operation(summary = "Get new access token by refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Failed validation")})
    @PostMapping("/refreshToken")
    ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshToken refreshToken){
        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(refreshToken);
        if (authenticationResponse != null) {
            return new ResponseEntity<>(authenticationService.refreshToken(refreshToken), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Sending email to user to change password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "User with such email not found"),
            @ApiResponse(responseCode = "400", description = "Failed validation")})
    @PostMapping("/forgotPassword")
    ResponseEntity<?> forgotPassword(@Valid @RequestBody EmailRequest emailRequest){
        User user = userService.getUserWithPasswordResetTokenByEmail(emailRequest.getEmail());
        String token = UUID.randomUUID().toString();
        System.out.println(token);
        if(user.getPasswordResetToken() != null){
            user.getPasswordResetToken().setToken(token);
            user.getPasswordResetToken().setExpirationDate();
            userService.saveUser(user);
        } else {
            PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
            passwordResetTokenService.savePasswordResetToken(passwordResetToken);
        }
        return new ResponseEntity<>(mailService.sendToken(token,emailRequest.getEmail()),HttpStatus.OK);
    }
    @Operation(summary = "Change password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Failed password validation"),
            @ApiResponse(responseCode = "404", description = "Token expired")})
    @PostMapping("/changePassword")
    ResponseEntity<?> changePassword( @RequestParam("token") String token,@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        if(passwordResetTokenService.validatePasswordResetToken(token)){
            String encodedPassword = bCryptPasswordEncoder.encode(changePasswordRequest.getNewPassword());
            PasswordResetToken passwordResetToken = passwordResetTokenService.getPasswordResetToken(token);
            passwordResetToken.getUser().setPassword(encodedPassword);
            passwordResetTokenService.savePasswordResetToken(passwordResetToken);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

package project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import project.entity.User;
import project.model.authenticationModel.AuthenticationRequest;
import project.model.authenticationModel.AuthenticationResponse;
import project.model.authenticationModel.RefreshToken;
import project.model.userModel.UserRequest;
import project.service.AuthenticationService;
import project.service.UserService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SecurityController {
    private final AuthenticationService authenticationService;

    public SecurityController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @Operation(summary = "Register user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Failed validation")})
    @PostMapping("/register")
    ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(authenticationService.register(userRequest));
    }
    @Operation(summary = "Authenticate user")
    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
    @PostMapping("/refreshToken")
    ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshToken refreshToken){
        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(refreshToken);
        if (authenticationResponse != null) {
            return new ResponseEntity<>(authenticationService.refreshToken(refreshToken), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

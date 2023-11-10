package project.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.model.authenticationModel.AuthenticationRequest;
import project.model.authenticationModel.AuthenticationResponse;
import project.model.authenticationModel.RefreshToken;
import project.model.userModel.UserRequest;

public interface AuthenticationService {
    AuthenticationResponse register(UserRequest userRequest);
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
    AuthenticationResponse refreshToken(RefreshToken refreshToken);
}

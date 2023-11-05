package project.service;

import project.model.authenticationModel.AuthenticationRequest;
import project.model.authenticationModel.AuthenticationResponse;
import project.model.userModel.UserRequest;

public interface AuthenticationService {
    AuthenticationResponse register(UserRequest userRequest);
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}

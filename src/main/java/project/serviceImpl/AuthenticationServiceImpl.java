package project.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.entity.*;
import project.model.authenticationModel.AuthenticationRequest;
import project.model.authenticationModel.AuthenticationResponse;
import project.model.authenticationModel.RefreshToken;
import project.model.userModel.UserRequest;
import project.repository.ProductRepository;
import project.repository.UserRepository;
import project.service.AuthenticationService;
import project.service.JwtService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ProductRepository productRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.productRepository = productRepository;
    }

    @Override
    public AuthenticationResponse register(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEmail(userRequest.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(UserStatus.NEW);
        user.setLanguage(Language.UKR);
        user.setRegistrationDate(LocalDate.now());
        List<Product> products = productRepository.findProductsForAward();
        List<Product> userProducts = new ArrayList<>(1);
        userProducts.add(products.get(0));
        user.setProducts(userProducts);
        userRepository.save(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtService.generateAccessToken(user));
        authenticationResponse.setRefreshToken(jwtService.generateRefreshToken(user));
        return authenticationResponse;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),authenticationRequest.getPassword()
                ));
        User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(EntityNotFoundException::new);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtService.generateAccessToken(user));
        authenticationResponse.setRefreshToken(jwtService.generateRefreshToken(user));
        return authenticationResponse;
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshToken refreshToken) {
//        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        String refreshToken;
//        String email;
//        if(authHeader == null || !authHeader.startsWith("Bearer ")){
//            return;
//        }
//        refreshToken = authHeader.substring(7);
        String email = jwtService.extractUserEmail(refreshToken.getRefreshToken());
        if(email != null){
            User user = userRepository.findByEmail(email).orElseThrow();
            if(jwtService.isTokenValid(refreshToken.getRefreshToken(), user)){
                AuthenticationResponse authenticationResponse = new AuthenticationResponse();
                authenticationResponse.setAccessToken(jwtService.generateAccessToken(user));
                authenticationResponse.setRefreshToken(refreshToken.getRefreshToken());
                return authenticationResponse;
            }
        }
        return null;
    }
}

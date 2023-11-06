package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.entity.*;
import project.model.authenticationModel.AuthenticationRequest;
import project.model.authenticationModel.AuthenticationResponse;
import project.model.userModel.UserRequest;
import project.repository.ProductRepository;
import project.repository.UserRepository;
import project.service.AuthenticationService;
import project.service.JwtService;

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
        authenticationResponse.setToken(jwtService.generateToken(user));
        return authenticationResponse;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),authenticationRequest.getPassword()
                ));
        User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(EntityNotFoundException::new);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtService.generateToken(user));
        return authenticationResponse;
    }
}

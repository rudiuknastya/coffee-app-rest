package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.entity.*;
import project.mapper.UserMapper;
import project.model.authenticationModel.AuthenticationRequest;
import project.model.authenticationModel.AuthenticationResponse;
import project.model.authenticationModel.RefreshToken;
import project.model.userModel.UserRequest;
import project.repository.ProductRepository;
import project.repository.UserRepository;
import project.service.AuthenticationService;
import project.service.JwtService;
import project.service.ShoppingCartService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ProductRepository productRepository;
    private final ShoppingCartService shoppingCartService;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, ProductRepository productRepository, ShoppingCartService shoppingCartService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.productRepository = productRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public AuthenticationResponse register(UserRequest userRequest) {
        User user = UserMapper.USER_MAPPER.userRequestToUser(userRequest,Language.UKR,UserStatus.NEW,Role.USER);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        List<Product> products = productRepository.findProductsForAward();
        List<Product> userProducts = new ArrayList<>(1);
        userProducts.add(products.get(0));
        user.setProducts(userProducts);
        User savedUser = userRepository.save(user);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setPrice(BigDecimal.valueOf(0));
        shoppingCart.setUser(savedUser);
        shoppingCartService.saveShoppingCart(shoppingCart);
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
        String email = jwtService.extractUserEmail(refreshToken.getRefreshToken());
        if(email != null){
            User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
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

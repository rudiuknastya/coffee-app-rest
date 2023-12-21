package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.entity.PasswordResetToken;
import project.entity.User;
import project.model.authenticationModel.ChangePasswordRequest;
import project.model.authenticationModel.EmailRequest;
import project.repository.PasswordResetTokenRepository;
import project.repository.UserRepository;
import project.service.PasswordResetTokenService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private Logger logger = LogManager.getLogger("serviceLogger");
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetTokenServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        logger.info("validatePasswordResetToken() - Finding password reset token and validating it");
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(token);
        boolean isValid = passwordResetToken.isPresent() && !passwordResetToken.get().getExpirationDate().isBefore(LocalDateTime.now());
        logger.info("validatePasswordResetToken() - Password reset token was found and validated");
        return isValid;
    }


    @Override
    public void updatePassword(ChangePasswordRequest changePasswordRequest, String token) {
        logger.info("updatePassword() - Updating password");
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(()-> new EntityNotFoundException("Password reset token not found"));
        passwordResetToken.getUser().setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        passwordResetTokenRepository.save(passwordResetToken);
        logger.info("updatePassword() - Password was updated");
    }

    @Override
    public String createOrUpdatePasswordResetToken(EmailRequest emailRequest) {
        User user = userRepository.findWithPasswordResetTokenByEmail(emailRequest.getEmail()).orElseThrow(()-> new EntityNotFoundException("User was not found by email "+emailRequest.getEmail()));
        String token = UUID.randomUUID().toString();
        if(user.getPasswordResetToken() != null){
            user.getPasswordResetToken().setToken(token);
            user.getPasswordResetToken().setExpirationDate();
            userRepository.save(user);
        } else {
            PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
            passwordResetTokenRepository.save(passwordResetToken);
        }
        return token;
    }
}

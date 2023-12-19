package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.entity.PasswordResetToken;
import project.repository.PasswordResetTokenRepository;
import project.service.PasswordResetTokenService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetTokenServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public PasswordResetToken savePasswordResetToken(PasswordResetToken passwordResetToken) {
        logger.info("savePasswordResetToken() - Saving password reset token");
        PasswordResetToken passwordResetToken1 = passwordResetTokenRepository.save(passwordResetToken);
        logger.info("savePasswordResetToken() - Password reset token was saved");
        return passwordResetToken1;
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
    public PasswordResetToken getPasswordResetToken(String token) {
        logger.info("getPasswordResetToken() - Finding password reset token");
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(()-> new EntityNotFoundException("Password reset token not found"));
        logger.info("getPasswordResetToken() - Password reset token was found");
        return passwordResetToken;
    }

}

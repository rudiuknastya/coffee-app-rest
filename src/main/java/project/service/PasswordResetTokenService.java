package project.service;

import project.entity.PasswordResetToken;
import project.model.authenticationModel.ChangePasswordRequest;
import project.model.authenticationModel.EmailRequest;

public interface PasswordResetTokenService {
    boolean validatePasswordResetToken(String token);
    void updatePassword(ChangePasswordRequest changePasswordRequest, String token);
    String createOrUpdatePasswordResetToken(EmailRequest emailRequest);
}

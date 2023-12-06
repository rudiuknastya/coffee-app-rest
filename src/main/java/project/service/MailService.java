package project.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.concurrent.CompletableFuture;

public interface MailService {
    CompletableFuture<String> sendToken(String token, String to, HttpServletRequest httpRequest);
}

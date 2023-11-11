package project.service;

import java.util.concurrent.CompletableFuture;

public interface MailService {
    CompletableFuture<String> sendToken(String token, String to);
}

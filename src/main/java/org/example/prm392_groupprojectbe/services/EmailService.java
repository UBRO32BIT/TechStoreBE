package org.example.prm392_groupprojectbe.services;

import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public interface EmailService {
    @Async("emailExecutor")
    void sendEmail(String to, String subject, String templateName, Map<String, Object> variables);

    void sendVerifyAccountEmail(String email, String username, String otp);

    void sendForgotPasswordEmail(String email, String username, String url);
}

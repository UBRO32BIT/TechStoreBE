package org.example.prm392_groupprojectbe.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.prm392_groupprojectbe.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Override
    @Async("emailExecutor")
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(templateName, context);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom("32MINE SUPPORT <mduct4k1061203@gmail.com>");

            mailSender.send(message);
        }
        catch (MessagingException e) {
            logger.error("Failed to send email: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendVerifyAccountEmail(String email, String username, String otp) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("otp", otp);
        sendEmail(email, "[32MINE] Verify your account", "verify-account", variables);
    }

    @Override
    public void sendForgotPasswordEmail(String email, String username, String url) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("url", url);
        sendEmail(email, "[32MINE] Reset your password", "forgot-password", variables);
    }
}

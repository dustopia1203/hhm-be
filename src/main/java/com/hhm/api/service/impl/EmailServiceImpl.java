package com.hhm.api.service.impl;

import com.hhm.api.service.EmailService;
import com.hhm.api.support.constants.Constants;
import com.hhm.api.support.enums.error.InternalServerError;
import com.hhm.api.support.exception.ResponseException;
import com.hhm.api.support.util.IdUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Value("${app.active-account-url}")
    private String activationUrl;

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    @Override
    public void sendActivationAccountEmail(String to, UUID userId, String username, String activationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

            helper.setFrom("noreply@hhmshop.com");
            helper.setTo(to);
            helper.setSubject("Activation mail");

            Map<String, Object> properties = new HashMap<>();

            properties.put("username", username);
            properties.put("activationCode", activationCode);
            properties.put("activationUrl", activationUrl + "/" + IdUtils.convertUUIDToString(userId));

            Context context = new Context();
            context.setVariables(properties);

            String template = templateEngine.process(Constants.Template.ACTIVE_ACCOUNT_EMAIL_TEMPLATE, context);

            helper.setText(template, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new ResponseException(InternalServerError.INTERNAL_SERVER_ERROR);
        }
    }
}

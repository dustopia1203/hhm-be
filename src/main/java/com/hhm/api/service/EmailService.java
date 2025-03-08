package com.hhm.api.service;

import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;

import java.util.UUID;

public interface EmailService {
    void sendActivationAccountEmail(String to, UUID userId, String username, String activationCode);
}

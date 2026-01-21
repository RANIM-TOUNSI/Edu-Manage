package com.gestion.backend.services.impl;

import com.gestion.backend.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    // Commented out to avoid build failures if mail properties aren't set
    // private final JavaMailSender mailSender;

    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        log.info("Simulating email send to: {}", to);
        log.info("Subject: {}", subject);
        log.info("Content: {}", text);

        // Actual implementation would be:
        /*
         * SimpleMailMessage message = new SimpleMailMessage();
         * message.setTo(to);
         * message.setSubject(subject);
         * message.setText(text);
         * mailSender.send(message);
         */
    }
}

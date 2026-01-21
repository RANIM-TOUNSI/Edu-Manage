package com.gestion.backend.services;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
}

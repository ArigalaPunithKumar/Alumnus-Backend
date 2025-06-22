package org.alumni.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendPasswordResetEmail(String to, String name, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@alumniconnect.com"); // Can be configured
        message.setTo(to);
        message.setSubject("Alumni Connect - Password Reset Request");
        message.setText(
                "Hello " + name + ",\n\n" +
                        "You have requested to reset your password. Please click the link below to proceed:\n" +
                        resetLink + "\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "Best regards,\nThe Alumni Connect Team"
        );
        emailSender.send(message);
    }
}
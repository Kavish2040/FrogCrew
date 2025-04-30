package com.frogcrew.frogcrew.service.impl;

import com.frogcrew.frogcrew.model.EmailQueue;
import com.frogcrew.frogcrew.model.EmailStatus;
import com.frogcrew.frogcrew.repository.EmailQueueRepository;
import com.frogcrew.frogcrew.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.ZonedDateTime;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final EmailQueueRepository emailQueueRepository;

    @Override
    @Transactional
    public void sendEmail(String to, String subject, String body) {
        EmailQueue email = EmailQueue.builder()
                .toEmail(to)
                .subject(subject)
                .body(body)
                .status(EmailStatus.PENDING)
                .retryCount(0)
                .createdAt(ZonedDateTime.now())
                .build();
        emailQueueRepository.save(email);
        log.info("Email queued to: {}", to);
    }

    @Override
    @Async
    @Scheduled(fixedDelay = 30000) // 30 seconds
    @Transactional
    public void processEmailQueue() {
        emailQueueRepository.findBySentAtIsNullOrderByCreatedAtAsc()
                .forEach(email -> {
                    try {
                        SimpleMailMessage message = new SimpleMailMessage();
                        message.setTo(email.getToEmail());
                        message.setSubject(email.getSubject());
                        message.setText(email.getBody());
                        mailSender.send(message);

                        email.setSentAt(ZonedDateTime.now());
                        email.setStatus(EmailStatus.SENT);
                        emailQueueRepository.save(email);
                        log.info("Email sent to: {}", email.getToEmail());
                    } catch (Exception e) {
                        log.error("Error sending email to {}: {}", email.getToEmail(), e.getMessage());
                        email.setStatus(EmailStatus.FAILED);
                        email.setErrorMessage(e.getMessage());
                        email.setRetryCount(email.getRetryCount() + 1);
                        emailQueueRepository.save(email);
                    }
                });
        log.info("Processing email queue complete");
    }

    @Override
    public void sendEmail(String[] to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        log.info("Email sent to multiple recipients");
    }

    @Override
    public void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            File file = new File(attachmentPath);
            helper.addAttachment(file.getName(), file);

            mailSender.send(message);
            log.info("Email with attachment sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Error sending email with attachment", e);
            throw new RuntimeException("Failed to send email with attachment", e);
        }
    }
}
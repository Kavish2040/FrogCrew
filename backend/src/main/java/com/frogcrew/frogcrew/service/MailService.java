package com.frogcrew.frogcrew.service;

import com.frogcrew.frogcrew.model.EmailQueue;

public interface MailService {
    void sendEmail(String to, String subject, String body);

    void sendEmail(String[] to, String subject, String body);

    void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath);

    void processEmailQueue();
}
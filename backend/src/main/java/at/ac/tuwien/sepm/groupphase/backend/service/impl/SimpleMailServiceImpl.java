package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.service.SimpleMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class SimpleMailServiceImpl implements SimpleMailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final JavaMailSender javaMailSender;

    @Autowired
    public SimpleMailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    @Override
    public void sendMail(String recipient, String subject, String message) {
        LOGGER.info("Sending Mail");
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(recipient);
        msg.setSubject(subject);
        msg.setText(message);

        javaMailSender.send(msg);
    }
}

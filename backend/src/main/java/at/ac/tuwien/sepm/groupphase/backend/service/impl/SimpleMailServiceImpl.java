package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.exception.MailException;
import at.ac.tuwien.sepm.groupphase.backend.service.SimpleMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.lang.invoke.MethodHandles;
import java.util.List;

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
    public void sendMail(String recipient, String subject, String message, List<File> attachments) {
        LOGGER.info("Sending Mail");
        MimeMessage msg = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(message, true);

            if (attachments != null) {
                for (int i = 0; i < attachments.size(); i++) {
                    File emailAttachment = attachments.get(i);
                    helper.addAttachment(String.format("Attachment%d.%s", i, emailAttachment.getType().getFileExtension()), new ByteArrayResource(emailAttachment.getData()));
                }
            }

            javaMailSender.send(msg);
        } catch (MessagingException exception) {
            throw new MailException("Sending mail failed: " + exception.getMessage());
        }
    }
}

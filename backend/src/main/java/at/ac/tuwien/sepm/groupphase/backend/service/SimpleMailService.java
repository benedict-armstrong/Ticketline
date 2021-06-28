package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.File;

import java.util.List;

public interface SimpleMailService {

    /**
     * Send a Mail from "sepm.ticketline@gmail.com" with a custom subject and message
     *
     * @param recipient Email address of recipient
     * @param subject Subject line of the email
     * @param message Message of the email as a String
     * @param attachments files to attach to the mail
     */
    void sendMail(String recipient, String subject, String message, List<File> attachments);

}

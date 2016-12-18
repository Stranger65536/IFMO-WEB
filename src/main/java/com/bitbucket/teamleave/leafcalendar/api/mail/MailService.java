package com.bitbucket.teamleave.leafcalendar.api.mail;

import com.bitbucket.teamleave.leafcalendar.api.model.UserModel;
import org.springframework.mail.MailSender;

/**
 * @author vladislav.trofimov@emc.com
 */
public interface MailService {
    void setMailSender(final MailSender mailSender);

    void sendVerificationMail(
            final String to,
            final String userId,
            final String validationToken);

    void sendVerificationMail(final UserModel userModel);
}
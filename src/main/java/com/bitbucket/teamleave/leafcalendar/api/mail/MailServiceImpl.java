package com.bitbucket.teamleave.leafcalendar.api.mail;

import com.bitbucket.teamleave.leafcalendar.api.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import static com.bitbucket.teamleave.leafcalendar.api.mail.MailConstants.VERIFICATION_SUBJECT;

/**
 * @author vladislav.trofimov@emc.com
 */
@Service
@SuppressWarnings({"DuplicateStringLiteralInspection", "FieldHasSetterButNoGetter"})
public class MailServiceImpl implements MailService {
    @Autowired
    @Qualifier("environment")
    Environment env;
    @Autowired
    private MailSender mailSender;

    @Override
    public void setMailSender(final MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationMail(
            final String to,
            final String userId,
            final String validationToken) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(env.getProperty("spring.mail.username"));
        message.setTo(to);
        message.setSubject(VERIFICATION_SUBJECT);
        message.setText("Please verify your registration id " + userId +
                " with code: " + validationToken);
        //todo html template for mail
        mailSender.send(message);
    }

    @Override
    public void sendVerificationMail(final UserModel userModel) {
        sendVerificationMail(userModel.getEmail(), userModel.getUserId(),
                userModel.getEmailValidationToken());
    }
}
package tech.nosy.nosyemail.nosyemail.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import tech.nosy.nosyemail.nosyemail.config.EmailConfigPopulationBean;
import tech.nosy.nosyemail.nosyemail.model.EmailFromProvider;
import tech.nosy.nosyemail.nosyemail.model.ReadyEmail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailSenderService {
    private Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

  @Value("${nosy.email.default.username}")
  private String emailDefaultUsername;

    @Value("${nosy.email.default.password}")
    private String emailDefaultPassword;


    private EmailConfigPopulationBean emailConfigPopulationBean;
    @Autowired
    public EmailSenderService(EmailConfigPopulationBean emailConfigPopulationBean){
        this.emailConfigPopulationBean=emailConfigPopulationBean;
    }
    public void send(ReadyEmail readyEmail, JavaMailSenderImpl javaMailSender){
        if (readyEmail.getEmailTemplate().getEmailTemplateFromProvider().equals(EmailFromProvider.DEFAULT) ||
                readyEmail.getEmailTemplate().getEmailTemplateFromAddress()==null ||
                readyEmail.getEmailProviderProperties()==null ||
                readyEmail.getEmailProviderProperties().getUsername()==null ||
                readyEmail.getEmailProviderProperties().getPassword()==null) {
            javaMailSender.setUsername(emailDefaultUsername);
            javaMailSender.setPassword(emailDefaultPassword);
            readyEmail.getEmailTemplate().setEmailTemplateFromAddress(emailDefaultUsername);
        }
        else{
            javaMailSender.setUsername(readyEmail.getEmailProviderProperties().getUsername());
            javaMailSender.setPassword(readyEmail.getEmailProviderProperties().getPassword());
        }
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = emailConfigPopulationBean.mimeMessageHelper(message);
        try {

            mimeMessageHelper.setFrom(readyEmail.getEmailTemplate().getEmailTemplateFromAddress());
            mimeMessageHelper.setSubject(readyEmail.getEmailTemplate().getEmailTemplateSubject());
            if(readyEmail.getEmailTemplate().getEmailTemplateFromProvider().equals(EmailFromProvider.DEFAULT)){
                mimeMessageHelper.setText(
                        readyEmail.getEmailTemplate().getEmailTemplateFromAddress()
                                + ":   "
                                + readyEmail.getEmailTemplate().getEmailTemplateText(),
                        true);

            }
            mimeMessageHelper.setText(readyEmail.getEmailTemplate().getEmailTemplateText(),
                    true);
            readyEmail
                    .getEmailTemplate()
                    .getEmailTemplateTo()
                    .forEach(
                            emailTo -> {
                                try {
                                    mimeMessageHelper.addTo(emailTo);
                                } catch (MessagingException e) {
                                    logger.error(e.getMessage());
                                }
                            });
                readyEmail
                        .getEmailTemplate()
                        .getEmailTemplateCc()
                        .forEach(
                                emailCc -> {

                                    try {
                                        mimeMessageHelper.addCc(emailCc);
                                    } catch (MessagingException e) {
                                        logger.error(e.getMessage());
                                    }

                                });
            javaMailSender.send(message);
        } catch (MessagingException messageException) {
            logger.error(messageException.getMessage());
        }
    }
}

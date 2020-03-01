package tech.nosy.nosyemail.nosyemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import tech.nosy.nosyemail.nosyemail.config.EmailConfigPopulationBean;
import tech.nosy.nosyemail.nosyemail.model.EmailFromProvider;
import tech.nosy.nosyemail.nosyemail.model.ReadyEmail;

@Service
public class EmailService {
  private EmailConfigPopulationBean emailConfigPopulationBean;
  private EmailSenderService emailSenderService;
  private JavaMailSenderImpl javaMailDefaultSender;
  private JavaMailSenderImpl javaMailGmailSender;
  private JavaMailSenderImpl javaMailYandexSender;

  @Autowired
  public EmailService(
          EmailSenderService emailSenderService,
          @Qualifier("Yandex") JavaMailSenderImpl javaMailYandexSender,
          @Qualifier("Default") JavaMailSenderImpl javaMailDefaultSender,
          @Qualifier("Gmail") JavaMailSenderImpl javaMailGmailSender,
          EmailConfigPopulationBean emailConfigPopulationBean) {
    this.emailSenderService = emailSenderService;
    this.emailConfigPopulationBean=emailConfigPopulationBean;
    this.javaMailYandexSender = javaMailYandexSender;
    this.javaMailDefaultSender = javaMailDefaultSender;
    this.javaMailGmailSender = javaMailGmailSender;
  }

  public void handleReadyEmail(ReadyEmail readyEmail) {
    EmailFromProvider emailProvider = readyEmail.getEmailTemplate().getEmailTemplateFromProvider();
    switch (emailProvider) {
      case YANDEX:
        emailSenderService.send(readyEmail, javaMailYandexSender);
        break;
      case GMAIL:
        emailSenderService.send(readyEmail, javaMailGmailSender);
        break;
      case CUSTOM:
        emailSenderService.send(readyEmail,
                emailConfigPopulationBean.javaMailCustomSender(readyEmail.getEmailTemplate().getEmailConfig().getEmailConfigName(), readyEmail.getEmailTemplate().getEmailConfig().getEmail()));
        break;
      default:
        emailSenderService.send(readyEmail, javaMailDefaultSender);
        break;
    }
  }
}

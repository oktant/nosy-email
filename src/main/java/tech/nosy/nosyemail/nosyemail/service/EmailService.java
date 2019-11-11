package tech.nosy.nosyemail.nosyemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import tech.nosy.nosyemail.nosyemail.model.EmailFromProvider;
import tech.nosy.nosyemail.nosyemail.model.ReadyEmail;

@Service
public class EmailService {

  private EmailSenderService emailSenderService;
  private JavaMailSenderImpl javaMailDefaultSender;
  private JavaMailSenderImpl javaMailGmailSender;
  private JavaMailSenderImpl javaMailYandexSender;

  @Autowired
  public EmailService(
          EmailSenderService emailSenderService,
          @Qualifier("Yandex") JavaMailSenderImpl javaMailYandexSender,
          @Qualifier("Default") JavaMailSenderImpl javaMailDefaultSender,
          @Qualifier("Gmail") JavaMailSenderImpl javaMailGmailSender) {
    this.emailSenderService = emailSenderService;
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
      default:
        emailSenderService.send(readyEmail, javaMailDefaultSender);
        break;
    }
  }
}

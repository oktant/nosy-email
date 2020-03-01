package tech.nosy.nosyemail.nosyemail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import tech.nosy.nosyemail.nosyemail.model.EmailConfig;
import tech.nosy.nosyemail.nosyemail.service.EmailConfigService;

import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailConfigPopulationBean {

    private EmailConfigService emailConfigService;

    public EmailConfigPopulationBean(EmailConfigService emailConfigService) {
        this.emailConfigService = emailConfigService;
    }

    @Bean("Yandex")
    public JavaMailSenderImpl javaMailYandexSender() {
        JavaMailSenderImpl yandex = new JavaMailSenderImpl();
        yandex.setHost("smtp.yandex.ru");
        yandex.setPort(465);
        yandex.setJavaMailProperties(getProperties());
        return yandex;
    }

    private Properties getProperties() {
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.debug", "true");
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.put("mail.smtp.ssl.checkserveridentity", true);
        javaMailProperties.put("smtp.starttls.enable", "true");
        javaMailProperties.put("smtp.starttls.required", "true");
        javaMailProperties.put("smtp.ssl.enable", "true");
        return javaMailProperties;
    }

    @Bean("Gmail")
    public JavaMailSenderImpl javaMailGmailSender() {
        JavaMailSenderImpl gmail = new JavaMailSenderImpl();
        gmail.setHost("smtp.gmail.com");
        gmail.setPort(465);
        gmail.setJavaMailProperties(getProperties());
        return gmail;
    }

    @Bean("Default")
    public JavaMailSenderImpl javaMailDefaultSender() {
        return javaMailYandexSender();
    }

    public MimeMessageHelper mimeMessageHelper(MimeMessage message) {
        return new MimeMessageHelper(message);
    }

    public JavaMailSenderImpl javaMailCustomSender(String configName, String email) {
        EmailConfig emailConfig = emailConfigService.getConfig(email, configName);
        JavaMailSenderImpl custom = new JavaMailSenderImpl();
        custom.setPort(emailConfig.getPort());
        custom.setHost(emailConfig.getHost());
        custom.setJavaMailProperties(getProperties());
        return custom;
    }

}

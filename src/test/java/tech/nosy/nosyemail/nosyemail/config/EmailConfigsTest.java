package tech.nosy.nosyemail.nosyemail.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.internet.MimeMessage;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)

public class EmailConfigsTest {

  @InjectMocks
  EmailConfigPopulationBean emailConfigPopulationBean;

  @Mock
  JavaMailSenderImpl javaMailSender;



  @Test
  public void javaMailYandexSenderTest() {
    assertEquals("smtp.yandex.ru",emailConfigPopulationBean.javaMailYandexSender().getHost());
  }

  @Test
  public void javaMailGmailSenderTest() {
    assertEquals("smtp.yandex.ru",emailConfigPopulationBean.javaMailDefaultSender().getHost());


  }

  @Test
  public void javaMailDefaultSenderTest() {
    assertEquals("smtp.gmail.com",emailConfigPopulationBean.javaMailGmailSender().getHost());

  }

  @Test(expected = Test.None.class)
  public void mimeMessageHelperTest() {
    MimeMessage mimeMessage=mock(MimeMessage.class);

    emailConfigPopulationBean.mimeMessageHelper(mimeMessage);

  }
}

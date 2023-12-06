package project.serviceImpl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import project.service.MailService;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
@Service
public class MailServiceImpl implements MailService {
    private final TemplateEngine templateEngine;
    @Value("${sender}")
    private String sender;
    private final SendGrid sendGrid;

    public MailServiceImpl(TemplateEngine templateEngine, SendGrid sendGrid) {
        this.templateEngine = templateEngine;
        this.sendGrid = sendGrid;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");
    @Async
    @Override
    public CompletableFuture<String> sendToken(String token, String to,HttpServletRequest httpRequest) {
        logger.info("sendToken() - Sending token to "+to);
        Email from = new Email(sender);
        String subject = "Встановлення нового паролю";
        Email toEmail = new Email(to);
        Content content = new Content("text/html", build(token,httpRequest));
        Mail mail = new Mail(from, subject, toEmail, content);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            logger.info("sendToken() - Token was sent");
            return CompletableFuture.completedFuture(response.getBody());
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
    private String build(String token, HttpServletRequest httpRequest) {
        Context context = new Context();
        final String fullUrl = ServletUriComponentsBuilder.fromRequestUri(httpRequest).build().toUriString();
        logger.info("url: "+ fullUrl);
        int in = fullUrl.lastIndexOf("/");
        String baseUrl = fullUrl.substring(0,in);
        String l = baseUrl +"/changePassword?token="+token;
        context.setVariable("link", l);
        return templateEngine.process("emailTemplate", context);
    }
}

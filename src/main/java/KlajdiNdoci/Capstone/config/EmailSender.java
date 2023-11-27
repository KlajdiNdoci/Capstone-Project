package KlajdiNdoci.Capstone.config;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EmailSender {
    private final String apikey;
    private final String sender;

    public EmailSender(@Value("${sendgrid.apikey}") String apikey,
                       @Value("${sengrid.sender}") String sender) {
        this.apikey = apikey;
        this.sender = sender;
    }

    public void sendRegistrationEmail(String recipient) throws IOException {
        Email from = new Email(sender);
        String subject = "Registration was successful!";
        Email to = new Email(recipient);
        Content content = new Content("text/html",
                "<p>Welcome</p>"
        );
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apikey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sg.api(request);
    }

}

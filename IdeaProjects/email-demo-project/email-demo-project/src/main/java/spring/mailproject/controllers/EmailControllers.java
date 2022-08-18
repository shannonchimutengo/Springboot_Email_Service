package spring.mailproject.controllers;

import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class EmailControllers{

    public final JavaMailSender mailSender;
// might throw error: -> "Could not autowire. No beans of 'JavaMailSender' type found.
// however it will work do not worry about the error"
    public EmailControllers(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @GetMapping("/contact")

    public String ContactPage(){

        return "contact_form";
    }
    @GetMapping("/sucess-alert")


    @RequestMapping("/sendmail")

    public String SendEmail(HttpServletRequest request, @RequestParam("attachment")MultipartFile multipartFile) throws MessagingException {
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message,true);

        messageHelper.setFrom("Company@mail.com");
        messageHelper.setTo("youremail@gmail.com");
        messageHelper.setSubject("subject:"+subject+"\n");
        messageHelper.setText("content:"+content+"\n email"+email+"\n name:"+fullname+"\n");


        if(!multipartFile.isEmpty()){
            String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            InputStreamSource inputStreamSource = new InputStreamSource() {
                @Override
                public InputStream getInputStream() throws IOException {
                    return multipartFile.getInputStream();
                }
            };

            messageHelper.addAttachment(filename,inputStreamSource);

        }
    mailSender.send(message);
        return "message_sent";
    }
}

package util;


import java.util.Properties;


import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class EnviarEmailThread implements Runnable {
	String destinatario;
	String titulo;
	String mensagem;


	public EnviarEmailThread(String destinatario, String titulo, String mensagem) {
		this.destinatario = destinatario;
		this.titulo = titulo;
		this.mensagem = mensagem;
	}

	public void run() { // Rotina de envio dos Emails
		Boolean flag = false;
		try {	Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		//prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		Session session = Session.getInstance(prop, new Authenticator() {	
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication("sistemasifprparanavai@gmail.com", "password");
		    }
		});
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("sistemasifprparanavai@gmail.com"));
		message.setRecipients(
		  Message.RecipientType.TO, InternetAddress.parse(destinatario));
		message.setSubject(titulo);

		String msg = mensagem;

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(msg, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);

		message.setContent(multipart);

		
			Transport.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			
		}
		
	}

}

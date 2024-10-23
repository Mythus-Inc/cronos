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



import com.google.gson.JsonObject;

public class EnviarEmail {
	public static Boolean enviarEmail(String destinatario, String titulo, String mensagem) {
	
		
		Boolean flag = false;
		try {	Properties prop = new Properties();
		prop.put("mail.smtp.host", "nao-responda2.ifpr.edu.br");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        //prop.put("mail.smtp.socketFactory.port", "587");
        //prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.starttls.enable","true");
        //prop.put("mail.smtp.ssl.trust", "nao-responda2.ifpr.edu.br");
		//prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		Session session = Session.getInstance(prop, new Authenticator() {	
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication("cronos.paranavai@nao-responda2.ifpr.edu.br", "password");
		    }
		});
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("cronos.paranavai@nao-responda2.ifpr.edu.br"));
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
		
			e.printStackTrace();
			return false;
		}
		return true;
////		
		
	
//		Client client = ClientBuilder.newClient();
//
//		WebTarget resource = client.target("http://176.223.140.239:8080/gestaofacil/rest/service/enviarEmail");
//
//		Builder request = resource.request();
//		//request.accept("charset=UTF-8");
//		//request.accept(MediaType.APPLICATION_JSON);
//		request.property("charset", "utf-8");
//		request.header("Content-type", "application/json;charset=UTF-8");
//			
//		
//		JsonObject json = new JsonObject();
//		json.addProperty("destinatario", destinatario);
//		json.addProperty("titulo", titulo);
//		json.addProperty("mensagem", mensagem);
//		
////System.out.println("JSON "+json.toString());
//		//Response response = request.post(Entity.json("{\"destinatario\":\""+destinatario+"\",\"titulo\":\""+titulo+"\",\"mensagem\":\""+mensagem+"\"}"));
//		Response response = request.post(Entity.json(json.toString()));
//		
//		if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
//		//    System.out.println("Success! " + response.getStatus());
//		  //  System.out.println(response.getEntity());
//		    response.close();
//		    return true;
//		} else {
//		 //   System.out.println("ERROR! " + response.getStatus());    
//		   // System.out.println(response.getEntity());
//		    response.close();
//		    ExibirMensagem.exibirMensagem("Vixii, deu erro");
//		    return false;
//		}
		
	}
}

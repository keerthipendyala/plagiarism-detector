package edu.northeastern.cs5500.email;

import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.northeastern.cs5500.daos.ReportDao;


/**
 * 
 * @author karan sharma 
 * @desc This class will have functionality to send emails to the users intended 
 *
 */
public class SendEmail {

	Logger logger=Logger.getLogger(SendEmail.class.getName());
	private static SendEmail instance=null;
	
	/**
	 * This method will create a single instance of SendEmail class for the application 
	 * @return a single instance of SendEmail class 
	 */
	public static SendEmail getInstance() {
		if (instance == null) {
			instance = new SendEmail();

		}
		return instance;
	}

	
	private SendEmail() {
		
	}
	
	
	
	/**
	 * 
	 * @param message the message that the application wants to send 
	 * @param to the recipient of the message 
	 * @param subject the subject of the email 
	 * @throws MessagingException throw an exception if the email server is not ready 
	 */
	public void sendEmail(StringBuilder message,String to,String subject) throws MessagingException {
		
		final String from="copycatchteam@gmail.com";
		Properties prop=new Properties();
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		/**
		 * get the session of mail authenticator 
		 */
		Session session= Session.getInstance(prop,new javax.mail.Authenticator(){
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				/**
				 * authenticate the server with email and password 
				 */
				return new PasswordAuthentication(from,"copycatch@123");
			}
		});
		
		
		/**
		 * Create the message using session 
		 */
		MimeMessage msg= new MimeMessage(session);
		/**
		 * set the from address of the message to be sent 
		 */
		msg.setFrom(new InternetAddress(from));
		/**
		 * add the to address to the message 
		 */
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		/**
		 * set the message of the subject 
		 */
		msg.setSubject(subject);
		/**
		 * set the text of the message 
		 */
		msg.setText(message.toString());
		/**
		 * send the message using Transport 
		 */
		Transport.send(msg);
		
	}
	
	
	/**
	 * 
	 * @param to email id of the students 
	 * @param subject the subject of the message to be sent 
	 * @param reportid the id of the report we need to fetch 
	 * @throws AddressException if any, throw this exception 
	 * @throws MessagingException if any, throw this exception 
	 */
	public void sendEmailToStudents(String[] to,String subject,int reportid) throws AddressException, MessagingException {
		
		/**
		 * from address of the email 
		 */
		final String from="copycatchteam@gmail.com";
		Properties prop=new Properties();
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		
		Session session= Session.getInstance(prop,new javax.mail.Authenticator(){
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(from,"copycatch@123");
			}
		});
		
		/**
		 * get the instance of reportDAO class 
		 */
		ReportDao rdao=ReportDao.getInstance();
		
		/**
		 * get the dummylink that will be sent to the users from reports table  
		 */
		String dummylink=rdao.getDummyLink(reportid);
		
		/**
		 * create a builder for message 
		 */
		StringBuilder message=new StringBuilder();
		
		/**
		 * for all toemails that we want to send the email 
		 */
		for(String toemail: to) {
			
		
			/**
			 * construct to create the message 
			 */
			message.append("Hi "+ toemail+",");
			
			message.append(System.lineSeparator());
			message.append("CopyCatch caught you plagiarising an assignment. Details are in the report.Please click on "+dummylink+System.lineSeparator());
			message.append("Remember to login to check the link. "+System.lineSeparator());
			message.append("Regards,CopyCatch  Team");
			
			MimeMessage msg= new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toemail));
			msg.setSubject(subject);
			msg.setText(message.toString());
			/**
			 * send email with the message 
			 */
			Transport.send(msg);
			/**
			 * change the length of builder to 0 
			 */
			message.setLength(0);
			
		}
		
		
	}
	
	
	
	
	
	
	
	
	
}

package com.brightleaf.usercompanyservice.resource;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EmailUtility {
	
	private static Logger logger = Logger.getLogger(EmailUtility.class);
	
	private static final String HEAD_BODY = "<html><head></head><body>"; 
	
	private static final String TABLE_WIDTH = "<table width=\"100%\"><tr><td>"; 
	
	private static final String P_HI = "<p>Hi "; 
	
	private static final String P_THANKS = "<p><br>Thanks and Regards,<br>Helpdesk Team</p></td></tr></table></body></html>"; 
	
	private EmailUtility()
	{
		
	}
	
	private static final String SMTP_HOST_NAME = "secure.emailsrvr.com";

	private static final String SMTP_AUTH_USER = "helpdesk@brightleaf.com";

	private static final String SMTP_AUTH_PSWD = "Welcome@123";

	private static final String EMAIL_SUBJECT_TXT = "Text Extraction Engine Password Reset";

	private static final String EMAIL_SUBJECT_TXT_OTP = "Text Extraction Engine - One Time Password";

	private static final String EMAIL_FROM_ADDRESS = "helpdesk@brightleaf.com";

	// Add List of Email address to who email needs to be sent to

	public static void postMail(String userName, String recipient, String urlPath, String otpString)
			throws MessagingException {

		boolean debug = false;
		java.security.Security.addProvider(new BouncyCastleProvider());
		Authenticator auth = new SMTPAuthenticator();
		
		


		// Set the host smtp address
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.debug", "false");

		Session session = Session.getInstance(props, auth);

		session.setDebug(debug);
		MimeMultipart multipart = new MimeMultipart();
		BodyPart messageBodyPart = new MimeBodyPart();
		StringBuilder streamBuf = new StringBuilder("");
		if (urlPath != null && otpString == "") {
			streamBuf.append(HEAD_BODY);
			streamBuf.append(TABLE_WIDTH);
			streamBuf.append(P_HI + userName + "," + "</p>");
			streamBuf.append(
					"<p><br>We have received a request to reset your password. If you didn't make the request, just ignore this email. Otherwise, you can reset your password using this link :");
			streamBuf.append("<font color=\"#ff6600\"><p>");
			streamBuf.append("<a href=" + "\"" + urlPath + "/angular/#/dashboard/resetpassword/" + recipient
					+ "\">Reset Password</a></p> </font>");
			streamBuf.append(P_THANKS);
			streamBuf.append("");
		} else {
			streamBuf.append(HEAD_BODY);
			streamBuf.append(TABLE_WIDTH);
			streamBuf.append(P_HI + userName + "," + "</p>");
			streamBuf.append("<p><br>The One Time Password (OTP) is :" + otpString);
			streamBuf.append(P_THANKS);
			streamBuf.append("");
		}
		messageBodyPart.setContent(streamBuf.toString(), "text/html");
		// create a message
		MimeMessage mimeMessage = new MimeMessage(session);
		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(EMAIL_FROM_ADDRESS);
		mimeMessage.setFrom(addressFrom);
		multipart.addBodyPart(messageBodyPart);
		mimeMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipient));

		// Setting the Subject and Content Type
		mimeMessage.setSubject(EMAIL_SUBJECT_TXT);
		if (urlPath != null && otpString == "") {
			mimeMessage.setSubject(EMAIL_SUBJECT_TXT);
		} else {
			mimeMessage.setSubject(EMAIL_SUBJECT_TXT_OTP);
		}
		mimeMessage.setContent(multipart);
		Transport.send(mimeMessage);
		logger.info("receipient = " + recipient + " userName = " + userName + " url path = " + urlPath);
	}
	
	
	public static void postMailForNewPassword(String userName, String recipient, String urlPath, String otpString)
			throws MessagingException {

		boolean debug = false;
		java.security.Security.addProvider(new BouncyCastleProvider());
		Authenticator auth = new SMTPAuthenticator();

		// Set the host smtp address
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.debug", "false");

		Session session = Session.getInstance(props, auth);

		session.setDebug(debug);
		MimeMultipart multipart = new MimeMultipart();
		BodyPart messageBodyPart = new MimeBodyPart();
		StringBuilder streamBuf = new StringBuilder("");
		if (urlPath != null && otpString == "") {
			streamBuf.append(HEAD_BODY);
			streamBuf.append(TABLE_WIDTH);
			streamBuf.append(P_HI + userName + "," + "</p>");
			streamBuf.append(
					"<p><br>Please create your new password using this link :");
			streamBuf.append("<font color=\"#ff6600\"><p>");
			streamBuf.append("<a href=" + "\"" + urlPath + "/angular/#/dashboard/createpassword/" + recipient
					+ "\">Create New Password</a></p> </font>");
			streamBuf.append(P_THANKS);
			streamBuf.append("");
		} else {
			streamBuf.append(HEAD_BODY);
			streamBuf.append(TABLE_WIDTH);
			streamBuf.append(P_HI + userName + "," + "</p>");
			streamBuf.append("<p><br>The One Time Password (OTP) is :" + otpString);
			streamBuf.append(P_THANKS);
			streamBuf.append("");
		}
		messageBodyPart.setContent(streamBuf.toString(), "text/html");
		// create a message
		MimeMessage mimeMessage = new MimeMessage(session);
		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(EMAIL_FROM_ADDRESS);
		mimeMessage.setFrom(addressFrom);
		multipart.addBodyPart(messageBodyPart);
		mimeMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipient));

		// Setting the Subject and Content Type
		mimeMessage.setSubject(EMAIL_SUBJECT_TXT);
		if (urlPath != null && otpString == "") {
			mimeMessage.setSubject(EMAIL_SUBJECT_TXT);
		} else {
			mimeMessage.setSubject(EMAIL_SUBJECT_TXT_OTP);
		}
		mimeMessage.setContent(multipart);
		Transport.send(mimeMessage);
		logger.info("receipient = " + recipient + " userName = " + userName + " url path = " + urlPath);
	}

	private static class SMTPAuthenticator extends Authenticator {
		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String smtppswd = SMTP_AUTH_PSWD;
			return new PasswordAuthentication(username, smtppswd);
		}
	}

}

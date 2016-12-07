/**
 * @author Jan-Peter Petersen
 * @version 1.0
 * thanks to Flah-Uddin Ahmad and Andreas Loeffler
 */

package WeatherDBService;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * MailNotification will send an E-Mail to miweatherservice(at)gmail.com
 */

public class MailNotification {
	  private static final String SMTP_HOST = "smtp.gmail.com";
	  private static final int SMTP_PORT = 465;
	  
	  private static final String USERNAME = "miweatherservice@gmail.com";
	  private static final String PASSWORD = "!miws-16";
	  
	  static void sendMail(Exception e) {
		  StringWriter sw = new StringWriter();
		  PrintWriter pw = new PrintWriter(sw);
		  e.printStackTrace(pw);
		    try {
		    	SimpleEmail email = new SimpleEmail();
			    email.setHostName(SMTP_HOST);
			    email.setAuthentication(USERNAME, PASSWORD);
			    email.setDebug(true);
			    email.setSmtpPort(SMTP_PORT);
			    email.setSSLOnConnect(true);
				email.addTo(USERNAME);
				email.setFrom(USERNAME, "WeatherDBService");
			    email.setSubject("Exception in WeatherDBService");
			    email.setMsg(sw.toString());
			    email.send();
			} catch (EmailException e1) {
				e1.printStackTrace();
			}
	  }
}

/**
 * Klasse zum Versenden eines Mieterhöhungsbescheides
 * 
 * @email s0558029@htw-berlin.de,  s0559875@htw-berlin.de, s0558587@htw-berlin.de
 * @author Handan Fidan, Stefen Fröhlich, Christian Fiebelkorn
 * @since 2017-01-05
 * @version 1.0.0 - 05012018
 * 
 */

package MAS_Projekt.mieteErhoehen;

import java.io.InputStream;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.mail.internet.MimeBodyPart;

import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.FileValue;


/**
 * Klasse zum Versenden eines Mieterhöhungsbescheides
 * 
 */
public class EmailBescheid implements JavaDelegate {
	
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
		
	  
    /**
    * Einstiegs-Methode zum Aufruf der Klasse,
    * zum Laden der notwendigen Vorgangsdaten und
    * zum Aufruf der Methode zum Senden der Email
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	public void execute(DelegateExecution execution) throws Exception {
		LOGGER.info("-> EmailBescheid...\n");
		execution.setVariable("Erhoehung", true);
		LOGGER.info("-> EB1...\n");
		String anrede = (String) execution.getVariable("Anrede");
		String vorname = (String) execution.getVariable("VornameHauptmieter");
		String nachname = (String) execution.getVariable("NachnameHauptmieter");
		String toEmail = (String) execution.getVariable("E-Mail");
		double mieterhoehung = (Double) execution.getVariable("relevanteMieterhoehung");
		
		String subject = "Bescheid zur Bewilligung der Mieterhöhung";
		String mailtext = "Sehr geehrte/er " + anrede + " " + vorname + " " + nachname + ",\n"
		+ "\nIhre Miete wird gem. §558 Abs. 1 BGB um " + mieterhoehung + " EUR erhöht."
		+ "\nBitte füllen Sie das im Anhang beigefügte Formular aus und schicken Sie es uns binnen 6 Wochen unterschrieben zurück."
		+ "\n\nMit freundlichen Grüßen,\n Wohnbau Lichtenberg\n\n";

//		FileValue retrievedTypedFileValue = execution.getVariableTyped("Bescheid");
//		InputStream fileContent = retrievedTypedFileValue.getValue(); // bytestream
//		String fileName = retrievedTypedFileValue.getFilename(); // filename
//		String mimeType = retrievedTypedFileValue.getMimeType(); // memetype
//		String encoding = retrievedTypedFileValue.getEncoding(); // encodung
//
//		MimeBodyPart attachment = new MimeBodyPart();
//		ByteArrayDataSource ds = new ByteArrayDataSource(fileContent, mimeType);
//
//		attachment.setDataHandler(new DataHandler(ds));
//		attachment.setFileName(fileName);
		LOGGER.info("-> BE1\n");
		sendEmail(execution, mailtext, subject, toEmail);//, ds, fileName, encoding);
		
	}
	

    /**
    * Methode zum Ausführen der implementierten Geschäftlogik:
    * Laden und Versenden eines Mieterhöhungsbescheides per Email
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
     * @throws EmailException 
    */
	public void sendEmail(DelegateExecution execution, String mailtext, String subject, String toEmail) throws EmailException {//, ByteArrayDataSource ds, String fileName, String encoding) throws EmailException {
		LOGGER.info("-> BE2\n");
		MultiPartEmail email = new MultiPartEmail();
//		email.setCharset("utf-8");
////		email.setSSLOnConnect(true);
//		email.setSmtpPort(465);
//		email.setHostName("mail.gmx.net");
//		email.setAuthentication("htw-berlin@gmx.de", "HTW-Berlin-MAS");
//		email.setFrom("htw-berlin@gmx.de");
//		email.addTo(toEmail);
//		email.setSubject(subject);
//		email.setMsg(mailtext);

		
		email.setCharset("utf-8");
		email.setSSLOnConnect(true);
		email.setSmtpPort(465);
		email.setHostName("mail.htw-berlin.de");
		email.setAuthentication("s0558587", "MauseFalle4");
		email.setFrom("s0558587@htw-berlin.de");
		email.addTo(toEmail);
		email.setSubject(subject);
		email.setMsg(mailtext);
//Bescheid anhängen
		LOGGER.info("-> vorAttach\n");
		  EmailAttachment attachment = new EmailAttachment();
		  LOGGER.info("-> Attach1\n");
		  attachment.setPath("bescheid.txt");
		  LOGGER.info("-> Attach2\n");
		  attachment.setDisposition(EmailAttachment.ATTACHMENT);
		  LOGGER.info("-> Attach3\n");
		  //attachment.setDescription("Picture of John");
		  //attachment.setName("John");
		  //email.attach(ds, "bescheid.txt", "UTF-8");
		  email.attach(attachment);
		  LOGGER.info("-> Attach4\n");
//Zustimmung anhängen
		  EmailAttachment attachment2 = new EmailAttachment();
		  LOGGER.info("-> Attach1a\n");
		  attachment2.setPath("zustimmungserklaerung.txt");
		  LOGGER.info("-> Attach2a\n");
		  attachment2.setDisposition(EmailAttachment.ATTACHMENT);
		  LOGGER.info("-> Attach3a\n");
		  //attachment.setDescription("Picture of John");
		  //attachment.setName("John");
		  //email.attach(ds, "bescheid.txt", "UTF-8");
		  email.attach(attachment2);
		  LOGGER.info("-> Attach4a\n");
//Härtefallbescheid anhängen
		  EmailAttachment attachment3 = new EmailAttachment();
		  LOGGER.info("-> Attach1b\n");
		  attachment3.setPath("haertefallbescheid.txt");
		  LOGGER.info("-> Attach2b\n");
		  attachment3.setDisposition(EmailAttachment.ATTACHMENT);
		  LOGGER.info("-> Attach3b\n");
		  //attachment.setDescription("Picture of John");
		  //attachment.setName("John");
		  //email.attach(ds, "bescheid.txt", "UTF-8");
		  email.attach(attachment3);
		  LOGGER.info("-> Attach4b\n");
		  
		  
		email.send();
		LOGGER.info("-> BE3\n");
		
	}
		
	

}


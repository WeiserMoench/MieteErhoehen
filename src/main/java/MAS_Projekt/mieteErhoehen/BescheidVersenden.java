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

import java.util.logging.Logger;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


/**
 * Klasse zum Versenden eines Mieterhöhungsbescheides
 * 
 */
public class BescheidVersenden implements JavaDelegate {
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	private String anrede;
	private String vorname;
	private String nachname;
	private String toEmail;
	private Double mieterhoehung;


	//alle executionVariablen als Klassenvariablen
		
	  
	  
    /**
    * Einstiegs-Methode zum Aufruf der Klasse
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	public void execute(DelegateExecution execution) throws Exception {
		LOGGER.info("-> ");
		getExecutionVariablen(execution);
		performGeschaeftslogik();
	}//E execute
	

    /**
    * Methode zum Ausführen der implementierten Geschäftlogik:
    * Laden und Versenden eines Mieterhöhungsbescheides per Email
     * @throws EmailException 
    * 
    */
	private void performGeschaeftslogik() throws EmailException {


		
		String subject = "Bescheid zur Bewilligung der Mieterhöhung nach Härtefallprüfung";
		String mailtext = "Sehr geehrte/er " + anrede + " " + vorname + " " + nachname + ",\n"
		+ "\nIhre Miete wird gem. §558 Abs. 1 BGB um " + mieterhoehung + " EUR erhöht."
		+ "\nBitte füllen Sie das im Anhang beigefügte Formular aus und schicken Sie es uns binnen 4 Wochen unterschrieben zurück."
		+ "\nDieser neue Bescheid enthält bereits die Anpassung auf Grund Ihres positiv ausgefallenen Antrages auf Härtefallregelung"
		+ "\n\nMit freundlichen Grüßen,\n Wohnbau Lichtenberg\n\n";
		
		sendEmail(mailtext, subject, toEmail);
		
	}//E performGeschaeftslogik
	
	/**
	 * Diese Methode versendet die Email
	 * 
	 * @param mailtext - Text der Email
	 * @param subject - Betreff der Email
	 * @param toEmail2 - EMailadresse des Empfängers
	 * @throws EmailException
	 */
    private void sendEmail(String mailtext, String subject, String toEmail2) throws EmailException {
    	MultiPartEmail email = new MultiPartEmail();

		
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
		  EmailAttachment attachment = new EmailAttachment();
		  attachment.setPath("bescheid_Haertefall.txt");
		  attachment.setDisposition(EmailAttachment.ATTACHMENT);
		  //attachment.setDescription("Picture of John");
		  //attachment.setName("John");
		  //email.attach(ds, "bescheid.txt", "UTF-8");
		  email.attach(attachment);
//Zustimmung anhängen
		  EmailAttachment attachment2 = new EmailAttachment();
		  attachment2.setPath("zustimmungserklaerung_n_Haertefall.txt");
		  attachment2.setDisposition(EmailAttachment.ATTACHMENT);
		  //attachment.setDescription("Picture of John");
		  //attachment.setName("John");
		  //email.attach(ds, "bescheid.txt", "UTF-8");
		  email.attach(attachment2);
		  
		email.send();
	}


	/**
    * Methode zum Laden der notwendigen Vorgangsdaten
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void getExecutionVariablen(DelegateExecution execution) {
		execution.setVariable("Erhoehung", true);

		anrede = (String) execution.getVariable("Anrede");
		vorname = (String) execution.getVariable("VornameHauptmieter");
		nachname = (String) execution.getVariable("NachnameHauptmieter");
		toEmail = (String) execution.getVariable("E-Mail");
		mieterhoehung = (Double) execution.getVariable("relevanteMieterhoehung");
	}//E getExecutionVariablen
	
	

}//E Class
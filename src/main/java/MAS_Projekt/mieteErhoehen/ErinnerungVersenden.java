/**
 * Klasse zum Versenden einer Erinnerung zum Zusenden der
 * Zustimmungserklärung zum Mieterhöhungsverlangen
 * 
 * @email s0558029@htw-berlin.de,  s0559875@htw-berlin.de, s0558587@htw-berlin.de
 * @author Handan Fidan, Stefen Fröhlich, Christian Fiebelkorn
 * @since 2017-01-05
 * @version 1.0.0 - 15012018
 * 
 */

package MAS_Projekt.mieteErhoehen;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


/**
 * Klasse zum Versenden einer Erinnerung zum Zusenden der
 * Zustimmungserklärung zum Mieterhöhungsverlangen
 * 
 */
public class ErinnerungVersenden implements JavaDelegate {
	//alle executionVariablen als Klassenvariablen
	  
	
    /**
    * Einstiegs-Methode zum Aufruf der Klasse,
    * zum Laden der notwendigen Vorgangsdaten und
    * zum Aufruf der Methode zum Senden der Email
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	public void execute(DelegateExecution execution) throws Exception {

		execution.setVariable("Erhoehung", true);
		String anrede = (String) execution.getVariable("Anrede");
		String vorname = (String) execution.getVariable("VornameHauptmieter");
		String nachname = (String) execution.getVariable("NachnameHauptmieter");
		String toEmail = (String) execution.getVariable("E-Mail");
		double mieterhoehung = (Double) execution.getVariable("relevanteMieterhoehung");
		
		String subject = "Erinnerung an Bescheid zur Bewilligung der Mieterhöhung";
		String mailtext = "Sehr geehrte/er " + anrede + " " + vorname + " " + nachname + ",\n"
		+ "\nIhre Miete wird gem. §558 Abs. 1 BGB um " + mieterhoehung + " EUR erhöht."
		+ "\nBitte fuellen Sie das im Anhang beigefuegte Formular aus und schicken Sie es uns binnen 4 Wochen unterschrieben zurück."
		+ "\nBitte beachten Sie, dass bei nicht Antwort unsere Rechtsabteilung eingeschaltet werden muss."
		+ "\n\nMit freundlichen Grüßen,\n Wohnbau Lichtenberg\n\n";
		
		sendEmail(execution, mailtext, subject, toEmail);

	}//E execute
	

    /**
    * Methode zum Ausführen der implementierten Geschäftlogik:
    * Laden und Versenden eines Mieterhöhungsbescheides per Email
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
     * @throws EmailException 
    */
	public void sendEmail(DelegateExecution execution, String mailtext, String subject, String toEmail) throws EmailException {
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
		  attachment.setPath("bescheid.txt");
		  attachment.setDisposition(EmailAttachment.ATTACHMENT);
		  email.attach(attachment);
//Zustimmung anhängen
		  EmailAttachment attachment2 = new EmailAttachment();
		  attachment2.setPath("zustimmungserklaerung.txt");
		  attachment2.setDisposition(EmailAttachment.ATTACHMENT);
		  email.attach(attachment2);
//Härtefallbescheid anhängen
		  EmailAttachment attachment3 = new EmailAttachment();
		  attachment3.setPath("haertefallbescheid.txt");
		  attachment3.setDisposition(EmailAttachment.ATTACHMENT);
		  email.attach(attachment3);
		    
		email.send();
		
	}

}//E Class
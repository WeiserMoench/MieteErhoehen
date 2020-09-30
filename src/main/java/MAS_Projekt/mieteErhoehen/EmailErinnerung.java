/**
 * Klasse zum Erstellen einer Erinnerung zum Zusenden der
 * Zustimmungserklärung zum Mieterhöhungsverlangen
 * 
 * @email s0558029@htw-berlin.de,  s0559875@htw-berlin.de, s0558587@htw-berlin.de
 * @author Handan Fidan, Stefen Fröhlich, Christian Fiebelkorn
 * @since 2017-01-05
 * @version 1.0.0 - 05012018
 * 
 */

package MAS_Projekt.mieteErhoehen;

// import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


/**
 * Klasse zum Erstellen einer Erinnerung zum Zusenden der
 * Zustimmungserklärung zum Mieterhöhungsverlangen
 * 
 */
public class EmailErinnerung implements JavaDelegate {
		
	  
    /**
    * Einstiegs-Methode zum Aufruf der Klasse,
    * zum Laden der notwendigen Vorgangsdaten und
    * zum Aufruf der Methode zum Senden der Email
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	public void execute(DelegateExecution execution) throws Exception {

		String anrede = (String) execution.getVariable("Anrede");
		String vorname = (String) execution.getVariable("VornameHauptmieter");
		String nachname = (String) execution.getVariable("NachnameHauptmieter");
		String toEmail = (String) execution.getVariable("E-Mail");
	
		String mailtext = "Sehr geehrte/er " + anrede + " " + vorname + " " + nachname + ",\n" + "\nLeider haben wir noch keine Antwort von Ihnen"
		+ "zu unserer geplanten Mieterhoehung erhalten. Wir bitten Sie, dies innerhalb einer Frist von 4 Wochen nachzuholen.\n" + "\nFalls Sie nicht reagieren sollten, "
				+ "\nwerden wir den Vorgang an die Rechtsabteilung weiterleiten."
		+ "\n Mit freundlichen Gruessen,\n die Wohnbaugesellschaft Lichtenberg.";

		String subject = "Erinnerung zur Annahme der Mieterhoehung";
		System.out.println(subject);
		sendEmail(mailtext, subject, toEmail);
	}
	

    /**
    * Methode zum Ausführen der implementierten Geschäftlogik:
    * Laden und Versenden einer Erinnerung zum Zusenden der
	* Zustimmungserklärung zum Mieterhöhungsverlangen
	* 
    * 
    * @param mailtext (String): Email-Text
    * @param subject (String): Betreffzeile
    * @param toEmail (String): Email-Adresse
    */
	public void sendEmail(String mailtext, String subject, String toEmail) throws EmailException {

		MultiPartEmail email = new MultiPartEmail();
		email.setCharset("utf-8");
		email.setSSLOnConnect(true);
		email.setSmtpPort(465);
		email.setHostName("mail.htw-berlin.de");
		email.setAuthentication("s0558587", "$pAidermAn4-HTW");
		email.setFrom("s0558587@htw-berlin.de");
		email.addTo(toEmail);
		email.setSubject(subject);
		email.setMsg(mailtext);
		email.send();
	}
		
}

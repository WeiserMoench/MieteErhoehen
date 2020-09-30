/**
 * Diese Klasse erzeugt die Email fuer den Ablehungsbescheid, im Falle eines abgelehnten Antrags auf Haertefallregelung,
 * und versendet die Email
 * 
 * @email s0558029@htw-berlin.de,  s0559875@htw-berlin.de, s0558587@htw-berlin.de
 * @author Handan Fidan, Stefen Fröhlich, Christian Fiebelkorn
 * @since 2017-01-02
 * @version 1.0.1 - 12012018
 * 
 */

package MAS_Projekt.mieteErhoehen;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


/**
 * Diese Klasse erzeugt die Email fuer den Ablehungsbescheid, im Falle eines abgelehnten Antrags auf Haertefallregelung,
 * und versendet die Email
 * 
 */
public class AblehnungVersenden implements JavaDelegate {
	//alle executionVariablen als Klassenvariablen
	  
    private String anrede;
	private String vorname;
	private String nachname;
	private String toEmail;

		
	  
    /**
    * Einstiegs-Methode zum Aufruf der Klasse
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	public void execute(DelegateExecution execution) throws Exception {
		execution.setVariable("Erhoehung", true);
		getExecutionVariablen(execution);
		performGeschaeftslogik();
	}//E execute
	

    /**
    * Methode zum Ausführen der implementierten Geschäftlogik:
    * Laden und Versenden eines Härtefallantrags-Ablehnungsbescheides per Email
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
     * @throws EmailException 
    */
	private void performGeschaeftslogik() throws EmailException {

		String subject = "Antrag auf Härtefallregelung wurde abgeleht";
		String mailtext = "Sehr geehrte/er " + anrede + " " + vorname + " " + nachname + ",\n"
		+ "\nNach einer ausführlichen Prüfung Ihres Härtefallantrages sind wir zu dem Schluss gegommen,"
		+ "\ndass Sie kein anrecht auf eine Härtfallregelung haben."
		+ "\nDaher fordern wir Sie auf die Zustimmungserklärung aus der ersten Email und "
		+ "\nin den nächsten 4 Wochen an uns zurück zu senden."
		+ "\nAnsonsten sind wir leider gezwungen unsere Rechtabteilung einzuschalten"
		+ "\n\nMit freundlichen Grüßen,\n Wohnbau Lichtenberg\n\n";
		
		sendEmail(mailtext, subject, toEmail);
		
	}//E performGeschaeftslogik
	
	
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
		email.send();
		
	}


	/**
    * Methode zum Laden der notwendigen Vorgangsdaten
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void getExecutionVariablen(DelegateExecution execution) {
		
		anrede = (String) execution.getVariable("Anrede");
		vorname = (String) execution.getVariable("VornameHauptmieter");
		nachname = (String) execution.getVariable("NachnameHauptmieter");
		toEmail = (String) execution.getVariable("E-Mail");
		
	}//E getExecutionVariablen
	
	
}//E Class
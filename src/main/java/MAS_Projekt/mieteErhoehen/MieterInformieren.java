/**
 * Klasse zum Versenden einer Information, daß im Rahmen eines Härtefallantrages
 * auf eine Mieterhöhung verzichtet wird
 * 
 * @email s0558029@htw-berlin.de,  s0559875@htw-berlin.de, s0558587@htw-berlin.de
 * @author Handan Fidan, Stefen Fröhlich, Christian Fiebelkorn
 * @since 2017-01-05
 * @version 1.0.0 - 05012018
 * 
 */

package MAS_Projekt.mieteErhoehen;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


/**
 * Klasse zum Versenden einer Information, daß im Rahmen eines Härtefallantrages
 * auf eine Mieterhöhung verzichtet wird
 * 
 */
public class MieterInformieren implements JavaDelegate {
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
		
		getExecutionVariablen(execution);
		performGeschaeftslogik();
		execution.setVariable("Erhoehung", false);
	}//E execute
	

    /**
    * Methode zum Ausführen der implementierten Geschäftlogik:
    * Versenden einer Information, daß im Rahmen eines Härtefallantrages
    * auf eine Mieterhöhung verzichtet wird
     * @throws EmailException 
    * 
    */
	private void performGeschaeftslogik() throws EmailException {

		String subject = "Verzicht auf Mieterhöhung nach Prüfung des Härtefallantrages";
		String mailtext = "Sehr geehrte/er " + anrede + " " + vorname + " " + nachname + ",\n"
		+ "\nNach einer ausführlichen Prüfung Ihres Härtefallantrages sind wir zu dem Schluss gegommen,"
		+ "\ndass eine Erhöhung Ihrer Miete für Sie nicht zu tragen wäre und Sie alle nötigen Voraussetzungen"
		+ "\nerfüllt haben. Daher sehen wir von einer Mieterhöhung in diesem Fall ab."
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
		//...
	}//E getExecutionVariablen
	

}//E Class
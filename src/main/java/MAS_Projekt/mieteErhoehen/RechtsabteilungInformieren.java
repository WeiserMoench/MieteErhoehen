/**
 * Klasse zur Übergabe eines Vorganges an die Rechtsabteilung
 * Diese Klasse erzeugt die Email um die Rechtsabteilung zu informieren,
 * dass der Mieter entweder die Erhoehung nicht akzetpiert oder einfach nicht reagiert hat
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
 * Klasse zur Übergabe eines Vorganges an die Rechtsabteilung
 */
public class RechtsabteilungInformieren implements JavaDelegate {
	
	/**
	 * Diese Methode steuert die Ausfuehung der Klasse
	 * Dabei liest sie Daten aus dem Camunda Prozess aus und erzeugt den Inhalt der Email
	 */
	public void execute(DelegateExecution execution) throws Exception {
		
		String vorgangsid = (String) execution.getProcessInstanceId();
		String mietvertragnummer = (String) execution.getVariable("Mietvertragsnummer");

		String subject = "Probleme bei der Mietpreiserhöhung zu dem Mietvertrag mit der Nummer " + mietvertragnummer;
		String mailtext = "Sehr geehrte Rechtsabteilung,\n" + "\n"
		+ "\nLeider gibt es ein Problem bei der Mietpreiserhöhung zu dem Mietvertrag mit der Nummer " + mietvertragnummer
		+ ", denn der Mieter hat nicht Fristgerecht auf die Erhöhung reagiert oder weigert sich, die Erhöhung anzuerkennen"
		+ "\nAlle Infos zu diesem Vorgang finden Sie unter der ID " + vorgangsid + "Im System der Mietpreiserhöhung"
		+ "\n"
		+ "\nWir danken für Ihre Unterstützung." + "\n\nMit freundlichen Grüßen,\n Das Mietenmanagement";



		sendEmail(mailtext, subject);
		
		
	}	
	
	/**
	 * Diese Methode erzeugt die Email und versendet diese
	 * 	
	 * @param mailtext - Der Text, den die Email enthalten soll
	 * @param subject - Der Betreff der Email
	 * @throws EmailException
	 */
	public void sendEmail(String mailtext, String subject) throws EmailException {
		
		MultiPartEmail email = new MultiPartEmail();

		email.addTo("s0558587@htw-berlin.de"); // s0558029@htw-berlin.de Emailadresse der Rechtsabteilung, Handan Fidan ist im Beispiel unsere Rechtsabteilung
		email.setCharset("utf-8");
		email.setSSLOnConnect(true);
		email.setSmtpPort(465);
		email.setHostName("mail.htw-berlin.de");
		email.setAuthentication("s0558587", "MauseFalle4");
		email.setFrom("s0558587@htw-berlin.de");
		email.setSubject(subject);
		email.setMsg(mailtext);
		email.send();
		
		
	}
		
		

}

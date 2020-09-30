/**
 * Klasse zum Versenden einer Bestätigung zu einem Härtefallantrag
 * 
 * @email s0558029@htw-berlin.de,  s0559875@htw-berlin.de, s0558587@htw-berlin.de
 * @author Handan Fidan, Stefen Fröhlich, Christian Fiebelkorn
 * @since 2017-01-05
 * @version 1.0.0 - 05012018
 * 
 */

package MAS_Projekt.mieteErhoehen;

	import java.io.InputStream;

	import javax.activation.DataHandler;
	import javax.mail.internet.MimeBodyPart;
	import org.apache.commons.mail.ByteArrayDataSource;
	import org.apache.commons.mail.EmailException;
	import org.apache.commons.mail.MultiPartEmail;
	import org.camunda.bpm.engine.delegate.DelegateExecution;
	import org.camunda.bpm.engine.delegate.JavaDelegate;
	import org.camunda.bpm.engine.variable.value.FileValue;


	/**
	 * Klasse zum Versenden einer Bestätigung zu einem Härtefallantrag
	 * 
	 */
	public class EmailMieterInformieren implements JavaDelegate {
		
		  
	    /**
	    * Einstiegs-Methode zum Aufruf der Klasse,
	    * zum Laden der notwendigen Vorgangsdaten und
	    * zum Aufruf der Methode zum Senden der Email
	    * 
	    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
	    */
		public void execute(DelegateExecution execution) throws Exception {
			
			execution.setVariable("Erhoehung", false);
			
			String anrede = (String) execution.getVariable("anrede");
			String vorname = (String) execution.getVariable("vorname");
			String nachname = (String) execution.getVariable("nachname");
			String toEmail = (String) execution.getVariable("email");
			String mieterhoehung = (String) execution.getVariable("mieterhoehung");
			String aktMiete = (String) execution.getVariable("AktMiete");
			String subject = "Ablehnung der Mieterhoehung um " + mieterhoehung + "Euro";
			String mailtext = "Sehr geehrte/er " + anrede + " " + vorname + " " + nachname + ",\n"
			+ "\nIhrem Antrag auf eine Haertefallregelung bezüglich der von uns angekündigten Mieterhöhung wird von uns stattgegeben."
			+ "\nEs liegen demnach keine Aenderungen in Ihrem Mietpreis vor. Ihr aktueller Mietpreis von" + aktMiete + "Euro bleibt bis auf Weiteres unveraendert."
			+ "\n"
			+ "\n" + "\n\nMit freundlichen Grüßen,\n Wohnbaugesellschaft Lichtenberg";

//			FileValue retrievedTypedFileValue = execution.getVariableTyped("Mieterinformation");
//			InputStream fileContent = retrievedTypedFileValue.getValue(); // bytestream
//			String fileName = retrievedTypedFileValue.getFilename(); // filename
//			String mimeType = retrievedTypedFileValue.getMimeType(); // memetype
//			String encoding = retrievedTypedFileValue.getEncoding(); // encodung
//
//			MimeBodyPart attachment = new MimeBodyPart();
//			ByteArrayDataSource ds = new ByteArrayDataSource(fileContent, mimeType);
//
//			attachment.setDataHandler(new DataHandler(ds));
//			attachment.setFileName(fileName);

			sendEmail(mailtext, subject, toEmail);//, ds, fileName, encoding);
			
		}
		

	    /**
	    * Methode zum Ausführen der implementierten Geschäftlogik:
	    * Laden und Versenden einer Bestätigung zu einem Härtefallantrag
	    * 
	    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
	     * @throws EmailException 
	    */
		public void sendEmail(String mailtext, String subject, String toEmail) throws EmailException {//, ByteArrayDataSource ds, String fileName, String encoding) throws EmailException {
			
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
//			email.attach(ds, fileName, encoding);
			email.send();
			
			
		}
			
			

	}

/**
 * Klasse zum Erstellen einer Zustimmungserklärung zu einer Mieterhöhung
 * 
 * @email s0558029@htw-berlin.de,  s0559875@htw-berlin.de, s0558587@htw-berlin.de
 * @author Handan Fidan, Stefen Fröhlich, Christian Fiebelkorn
 * @since 2017-01-05
 * @version 1.0.0 - 05012018
 * 
 */

package MAS_Projekt.mieteErhoehen;

//import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import java.io.*;
import java.util.logging.Logger;


/**
 * Klasse zum Erstellen einer Zustimmungserklärung zu einer Mieterhöhung
 * 
 */
public class BescheidZustimmungErstellennachHaertefall implements JavaDelegate {
	
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	//alle executionVariablen als Klassenvariablen
	String name;
	String strasse;
	String plzOrt;
	String betreff;
	String text;
	String schluss;
	String mietvertragsID;
	double maximaleErhoehung;
	FileWriter fw;
	private String vorgangsid;
	private String mietvertragsnummer;
	private Double aktuelleMiete;
	private Double neueMiete;
	
	  
	public void execute(DelegateExecution execution) throws Exception {
		LOGGER.info("-> Zustimmungserklärung nach Härtefallregelunga\n");
		getExecutionVariablen(execution);
		LOGGER.info("-> BZEb\n");
		performGeschaeftslogik();
		LOGGER.info("-> BZEc\n");
		setExecutionVariablen(execution);
		LOGGER.info("-> BZE_Ende\n");
	}//E execute
	

    /**
    * Methode zum Ausführen der implementierten Geschäftlogik:
    * Erstellen einer Zustimmungserklärung zu einer Mieterhöhung
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void performGeschaeftslogik() {
		FileWriter fw;
		String anschrift = "Wohnbau Lichtenberg\n"+
				"Hauptstraße 34\n"+
				"12345 Berlin\n\n\n";
		String betreff = "Zustimmungserklärung zur Mietpreiserhöhung mit der ID " + vorgangsid;
		String text = 	"Bitte stimmen Sie der Mietpreiserhöhung binnen "
					+ "der in der EMail genannten Frist zu."
					+"\nSchicken Sie bitte dieses Schreiben an die oben genannte Adresse zurück." +
					"\n\nMieterhöhungsverlangen für Mietvertrag" + " " + mietvertragsnummer +
					"\nHiermit erkläre ich mein Einverständnis zur Mieterhöhung "
					+ "\nfür das genannte Mietobjekt um" + " " + maximaleErhoehung + "EUR" +
					" auf nun "+ neueMiete + " EUR zu."+
					"\n\n\nName ....................." + 
					"\n\n\nBerlin, den ..................." +
					"\n\n\n\n\n\n"+
					"Da dieser Bescheid nach einem bereits gestellten Härtefallantrag erzeugt wurde "
					+ "\nbesteht kein Recht mehr auf einen wieten Härtefallantrag\n\n";
		String schluss = "Mit freundlichen Grüßen" + "\nWohnbau Lichtenberg";
		
		try {
			LOGGER.info("-> BZE1\n");
			fw = new FileWriter("zustimmungserklaerung_n_Haertefall.txt");
			LOGGER.info("-> BZE2\n");
			fw.write(anschrift);
			fw.write("\n\n\n");
			fw.write(betreff);
			fw.write("\n");
			fw.write(text);
			fw.write("\n\n");
			fw.write(schluss);
			LOGGER.info("-> BZE3\n");
			fw.close();
			LOGGER.info("-> BZE4\n");
			
		}catch (Exception e) {};//FileNotFound- & IOException
	}//E performGeschaeftslogik
	
	
	/**
    * Methode zum Laden der notwendigen Vorgangsdaten
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void getExecutionVariablen(DelegateExecution execution) {
		vorgangsid = (String)  execution.getProcessInstanceId();
		mietvertragsnummer = (String)   execution.getVariable("Mietvertragsnummer");
		aktuelleMiete 		= (Double)   execution.getVariable("aktuelle_Miete");	
		
		LOGGER.info("-> BZEa1\n");
		name 		= (String)   execution.getVariable("name");	
		LOGGER.info("-> BZEa2\n");
		strasse 		= (String)   execution.getVariable("strasse");
		LOGGER.info("-> BZEa3\n");
		plzOrt 		= (String)   execution.getVariable("plzOrt");
		LOGGER.info("-> BZEa4\n");
		mietvertragsID 		= (String)   execution.getVariable("mietvertragsID");
		LOGGER.info("-> BZEa5\n");
		maximaleErhoehung 		= (double)   execution.getVariable("erhoehungHaertefallregelung");
		LOGGER.info("-> BZEa_E\n");
		neueMiete = Math.rint((aktuelleMiete+maximaleErhoehung)*100.0)/100.0;

	}//E getExecutionVariablen
	
	
	/**
    * Methode zum Speichern der geänderten Vorgangsdaten
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void setExecutionVariablen(DelegateExecution execution) {
		
		//execution.setVariable("executionVariable", klassenVariable);
		//...
	}//E setExecutionVariablen

}//E Class
/**
 * Klasse zum Erstellen einer Zustimmungserklärung zum Mieterhöhungsverlangen
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


/**
 * Klasse zum Erstellen einer Zustimmungserklärung zum Mieterhöhungsverlangen
 * 
 */
public class ZustimmungErstellen implements JavaDelegate {
	//alle executionVariablen als Klassenvariablen
	private String name;
	private String strasse;
	private String plzOrt;
	private String betreff;
	private String text;
	private String schluss;
	private String mietvertragsID;
	private double maximaleErhoehung;
	private String vorgangsid;
	private String mietvertragsnummer;
	private double neueMiete;
	private String haertefallantrag;
	FileWriter fw;
	  
	
    /**
    * Einstiegs-Methode zum Aufruf der Klasse
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	public void execute(DelegateExecution execution) throws Exception {
		
		getExecutionVariablen(execution);
		performGeschaeftslogik();
		setExecutionVariablen(execution);
	}//E execute
	

    /**
    * Methode zum Ausführen der implementierten Geschäftlogik:
    * Erstellen einer Zustimmungserklärung zum Mieterhöhungsverlangen
    * 
    */
	private void performGeschaeftslogik() {
	
		String anschrift = "Wohnbau Lichtenberg\n"+
							"Hauptstraße 34\n"+
							"12345 Berlin\n\n\n";
		String betreff = "Zustimmungserklaerung zur Mietpreiserhöhung mit der ID " + vorgangsid;
		String text = 	"Bitte stimmen Sie der Mietpreiserhöhung binnen "
						+ "\n 6 Wochen zu. Schicken Sie bitte dieses Schreiben an die obengenante Adresse zurück." +
						"\n\\nMieterhöhungsverlangen für Mietvertrag" + " " + mietvertragsnummer +
						"\nHiermit erklaere ich mein Einverständnis zur Mieterhöhung "
						+ "\nfuer das genannte Mietobjekt um" + " " + maximaleErhoehung + "EUR" +
						" auf nun "+ neueMiete + " EUR zu."+
						"\n\n\nName ....................." + 
						"\n\n\nBerlin, den ..................." +
						"\n\n\n\n\n\n"+
						"Sollten Sie aus finanziellen Gründen nicht in der Lage sein, die Erhöhung "
						+ "\nzu tragen, können Sie den Antrag auf Härtefallregelung ausfüllen und unterschreiben\n\n";
		haertefallantrag = "Antrag auf Härtefallregelung zum Mietvertrag "+ mietvertragsnummer 
						+"\nDieser kann nur akzeptiert werden, wenn er vollstängig ausgefüllt wurde\nund unterschrieben ist."
						+ "\n Vorgangsid: " + vorgangsid				
						+ "\nAnzahl der Bewohner der Wohnung: .........."
						+ "\nAnzahl der Kinder: ..........."
						+ "\nAnzahl der Einkommensbezieher: ..........."
						+ "\nSumme der Bruttoeinkomemn pro Jahr: ..........."
						+ "\nIst die Arbeitnehmerpauschale zu berücksichtigen? ..........."
						+ "\nIst die Einkommenssteuer zu berücksichtigen? ..........."
						+ "\nIst die Krankenversicherung zu berücksichtigen? ..........."
						+ "\nIst die Rentenversicherung zu berücksichtigen? ..........."
						+ "\n\n\nName ....................." 
						+ "\n\n\nBerlin, den ..................."
						;
		
		String schluss = "Mit freundlichen Grüßen" + "\nWohnbaugesellschaft Lichtenberg";
		
		try {
			fw = new FileWriter("zustimmungserklaerung.txt");
			fw.write(anschrift);
			fw.write("\n\n\n");
			fw.write(betreff);
			fw.write("\n");
			fw.write(text);
			fw.write("\n\n\n");
			fw.write(haertefallantrag);
			fw.write("\n\n\n");
			fw.write(schluss);
			
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
		neueMiete 		= (Double)   execution.getVariable("relevanteNeueMiete");	
			
		name 		= (String)   execution.getVariable("name");	
		strasse 		= (String)   execution.getVariable("strasse");
		plzOrt 		= (String)   execution.getVariable("plzOrt");
		mietvertragsID 		= (String)   execution.getVariable("mietvertragsID");
		maximaleErhoehung 		= (Double)   execution.getVariable("relevanteMieterhoehung");

	}//E getExecutionVariablen
	
	
    /**
    * Methode zum Speichern der geänderten Vorgangsdaten
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void setExecutionVariablen(DelegateExecution execution) {
		
//		execution.setVariable("zustimmungserklaerung", fw);
		
		//execution.setVariable("executionVariable", klassenVariable);
		//...
	}//E setExecutionVariablen

}//E Class
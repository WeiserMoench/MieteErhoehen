/**
 * Klasse zum Erstellen eines Mieterhöhungsbescheides
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
 * Klasse zum Erstellen eines Mieterhöhungsbescheidession
 * 
 */
public class BescheidErstellenNachHaertefall implements JavaDelegate {
	
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	
	private String anrede;
	private String name;
	private String strasse;
	private String plzOrt;
	private String betreff;
	private String text;
	private String tabelle;
	private String schluss;
	private double aktuelle_Miete;
	private double erhoehungMietespiegel;
	private double erhoehungSelbstverpflichtung;
	private double erhoehungHaertefall;
	private double relevanteNeueMiete;
	private FileWriter fw;
	private String vorname;
	private String nachname;
	  
	
    /**
    * Einstiegs-Methode zum Aufruf der Klasse
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	public void execute(DelegateExecution execution) throws Exception {
		LOGGER.info("-> BescheidErstellen nach Härtefallregelung\n");
		
		getExecutionVariablen(execution);
		LOGGER.info("-> BEnHa\n");
		geschaeftslogikBescheidErstellen();
		LOGGER.info("-> BEnHb\n");
		setExecutionVariablen(execution);
		LOGGER.info("-> BEnHc\n");
	}
	

    /**
    * Methode zum Ausführen der implementierten Geschäftlogik:
    * Erstellen eines Mieterhöhungsbescheides
    * 
    */
	private void geschaeftslogikBescheidErstellen() {


		String anschrift = 	anrede + "\n"+
							vorname + " " + nachname + "\n"+
							strasse + "\n"+
							plzOrt+ "\n\n\n";
		String betreff = "Mieterhöhungsverlangen gem. §§558-558e BGB";
		String text = 	"\nSehr geehrte/er " + anrede + " " + vorname + " " + nachname + ",\n"
				+ "\nHiermit bitten wir Sie gem.§§558-558e des BGB um Zustimmung zur Erhöhung der "
				+ "\nNettokaltmiete für Ihre Wohnung um" + " " + erhoehungHaertefall + " EUR"
				+ "\nWir bitten Sie, dies innerhalb der in der EMail genannten Frist zu tun.\n"
				+ "\nIhre Nettokaltmiete verändert sich damit von" + " " + aktuelle_Miete + "EUR" + " " + "auf" + " " + relevanteNeueMiete + "EUR"
				+ "\nDer Vermieter kann die Zustimmung zu einer Erhöhung der Nettokaltmiete vom Mieter verlangen, wenn\n"
				+ "		" + "1. die Nettokaltmiete seit einem Jahr unverändert ist (§558 Abs. 1 BGB) und\n"
				+ "		" + "2. die verlangte Nettomiete die üblichen Entgelte für Wohnraum im Land Berlin "
						+ "\nnicht übersteigt (§§558a und c BGB) und "
				+ "		" + "\ndie Nettokaltmiete sich innerhalb von drei Jahren um nicht mehr als 20% erhöht (§558 Abs. 3 BGB) \n\n"
				+ "\n\n Diese Mieterhöhung ist bereits auf Grundlage einer Härtefallregelung entstanden."		;
		
		String tabelle = "Ihre bisherige Nettokaltmiete:" + " "+ aktuelle_Miete +" EUR" +
						 "\nMietbegrenzung gem.§558 Abs.3 BGB und "
//						 + "\nfreiwilliger Selbsverpflichtung gem. Abschn.II." + erhoehungSelbstverpflichtung
						 + "\nfestgelegter monatlicher Erhöhungsbeitrag:"+ " " + erhoehungHaertefall +" EUR" +
						 "\nFestgelegte neue Nettokaltmiete:" + " " + relevanteNeueMiete + " EUR" ;
		
		String schluss = "Mit freundlichen Grüßen" + "\nWohnbau Lichtenberg";
		LOGGER.info("-> BEnH1\n");
		
		try {
			fw = new FileWriter("bescheid_Haertefall.txt");
			fw.write(anschrift);
			fw.write("\n\n\n");
			fw.write(betreff);
			fw.write("\n");
			fw.write(text);
			fw.write("\n");
			fw.write(tabelle);
			fw.write("\n\n\n");
			fw.write(schluss);
			LOGGER.info("-> BEnH2\n");
			fw.close();
			LOGGER.info("-> BEnH3\n");
			
		}catch (Exception e) {
			
		};//FileNotFound- & IOException

	}//E performGeschaeftslogik
	
	
    /**
    * Methode zum Laden der notwendigen Vorgangsdaten
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void getExecutionVariablen(DelegateExecution execution) {
		LOGGER.info("-> BEnHq1\n");
		anrede 		= (String)   execution.getVariable("Anrede");
		LOGGER.info("-> BEnHq2\n");
		vorname 		= (String)   execution.getVariable("VornameHauptmieter");
		LOGGER.info("-> BEnHq3\n");
		nachname		= (String)   execution.getVariable("NachnameHauptmieter");
		LOGGER.info("-> BEnHq4\n");
		strasse 		= (String)   execution.getVariable("StraßeHausnummer");
		LOGGER.info("-> BEnHq5\n");
		plzOrt 		= (String)   execution.getVariable("PLZOrt");
		LOGGER.info("-> BEnHq6\n");
		aktuelle_Miete 		= (double)   execution.getVariable("aktuelle_Miete");
		LOGGER.info("-> BEnHq7\n");
		erhoehungMietespiegel 		= (double)   execution.getVariable("mitteMieteMietspiegel"); 
//		erhoehungSelbstverpflichtung 		= (double)   execution.getVariable("erhoehungSelbstverpflichtung");
		erhoehungHaertefall 		= (double)   execution.getVariable("erhoehungHaertefallregelung");
		LOGGER.info("-> BEnHq8\n");
		relevanteNeueMiete 		= Math.rint((aktuelle_Miete+erhoehungHaertefall)*100.0)/100.0;
		LOGGER.info("-> BEnHq9\n");

	}//E getExecutionVariablen
	
	
    /**
    * Methode zum Speichern der geänderten Vorgangsdaten
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void setExecutionVariablen(DelegateExecution execution) {
		
		execution.setVariable("Bescheid_n_Haertefall", fw);
		
		//execution.setVariable("executionVariable", klassenVariable);
		//...
	}//E setExecutionVariablen

}//E Class
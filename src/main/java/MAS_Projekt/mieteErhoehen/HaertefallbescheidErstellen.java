/**
 * Klasse zum Erstellen eines Mieterhöhungsbescheides im Rahmen eines Härtefallantrages
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
 * Klasse zum Erstellen eines Mieterhöhungsbescheides im Rahmen eines Härtefallantrages
 * 
 */
public class HaertefallbescheidErstellen implements JavaDelegate {
	
	String anrede;
	String name;
	String strasse;
	String plzOrt;
	String betreff;
	String text;
	String tabelle;
	String schluss;
	double alteMiete;
	double erhoehungMietespiegel;
	double erhoehungSelbstverpflichtung;
	double maximaleErhoehung;
	double relevanteNeueMiete;	
	FileWriter fw;
	private String mietvertragsID;
	private String mietvertragsnummer;
	private String vorgangsid;
	private String vorname;
	private String nachname;
	private String haertefallantrag;
	  
	public void execute(DelegateExecution execution) throws Exception {
		
		getExecutionVariablen(execution);
		performGeschaeftslogik();
		setExecutionVariablen(execution);
	}
	

    /**
    * Methode zum Ausführen der implementierten Geschäftlogik:
    * Erstellen eines Mieterhöhungsbescheides im Rahmen eines Härtefallantrages
    * 
    */
	private void performGeschaeftslogik() {


		String anschrift = "Wohnbau Lichtenberg\n"+
				"Hauptstraße 34\n"+
				"12345 Berlin\n\n\n";
		
		String betreff = "Haertefallregelung nach dem Berliner Wohnraumversorgungsgesetz";
		String text = 	"\nSehr geehrte/er " + anrede + " " + vorname + " " + nachname + ",\n"
				+ "\nWir danken, für Ihr Schreiben, sowie die Einreichung Ihrer Unterlagen zum "
				+ "\nEinspruch gegen das Mieterhöhungsverlangen nach §§558-560e BGB "
				+ "\nNach sorgfältger Prüfung der von Ihnen eingereichten Unterlagen und der Miethöhe haben "
				+ "\n\nwir leider festgestellt, dass in Ihrem Fall die Voraussetzungen fuer eine Haertefallregelung "
				+ "\naus dem Berliner Wohnraumversorgungstreffen zutreffen."
				+ "\nWir setzen auf dieser Grundlage ohne Anerkennung einer Rechtsverpflichtung die Mieterhoehung auf diesem Grund Ihrer "
				+ "\npersönlichen Einkommensverhältnisse für einen Zeitraum von 12 Monaten aus."
				+ "\nWir machen Sie darauf aufmerksam, dass Sie aus der aktuellen Bewerbung Ihres Vertragsverhältnisses keinen Anspruch "
				+ "\nauf künftige Mietanpassungen gem. §§ 558-558e BGB ableiten können. Wir behalten uns jederzeit eine erneute Prüfung "
				+ "\nIhrer persönlichen Situation vor.";		
		
		haertefallantrag = "Antrag auf Härtefallregelung zum Mietvertrag "+ mietvertragsnummer 
				+"\nDieser kann nur akzeptiert werden, wenn er vollstängig ausgefüllt wurde\nund unterschrieben ist."
				+ "\n\n\nVorgangsid: " + vorgangsid				
				+ "\n\n\nAnzahl der Bewohner der Wohnung: .........."
				+ "\nAnzahl der Kinder: ..........."
				+ "\nAnzahl der Einkommensbezieher: ..........."
				+ "\nSumme der Bruttoeinkommen pro Jahr: ..........."
				+ "\nIst die Arbeitnehmerpauschale zu berücksichtigen? ..........."
				+ "\nIst die Einkommenssteuer zu berücksichtigen? ..........."
				+ "\nIst die Krankenversicherung zu berücksichtigen? ..........."
				+ "\nIst die Rentenversicherung zu berücksichtigen? ..........."
				+ "\n\n\nName ....................." 
				+ "\n\n\nBerlin, den ..................."
				;
		
		String schluss = "Mit freundlichen Grüßen" + "\nWohnbau Lichtenberg";
		
		try {
			fw = new FileWriter("haertefallbescheid.txt");
			fw.write(anschrift);
			fw.write("\n\n\n");
			fw.write(betreff);
			fw.write("\n");
			fw.write(text);
			fw.write("\n");
			fw.write(haertefallantrag);
			fw.write("\n\n");
			fw.write(schluss);
			fw.close();
			
		}catch (Exception e) {
			
		};//FileNotFound- & IOException

	}//E performGeschaeftslogik
	
	
    /**
    * Methode zum Laden der notwendigen Vorgangsdaten
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void getExecutionVariablen(DelegateExecution execution) {
		
		anrede 		= (String)   execution.getVariable("Anrede");
		strasse 		= (String)   execution.getVariable("StraßeHausnummer");
		plzOrt 		= (String)   execution.getVariable("PLZOrt");
		alteMiete 		= (double)   execution.getVariable("aktuelle_Miete");
		erhoehungMietespiegel 		= (double)   execution.getVariable("mitteMieteMietspiegel");
//		erhoehungSelbstverpflichtung 		= (double)   execution.getVariable("erhoehungSelbstverpflichtung");
		maximaleErhoehung 		= (double)   execution.getVariable("relevanteMieterhoehung");
		relevanteNeueMiete 		= (double)   execution.getVariable("relevanteNeueMiete");
		
		vorgangsid = (String)  execution.getProcessInstanceId();
		mietvertragsnummer = (String)   execution.getVariable("Mietvertragsnummer");
		vorname 		= (String)   execution.getVariable("VornameHauptmieter");
		nachname		= (String)   execution.getVariable("NachnameHauptmieter");
			
		mietvertragsID 		= (String)   execution.getVariable("mietvertragsID");
		maximaleErhoehung 		= (Double)   execution.getVariable("relevanteMieterhoehung");
		

	}//E getExecutionVariablen
	
	
    /**
    * Methode zum Speichern der geänderten Vorgangsdaten
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void setExecutionVariablen(DelegateExecution execution) {
		
//		execution.setVariable("haertefallbescheid", fw);
		
		//execution.setVariable("executionVariable", klassenVariable);
		//...
	}//E setExecutionVariablen

}//E Class
/**
 * Klasse zum Ermitteln der relevanten Mieterhöhungsgrenze aus
 * §558 BGB, ?????????????????Selbstverpflichtung???????????????????? und Mietspiegel
 * 
 * @email s0558029@htw-berlin.de,  s0559875@htw-berlin.de, s0558587@htw-berlin.de
 * @author Handan Fidan, Stefen Fröhlich, Christian Fiebelkorn
 * @since 2017-01-05
 * @version 1.0.0 - 05012018
 * 
 */

package MAS_Projekt.mieteErhoehen;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Date;
import java.util.logging.Logger;


/**
 * Klasse zum Ermitteln der relevanten Mieterhöhungsgrenze aus
 * §558 BGB, Selbstverpflichtung und Mietspiegel
 * 
 */
public class RelevanteErhoehung implements JavaDelegate {
	private double mieteRechtlich;
	private double mieteSelbstverpflichtung;
	private double mieteMietspiegel;
	private double mieteAktuell;
	private double relevanteNeueMiete;
	private double relevanteMieterhoehung;
	private String relevanterRahmen;
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	private Date datumDerGeplantenErhoehung;
	
	
    /**
    * Einstiegs-Methode zum Aufruf der Klasse
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		LOGGER.info("\n\n Start RelevanteErhoehung \n\n");
		
		getExecutionVariablen(execution);
		LOGGER.info("-> Ende: RelevanteErhoehung.getExecutionVariablen\n");
		erhoehungsDatum();
		evaluiereRelevanteMiete();
		LOGGER.info("-> Ende: RelevanteErhoehung.evaluiereRelevanteMiete\n");
		setExecutionVariablen(execution);
		LOGGER.info("-> Ende: RelevanteErhoehung.execute\n");
		

	}//E execute
	

    private void erhoehungsDatum() {
//		Date heute = new Date();
//    		datumDerGeplantenErhoehung = heute;
	}


	/**
    * Methode zum Ausführen der implementierten Geschäftlogik:
    * Ermitteln der relevanten Mieterhöhungsgrenze aus
    * §558 BGB, ??????????????????Selbstverpflichtung??????????????????????????? und Mietspiegel
    * 
    */
	private void evaluiereRelevanteMiete() {
		
//		if (mieteRechtlich >= mieteSelbstverpflichtung){
//			relevanteNeueMiete = mieteSelbstverpflichtung;
//			relevanteMieterhoehung = mieteSelbstverpflichtung - mieteAktuell;
//			relevanterRahmen = "rechtlich";
//		}
//		else{
//			relevanteNeueMiete = mieteRechtlich;
//			relevanteMieterhoehung = mieteRechtlich - mieteAktuell;
//			relevanterRahmen = "Selbstverpflichtung";
//		}
		
		//neu
		relevanteNeueMiete= mieteRechtlich;
		relevanterRahmen = "rechtlich";
		relevanteMieterhoehung = mieteRechtlich - mieteAktuell;
		
		
		if (relevanteNeueMiete > mieteMietspiegel){
			relevanteNeueMiete = mieteMietspiegel;
			relevanteMieterhoehung = mieteMietspiegel - mieteAktuell;
			relevanterRahmen = "Mietspiegel";
		}
	}//E evaluiereMiete
	
	
    /**
    * Methode zum Laden der notwendigen Vorgangsdaten
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void getExecutionVariablen(DelegateExecution execution) {
		
		mieteRechtlich 			= (double) execution.getVariable("moeglicheNeueMiete");
//		mieteSelbstverpflichtung= (double) execution.getVariable("neueMieteSelbstverpflichtung");
		mieteMietspiegel		= (double) execution.getVariable("mitteMieteMietspiegel");
		mieteAktuell			= (double) execution.getVariable("aktuelle_Miete");
	}//E getExecutionVariablen
	
	
    /**
    * Methode zum Speichern der geänderten Vorgangsdaten
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void setExecutionVariablen(DelegateExecution execution) {
		
		execution.setVariable("relevanteNeueMiete", relevanteNeueMiete);
		execution.setVariable("relevanteMieterhoehung", Math.rint(relevanteMieterhoehung*100.0)/100.0);
		execution.setVariable("relevanterRahmen", relevanterRahmen);
		execution.setVariable("Abbruchmeldung", "Erhoehung waere unter 5 Euro pro Monat");
		
	}//E setExecutionVariablen

}//E Class
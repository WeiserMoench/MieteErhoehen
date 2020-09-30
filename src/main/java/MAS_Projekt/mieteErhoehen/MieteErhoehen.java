/**
 * Klasse zum Festschreiben der Mieterhöhungsdaten in der Datenbank
 * 
 * @email s0558029@htw-berlin.de,  s0559875@htw-berlin.de, s0558587@htw-berlin.de
 * @author Handan Fidan, Stefen Fröhlich, Christian Fiebelkorn
 * @since 2017-01-05
 * @version 1.0.0 - 05012018
 * 
 */

package MAS_Projekt.mieteErhoehen;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


/**
 * Klasse zum Festschreiben der Mieterhöhungsdaten in der Datenbank
 * 
 */
public class MieteErhoehen implements JavaDelegate {
	private Connection connection = null;
	  
	
    /**
    * Einstiegs-Methode zum Aufruf der Klasse,
    * zum Laden der benötigten Daten und
    * zum Aufruf untergeordneter Methoden
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		double neueMiete;
		double aktuelleMiete;
		int wohnungs_id;
		int mietvertrags_id;
		int anzahlMieterhoehungen;
		double mieterhoehung;
		
		wohnungs_id = (Integer) execution.getVariable("Wohnungs_ID");
		mietvertrags_id = (Integer) execution.getVariable("Mietvertrags_ID");
		neueMiete = (Double) execution.getVariable("relevanteNeueMiete");
		aktuelleMiete = (Double) execution.getVariable("aktuelle_Miete");
		mieterhoehung = (Double) execution.getVariable("relevanteMieterhoehung");
		
		connection = BaueDBVerbindung.getConnection();
		
		anzahlMieterhoehungen = anzahlMieterhoehungenBestimmen();
		insertMieterhoehung(mietvertrags_id, anzahlMieterhoehungen, aktuelleMiete, neueMiete, mieterhoehung);
		updateWohnung(wohnungs_id, neueMiete);
		

	}
	  
	
    /**
    * Methode zum Bestimmen der höhsten ÄnderungsID der Mieterhöhungen
    */
	private int anzahlMieterhoehungenBestimmen() {
		String query = "SELECT MAX(ID) AS AKTUELLES_MAX FROM mieterhoehung";
		PreparedStatement preparedstatement = null;
		ResultSet result = null;
		
		try {
			preparedstatement = connection.prepareStatement(query);
			result = preparedstatement.executeQuery();
			if(result.next()) {
				return result.getInt("AKTUELLES_MAX");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
    /**
    * Methode zum Festschreiben der Mieterhöhungsdaten in der Datenbank
    * 
    * @param mietvertrags_id (int): Datenbank-ID des Mietvertrages
    * @param anzahlMieterhoehungen (int): Anzahl der Mieterhöhungen zum Mietvertrag
    * @param aktuelleMiete (double): derzeitige Miete in Euro
    * @param neueMiete (double): Miete nach Mieterhöhung in Euro
    * @param mieterhoehung (double): Höhe der Mieterhöhung in Euro
    */
	private void insertMieterhoehung(int mietvertrags_id, int anzahlMieterhoehungen, double aktuelleMiete, double neueMiete, double mieterhoehung) {
		String query = "INSERT INTO mieterhoehung (mietvertrag_id,id,datum,altemiete,neuemiete,erhoehung) "
				+ "VALUES (?,?,?,?,?,?)";
		PreparedStatement preparedstatement = null;
		int neueID = anzahlMieterhoehungen+1;
		aktuelleMiete= Math.rint(aktuelleMiete*100.0)/100.0;
		neueMiete=Math.rint(neueMiete*100.0)/100.0;
		mieterhoehung=Math.rint(mieterhoehung*100.0)/100.0;
		
		try {
			preparedstatement = connection.prepareStatement(query);
			preparedstatement.setInt(1, mietvertrags_id);
			preparedstatement.setInt(2, neueID);
			preparedstatement.setDate(3, new Date(new java.util.Date().getTime()));
			preparedstatement.setDouble(4, aktuelleMiete);
			preparedstatement.setDouble(5, neueMiete);
			preparedstatement.setDouble(6, mieterhoehung);
			preparedstatement.executeUpdate();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	
    /**
    * Methode zum Festschreiben der Mieterhöhung im Wohnungsdatensatz der Datenbank
    * 
    * @param mwohnungs_id (int): Datenbank-ID der Wohnung
    * @param neueMiete (double): Miete nach Mieterhöhung in Euro
    */
	private void updateWohnung(int wohnungs_id, double neueMiete) {
		String query = "UPDATE wohnung SET mietpreis = ? WHERE ID = ?";
		PreparedStatement preparedstatement = null;
		
		try {
			preparedstatement = connection.prepareStatement(query);
			preparedstatement.setDouble(1, neueMiete);
			preparedstatement.setInt(2, wohnungs_id);
			preparedstatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	

}

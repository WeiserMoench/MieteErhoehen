/**
 * Klasse zum Ermitteln der Mietspiegeldaten der einer Wohnung
 * 
 * @email s0558029@htw-berlin.de,  s0559875@htw-berlin.de, s0558587@htw-berlin.de
 * @author Handan Fidan, Stefen Fröhlich, Christian Fiebelkorn
 * @since 2017-01-05
 * @version 1.0.0 - 05012018
 * 
 */

package MAS_Projekt.mieteErhoehen;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


/**
 * Klasse zum Ermitteln der Mietspiegeldaten der einer Wohnung
 * 
 */
public class ErhoehungMietspiegel implements JavaDelegate {
	private int wohnungsID;
	private double wohnflaeche;
	private int mietspiegelID;
	private String bezeichnungMietspiegelKategorie;
	private double qmMieteMin;
	private double qmMieteMitt;
	private double qmMieteMax;
	

    /**
    * Einstiegs-Methode zum Aufruf der Klasse
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		getExecutionVariablen(execution);
		getMietspiegelDaten(wohnungsID);
		setExecutionVariablen(execution);
	}//E execute
	

    /**
    * Methode zum Ausführen der implementierten Geschäftlogik:
    * rmitteln der Mietspiegeldaten der einer Wohnung
    * 
    * @param wohnungsID (int): Datenbank-ID der betroffenen Wohnung
    */
	private void getMietspiegelDaten(int wohnungsID) {
		String sql = "select MS.ID AS ID, MS.BEZEICHNUNG AS BEZEICHNUNG, MS.UNTERERPREIS AS UNTERERPREIS, "
							+ "MS.MITTLERERPREIS AS MITTLERERPREIS, MS.OBERERPREIS AS OBERERPREIS "
							+ "from MIETSPIEGEL MS "
							+ "inner join WOHNUNG W on MS.ID = W.MIETSPIEGEL_ID "
							+ "where W.ID = ?";
		
		try {//(Connection connection = baueDBVerbindung.getConnection()){
			Connection connection = BaueDBVerbindung.getConnection();
			try {//(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, wohnungsID);
				try {//(ResultSet resultset = preparedStatement.executeQuery()){
					ResultSet resultset = preparedStatement.executeQuery();
					if (resultset.next()) {
						mietspiegelID = resultset.getInt("ID");
						bezeichnungMietspiegelKategorie = resultset.getString("BEZEICHNUNG");
						qmMieteMin = resultset.getDouble("UNTERERPREIS");
						qmMieteMitt = resultset.getDouble("MITTLERERPREIS");
						qmMieteMax = resultset.getDouble("OBERERPREIS");
					} else {
						throw new Exception("Mietspiegel-ID konnte nicht verarbeitet werden: Wohnungs-ID " + String.valueOf(wohnungsID));
					}
				}//E Resultset 
				finally {}
			}//E preparedStatement
			finally {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}//E Connection
		
	}//E getMietspiegelData
	
	
    /**
    * Methode zum Laden der notwendigen Vorgangsdaten
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void getExecutionVariablen(DelegateExecution execution) {
		wohnungsID 		= (int)   execution.getVariable("Wohnungs_ID");
		wohnflaeche		= (double) execution.getVariable("Nettowohnflaeche");
	}//E getExecutionVariablen
	
	
    /**
    * Methode zum Speichern der geänderten Vorgangsdaten
    * 
    * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
    */
	private void setExecutionVariablen(DelegateExecution execution) {
		
//		execution.setVariable("mietspiegel_ID", mietspiegelID);
		execution.setVariable("bezeichnungMietspiegelKategorie", bezeichnungMietspiegelKategorie);
		execution.setVariable("minQmMieteMietspiegel", qmMieteMin);
		execution.setVariable("mitteQmMieteMietspiegel", qmMieteMitt);
		execution.setVariable("maxQmMieteMietspiegel", qmMieteMax);
		execution.setVariable("minMieteMietspiegel", Math.rint(qmMieteMin * wohnflaeche*100.0)/100.0);//Math.rint(d*100.0)/100.0
		execution.setVariable("mitteMieteMietspiegel", Math.rint(qmMieteMitt * wohnflaeche*100.0)/100.0);
		execution.setVariable("maxMieteMietspiegel", Math.rint(qmMieteMax * wohnflaeche*100.0)/100.0);
	}//E setExecutionVariablen

}//E Class
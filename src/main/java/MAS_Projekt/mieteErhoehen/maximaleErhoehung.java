/**
 * Klasse zum Ermitteln der max. möglichen Mieterhoehung nach 
 * §558 BGB
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
import java.sql.SQLException;
import java.util.Date;
import java.util.TreeMap;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * Klasse zum Ermitteln der max. möglichen Mieterhoehung nach
 * 
 */
public class maximaleErhoehung implements JavaDelegate {
	private Connection connection = BaueDBVerbindung.getConnection();
	private Date datum = null; // letzte Erhoehung aelter als 3 Jahre, oder letztes vorhandenes
	private double maximaleErhoehung;
	private double maximaleNeueMiete;

	/**
	 * Einstiegs-Methode zum Aufruf der Klasse, zum Laden der benötigten Daten und
	 * zum Speichern der ermittelten Mietdaten
	 * 
	 * @param execution
	 *            (DelegateExecution): Datenstruktur mit Daten des Vorgangs
	 */
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		TreeMap<Date, Double> mietdaten = new TreeMap<Date, Double>();
		int mietvertrags_id;
		double aktuelleMiete; //
		double anfangsmiete; // Miete vor 3 Jahren, bzw am Abschlussdatum

		mietvertrags_id = (int) execution.getVariable("Mietvertrags_ID");
		aktuelleMiete = (double) execution.getVariable("aktuelle_Miete");

		anfangsmiete = anfangsMieteBestimmen(trageMietdatenInHM(mietvertrags_id));
		maximaleErhoehungBestimmen(aktuelleMiete, anfangsmiete);

		execution.setVariable("maximaleErhoehung", Math.rint(maximaleErhoehung * 100.0) / 100.0);
		execution.setVariable("moeglicheNeueMiete", Math.rint(maximaleNeueMiete * 100.0) / 100.0);
		execution.setVariable("DatumDerAnfangsmiete", datum);
		execution.setVariable("miete3JahreAlt", anfangsmiete);

	}

	/**
	 * Methode zum Berechnen der nach §558 BGB maximal möglichen Mieterhöhung
	 * 
	 * @param aktuelleMiete
	 *            (double): derzeitige Miete
	 * @param anfangsmiete
	 *            (double): Miete zu Beginn des Begrenzungszeitraumes nach §558 BGB
	 */
	private void maximaleErhoehungBestimmen(double aktuelleMiete, double anfangsmiete) {

		maximaleErhoehung = anfangsmiete * 1.2D - aktuelleMiete;
		maximaleNeueMiete = anfangsmiete * 1.2D;

		/**
		 * http://www.stadtentwicklung.berlin.de/wohnen/mieterfibel/de/m_miete0.shtml
		 * Berliner Verordnung: max 15 % in 3 Jahren lassen als 20%
		 * MieterhöhungsVERLANGEN min 12 Monate nach letzter Erhöhung MietERHÖHUNG min
		 * 15 Monate nach letzter Erhöhung
		 */

	}

	/**
	 * Methode zum Berechnen der Miete zu Beginn des Begrenzungszeitraumes nach §558
	 * BGB
	 * 
	 * @param mietdaten
	 *            (TreeMap<Date, Double> mietdaten): Datenstruktur mit Mietdaten zum
	 *            Vorgang
	 */
	private double anfangsMieteBestimmen(TreeMap<Date, Double> mietdaten) {
		double anfangsmiete = 0;
		long dreiJahreInMS = 94608000000L;
		long zeitdifferenz = 0;
		Date heute = new Date();
		boolean erfuellt = true;

		Date lastDate = mietdaten.firstKey();
		double lastMiete = mietdaten.get(lastDate);

		for (Date key : mietdaten.keySet()) {

			while (erfuellt == true) {

				zeitdifferenz = heute.getTime() - key.getTime();

				if (zeitdifferenz > dreiJahreInMS) {
					datum = key;
					anfangsmiete = mietdaten.get(key);
					erfuellt = true;
					lastDate = key;
					lastMiete = mietdaten.get(key);
					break;
				} else {
					datum = lastDate;
					anfangsmiete = lastMiete;
					erfuellt = false;
				}
			}
		}

		return anfangsmiete;
	}

	/**
	 * Methode zum Ermitteln von Mietdaten zum Vorgang
	 * 
	 * @return mietdaten (TreeMap<Date, Double> mietdaten): Datenstruktur mit
	 *         Mietdaten zum Vorgang
	 */
	private TreeMap<Date, Double> trageMietdatenInHM(int mietvertragsid) {
		TreeMap<Date, Double> mietdaten = new TreeMap<Date, Double>();
		String query = "SELECT datum, neuemiete " + "FROM mieterhoehung " + "WHERE mietvertrag_id = ? " + "UNION "
				+ "SELECT gueltigab, mietpreisbeiabschluss " + "FROM mietvertrag " + "WHERE ID = ?";
		PreparedStatement preparedstatement = null;
		ResultSet result = null;

		try {
			preparedstatement = connection.prepareStatement(query);
			preparedstatement.setInt(1, mietvertragsid);
			preparedstatement.setInt(2, mietvertragsid);
			result = preparedstatement.executeQuery();
			while (result.next()) {
				mietdaten.put(result.getDate("DATUM"), result.getDouble("NEUEMIETE"));
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return mietdaten;

	}

}

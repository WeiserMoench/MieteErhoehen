/**
 * Klasse zum Laden der notwendigen Mieter- Miet- und Wohnungsdaten aus der Datenbank
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * Klasse zum Laden der notwendigen Mieter- Miet- und Wohnungsdaten aus der
 * Datenbank
 * 
 */
public class PruefenMitDB implements JavaDelegate {

	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	// private String mietvertragsNr;
	// private int abbruchmeldung;
	private Connection connection;
	private String nachnamehauptmieter;
	private String vornamehauptmieter;
	private String statusmietvertrag;
	private Date abschluss;
	private int wohnungsId;
	private String email;
	private String straßeHausnummer;
	private String plzOrt;
	private double nettowohnflaeche;
	private int anzahlZimmer;
	private double mietpreis;
	private int mietvertrag_id;
	private Date datumLetzteErhoehung;
	private Calendar heute = new GregorianCalendar();
	private int mietspiegelID;
	private String anrede;

	/**
	 * Einstiegs-Methode zum Aufruf der Klasse, zur Steuerung der Klasse und zum
	 * Speichern der geänderten Daten
	 * 
	 * @param execution
	 *            (DelegateExecution): Datenstruktur mit Daten des Vorgangs
	 */
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String mietvertragsNr = (String) execution.getVariable("Mietvertragsnummer");

		LOGGER.info("\n\n  ... LoggerDelegate invoked by PRUEFEN_MIT_DB" + "processDefinitionId="
				+ execution.getProcessDefinitionId() + ", activtyId=" + execution.getCurrentActivityId()
				+ ", activtyName='" + execution.getCurrentActivityName() + "'" + ", processInstanceId="
				+ execution.getProcessInstanceId() + ", businessKey=" + execution.getProcessBusinessKey()
				+ ", executionId=" + execution.getId() + " \n\n");
		LOGGER.info("-> connection aufbauen...\n");
		connection = BaueDBVerbindung.getConnection();
		LOGGER.info("-> connection aufgebaut.\n");
		if (pruefeMietvertragVorhanden(mietvertragsNr)) {
			leseDBZuMietvertrag(mietvertragsNr);
			leseWohnungsdaten();
			if (statusmietvertrag.equals("inactive")) {
				execution.setVariable("Abbruchmeldung", "Mietvertragsnummer nicht mehr aktiv");
				execution.setVariable("erhoehungMoeglich", false);
			} else {
				execution.setVariable("Anrede", anrede);
				execution.setVariable("statusmietvertrag", statusmietvertrag);
				execution.setVariable("NachnameHauptmieter", nachnamehauptmieter);
				execution.setVariable("VornameHauptmieter", vornamehauptmieter);
				execution.setVariable("Abschluss", abschluss);
				execution.setVariable("E-Mail", email);
				execution.setVariable("StraßeHausnummer", straßeHausnummer);
				execution.setVariable("PLZOrt", plzOrt);
				execution.setVariable("Nettowohnflaeche", Math.rint(nettowohnflaeche * 100.0) / 100.0);
				execution.setVariable("AnzahlZimmer", anzahlZimmer);
				execution.setVariable("aktuelle_Miete", Math.rint(mietpreis * 100.0) / 100.0);
				execution.setVariable("erhoehungMoeglich", true);
				execution.setVariable("Mietvertrags_ID", mietvertrag_id);
				execution.setVariable("Wohnungs_ID", wohnungsId);
				execution.setVariable("Mietspiegel_ID", mietspiegelID);

				if (!vergleicheDatumDerLetztenErhoehung(mietvertrag_id)) {
					execution.setVariable("Abbruchmeldung", "Miete kann zur Zeit nicht erhoeht werden");
					execution.setVariable("erhoehungMoeglich", false);

				} else {
					execution.setVariable("DatumLetzteErhoehung", datumLetzteErhoehung);
					execution.setVariable("aktuelles_Datum", heute);
				}
			}
		} else {
			execution.setVariable("Abbruchmeldung", "Mietvertragsnummer nicht vorhanden");
			execution.setVariable("erhoehungMoeglich", false);

		}

		LOGGER.info("-> Ende: pruefenMitDB.execute\n");

	}

	/**
	 * Diese Methode fragt das Datum der letzten Mietpreiserhoehung ab und prueft ob
	 * dieses aelter als 12 Monate ist. Ebenso wird gleichzeitig geprueft ob die
	 * Zeit zwischen Einzug und und heute ausreichend waere.
	 * 
	 * @param mietvertrag_id
	 *            - ID des zupruefenden Mietvertrages
	 * @return boolean, der angibt ob die letzte aktualisierung aelter als 12 Monate
	 *         ist
	 */
	private boolean vergleicheDatumDerLetztenErhoehung(int mietvertrag_id) {
		Date heute = new Date();
		boolean ergebnis = false;
		String query = "SELECT MAX(datum) AS datum " + "FROM (SELECT MAX(datum) AS datum "
				+ "FROM mieterhoehung WHERE mietvertrag_id=? "
				+ "UNION SELECT abschluss AS datum FROM mietvertrag WHERE ID=?)";
		PreparedStatement preparedstatement = null;
		ResultSet result = null;
		LOGGER.info("-> Beginn: vergleicheDatumDerLetztenErhoehung.try\n");
		try {
			preparedstatement = connection.prepareStatement(query);
			preparedstatement.setInt(1, mietvertrag_id);
			preparedstatement.setInt(2, mietvertrag_id);
			result = preparedstatement.executeQuery();
			if (result.next()) {
				datumLetzteErhoehung = result.getDate("datum");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(heute.getTime());
		long differenz = (long) ((heute.getTime() - datumLetzteErhoehung.getTime()) / 10000);
		if ((differenz) >= ((60 * 60 * 24 * 365 / 10) + 1)) {
			ergebnis = true;
		} else {
			ergebnis = false;
		}

		LOGGER.info("-> Ende: vergleicheDatumDerLetztenErhoehung\n");
		return ergebnis;
	}

	/**
	 * Diese Methode liest alle benoetigten Daten aus der Tabelle Wohnung aus.
	 */
	private void leseWohnungsdaten() {
		String query = "SELECT STRASSEHAUSNUMMER, PLZORT, NETTOWOHNFLAECHE, ZIMMER, MIETPREIS, MIETSPIEGEL_ID FROM WOHNUNG WHERE ID = ?";
		PreparedStatement preparedstatement = null;
		ResultSet result = null;
		LOGGER.info("-> Beginn: leseWohnungsdaten.try\n");
		try {
			preparedstatement = connection.prepareStatement(query);
			preparedstatement.setInt(1, wohnungsId);
			result = preparedstatement.executeQuery();
			if (result.next()) {
				straßeHausnummer = result.getString("STRASSEHAUSNUMMER");
				plzOrt = result.getString("PLZORT");
				nettowohnflaeche = result.getFloat("NETTOWOHNFLAECHE");
				anzahlZimmer = result.getInt("ZIMMER");
				mietpreis = result.getFloat("MIETPREIS");
				mietspiegelID = result.getInt("MIETSPIEGEL_ID");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		LOGGER.info("-> Ende: leseWohnungsdaten\n");
	}

	/**
	 * Diese Methode prueft, ob ein Mietvertrag mit dieser Nummer vorhanden ist.
	 * 
	 * @param mietvertragsNr
	 *            - Nummer des zu pruefenden Mietvertrages
	 * @return boolean
	 */
	private boolean pruefeMietvertragVorhanden(String mietvertragsNr) {

		String query = "SELECT Count(ID) as Anzahl FROM MIETVERTRAG WHERE MIETVERTRAGSNR = ?";
		PreparedStatement preparedstatement = null;
		ResultSet result = null;
		LOGGER.info("-> Beginn: pruefeMietvertrag.try\n");
		try {
			if (connection == null) {
				LOGGER.warning("CONNECTION IS NULL");
			}
			preparedstatement = connection.prepareStatement(query);
			preparedstatement.setString(1, mietvertragsNr);
			result = preparedstatement.executeQuery();
			if (result.next()) {
				return result.getInt("Anzahl") > 0;
				// } else {
				// abbruchmeldung = 1;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		LOGGER.info("-> Ende: pruefeMietvertrag\n");
		return false;

	}

	/**
	 * Diese Methode liest alle Daten aus der Tabelle Mietvertrag aus
	 * 
	 * @param mietvertragsNr
	 *            - Nummer des Mietvertrages, aus dem die Daten ausgelesen werden
	 *            sollen
	 */
	private void leseDBZuMietvertrag(String mietvertragsNr) {
		String query = "SELECT ID, ANREDE, NACHNAMEHAUPTMIETER, VORNAMEHAUPTMIETER, STATUS, ABSCHLUSS, WOHNUNG_ID,EMAIL "
				+ "FROM MIETVERTRAG " + "WHERE MIETVERTRAGSNR = ?";
		PreparedStatement preparedstatement = null;
		ResultSet result = null;
		LOGGER.info("-> Beginn: leseDBZuMietvertrag.try\n");
		try {
			preparedstatement = connection.prepareStatement(query);
			preparedstatement.setString(1, mietvertragsNr);
			result = preparedstatement.executeQuery();
			if (result.next()) {
				mietvertrag_id = result.getInt("ID");
				anrede = result.getString("ANREDE");
				nachnamehauptmieter = result.getString("NACHNAMEHAUPTMIETER");
				vornamehauptmieter = result.getString("VORNAMEHAUPTMIETER");
				statusmietvertrag = result.getString("STATUS");
				abschluss = result.getDate("ABSCHLUSS");
				wohnungsId = result.getInt("WOHNUNG_ID");
				email = result.getString("EMAIL");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		LOGGER.info("-> Ende: leseDBZuMietvertrag\n");
	}

}

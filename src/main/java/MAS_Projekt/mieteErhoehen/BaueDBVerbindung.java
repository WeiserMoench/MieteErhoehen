/**
 * Diese Klasse baut die benoetigte Datenbankverbindung auf und stellt sie anderen Methoden per getConnection zur Verfuegung
 * 
 * @email s0558029@htw-berlin.de,  s0559875@htw-berlin.de, s0558587@htw-berlin.de
 * @author Handan Fidan, Stefen Fröhlich, Christian Fiebelkorn
 * @since 2017-12-25
 * @version 1.0.0 - 02012018
 * 
 */

package MAS_Projekt.mieteErhoehen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Diese Klasse baut die benoetigte Datenbankverbindung auf und stellt sie anderen Methoden per getConnection zur Verfuegung
 * 
 */
public class BaueDBVerbindung {
	final static Connection connection = baueDBVerbindung();
	
	
    /**
    * Methode liefert eine Datenbank-Verbindeung
    * 
    */
	public static Connection getConnection() {
		return connection;
	}


    /**
    * Methode zum Setzen eine Datenbankverbindung
    * 
    * @param connection (Connection): enthält die Daten für die zu setzende Datenbank-Verbindung
    */
	public void setConnection(Connection connection) {
		
		connection = connection;
	}


    /**
    * Methode baut eine Datenbankverbindung auf und giebt sie an die aufrufende Methode zurück
    * 
    * @return connection (Connection): aufgebaute Datenbank-Verbindung
    */
	private static Connection baueDBVerbindung() {
		Connection connection = null;
		final String driverClass = "oracle.jdbc.driver.OracleDriver";
		final String url = "jdbc:oracle:thin:@oradbs02.f4.htw-berlin.de:1521:orcl";
		final String user = "u558587";
		final String password = "558587p";

		try {
			Class.forName(driverClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
		

	}

}

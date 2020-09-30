/**
 * Klasse zum Tesetn während der Entwicklung
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


/**
 * Klasse zum Tesetn während der Entwicklung
 * 
 */
public class test implements JavaDelegate {


	/**
	 * Methode zum Tesetn während der Entwicklung
	 * 
	 */
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		execution.setVariable("Muell", "Muell");
		execution.setVariable("mietspiegel_ID", 6);
		
	}

}

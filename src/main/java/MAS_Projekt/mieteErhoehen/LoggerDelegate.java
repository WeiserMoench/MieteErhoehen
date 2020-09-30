/**
 * This is an easy adapter implementation 
 * illustrating how a Java Delegate can be used 
 * from within a BPMN 2.0 Service Task.
 * 
 * @author Camunda Services GmbH
 */

package MAS_Projekt.mieteErhoehen;

import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


/**
 * This is an easy adapter implementation 
 * illustrating how a Java Delegate can be used 
 * from within a BPMN 2.0 Service Task.
 */
public class LoggerDelegate implements JavaDelegate {
  private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
  
  
  /**
  * Einstiegs-Methode zum Aufruf der Klasse und zum Ausgeben von Log-Daten
  * 
  * @param execution (DelegateExecution): Datenstruktur mit Daten des Vorgangs
  */
  public void execute(DelegateExecution execution) throws Exception {
    
    LOGGER.info("\n\n  ... LoggerDelegate invoked by "
            + "processDefinitionId=" + execution.getProcessDefinitionId()
            + ", activtyId=" + execution.getCurrentActivityId()
            + ", activtyName='" + execution.getCurrentActivityName() + "'"
            + ", processInstanceId=" + execution.getProcessInstanceId()
            + ", businessKey=" + execution.getProcessBusinessKey()
            + ", executionId=" + execution.getId()
            + " \n\n");
    
  }

}

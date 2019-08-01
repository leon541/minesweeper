package ms.view;

import ms.controller.Controller;
import ms.model.Model;
/**
 * This is the interface exposed mainly to Controller and Game.
 *  
 * @author lwang
 */
public interface View {
	
		
	/**
	 * inject the controller
	 * @param controller
	 */
	public void setController(Controller controller);
	
	public void setModel(Model model);
}

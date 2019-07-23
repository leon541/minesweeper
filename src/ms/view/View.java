package ms.view;

import ms.controller.Controller;

/**
 * This is the interface exposed mainly to Controller and Game.
 *  
 * @author lwang
 */
public interface View {
	
		
	/**
	 *  update cell image, used by Controller 
	 * 
	 * @param row  
	 * @param col
 	 * @param cover   (0: uncovered, 1: cover, 2: flag, 3: ? ) more detailed 
	 * @param under   (-1: mine, 0 - 8: number of mines, )  
	 * 				   -2: mine fired	
	 * @return   0: init 
	 *           1: undergoing
	 *           2: win
	 *           3: lose
	 */
	public int updateCell(int row, int col, int cover, int under);
	
	/**
	 * update counter value, used by Controller
	 * @param numberOfMines
	 */
	public void updateCounter(int numberOfMines);
	
	/**
	 * inject the controller
	 * @param controller
	 */
	public void setController(Controller controller);
}

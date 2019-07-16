/**
 * 
 */
package ms.view;

import ms.controller.Controller;

/**
 * @author lwang
 *
 */
public interface View {
	
	/**
	 * initialize the view 
	 * 
	 * @param rows
	 * @param cols
	 * @param mines
	 */
	public void init(int level, int rows, int cols, int mines);
	
	/**
	 * 
	 * @param level
	 */
	public void init(int level);
		
	/**
	 *  update cell image, used by Controller 
	 * 
	 * @param row  
	 * @param col
 	 * @param cover   (0: uncovered, 1: cover, 2: flag, 3: ? )
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
	 * 
	 * @param controller
	 */
	public void setController(Controller controller);
}

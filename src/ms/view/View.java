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
	public void init(int rows, int cols, int mines);
	
		
	/**
	 * reset the view
	 */
	public void reset();
	
	/**
	 *  update cell 
	 */
	
	/**
	 * 
	 * @param row  
	 * @param col
 	 * @param cover   (0: uncovered, 1: cover, 2: flag, 3: ? )
	 * @param under   (-1: mine, 0 - 8: number of mines, 9: mine fired)  
	 * @return
	 */
	public int updateCell(int row, int col, int cover, int under);
	/**
	 * 
	 * @param controller
	 */
	public void setController(Controller controller);
}

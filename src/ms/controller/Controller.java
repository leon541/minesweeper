package ms.controller;

public interface Controller {
	/**
	 * 
	 *  view uses this method to pass the user action
	 *  
	 * @param row
	 * @param col
	 * @param type    0: left, 1: right, 2: left + right
	 * @return        
	 */
	void  clickedGrid(int row, int col, int type) ;
	
	/**
	 * 
	 * @param rows
	 * @param cols
	 * @param mines
	 */
	void configure(int rows, int cols, int mines);
	
	/**
	 * 
	 */
	void start();

}

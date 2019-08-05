package ms.controller;

public interface Controller {
	/**
	 * 
	 *  view uses this method to pass the user action
	 *  
	 * @param row    row
	 * @param col    column
	 * @param type    0: left, 1: right, 2: left + right
	 */
	void  clickedGrid(int row, int col, int type) ;
	
	/**
	 * 
	 * @param rows   number of rows
	 * @param cols   number of columns
	 * @param mines  number of mines
	 */
	void configure(int rows, int cols, int mines);
	
	/**
	 * 
	 */
	void start();

}

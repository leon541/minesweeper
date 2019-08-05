package ms.model;

/**
 * @author      Preston, Robert (RP1211) 
 */
public interface Model{		
	/**
	 * Retrieve the number of rows of the field.
	 *
	 * @return		The number of rows the fields have.
	 */
	public int getRows();
	//public void setRows(int rows);
	
	/**
	 * Retrieve the number of columns of the field.
	 * 
	 * @return		The number of columns the fields have.
	 */
	public int getColumns();
	//public void setColumns(int columns);
	
	/**
	 * Retrieve the current number of flags available for the field.
	 * 
	 * @return		The current number of available flags.
	 */
	public int getNumFlags();
	//public void setNumFlags(int numFlags);
	
	/**
	 * Retrieve the number of mines on the field.
	 * 
	 * @return		The number of mines.
	 */
	public int getNumMines();
	//public void setNumMines(int numMines);
	
	/**
	 * Retrieve the 'game over' indicator.
	 * 
	 * @return		True if the game is over; False if not.
	 */
	public boolean isGameOver();
	//public void setGameOver(boolean gameover);
	
	/**
	 * Retrieve the 'mine found' indicator.
	 * 
	 * @return		True if a mine was found on the field; False otherwise.
	 */
	public boolean isMineFound();
	//public void setMineFound(boolean mine_found);
	
	/**
	 * Retrieve the current status of the game.
	 * 
	 * @return		The integer representing the current game status.
	 */
	public int getGameState();
	//public void setGameState(int gameState);
		
	/**
	 * Retrieve the visible field array.
	 * 
	 * Returns a copy the 2D array of visibility values for the field.
	 * 
	 * @return		The 2D integer array of visibility values for the field.
	 */
	public int[][] getVisibleField();
	//public void setVisibleField(int[][] visible_field);
	
	/**
	 * Retrieve the mine field array.
	 * 
	 * Returns a copy of the 2D array of mine values for the field.
	 *
	 * @return		The 2D integer array of mine values for the field.
	 */
	public int[][] getMineField();
	//public void setMineField(int[][] mine_field);
	
	/**
	 * Retrieve the visibility value of a cell.
	 * 
	 * Using the given row (x) and column (y) values of a cell, this function will return the
	 * value stored in the visible field array. If the cell values do not exist in the array,
	 * the function will return -1.
	 * 
	 * @param x		The row value of the cell.
	 * @param y		The column value of the cell.
	 * @return		The visibility value at index [x, y]; -1 if index [x, y] does not exist.
	 */	
	public int getVisibleValue(int x, int y);
	
	/**
	 * Set the visibility value of a cell.
	 * 
	 * Using the given row (x) and column (y) values of a cell, this function will set the
	 * value of the cell in the visibility field to the given visibility value. If the cell 
	 * values do not exist in the array, the function will return -1.
	 * 
	 * @param x				The row value of the cell.
	 * @param y				The column value of the cell.
	 * @param visibility	The desired visibility value at index [x, y]; -1 if index [x, y]
	 * 						does not exist.
	 * @return				0 for success; 1 for failure
	 */	
	public int setVisibleValue(int x, int y, int visibility);
	
	/**
	 * Retrieve the mine value of a cell.
	 * 
	 * Using the given row (x) and column (y) values of a cell, this function will return the
	 * value stored in the mine field array. If the cell values do not exist in the array,
	 * the function will return -1.
	 * 
	 * @param x		The row value of the cell.
	 * @param y		The column value of the cell.
	 * @return		The mine value at index [x, y]; -1 if index [x, y] does not exist.
	 */	
	public int getMineValue(int x, int y);
	
	/**
	 * Set the mine value of a cell.
	 * 
	 * Using the given row (x) and column (y) values of a cell, this function will set the
	 * value of the cell in the mine field to the given mine value. If the cell values do not 
	 * exist in the array, the function will return -1.
	 * 
	 * @param x				The row value of the cell.
	 * @param y				The column value of the cell.
	 * @param mine_value	The desired mine value at index [x, y]; -1 if index [x, y] does 
	 * 						not exist.
	 * @return				0 for success; 1 for failure
	 */	
	public int setMineValue(int x, int y, int mine_value);
	
	/**
	 * Rebuilds the field based on the given number of rows, columns and mines.
	 * 
	 * Reconstructs the fields using the given number of rows, columns and mines. The fields, 
	 * status and indicators are then set to their default values.
	 * 
	 * @param rows			The desired new number of rows
	 * @param columns		The desired new number of columns
	 * @param numMines		The desired new number of mines
	 */
	public void redoField(int rows, int columns, int numMines);
	
	/**
	 * Set both arrays, state, indicators and available flags to their default values.
	 * 
	 * The number of available flags will be set to the number of mines in the field (as its
	 * maximum), and the 'game over' and 'mine found' indicators will be set to False. Each 
	 * 2D-index of both the visibility and mine fields will be set to their default values: 
	 * the visibility value is defaulted to the standard "HIDDEN" value; the mine value is
	 * defaulted to 0 (indicating a "blank space").
	 */
	public void resetField();
	
	/**
	 * Indicate the game is over.
	 * 
	 * Sets the 'game over' indicator to True.
	 */
	public void gameOver();
	
	/**
	 * Indicate a mine was found on the field.
	 * 
	 * Sets the 'mine found' indicator to true.
	 */
	public void mineFound();
	
	/**
	 * Update the current status of the game.
	 * 
	 * Based on the 'mine found' and 'game over' indicators, the game status will be updated
	 * accordingly. If the game status already show the game has ended, nothing happens; if
	 * the game has not ended, the status is set to "Ongoing"; if the game has ended but no 
	 * mine was found, the status is set to "Win"; otherwise, the game ended with a mine being
	 * found, thus the status is set to "Lose".
	 */
	public void updateGameState();
	
	/**
	 * Increase the number of available flags.
	 */
	public void removedFlag();
	
	/**
	 * Decrease the number of available flags.
	 */
	public void placedFlag();
	
	/**
	 * Determine whether the remaining hidden cells on the field are all hiding mines.
	 * 
	 * @return		True if all remaining hidden cells have a mine; False otherwise.
	 */
	public boolean minesLastStanding();
}
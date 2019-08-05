package ms.model;
import java.util.Arrays;

import ms.Constants;
import java.lang.Exception;

/**
 * @author      Preston, Robert (RP1211) 
 */
public class Minefield implements Model{	
	/** 2D array of visibility values for cells. */
	private int[][] visible_field;
	/** 2D array of mine values for cells. */
	private int[][] mine_field;
	
	/** Number of Columns and Rows for the field. */
	private int columns;
	private int rows;
	
	/** Number of mines on the field. */
	private int numMines;
	/** Current number of available flags. */
	private int numFlags;
	/** Indicator if the current game is over. */
	private boolean gameover;
	/** Indicator if a mine was revealed. */
	private boolean mine_found;
	/** The current state of the game. */
	private int gameState;
	
	/**
	 * Default no-argument constructor of Minefield.
	 * 
	 * Initializes the fields and indicators at the default values (which are equivalent to
	 * the "INTERMEDIATE" level values in 'Constants.java').
	 * 
	 */
	public Minefield(){
		int[] default_param = Constants.LEVEL_PARAMETERS[Constants.LEVEL_INTERMEDIATE];
		
		columns = default_param[1];
		rows = default_param[0];
	    numMines = default_param[2];
		
		visible_field = new int[rows][columns];
		mine_field = new int[rows][columns];
		
		resetField();
	}
	
	/**
	 * Copy constructor of Minefield.
	 * 
	 * Initializes the fields and indicators to the exact values as the passed Minefield
	 * object. The values stored in each field are also copied from the values stored in the
	 * passed Minefield object.
	 * 
	 * @param m		The Minefield object to be copied from.
	 */
	public Minefield(Minefield m){
		this.columns = m.columns;
		this.rows = m.rows;
		
		this.visible_field = new int[rows][columns];
		this.mine_field = new int[rows][columns];
		
		for (int r = 0; r < rows; r++){
			this.visible_field[r] = Arrays.copyOf(m.visible_field[r], columns);
			this.mine_field[r] = Arrays.copyOf(m.mine_field[r], columns);
		}
		
		this.numMines = m.numMines;
		this.numFlags = m.numFlags;
		this.gameover = m.gameover;
		this.mine_found = m.mine_found;
		this.gameState = m.gameState;
	}
	
	/**
	 * R-C-M Constructor of Minefield.
	 * 
	 * Initializes the fields using the given number of rows, columns and mines. Then, the 
	 * fields and indicators will be set to their defaulted values.
	 * 
	 * @param rows		The number of rows the fields will have.
	 * @param columns	The number of columns the fields will have.
	 * @param numMines	The number of mines that will populate the mine field.
	 */
	public Minefield(int rows, int columns, int numMines){
		redoField(rows, columns, numMines);
	}
/*
	public Minefield(int rows, int columns){
		this.columns = columns;
		this.rows = rows;
		
		calcnumMines();
		resetField();
	}
 */
	/**
	 * Difficulty-based constructor of Minefield.
	 * 
	 * Initializes the fields using values based on the given difficulty level. The fields and
	 * indicators will then be set to their default values. If the given difficulty level is 
	 * not an acceptable value, it will output an error.
	 * 
	 * @param level		The difficulty level to construct the fields.
	 */
	public Minefield(int level){
		try{
			switch(level){
				case Constants.LEVEL_BEGINNER: rows = 9;
											   columns = 9;
											   numMines = 10;
											   break;
				case Constants.LEVEL_INTERMEDIATE: rows = 16;
								   				   columns = 16;
								   				   numMines = 40;
								   				   break;
				case Constants.LEVEL_EXPERT: rows = 16;
							 				 columns = 30;
							 				 numMines = 99;
							 				 break;
				default: throw new IllegalArgumentException("Not a valid difficulty setting.");
			}
			
			visible_field = new int[rows][columns];
			mine_field = new int[rows][columns];
			
			resetField();
		}
		catch(IllegalArgumentException e){
			System.out.println("Illegal Difficulty Setting.\n" + e.getMessage());
		}
	}
	
	/*
	private void calcnumMines(){
		int cells = rows * columns;
		double root = sqrt(cells)
		numMines = (2 * round(root)) - 8);
	}
	*/
	
	/**
	 * Retrieve the number of rows of the field.
	 *
	 * @return		The number of rows the fields have.
	 */
	public int getRows(){ return rows; }
	//public void setRows(int rows) { this.rows = rows;}
	
	/**
	 * Retrieve the number of columns of the field.
	 * 
	 * @return		The number of columns the fields have.
	 */
	public int getColumns() { return columns; }
	//public void setColumns(int columns) { this.columns = columns; }
	
	/**
	 * Retrieve the current number of flags available for the field.
	 * 
	 * @return		The current number of available flags.
	 */
	public int getNumFlags() { return numFlags; }
	//public void setNumFlags(int numFlags) { this.numFlags = numFlags; }
	
	/**
	 * Retrieve the number of mines on the field.
	 * 
	 * @return		The number of mines.
	 */
	public int getNumMines() { return numMines; }
	//public void setNumMines(int numMines) { this.numMines = numMines; }
	
	/**
	 * Retrieve the 'game over' indicator.
	 * 
	 * @return		True if the game is over; False if not.
	 */
	public boolean isGameOver() { return gameover; }
	
	/**
	 * Indicate the game is over.
	 * 
	 * Sets the 'game over' indicator to True.
	 */
	public void gameOver() { gameover = true; }
	
	/**
	 * Retrieve the 'mine found' indicator.
	 * 
	 * @return		True if a mine was found on the field; False otherwise.
	 */
	public boolean isMineFound() { return mine_found; }
	
	/**
	 * Indicate a mine was found on the field.
	 * 
	 * Sets the 'mine found' indicator to true.
	 */
	public void mineFound() { mine_found = true; }
	
	/**
	 * Retrieve the current status of the game.
	 * 
	 * @return		The integer representing the current game status.
	 */
	public int getGameState() { return gameState; }
	
	/**
	 * Update the current status of the game.
	 * 
	 * Based on the 'mine found' and 'game over' indicators, the game status will be updated
	 * accordingly. If the game status already show the game has ended, nothing happens; if
	 * the game has not ended, the status is set to "Ongoing"; if the game has ended but no 
	 * mine was found, the status is set to "Win"; otherwise, the game ended with a mine being
	 * found, thus the status is set to "Lose".
	 */
	public void updateGameState() {
		try {
			if(mine_found && !gameover)
				throw new IllegalStateException("Error: Invalid game status - Mine found but game is not over.");
			
			if(gameState > Constants.GAME_STATUS_ONGOING) { return; }
			
			gameState = Constants.GAME_STATUS_ONGOING;
			
			if(!gameover) { return; }
			
			gameState++;
			
			if(!mine_found) { return; }
				
			gameState++;
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	/**
	 * Retrieve the visible field array.
	 * 
	 * Returns a copy the 2D array of visibility values for the field.
	 * 
	 * @return		The 2D integer array of visibility values for the field.
	 */
	public int[][] getVisibleField() {
		int[][] copyVF = new int[rows][columns];
		for(int r = 0; r < rows; r++)
			copyVF[r] = Arrays.copyOf(visible_field[r], columns);
		
		return copyVF;
	}
	
	/**
	 * Retrieve the mine field array.
	 * 
	 * Returns a copy of the 2D array of mine values for the field.
	 *
	 * @return		The 2D integer array of mine values for the field.
	 */
	public int[][] getMineField()  {
		int[][] copyMF = new int[rows][columns];
		for(int r = 0; r < rows; r++)
			copyMF[r] = Arrays.copyOf(mine_field[r], columns);
		
		return copyMF;
	}
	
	/**
	 * Decrease the number of available flags.
	 */
	public void placedFlag() { numFlags--; }
	
	/**
	 * Increase the number of available flags.
	 */
	public void removedFlag() { numFlags++; }
	
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
	public int getVisibleValue(int x, int y){
		try{
			return visible_field[x][y];
		}
		catch(Exception e){
			System.out.println(e.toString());
			return -1;
		}
	}
	
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
	public int setVisibleValue(int x, int y, int visibility){
		try{
			if (visibility < Constants.CELL_TYPE_REVEAL || visibility > Constants.CELL_TYPE_QUESTION)
				throw new IllegalArgumentException("Error: unknown cell visibility type value of " + visibility + ".");
			
			visible_field[x][y] = visibility;
			return 0;
		}
		catch(Exception e){
			System.out.println(e.toString());
			return -1;
		}
	}
	
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
	public int getMineValue(int x, int y){
		try{
			return mine_field[x][y];
		}
		catch(Exception e){
			System.out.println(e.toString());
			return -3;
		}
	}
	
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
	public int setMineValue(int x, int y, int mine_value){
		try{
			if (mine_value < Constants.SHOW_MINE_FIRED || mine_value > 8)
				throw new IllegalArgumentException("Error: unknown mine cell type value of " + mine_value + ".");
			
			mine_field[x][y] = mine_value;
			return 0;
		}
		catch(Exception e){
			System.out.println(e.toString());
			return -1;
		}
	}
	
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
	public void redoField(int rows, int columns, int numMines) {
		try {
			String eMsg = "";
			
			if(rows < 1)
				eMsg = eMsg.concat("Error: illegal value for rows - must be at least 1.");
			if(columns < 1) {
				if(!eMsg.equals(""))
					eMsg.concat("\n");
				eMsg = eMsg.concat("Error: illegal value for columns - must be at least 1.");
			}
			if(numMines < 0) {
				if(!eMsg.equals(""))
					eMsg.concat("\n");
				eMsg = eMsg.concat("Error: illegal value for mines - must be a non-negative value.");
			}
			
			if(!eMsg.equals(""))
				throw new IllegalArgumentException(eMsg);
				
			this.columns = columns;
			this.rows = rows;
			
			if(numMines > (rows * columns))
				this.numMines = rows * columns;
			else
				this.numMines = numMines;
			
			visible_field = new int[rows][columns];
			mine_field = new int[rows][columns];
			
			resetField();
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}		
	}
	
	/**
	 * Set both arrays, state, indicators and available flags to their default values.
	 * 
	 * The number of available flags will be set to the number of mines in the field (as its
	 * maximum), and the 'game over' and 'mine found' indicators will be set to False. Each 
	 * 2D-index of both the visibility and mine fields will be set to their default values: 
	 * the visibility value is defaulted to the standard "HIDDEN" value; the mine value is
	 * defaulted to 0 (indicating a "blank space").
	 */
	public void resetField() {
		numFlags = numMines;
		gameover = mine_found = false;
		gameState = Constants.GAME_STATUS_READY;
		
		for(int a = 0; a < rows; a++){
			Arrays.fill(visible_field[a], Constants.CELL_TYPE_HIDDEN);
			Arrays.fill(mine_field[a],0);
		}
	}
	
	/**
	 * Determine whether the remaining hidden cells on the field are all hiding mines.
	 * 
	 * @return		True if all remaining hidden cells have a mine; False otherwise.
	 */
	public boolean minesLastStanding(){
		int vfcValue = 0,
			mfcValue = 0;
		
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				vfcValue = visible_field[r][c];
				
				if(vfcValue == Constants.CELL_TYPE_REVEAL) { continue; }
				
				mfcValue = mine_field[r][c];
				
				if(mfcValue < 0) { continue; }
				
				return false;
			}
		}
		
		return true;
	}
}
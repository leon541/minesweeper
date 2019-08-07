package ms.controller;

import ms.Constants;
import ms.model.Minefield;
import ms.model.Model;

/**
 * @author      Preston, Robert (RP1211) 
 */
public class MSController implements Controller {

	/** The 'Model' board of the game associated with the Controller. */
	Model board;
	/** The first click indicator, as maintained by the Controller. */
	private boolean firstClick;
	
	/**
	 * Default no-argument constructor of MSController
	 * 
	 * Initializes the board at the default 'Intermediate' values. (See LEVEL_PARAMETERS in 
	 * 'Constants.java')
	 */
	public MSController() {
		int[] base = Constants.LEVEL_PARAMETERS[Constants.LEVEL_INTERMEDIATE];
		configure(base[0], base[1], base[2]);
	}
	
	/**
	 * Difficulty-based constructor of MSController
	 * 
	 * Initializes the board at the given difficulty level. If the level is not recognized 
	 * as a proper difficulty value, the game will not initialize: otherwise, the values 
	 * associated to the difficulty level will be used to initialize the board. (See 
	 * LEVEL_PARAMETERS in 'Constants.java')
	 * 
	 * @param level		The difficulty value to initialize the game.
	 */
	public MSController(int level) {
		try {
			int[] base = Constants.LEVEL_PARAMETERS[level];
			configure(base[0], base[1], base[2]);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Board-based constructor of MSController
	 * 
	 * Initializes the board to the given 'Minefield' object, and configures the 'first click'
	 * indicator based on its game state.
	 * 
	 * @param board		The 'Minefield' object to be the board.
	 */
	public MSController(Model board) {
		this.board = board;
		
		if(board == null) { 
			firstClick = true;
			return;
		}
		
		firstClick = board.getGameState() == Constants.GAME_STATUS_READY;
	}
	
	/**
	 * Configures the board and game states for the Controller.
	 * 
	 * Using the given number of rows, columns and mines, this function will
	 * configure a blank board to be used by the Controller. If the board is null or
	 * if one or more of the passed values differ from the values used to configure 
	 * the existing board, a new board will be created using the new values: otherwise,
	 * the existing board is reset to a blank board.
	 * 
	 * Additionally, the first click indicator and game status value are set to their
	 * default starting values.
	 * 
	 * @param rows		The number of rows for the board.
	 * @param cols		The number of columns for the board.
	 * @param mines		The number of mines for the board.
	 */
	@Override
	public void configure(int rows, int cols, int mines) {
		try {
			String err = "";
			if(rows < 1)
				err = err.concat("Illegal value for Rows: " + String.valueOf(rows) +". Must be at least 1.");
			if(cols < 1) {
				if (!err.equals(""))
					err = err.concat("\n");
				err = err.concat("Illegal value for Columns: " + String.valueOf(cols) + ". Must be at least 1.");
			}
			
			if(mines < 0) {
				if(!err.equals(""))
					err = err.concat("\n");
				err = err.concat("Illegal value for Mines: " + String.valueOf(mines) + ". Must be a non-negative value.");
			}
			
			if(!err.equals(""))
				throw new IllegalArgumentException(err);
			
			if(board == null)
				board = new Minefield(rows, cols, mines);
			else if(board.getRows() != rows || board.getColumns() != cols || board.getNumMines() != mines)
				board.redoField(rows, cols, mines);
			else
				board.resetField();
	
			firstClick = true;
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Executes the actions for specific cell clicks on the board grid.
	 * 
	 * This function will perform specific game functionalities based on the given
	 * cell selected (from its row and column values) and the type of click that was
	 * performed, update the game status from the results, and return the game status.
	 * 
	 * The function performs one of the following actions:
	 * 1) If the game has already ended, the current game status is immediately returned.
	 * 2) If a left-click is performed on a hidden non-flagged cell, it is revealed and
	 * the function will perform the appropriate functions based on the revealed cell's
	 * mine value: blanks will invoke the recursive blankReveal function; a mine will
	 * trigger the game loss functions; and any other values will do nothing further. 
	 * Afterwards, the End-of-Game conditions are checked.
	 * 3) If a right-click is performed on a non-revealed cell, it will toggle the marker
	 * on the cell in this repeating order: no marker, flag, question. If a flag is
	 * placed or removed in this way, the flag counter will be updated.
	 * 4) If a joint left-right-click is performed on a revealed cell, the areaReveal
	 * function is invoked. Afterwards, the End-of-Game conditions are checked.
	 * 5) The function will do no action for any other pairings of clicks and cell types.
	 * 
	 * After one of the above action is taken, the function will update the game state
	 * based on the action's results, and return that value. If the function encounters
	 * an error, it will return -1.
	 * 
	 * @param row		The row value of the cell clicked.
	 * @param col		The column value of the cell clicked.
	 * @param type		The type of click performed (See 'Constants.java').
	 */
	@Override
	public void clickedGrid(int row, int col, int type) {
		try {
			// Return if the game has already ended.
			if ( board.getGameState() > Constants.GAME_STATUS_ONGOING ) { return ; }
		}
		catch(Exception e) {
			// Typically encountered if board is null.
			System.out.println(e.toString());
			return;
		}
		
		int vfcValue = 0;
		
		// Retrieve the visible value of the cell.
		try{
			vfcValue = board.getVisibleValue(row, col);			
		}
		catch(Exception e){
			System.out.println(e.toString());
			return;
		}
		
		// Perform the functions from on the click type and visible value of the cell.
		try {
			// Actions taken if a left-click is made on a non-revealed cell.
			if (type == Constants.CLICK_TYPE_LEFT && vfcValue != Constants.CELL_TYPE_REVEAL) {
				switch(vfcValue) {
				case Constants.CELL_TYPE_FLAG: return;									// If the cell is flagged, do nothing and return;
				default: board.setVisibleValue(row, col, Constants.CELL_TYPE_REVEAL);   // Otherwise, reveal the cell on the board.
				}
				
				// Verify if this is the first click of the game.
				if(firstClick) {
					generateField(row, col);		// Generate field off first revealed cell.
					firstClick = false;				// Toggle first click indicator off.
				}
				
				int mfcValue = board.getMineValue(row, col);	// Retrieve mine value of revealed cell.
				
				switch(mfcValue) {
				case Constants.SHOW_MINE: // If mine is revealed, run game loss functions;
										  board.mineFound();	
				                          mineReveal();
				                          board.setMineValue(row, col, --mfcValue);
				                          break;
				case 0: blankReveal(row, col);		// If a blank space is revealed, run blankReveal function on cell;
				}									// Otherwise, do nothing else.				
			}
			
			// Actions taken if a right-click is made on any cell.
			else if(type == Constants.CLICK_TYPE_RIGHT) {
				switch(vfcValue) {
				case Constants.CELL_TYPE_HIDDEN: // If the cell is an unmarked hidden cell, toggle flag on hidden cell;
												 if(board.getNumFlags() == 0)	
												     vfcValue++;
												 else {
												     board.placedFlag();
												 }
				                                 break;
				case Constants.CELL_TYPE_FLAG: // If cell is a flagged hidden cell, toggle flag off hidden cell;
											   if(board.getNumFlags() == board.getNumMines())
					                               throw new IllegalStateException("Error: Flag state is incorrect - no flags should be on the board.");
											  
											   board.removedFlag();
				}	// Otherwise, do nothing for the moment.
				
				// Verify if clicked cell is a hidden cell (regardless if it is marked).
				if(vfcValue != Constants.CELL_TYPE_REVEAL) {
					// For a hidden cell, rotate and update the marker value of the hidden cell on the board.
				    vfcValue = (vfcValue % 3) + 1;
					board.setVisibleValue(row, col, vfcValue);
				}
			}
			
			// Actions taken if a joint left-right-click is made on a revealed cell.
			else if(type == Constants.CLICK_TYPE_BOTH && vfcValue == Constants.CELL_TYPE_REVEAL)
				areaReveal(row, col);		// Run areaReveal function on clicked cell.
				
			// Other pairings will result in no actions needed to be taken.
		}
		catch(Exception e){
			System.out.println(e.toString());
			return;
		}
		
		// Verify if End-of-Game conditions have been met.
		if(board.isMineFound() || board.minesLastStanding()) {
			// Set game to be over and run end-flagging function.
			board.gameOver();
			flagAndTag();
		}
		
		// Update game status based on results of the click
		board.updateGameState();
	}
	
	/**
	 * Reveals the mines on the board.
	 * 
	 * This function will search through each cell of the current game board and, for
	 * each cell that holds the value of a mine, reveal those cells that hid mines. This
	 * function should and will only be invoked if a mine was found during a game.
	 * 
	 * @return			0 if successful; -1 if failed.
	 */
	private int mineReveal(){
		if(!board.isMineFound()) { return -1; }		// If a mine was never found in the game, abort.
		
		int numMines = board.getNumMines(),
			mines = 0,
			rows = board.getRows(),
		    columns = board.getColumns(),
		    vfcValue = 0;
		
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
				// Verify if a cell hides a mine.
				if(board.getMineValue(r, c) != Constants.SHOW_MINE)		
					continue;
				
				mines++;
				vfcValue = board.getVisibleValue(r, c);

				// Verify if the cell has not been revealed or flagged.
				if (vfcValue != Constants.CELL_TYPE_REVEAL && vfcValue != Constants.CELL_TYPE_FLAG) {
					// Reveal the cell hiding a mine.
					board.setVisibleValue(r, c, Constants.CELL_TYPE_REVEAL);
				}
				
				// Verify if all mines were found.
				if(mines == numMines)
					return 0;
			}
		}
		
		return -1;
	}
	
	/**
	 * Recursively reveal cells adjacent to a blank space.
	 * 
	 * Given the row (x) and column (y) values of a blank space cell, this function will
	 * reveal all the non-flagged hidden adjacent cells. If this should reveal more blank
	 * spaces, this function will be recursively called for each of those newly-revealed 
	 * blank space cells.
	 */
	private void blankReveal(int x, int y){		
		int x_neighbor = 0,
		    y_neighbor = 0,
		    rows = board.getRows(),
		    columns = board.getColumns(),
		    vfcValue = 0,
		    mfcValue = 0;
		
		for (x_neighbor = x - 1; x_neighbor < x + 2; x_neighbor++){
			if (x_neighbor == rows)
				break;
			if (x_neighbor < 0)
				continue;
			
			for (y_neighbor = y - 1; y_neighbor < y + 2; y_neighbor++){
				if(y_neighbor == columns)
					break;
				if(y_neighbor < 0)
					continue;
				
				vfcValue = board.getVisibleValue(x_neighbor, y_neighbor);
				mfcValue = board.getMineValue(x_neighbor, y_neighbor);
				
				// Verify it is not revealed or flagged.
				if(vfcValue == Constants.CELL_TYPE_REVEAL || vfcValue == Constants.CELL_TYPE_FLAG)
					continue;
				
				
				// Reveal adjacent cell.
				board.setVisibleValue(x_neighbor, y_neighbor, Constants.CELL_TYPE_REVEAL);
				
				// Verify if the adjacent cell is also a blank space.
				if(mfcValue == 0)
					blankReveal(x_neighbor, y_neighbor);		//Recursive call on blank space cell.
			}
		}
	}
	
	/**
	 * Reveal the adjacent non-flagged hidden cells of a revealed cell.
	 * 
	 * Given the row (x) and column (y) values of a non-blank revealed cell, this
	 * function computes the number of adjacent flagged cells and compares it against
	 * the mine value of the revealed cell. If the values match, the other adjacent cells
	 * are revealed; otherwise, the function does nothing.
	 * 
	 * Any blank space cell or mine revealed during this action will be processed as if 
	 * they were clicked. (See the function 'clickedGrid')
	 */
	private void areaReveal(int x, int y) {
		int rows = board.getRows(),
		    columns = board.getColumns(),
		    vfcValue = 0,
		    mfcValue = board.getMineValue(x, y),
		    flags_in_area = 0,
		    cells_in_area = 0;
		int[][] area = new int[8][3];
		
		// Retrieve adjacent cells.
		for (int x_neighbor = x - 1; x_neighbor < x + 2; x_neighbor++){
			if (x_neighbor == rows)
				break;
			if (x_neighbor < 0)
				continue;
			
			for (int y_neighbor = y - 1; y_neighbor < y + 2; y_neighbor++){
				if(y_neighbor == columns)
					break;
				if(y_neighbor < 0 || (x == x_neighbor && y == y_neighbor))
					continue;
				
				vfcValue = board.getVisibleValue(x_neighbor, y_neighbor);

				area[cells_in_area][0] = x_neighbor;
				area[cells_in_area][1] = y_neighbor;
				area[cells_in_area][2] = vfcValue;
				cells_in_area++;
				
				// Verify if an adjacent cell is flagged.
				if(vfcValue == Constants.CELL_TYPE_FLAG)
					flags_in_area++;
			}
		}
		
		// Verify if the number of adjacent flags match the mine value of the cell.
		if(mfcValue != flags_in_area || flags_in_area == 8) { return; }
		
		for(int[] cell: area) {
			vfcValue = cell[2];
			
			//Verify the adjacent cell is not revealed or flagged
			if(vfcValue == Constants.CELL_TYPE_REVEAL || vfcValue == Constants.CELL_TYPE_FLAG)
				continue;
			
			// Reveal the adjacent cell.
			board.setVisibleValue(cell[0], cell[1], Constants.CELL_TYPE_REVEAL);
			mfcValue = board.getMineValue(cell[0], cell[1]);
			
			switch(mfcValue) {
			case Constants.SHOW_MINE: // If the adjacent cell is a mine, run the game loss functions;
									  board.mineFound();
									  mineReveal();
									  board.setMineValue(cell[0], cell[1], --mfcValue);
									  break;
			case 0: blankReveal(cell[0], cell[1]);		// If the adjacent cell is a blank space, run the blankReveal function;
			}											// Otherwise, do nothing else.				
						
			// Verify if all adjacent cells have been processed.
			if(--cells_in_area == 0)
				break;
		}
	}
	
	/**
	 * If the game is won, flag all remaining cells hiding mines.
	 * 
	 * This function searches the entire board and flags all remaining non-flagged
	 * hidden cells. This function should and will only be invoked if the game ended in
	 * a win (i.e. no mine was found and the remaining cells are all hiding mines.); 
	 * otherwise, the function does nothing.
	 */
	private void flagAndTag() {
		// Verify if the game has ended with the win conditions.
		if(board.isMineFound() || !board.minesLastStanding()) { return; }
		
		int rows = board.getRows(),
			columns = board.getColumns(),
			vfcValue = 0;
		
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < columns; c++) {
				vfcValue = board.getVisibleValue(r, c);
				
				// Verify if the cell has not been revealed or flagged.
				if(vfcValue == Constants.CELL_TYPE_REVEAL || vfcValue == Constants.CELL_TYPE_FLAG)
					continue;
				
				// Flag the cell.
				board.setVisibleValue(r, c, Constants.CELL_TYPE_FLAG);
			}
	}
	
	/**
	 * Generate the board for the game based on the cell first clicked.
	 * 
	 * Given the row (x) and column (y) values of the first-clicked cell, the board will
	 * be populated with mines randomly in such a way that the first-clicked cell will 
	 * not reveal a mine. The number of mines placed will be equal to the board's
	 * value set at its configuration.Each time a mine is placed, the mine values of
	 * adjacent non-mine cells are incrementally increased.
	 */
	private void generateField(int row, int col) {
		int x = 0,
		    y = 0,
		    rows = board.getRows(),
		    columns = board.getColumns(),
		    x_neighbor = 0,
		    y_neighbor = 0,
		    numMines = board.getNumMines(),
		    mfcValue = 0;
		
		// Verify if no mines exist for this board.
		if(numMines == 0) { return; }
		// Verify if all cells will hide mines.
		else if(numMines >= (rows * columns)) {
			for(int r = 0; r < rows; r++)
				for(int c = 0; c < columns; c++)
					board.setMineValue(r, c, Constants.SHOW_MINE);
			return;
		}
			
		int[] bombs = new int[numMines];	
		
		// Generate initial random mine placements.
		for(int b = 0; b < numMines; b++){
			bombs[b] = (int) Math.floor((Math.random() * (rows * columns)));
		}
		
		// Place mines onto board.
		for (int bombloc: bombs) {
			x = bombloc / columns;
			y = bombloc % columns;
			
			// Validate mine placements, or re-randomize.
			while (board.getMineValue(x, y) == Constants.SHOW_MINE || (x == row && y == col)){
				bombloc = (int) Math.floor((Math.random() * (rows * columns)));
				//bombloc = (bombloc + 1) % (rows * columns);
				x = bombloc / columns;
				y = bombloc % columns;
			}
			
			board.setMineValue(x, y, Constants.SHOW_MINE);
			
			// Adjust mine values for adjacent non-mine cells.
			for (x_neighbor = x - 1; x_neighbor < x + 2; x_neighbor++){
				if (x_neighbor == rows)
					break;
				if (x_neighbor < 0)
					continue;
				
				for (y_neighbor = y - 1; y_neighbor < y + 2; y_neighbor++){
					if(y_neighbor == columns)
						break;
					if(y_neighbor < 0)
						continue;
					
					mfcValue = board.getMineValue(x_neighbor, y_neighbor);
					
					if(mfcValue == Constants.SHOW_MINE)
						continue;
					
					board.setMineValue(x_neighbor, y_neighbor, mfcValue + 1);
				}
			}
		}
	}
}

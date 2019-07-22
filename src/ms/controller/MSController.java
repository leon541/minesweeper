package ms.controller;

import ms.Constants;
import ms.view.View;
import ms.model.Minefield;

public class MSController implements Controller {

	View view;
	Minefield board;
	private int gameStatus;
	private boolean firstClick;
	
	public MSController() {
		int[] base = Constants.LEVEL_PARAMETERS[Constants.LEVEL_INTERMEDIATE];
		configure(base[0], base[1], base[2]);
	}
	
	public MSController(int level) {
		try {
			int[] base = Constants.LEVEL_PARAMETERS[level];
			configure(base[0], base[1], base[2]);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void setView(View view) {
		this.view = view; 
	}
	
	@Override
	public int clickedGrid(int row, int col, int type) {
		// TODO Auto-generated method stub
		if ( gameStatus > Constants.GAME_STATUS_ONGOING ) {
			return gameStatus;
		}
		
		int vfcValue = 0;
		
		try{
			vfcValue = board.getVisibleValue(row, col);			
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return -1;
		}

		try {
			if (type == Constants.CLICK_TYPE_LEFT && vfcValue != Constants.CELL_TYPE_REVEAL) {
				/*
				 * if (vfcValue == 0)
					throw new IllegalStateException("Cell has already been revealed");
				*/
				
				switch(vfcValue) {
				case Constants.CELL_TYPE_FLAG: return gameStatus;
											   //view.updateCounter(board.getNumFlags());
				default: board.setVisibleValue(row, col, Constants.CELL_TYPE_REVEAL);
				}
				
				if(firstClick) {
					generateField(row, col);
					firstClick = false;
				}
				
				int mfcValue = board.getMineValue(row, col);
				
				switch(mfcValue) {
				case Constants.SHOW_MINE: board.mineFound();
				                          mineReveal();
				                          gameStatus = Constants.GAME_STATUS_LOSE;
				                          mfcValue--;
				                          break;
				case 0: blankReveal(row, col);
				}
				
				if(board.isMineFound() || board.minesLastStanding()) {
					board.gameOver();
					flagAndTag();
				}
					
				view.updateCell(row, col, Constants.CELL_TYPE_REVEAL, mfcValue);
			}
			else if(type == Constants.CLICK_TYPE_RIGHT) {
				switch(vfcValue) {
				case Constants.CELL_TYPE_HIDDEN: if(board.getNumFlags() == 0)
												     vfcValue++;
												 else {
												     board.placedFlag();
													 //view.updateCounter(board.getNumFlags());
												 }
				                                 break;
				case Constants.CELL_TYPE_FLAG: if(board.getNumFlags() == board.getNumBombs())
					                               throw new IllegalStateException("Error: Flag state is incorrect - no flags should be on the board.");
											  
											   board.removedFlag();
											   //view.updateCounter(board.getNumFlags());
				}
				
				if(vfcValue != Constants.CELL_TYPE_REVEAL) {
				    vfcValue = (vfcValue % 3) + 1;
					board.setVisibleValue(row, col, vfcValue);
					view.updateCell(row, col, vfcValue, 0);
				}
			}
			else if(type == Constants.CLICK_TYPE_BOTH && vfcValue == Constants.CELL_TYPE_REVEAL) {
				areaReveal(row, col);
			}
			
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return -1;
		}
		
		if(board.isGameOver() && gameStatus != Constants.GAME_STATUS_LOSE)
			gameStatus = Constants.GAME_STATUS_WIN;
		else if(gameStatus == Constants.GAME_STATUS_READY)
			gameStatus = Constants.GAME_STATUS_ONGOING;
		
		return gameStatus;
	}

	@Override
	public void configure(int rows, int cols, int mines) {
		// TODO Auto-generated method stub
		if(board == null || board.getRows() != rows || board.getColumns() != cols || board.getNumBombs() != mines)
			board = new Minefield(rows, cols, mines);
		else
			board.resetField();
		//view.updateCounter(board.getNumFlags());
		//generateField();
		firstClick = true;
		gameStatus = Constants.GAME_STATUS_READY;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
	}
	
	private int mineReveal(){
		if(!board.isMineFound()) { return -1; }
		
		int numBombs = board.getNumBombs(),
			mines = 0,
			rows = board.getRows(),
		    columns = board.getColumns(),
		    vfcValue = 0;
		
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
				if(board.getMineValue(r, c) != Constants.SHOW_MINE)
					continue;
				
				vfcValue = board.getVisibleValue(r, c);
				
				if (vfcValue == Constants.CELL_TYPE_REVEAL || vfcValue == Constants.CELL_TYPE_FLAG)
					continue;
				
				board.setVisibleValue(r, c, Constants.CELL_TYPE_REVEAL);
				view.updateCell(r, c, Constants.CELL_TYPE_REVEAL, Constants.SHOW_MINE);

				mines++;
				
				if(mines == numBombs)
					return 0;
			}
		}
		
		return -1;
	}
	
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
				
				if(vfcValue == Constants.CELL_TYPE_REVEAL || vfcValue == Constants.CELL_TYPE_FLAG)
					continue;
								
				board.setVisibleValue(x_neighbor, y_neighbor, Constants.CELL_TYPE_REVEAL);
				view.updateCell(x_neighbor, y_neighbor, Constants.CELL_TYPE_REVEAL, mfcValue);
				
				if(mfcValue == 0)
					blankReveal(x_neighbor, y_neighbor);
			}
		}
		
		//view.updateCounter(board.getNumFlags());
	}
	
	private void areaReveal(int x, int y) {
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
				
				if(vfcValue == Constants.CELL_TYPE_REVEAL || vfcValue == Constants.CELL_TYPE_FLAG)
					continue;
				
				board.setVisibleValue(x_neighbor, y_neighbor, Constants.CELL_TYPE_REVEAL);
				mfcValue = board.getMineValue(x_neighbor, y_neighbor);
				
				switch(mfcValue) {
				case Constants.SHOW_MINE: board.mineFound();
										  mineReveal();
										  gameStatus = Constants.GAME_STATUS_LOSE;
										  mfcValue--;
										  break;
				case 0: blankReveal(x_neighbor, y_neighbor);
				}					
				
				view.updateCell(x_neighbor, y_neighbor, Constants.CELL_TYPE_REVEAL, mfcValue);
			}
		}
		
		if(board.isMineFound() || board.minesLastStanding()) {
			board.gameOver();
			flagAndTag();
		}
	}
	
	private void flagAndTag() {
		if(board.isMineFound() || !board.minesLastStanding()) { return; }
		
		int rows = board.getRows(),
			columns = board.getColumns(),
			vfcValue = 0;
		
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < columns; c++) {
				vfcValue = board.getVisibleValue(r, c);
				
				if(vfcValue == Constants.CELL_TYPE_REVEAL || vfcValue == Constants.CELL_TYPE_FLAG)
					continue;
				
				board.setVisibleValue(r, c, Constants.CELL_TYPE_FLAG);
				view.updateCell(r, c, Constants.CELL_TYPE_FLAG, Constants.SHOW_MINE);
			}
	}
	
	private void generateField(int row, int col) {
		int x = 0,
		    y = 0,
		    rows = board.getRows(),
		    columns = board.getColumns(),
		    x_neighbor = 0,
		    y_neighbor = 0,
		    numBombs = board.getNumBombs(),
		    mfcValue = 0;
		
		if(numBombs == 0) { return; }
		else if(numBombs == (rows * columns)) {
			for(int r = 0; r < rows; r++)
				for(int c =0; c < columns; c++)
					board.setMineValue(r, c, Constants.SHOW_MINE);
			return;
		}
			
		int[] bombs = new int[numBombs];	
		
		for(int b = 0; b < numBombs; b++){
			bombs[b] = (int) Math.floor((Math.random() * (rows * columns)));
		}
		
		for (int bombloc: bombs) {
			x = bombloc / rows;
			y = bombloc % rows;
			
			while (board.getMineValue(x, y) == Constants.SHOW_MINE || (x == row && y == col)){
				bombloc = (int) Math.floor((Math.random() * (rows * columns)));
				//bombloc = (bombloc + 1) % (rows * columns);
				x = bombloc / rows;
				y = bombloc % rows;
			}
			
			board.setMineValue(x, y, Constants.SHOW_MINE);
			
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

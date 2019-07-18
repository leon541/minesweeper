package ms.controller;


import ms.Constants;
import ms.view.View;
import ms.model.Minefield;

public class MSController implements Controller {

	View view;
	Minefield board;
	
	public void setView(View view) {
		this.view = view; 
	}
	
	@Override
	public int clickedGrid(int row, int col, int type) {
		// TODO Auto-generated method stub
		if ( board.isGameOver() )
			return 1;
		
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
				case Constants.CELL_TYPE_FLAG: board.removedFlag();
											   view.updateCounter(board.getNumFlags());
				default: board.setVisibleValue(row, col, Constants.CELL_TYPE_REVEAL);
				}
				
				int mfcValue = board.getMineValue(row, col);
				
				switch(mfcValue) {
				case Constants.SHOW_MINE: board.mineFound();
				                          mineReveal();
				                          mfcValue--;
				                          break;
				case 0: blankReveal(row, col);
				}
				
				if(board.isMineFound() || board.minesLastStanding())
					board.gameOver();
				
				view.updateCell(row, col, Constants.CELL_TYPE_REVEAL, mfcValue);
			}
			else if(type == Constants.CLICK_TYPE_RIGHT) {
				switch(vfcValue) {
				case Constants.CELL_TYPE_HIDDEN: if(board.getNumFlags() == 0)
												     vfcValue++;
												 else {
												     board.placedFlag();
													 view.updateCounter(board.getNumFlags());
												 }
				                                 break;
				case Constants.CELL_TYPE_FLAG: if(board.getNumFlags() == board.getNumBombs())
					                               throw new IllegalStateException("Error: Flag state is incorrect - no flags should be on the board.");
											  
											   board.removedFlag();
											   view.updateCounter(board.getNumFlags());
				}
				
				if(vfcValue != Constants.CELL_TYPE_REVEAL) {
				    vfcValue = (vfcValue % 3) + 1;
					board.setVisibleValue(row, col, vfcValue);
					view.updateCell(row, col, Constants.CELL_TYPE_REVEAL, 0);
				}
			}
			else if(type == Constants.CLICK_TYPE_BOTH && vfcValue == Constants.CELL_TYPE_REVEAL) {
				areaReveal(row, col);
			}
			else
				return 1;
			
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return -1;
		}
		
		return 0;
	}

	@Override
	public void configure(int rows, int cols, int mines) {
		// TODO Auto-generated method stub
		
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
		    columns = board.getColumns();
		
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
				if(board.getMineValue(r, c) != Constants.SHOW_MINE || board.getVisibleValue(r, c) == Constants.CELL_TYPE_REVEAL)
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
		    vfcValue = 0;
		
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
				
				if(vfcValue == Constants.CELL_TYPE_REVEAL)
					continue;
				else if(vfcValue == Constants.CELL_TYPE_FLAG) {
					board.removedFlag();
				}
				
				board.setVisibleValue(x_neighbor, y_neighbor, Constants.CELL_TYPE_REVEAL);
				view.updateCell(x_neighbor, y_neighbor, Constants.CELL_TYPE_REVEAL, 0);
				
				if(board.getMineValue(x_neighbor, y_neighbor) == 0)
					blankReveal(x, y);
			}
		}
		
		view.updateCounter(board.getNumFlags());
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
										  mfcValue--;
										  break;
				case 0: blankReveal(x_neighbor, y_neighbor);
				}					
				
				view.updateCell(x_neighbor, y_neighbor, Constants.CELL_TYPE_REVEAL, mfcValue);
			}
		}
		
		if(board.isMineFound() || board.minesLastStanding())
			board.gameOver();
	}
}

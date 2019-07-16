package ms.model;
import java.util.Arrays;
import java.lang.Math;
import java.lang.String;
import java.lang.Exception;

public class Minefield{	
	private enum Difficulty { BEGINNER, INTERMEDIATE, EXPERT; }

	private int[][] visible_field;
	private int[][] bomb_field;
	
	private int columns;
	private int rows;
	
	private int numBombs;
	private int numFlags;
	private boolean gameover;
	private boolean mine_found;
	
	public Minefield(){
		columns = 9;
		rows = 9;
	    numBombs = 10;
		
		visible_field = new int[rows][columns];
		bomb_field = new int[rows][columns];
		
		generateField();
	}
	
	public Minefield(Minefield m){
		this.columns = m.columns;
		this.rows = m.rows;
		
		this.visible_field = new int[rows][columns];
		this.bomb_field = new int[rows][columns];
		
		for (int r = 0; r < rows; r++){
			this.visible_field[r] = Arrays.copyOf(m.visible_field[r], columns);
			this.bomb_field[r] = Arrays.copyOf(m.bomb_field[r], columns);
		}
		
		this.numBombs = m.numBombs;
		this.numFlags = m.numFlags;
		this.gameover = m.gameover;
		this.mine_found = m.mine_found;
	}
	/*
	public Minefield(int rows, int columns){
		this.columns = columns;
		this.rows = rows;
		
		calcNumBombs();
		
		visible_field = new int[rows][columns];
		bomb_field = new int[rows][columns];
		
		generateField();
	}
	*/
	public Minefield(String dif){
		try{
			switch(Difficulty.valueOf(dif.toUpperCase().trim())){
				case BEGINNER: rows = 9;
							   columns = 9;
							   numBombs = 10;
							   break;
				case INTERMEDIATE: rows = 16;
								   columns = 16;
								   numBombs = 40;
								   break;
				case EXPERT: rows = 16;
							 columns = 30;
							 numBombs = 99;
							 break;
			}
			
			visible_field = new int[rows][columns];
			bomb_field = new int[rows][columns];
			
			generateField();
		}
		catch(IllegalArgumentException e){
			System.out.println("Illegal Difficulty Setting.\n" + e.getMessage());
		}
	}
	
	/*
	private void calcNumBombs(){
		int cells = rows * columns;
		double root = sqrt(cells)
		numBombs = (2 * round(root)) - 8);
	}
	*/
	private void generateField(){
		numFlags = numBombs;
		gameover = mine_found = false;
		
		int[] bombs = new int[numBombs];
		int x = 0, y = 0, x_neighbor = 0, y_neighbor = 0;
			
		for(int b = 0; b < numBombs; b++){
			bombs[b] = (int) Math.floor((Math.random() * (rows * columns)));
		}
		
		for(int a = 0; a < rows; a++){
			Arrays.fill(visible_field[a],1);
			Arrays.fill(bomb_field[a],0);
		}
		
		for (int bombloc: bombs) {
			x = bombloc / rows;
			y = bombloc % rows;
			
			while (bomb_field[x][y] == -1){
				bombloc = (int) Math.floor((Math.random() * (rows * columns)));
				x = bombloc / rows;
				y = bombloc % rows;
			}
			
			bomb_field[x][y] = -1;
			
			for (x_neighbor = x - 1; x_neighbor < x + 2; x_neighbor++){
				if (x_neighbor == rows)
					break;
				if (x_neighbor < 0)
					continue;
				
				for (y_neighbor = y - 1; y_neighbor < y + 2; y_neighbor++){
					if(y_neighbor == columns)
						break;
					if(y_neighbor < 0 || bomb_field[x_neighbor][y_neighbor] == -1)
						continue;
					
					bomb_field[x_neighbor][y_neighbor]++;
				}
			}
		}
	}
	
	public int getRows(){ return rows; }
	public int getColumns() { return columns; }
	public int getNumFlags() { return numFlags; }
	public int getNumBombs() { return numBombs; }
	public boolean isGameOver() { return gameover; }
	public boolean isMineFound() { return mine_found; }
	
	public int[][] getVisibleField() { return visible_field; }
	public int[][] getBombField() { return bomb_field; }
	
	public int getVisibleValue(int x, int y){
		try{
			return visible_field[x][y];
		}
		catch(ArrayIndexOutOfBoundsException e){
			System.out.println(e.getMessage());
			return 1;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return -1;
		}
	}
	
	public int getMineValue(int x, int y){
		try{
			return bomb_field[x][y];
		}
		catch(ArrayIndexOutOfBoundsException e){
			System.out.println(e.getMessage());
			return 0;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return -2;
		}
	}
	
	public void resetGame(){
		generateField();
	}
	
	public void reveal(int x, int y){
		if ( gameover ) { return; }
		
		int vfcValue = 0;
		
		try{
			vfcValue = visible_field[x][y];
			
			if (vfcValue == 0)
				throw new IllegalStateException("Cell has already been revealed");
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return;
		}
		
		if(vfcValue == 2)
			numFlags++;
		
		visible_field[x][y] = 0;
		
		switch(bomb_field[x][y]){
			case -1: mine_found = true;
					 mineReveal();
					 break;
			case 0: blankReveal(x, y);
		}
		
		if(mine_found || minesLastStanding())
			gameover = true;
	}
	
	public void toggleFlag(int x, int y){
		if ( gameover ) { return; }
		
		int vfcValue = 0;
		
		try{
			vfcValue = visible_field[x][y];
			
			if (vfcValue == 0)
				throw new IllegalStateException("Cell has already been revealed");
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return;
		}
		
		try{
			if(vfcValue == 1){
				if (numFlags == 0)
					throw new IllegalStateException("Maximum number of flags reached.");
				visible_field[x][y] = 2;
				numFlags--;
			}
			else if(vfcValue == 2){
				if (numFlags == numBombs)
					throw new IllegalStateException("Error: Flag state is incorrect - no flags should be present.");
				
				visible_field[x][y] = 1;
				numFlags++;
			}
		}
		catch(IllegalStateException e){
			System.out.println(e.getMessage());
			return;
		}
	}
	
	private void blankReveal(int x, int y){
		int x_neighbor = 0,
		    y_neighbor = 0;
		
		for (x_neighbor = x - 1; x_neighbor < x + 2; x_neighbor++){
			if (x_neighbor == rows)
				break;
			if (x_neighbor < 0)
				continue;
			
			for (y_neighbor = y - 1; y_neighbor < y + 2; y_neighbor++){
				if(y_neighbor == columns)
					break;
				if(y_neighbor < 0 || visible_field[x_neighbor][y_neighbor] == 0)
					continue;
				
				if(visible_field[x_neighbor][y_neighbor] == 2)
					numFlags++;
				
				visible_field[x_neighbor][y_neighbor] = 0;
				
				if(bomb_field[x_neighbor][y_neighbor] == 0)
					blankReveal(x, y);
			}
		}
	}
	
	private boolean minesLastStanding(){
		int numLeft = 0;
		
		for(int[] row : visible_field){
			for(int cell : row){
				if(cell != 0){
					numLeft++;
					
					if(numLeft > numBombs)
						return false;
				}
			}
		}
		
		return true;
	}
	
	private void mineReveal(){
		if(!mine_found) { return; }
		
		int mines = 0;
		
		for(int r = 0; r < rows; r++){
			for(int c = 0; c < columns; c++){
				if(bomb_field[r][c] != -1)
					continue;
				
				visible_field[r][c] = 0;
				mines++;
				
				if(mines == numBombs)
					return;
			}
		}
	}
}
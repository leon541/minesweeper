package ms.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ms.model.*;
import ms.Constants;

class BoardTest{
	
	@Test
	public void testDefaultConstructor() {
		Model board = new Minefield();
		
		int[] expectedValues = Constants.LEVEL_PARAMETERS[Constants.LEVEL_INTERMEDIATE];
		int rows = expectedValues[0],
			columns = expectedValues[1],
			mines = expectedValues[2];
		
		assertEquals(rows, board.getRows(), "Error eDC-R1: 'Rows' is not the right value.  Expected: " + String.valueOf(rows));
		assertEquals(columns, board.getColumns(), "Error eDC-C1: 'Columns' is not the right value.  Expected: " + String.valueOf(columns));
		assertEquals(mines, board.getNumMines(), "Error eDC-M1: 'Number of mines' is not the right value.  Expected: " + String.valueOf(mines));
		assertEquals(mines, board.getNumFlags(), "Error eDC-F1: 'Number of flags' is not the right value.  Expected: " + String.valueOf(mines));
		assertFalse(board.isGameOver(), "Error eDC-GO1: 'Game over' indicator is not the right value.  Expected: FALSE");
		assertFalse(board.isMineFound(), "Error eDC-BF1: 'Mine found' indicator is not the right value.  Expected: FALSE");
		assertEquals(Constants.GAME_STATUS_READY, board.getGameState(), "Error eDC-GS1: Game state is not the right value.  Expected: 0 - \"Ready\"");
		
		checkBlankBoard(board);	
	}
	
	@Test
	public void testCopyConstructor() {
		Model board = new Minefield(2);
		board.setVisibleValue(8, 8, 2);
		board.setMineValue(8, 8, 5);
		
		boolean shouldFail = true;
		
		// Expected Value Test - Expected Results: Pass
		testCopyConstruct(board);
		
		// Unacceptable Value Tests - Expected Results: Fail
		try { testCopyConstruct(null); }
		catch(AssertionError e) { 
			System.out.println("Expected error detected for testing CC-E1>> " + e.getMessage());
			shouldFail = false; 
		}
		finally { 
			if(shouldFail) {
				fail("Error eCC-E1: Level Constuctor should have failed - NULL object given.");
				shouldFail = true;
			}
		}
	}
	
	@Test
	public void testLevelConstructor() {
		boolean shouldFail = true;
		
		// Expected Value Tests - Expected Results: Pass
		testLevelConstruct(0);
		testLevelConstruct(1);
		testLevelConstruct(2);
	
		// Unacceptable Value Tests - Expected Results: Fail
		try { testLevelConstruct(-1); }
		catch(AssertionError e) { 
			System.out.println("Expected error detected for testing LC-E1>> " + e.getMessage());
			shouldFail = false; 
		}
		finally { 
			if(shouldFail) {
				fail("Error eLC-E1: Level Constuctor should have failed - undervalued level given.");
				shouldFail = true;
			}
		}
		
		try { testLevelConstruct(3); }
		catch(AssertionError e) { 
			System.out.println("Expected error detected for testing LC-E2>> " + e.getMessage());
			shouldFail = false; 
		}
		finally { 
			if(shouldFail) {
				fail("Error eLC-E2: Level Constuctor should have failed - overvalued level given.");
				shouldFail = true;
			}
		}
	}
	
	@Test
	public void testParamConstructor(){
		boolean shouldFail = true;

		// Expected Values Test - Expected Results: Pass
		testParamConstruct(1,1,0);
		testParamConstruct(1,9,4);
		testParamConstruct(5,4,20);
		testParamConstruct(5,4,101);
		
		// Unacceptable Value Tests - Expected Results: Fail
		String em = "";
		for(int m = -1; m < 1; m++) {
			for(int c = -1; c < 2; c++) {
				for(int r = -1; r < 2; r++) {
					if(r > 0 && c > 0 && m > -1)
						continue;
					String code = "r" + String.valueOf(r + 2) + "c" + String.valueOf(c + 2) + "m" + String.valueOf(m + 2);
					try { testParamConstruct(r,c,m); }
					catch(AssertionError e) { 
						System.out.println("Expected error detected for testing PC-E" + code + ">> " + e.getMessage());
						shouldFail = false; 
					}
					finally { 
						if(shouldFail) {
							em = "ePC-E" + code + ": Parameter Constructor should have failed - ";
							
							if(r < 0) em = em.concat("Negative row parameter given");
							else if(r == 0) em = em.concat("Zero-value row parameter given");
							
							if(c < 1 && r < 1) em = em.concat("; ");
							if(c < 0) em = em.concat("Negative column parameter given");
							else if(c == 0) em = em.concat("Zero-value row parameter given");
							
							if(m < 0 && (c < 1 || r < 1)) em = em.concat("; ");
							if(m < 0) em = em.concat("Negative mine parameter given");
							
							em = em.concat(".");
							
							fail(em);
						}
						
						shouldFail = true;
					}
				}
			}
		}
	}
	
	@Test
	public void testFieldValuesMethods() {
		Model m = new Minefield();
		
		int rows = m.getRows(),
			columns = m.getColumns();
		
		int rand_cell = (int) Math.floor(Math.random() * (rows * columns)),
			x = rand_cell / columns,
			y = rand_cell % columns;
		
	// Visible Value Tests
		//Expected Values Tests - Expected Results: Pass
		assertEquals(Constants.CELL_TYPE_HIDDEN, m.getVisibleValue(x,y), "Error eM-GVVD: Default visible value at [" + String.valueOf(x) + ", " + String.valueOf(y) + "] does not match.  Expected: " + String.valueOf(Constants.CELL_TYPE_HIDDEN) + " - \"Hidden\"");
		
		for(int v = Constants.CELL_TYPE_REVEAL; v <= Constants.CELL_TYPE_QUESTION; v++) {
			assertEquals(0, m.setVisibleValue(x, y, v), "Error eM-SVV" + String.valueOf(v + 1) + ": Failed to store visible value at [" + String.valueOf(x) + ", " + String.valueOf(y) + "].  Expected: 0 - \"Success\"");
			assertEquals(v, m.getVisibleValue(x, y), "Error eM-GVV" + String.valueOf(v + 1) + ": Retrieved visible value at [" + String.valueOf(x) + ", " + String.valueOf(y) + "] does not match.  Expected: " + String.valueOf(v));
		}
		
		//Unacceptable Values Tests - Expected Results: Fail
		assertEquals(-1, m.setVisibleValue(x, y, -1),"Error eM-SVV5: Negative visible value was stored.  Expected: -1 - \"Failed\"");
		assertEquals(-1, m.setVisibleValue(x, y, 4), "Error eM-SVV6: Unacceptable visible value was stored.  Expected: -1 - \"Failed\"");
		
		assertEquals(-1, m.getVisibleValue(-1, y), "Error eM-GVV5r1: Indexed cell outside array at negative x-value returned a visible value.  Expected: -1 - \"Failed\"");
		assertEquals(-1, m.getVisibleValue(m.getRows(), y), "Error eM-GVV5r2: Indexed cell outside array at x-value >= 'Rows' returned a visible value.  Expected: -1 - \"Failed\"");

		assertEquals(-1, m.getVisibleValue(x, -1), "Error eM-GVV5c1: Indexed cell outside array at negative y-value returned a visible value.  Expected: -1 - \"Failed\"");
		assertEquals(-1, m.getVisibleValue(x, m.getColumns()), "Error eM-GVV5c2: Indexed cell outside array at y-value >= 'Columns' returned a visible value.  Expected: -1 - \"Failed\"");
		
	// Mine Value Tests
		//Expected Values Tests - Expected Results: Pass
		assertEquals(0, m.getMineValue(x,y), "Error eM-GMVD: Default mine value at [" + String.valueOf(x) + ", " + String.valueOf(y) + "] does not match.  Expected: 0");
		
		for(int b = Constants.SHOW_MINE_FIRED; b < 9; b++) {
			assertEquals(0, m.setMineValue(x, y, b), "Error eM-SMV" + String.valueOf(b + 1) + ": Failed to store mine value at [" + String.valueOf(x) + ", " + String.valueOf(y) + "].  Expected: 0 - \"Success\"");
			assertEquals(b, m.getMineValue(x, y), "Error eM-GMV" + String.valueOf(b + 1) + ": Retrieved mine value at [" + String.valueOf(x) + ", " + String.valueOf(y) + "] does not match.  Expected: " + String.valueOf(b));
		}
		
		//Unacceptable Values Tests - Expected Results: Fail
		assertEquals(-1, m.setMineValue(x, y, -3), "Error eM-SMV5: Unacceptable, negative mine value was stored.  Expected: -1 - \"Failed\"");
		assertEquals(-1, m.setMineValue(x, y, 9), "Error eM-SMV5: Unacceptable, positive mine value was stored.  Expected: -1 - \"Failed\"");

		assertTrue(m.getMineValue(-1, y) < Constants.SHOW_MINE_FIRED, "Error eM-GMV5r1: Indexed cell outside array at negative x-value returned a mine value.  Expected: <" + String.valueOf(Constants.SHOW_MINE_FIRED) + " - \"Failed\"");
		assertTrue(m.getMineValue(m.getRows(), y) < Constants.SHOW_MINE_FIRED, "Error eM-GMV5r2: Indexed cell outside array at x-value >= 'Rows' returned a mine value.  Expected: <" + String.valueOf(Constants.SHOW_MINE_FIRED) + " - \"Failed\"");

		assertTrue(m.getMineValue(x, -1) < Constants.SHOW_MINE_FIRED, "Error eM-GMV5c1: Indexed cell outside array at negative y-value returned a mine value.  Expected: <" + String.valueOf(Constants.SHOW_MINE_FIRED) + " - \"Failed\"");
		assertTrue(m.getMineValue(x, m.getColumns()) < Constants.SHOW_MINE_FIRED, "Error eM-GMV5c2: Indexed cell outside array at y-value >= 'Columns' returned a mine value.  Expected: <" + String.valueOf(Constants.SHOW_MINE_FIRED) + " - \"Failed\"");
	
	}
	
	@Test
	public void testGameStateIndicatorMethods() {
		Model m = new Minefield();
	// Game State, Mine Found & Game Over Tests	
		// Expected State Values Test - Expected Results: Pass
		assertEquals(Constants.GAME_STATUS_READY, m.getGameState(), "Error eM-GS1: 'Game state' default value does not match.  Expected: " + String.valueOf(Constants.GAME_STATUS_READY + " - \"READY\""));
		assertFalse(m.isGameOver(), "Error eM-GO1: 'Game Over' indicator default is incorrect.  Expected: False");
		assertFalse(m.isMineFound(), "Error eM-BF1: 'Mine Found' indicator default is incorrect.  Expected: False");
		m.updateGameState();
		assertEquals(Constants.GAME_STATUS_ONGOING, m.getGameState(), "Error eM-GS2: 'Game state' value does not match.  Expected: " + String.valueOf(Constants.GAME_STATUS_ONGOING + " - \"ONGOING\""));
		m.gameOver();
		assertTrue(m.isGameOver(), "Error eM-GO2: 'Game Over' indicator is incorrect.  Expected: True");
		m.updateGameState();
		assertEquals(Constants.GAME_STATUS_WIN, m.getGameState(), "Error eM-GS3: 'Game state' value does not match.  Expected: " + String.valueOf(Constants.GAME_STATUS_WIN + " - \"WIN\""));
		
		m.mineFound();
		assertTrue(m.isMineFound(), "Error eM-BF2: 'Mine Found' indicator is incorrect.  Expected: True");
		m.updateGameState();
		assertEquals(Constants.GAME_STATUS_WIN, m.getGameState(), "Error eM-GS4: 'Game state' value does not match - Should not change after 'End' condition is met.  Expected: " + String.valueOf(Constants.GAME_STATUS_WIN + " - \"WIN\""));

		m = new Minefield();
		m.gameOver();
		assertTrue(m.isGameOver(), "Error eM-GO3: 'Game Over' indicator is incorrect.  Expected: True");
		m.updateGameState();
		assertEquals(Constants.GAME_STATUS_WIN, m.getGameState(), "Error eM-GS5: 'Game state' value does not match.  Expected: " + String.valueOf(Constants.GAME_STATUS_WIN + " - \"WIN\""));
		
		m = new Minefield();
		m.updateGameState();
		assertEquals(Constants.GAME_STATUS_ONGOING,m.getGameState(), "Error eM-GS6: 'Game state' value does not match.  Expected: " + String.valueOf(Constants.GAME_STATUS_ONGOING + " - \"ONGOING\""));
		m.gameOver();
		assertTrue(m.isGameOver(), "Error eM-GO4: 'Game Over' indicator is incorrect.  Expected: True");
		m.mineFound();
		assertTrue(m.isMineFound(), "Error eM-BF3: 'Mine Found' indicator is incorrect.  Expected: True");
		m.updateGameState();
		assertEquals(Constants.GAME_STATUS_LOSE, m.getGameState(), "Error eM-GS7: 'Game state' value does not match.  Expected: " + String.valueOf(Constants.GAME_STATUS_LOSE + " - \"LOSE\""));
		
		m = new Minefield();
		m.gameOver();
		assertTrue(m.isGameOver(), "Error eM-GO5: 'Game Over' indicator is incorrect.  Expected: True");
		m.mineFound();
		assertTrue(m.isMineFound(), "Error eM-BF4: 'Mine Found' indicator is incorrect.  Expected: True");
		m.updateGameState();
		assertEquals(Constants.GAME_STATUS_LOSE, m.getGameState(), "Error eM-GS8: 'Game state' value does not match.  Expected: " + String.valueOf(Constants.GAME_STATUS_LOSE + " - \"LOSE\""));
		
		// Unacceptable State Values Tests - Expected Results: Fail
		m = new Minefield();
		m.mineFound();
		assertTrue(m.isMineFound(), "Error eM-BF5: 'Mine Found' indicator is incorrect.  Expected: True");
		m.updateGameState();
		assertEquals(Constants.GAME_STATUS_READY, m.getGameState(), "Error eM-GS9: 'Game state' value does not match - Illegal state accepted.  Expected: " + String.valueOf(Constants.GAME_STATUS_READY + " - \"READY\""));
		
		m = new Minefield();
		m.updateGameState();
		assertEquals(Constants.GAME_STATUS_ONGOING,m.getGameState(), "Error eM-GS10: 'Game state' value does not match.  Expected: " + String.valueOf(Constants.GAME_STATUS_ONGOING + " - \"ONGOING\""));
		m.mineFound();
		assertTrue(m.isMineFound(), "Error eM-BF6: 'Mine Found' indicator is incorrect.  Expected: True");
		m.updateGameState();
		assertEquals(Constants.GAME_STATUS_ONGOING, m.getGameState(), "Error eM-GS11: 'Game state' value does not match - Illegal state accepted.  Expected: " + String.valueOf(Constants.GAME_STATUS_ONGOING + " - \"ONGOING\""));
		
	// Mines-Last-Standing Test
		m = new Minefield();
		
		int rows = m.getRows(),
			columns = m.getColumns();
		
		int rand_cell = (int) Math.floor(Math.random() * (rows * columns)),
				x = rand_cell / columns,
				y = rand_cell % columns;
		
		assertFalse(m.minesLastStanding(), "Error eM-MLS1: Incorrect default result for remaining unrevealed mines - No mines on board.  Expected: False");
		
		m.setMineValue(x, y, -1);
		assertFalse(m.minesLastStanding(), "Error eM-MLS2: Incorrect result for remaining unrevealed mines - Other non-mine cells still hidden.  Expected: False");
		
		m.setVisibleValue((x - 1) % rows, y, Constants.CELL_TYPE_FLAG);
		m.setVisibleValue(x, (y - 1) % columns, Constants.CELL_TYPE_QUESTION);
		assertFalse(m.minesLastStanding(), "Error eM-MLS3: Incorrect result for remaining unrevealed mines - Other non-mine cells still hidden.  Expected: False");

		for(int r = 0; r < m.getRows(); r++) {
			for(int c = 0; c < m.getColumns(); c++) {
				if(r == x && c == y)
					continue;
				m.setVisibleValue(r, c, Constants.CELL_TYPE_REVEAL);
			}
		}
		
		assertTrue(m.minesLastStanding(), "Error eM-MLS4: Incorrect result for remaining unrevealed mines - Mine cells are the only ones still hidden.  Expected: True");

		m = new Minefield();
		for(int r = 0; r < m.getRows(); r++) {
			for(int c = 0; c < m.getColumns(); c++) {
				m.setMineValue(r, c, Constants.SHOW_MINE);
			}
		}
		
		assertTrue(m.minesLastStanding(), "Error eM-MLS5: Incorrect result for remaining unrevealed mines - Mine cells are the only ones still hidden.  Expected: True");
	}
	
	@Test
	public void testResetFieldMethod() {
		Model m = new Minefield();
		
		int rows = m.getRows(),
			columns = m.getColumns(),
			mines = m.getNumMines();
		
		int rand_cell = (int)Math.floor(Math.random() * (rows * columns)),
			x = rand_cell / columns,
			y = rand_cell % columns,
			v = 0,			// Any value other than Constant.CELL_TYPE_HIDDEN
			b = 6;			// Any non-zero value between -2 and 8
		
		m.setVisibleValue(x, y, v);
		m.setMineValue(x, y, b);
		
		int[][] blankVF = new int[rows][columns],
				blankMF = new int[rows][columns];
		
		for(int r = 0; r < rows; r++){
			Arrays.fill(blankVF[r], Constants.CELL_TYPE_HIDDEN);
			Arrays.fill(blankMF[r],0);
		}
		
		Model dupl = new Minefield((Minefield) m);
		Model mirror = dupl;
		dupl.resetField();
		
		assertEquals(rows, dupl.getRows(), "Error eM-RESF1: 'Rows' is not the right value.  Expected: " + String.valueOf(rows));
		assertEquals(columns, dupl.getColumns(), "Error eM-RESF2: 'Columns' is not the right value.  Expected: " + String.valueOf(columns));
		assertEquals(mines, dupl.getNumMines(), "Error eM-RESF3: 'Number of mines' is not the right value.  Expected: " + String.valueOf(mines));
		assertEquals(mines, dupl.getNumFlags(), "Error eM-RESF4: 'Number of flags' is not the right value.  Expected: " + String.valueOf(mines));
		assertFalse(dupl.isGameOver(), "Error eM-RESF5: 'Game over' indicator is not the right value.  Expected: FALSE");
		assertFalse(dupl.isMineFound(), "Error eM-RESF6: 'Mine found' indicator is not the right value.  Expected: FALSE");
		assertEquals(Constants.GAME_STATUS_READY, dupl.getGameState(), "Error eM-RESF7: Game state is not the right value.  Expected: 0 - \"Ready\"");
		
		assertFalse(Arrays.deepEquals(m.getVisibleField(), dupl.getVisibleField()), "Error eM-RESF8a: Reset visible field should not match original non-blank visible field.  Expected: False");
		assertFalse(Arrays.deepEquals(m.getMineField(), dupl.getMineField()), "Error eM-RESF8b: Reset mine field should not match original non-blank mine field.  Expected: False");
		assertTrue(Arrays.deepEquals(blankVF, dupl.getVisibleField()), "Error eM-RESF9a: Reset visible field should match blank visible field.  Expected: True");
		assertTrue(Arrays.deepEquals(blankMF, dupl.getMineField()), "Error eM-RESF9b: Reset mine field should match blank mine field.  Expected: True");
		
		assertSame(mirror, dupl, "Error eM-RESF10: Reset Model Object should maintain the same object reference.  Expected: Same");
	}
	
	@Test
	public void testRedoFieldMethod() {
		Model m = new Minefield();
		
		int rows = m.getRows(),
			columns = m.getColumns(),
			mines = m.getNumMines();
		
		int rand_cell = (int)Math.floor(Math.random() * (rows * columns)),
			x = rand_cell / columns,
			y = rand_cell % columns,
			v = (int)Math.floor(Math.random() * 4),
			b = (int)Math.floor(Math.random() * 11) - 2;
		
		m.setVisibleValue(x, y, v);
		m.setMineValue(x, y, b);
		
		//Expected Values Test - Expected Results: Pass
		Model dupl = new Minefield((Minefield) m);
		Model mirror = dupl;
		rows = 3;
		columns = 3;
		mines = 4;
		
		int[][] blankVF = new int[rows][columns],
				blankMF = new int[rows][columns];
		
		for(int r = 0; r < rows; r++){
			Arrays.fill(blankVF[r], Constants.CELL_TYPE_HIDDEN);
			Arrays.fill(blankMF[r],0);
		}
		dupl.redoField(rows, columns, mines);
		
		assertEquals(rows, dupl.getRows(), "Error eM-RDF1: 'Rows' is not the right value.  Expected: " + String.valueOf(rows));
		assertEquals(columns, dupl.getColumns(), "Error eM-RDF2: 'Columns' is not the right value.  Expected: " + String.valueOf(columns));
		assertEquals(mines, dupl.getNumMines(), "Error eM-RDF3: 'Number of mines' is not the right value.  Expected: " + String.valueOf(mines));
		assertEquals(mines, dupl.getNumFlags(), "Error eM-RDF4: 'Number of flags' is not the right value.  Expected: " + String.valueOf(mines));
		assertFalse(dupl.isGameOver(), "Error eM-RDF5: 'Game over' indicator is not the right value.  Expected: FALSE");
		assertFalse(dupl.isMineFound(), "Error eM-RDF6: 'Mine found' indicator is not the right value.  Expected: FALSE");
		assertEquals(Constants.GAME_STATUS_READY, dupl.getGameState(), "Error eM-RDF7: Game state is not the right value.  Expected: 0 - \"Ready\"");
		
		assertFalse(Arrays.deepEquals(m.getVisibleField(), dupl.getVisibleField()), "Error eM-RDF8a: Reset visible field should not match original non-blank visible field.  Expected: False");
		assertFalse(Arrays.deepEquals(m.getMineField(), dupl.getMineField()), "Error eM-RDF8b: Reset mine field should not match original non-blank mine field.  Expected: False");
		assertTrue(Arrays.deepEquals(blankVF, dupl.getVisibleField()), "Error eM-RDF9a: Reset visible field should match blank visible field.  Expected: True");
		assertTrue(Arrays.deepEquals(blankMF, dupl.getMineField()), "Error eM-RDF9b: Reset mine field should match blank mine field.  Expected: True");
		
		assertSame(mirror, dupl, "Error eM-RDF10: Reset Model Object should maintain the same object reference.  Expected: Same");
		
		//Unacceptable Values Test - Expected Results: Fail
		boolean shouldFail = true;
		
		dupl = new Minefield((Minefield) m);
		mirror = dupl;

		String em = "";
		for(mines = -1; mines < 1; mines++) {
			for(columns = -1; columns < 2; columns++) {
				for(rows = -1; rows < 2; rows++) {
					if(rows > 0 && columns > 0 && mines > -1)
						continue;
					String code = "r" + String.valueOf(rows + 2) + "c" + String.valueOf(columns + 2) + "m" + String.valueOf(mines + 2);
					try { 
						dupl.redoField(rows, columns, mines);
						
						assertEquals(rows, dupl.getRows(), "Error eM-RDF1: 'Rows' is not the right value.  Expected: " + String.valueOf(rows));
						assertEquals(columns, dupl.getColumns(), "Error eM-RDF2: 'Columns' is not the right value.  Expected: " + String.valueOf(columns));
						assertEquals(mines, dupl.getNumMines(), "Error eM-RDF3: 'Number of mines' is not the right value.  Expected: " + String.valueOf(mines));
						assertEquals(mines, dupl.getNumFlags(), "Error eM-RDF4: 'Number of flags' is not the right value.  Expected: " + String.valueOf(mines));
						assertFalse(dupl.isGameOver(), "Error eM-RDF5: 'Game over' indicator is not the right value.  Expected: FALSE");
						assertFalse(dupl.isMineFound(), "Error eM-RDF6: 'Mine found' indicator is not the right value.  Expected: FALSE");
						assertEquals(Constants.GAME_STATUS_READY, dupl.getGameState(), "Error eM-RDF7: Game state is not the right value.  Expected: 0 - \"Ready\"");
						
						assertFalse(Arrays.deepEquals(m.getVisibleField(), dupl.getVisibleField()), "Error eM-RDF8a: Reset visible field should not match original non-blank visible field.  Expected: False");
						assertFalse(Arrays.deepEquals(m.getMineField(), dupl.getMineField()), "Error eM-RDF8b: Reset mine field should not match original non-blank mine field.  Expected: False");
						assertTrue(Arrays.deepEquals(blankVF, dupl.getVisibleField()), "Error eM-RDF9a: Reset visible field should match blank visible field.  Expected: True");
						assertTrue(Arrays.deepEquals(blankMF, dupl.getMineField()), "Error eM-RDF9b: Reset mine field should match blank mine field.  Expected: True");
						
						assertSame(mirror, dupl, "Error eM-RDF10: Reset Model Object should maintain the same object reference.  Expected: Same");
					}
					catch(AssertionError e) { 
						System.out.println("Expected error detected for testing M-RDF11" + code + ">> " + e.getMessage());
						shouldFail = false; 
					}
					finally { 
						if(shouldFail) {
							em = "eM-RDF11" + code + ": Parameter Constructor should have failed - ";
							
							if(rows < 0) em = em.concat("Negative row parameter given");
							else if(rows == 0) em = em.concat("Zero-value row parameter given");
							
							if(columns < 1 && rows < 1) em = em.concat("; ");
							if(columns < 0) em = em.concat("Negative column parameter given");
							else if(columns == 0) em = em.concat("Zero-value row parameter given");
							
							if(mines < 0 && (columns < 1 || rows < 1)) em = em.concat("; ");
							if(mines < 0) em = em.concat("Negative mine parameter given");
							
							em = em.concat(".");
							
							fail(em);
						}
						
						shouldFail = true;
					}
				}
			}
		}	
	}
	
	protected void testLevelConstruct(int level) {
		boolean nonLevel = level < Constants.LEVEL_BEGINNER || level > Constants.LEVEL_EXPERT;
		
		assertFalse(nonLevel, "Error eLC-A1: Level has an improper or unrecognized value.  Expected: 0 - \"Beginner\", 1 - \"Intermediate\", OR 2 - \"Expert\"");
		
		if(nonLevel) { return; }
		Model board = new Minefield(level);
		
		int[] expectedValues = Constants.LEVEL_PARAMETERS[level];
		int rows = expectedValues[0],
			columns = expectedValues[1],
			mines = expectedValues[2];
		
		assertEquals(rows, board.getRows(), "Error eLC-R1: 'Rows' is not the right value.  Expected: " + String.valueOf(rows));
		assertEquals(columns, board.getColumns(), "Error eLC-C1: 'Columns' is not the right value.  Expected: " + String.valueOf(columns));
		assertEquals(mines, board.getNumMines(), "Error eLC-M1: 'Number of mines' is not the right value.  Expected: " + String.valueOf(mines));
		assertEquals(mines, board.getNumFlags(), "Error eLC-F1: 'Number of flags' is not the right value.  Expected: " + String.valueOf(mines));
		assertFalse(board.isGameOver(), "Error eLC-GO1: 'Game over' indicator is not the right value.  Expected: FALSE");
		assertFalse(board.isMineFound(), "Error eLC-BF1: 'Mine found' indicator is not the right value.  Expected: FALSE");
		assertEquals(Constants.GAME_STATUS_READY, board.getGameState(), "Error eLC-GS1: Game state is not the right value.  Expected: 0 - \"Ready\"");
		
		checkBlankBoard(board);	
	}
	
	protected void testParamConstruct(int rows, int columns, int mines) {
		boolean goodParams = (rows > 0) && (columns > 0) && (mines > -1);
		
		assertTrue(goodParams, "Error ePC-A1: One or more arguments have an invalid value.  Expected: Rows > 0; Columns > 0; Mines >= 0");
		
		if(!goodParams) { return; }
		
		Model board = new Minefield(rows, columns, mines);
		
		if(mines > (rows * columns))
			mines = rows * columns;
		
		assertEquals(rows, board.getRows(), "Error ePC-R1: 'Rows' is not the right value.  Expected: " + String.valueOf(rows));
		assertEquals(columns, board.getColumns(), "Error ePC-C1: 'Columns' is not the right value.  Expected: " + String.valueOf(columns));
		assertEquals(mines, board.getNumMines(), "Error ePC-M1: 'Number of mines' is not the right value.  Expected: " + String.valueOf(mines));
		assertEquals(mines, board.getNumFlags(), "Error ePC-F1: 'Number of flags' is not the right value.  Expected: " + String.valueOf(mines));
		assertFalse(board.isGameOver(), "Error ePC-GO1: 'Game over' indicator is not the right value.  Expected: FALSE");
		assertFalse(board.isMineFound(), "Error ePC-BF1: 'Mine found' indicator is not the right value.  Expected: FALSE");
		assertEquals(Constants.GAME_STATUS_READY, board.getGameState(), "Error ePC-GS1: Game state is not the right value.  Expected: 0 - \"Ready\"");
		
		checkBlankBoard(board);	
	}
	
	private void testCopyConstruct(Model m) {
		assertNotNull(m,"Error eCC-A1: Argument is null.  Expected: Non-null Model Object.");
		
		if(m == null) { return; }
		
		Model board = new Minefield((Minefield) m);
		
		int rows = m.getRows(),
			columns = m.getColumns();
		
		assertEquals(rows, board.getRows(), "Error eCC-R1: 'Rows' is not the right value.  Expected: " + String.valueOf(rows));
		assertEquals(columns, board.getColumns(), "Error eCC-C1: 'Columns' is not the right value.  Expected: " + String.valueOf(columns));
		assertEquals(m.getNumMines(), board.getNumMines(), "Error eCC-M1: 'Number of Mines' is not the right value.  Expected: " + String.valueOf(m.getNumMines()));
		assertEquals(m.getNumFlags(), board.getNumFlags(), "Error eCC-F1: 'Number of Flags' is not the right value.  Expected: " + String.valueOf(m.getNumFlags()));
		assertEquals(m.isGameOver(), board.isGameOver(), "Error eCC-GO1: 'Game Over' indicator is not the right value.  Expected: " + String.valueOf(m.isGameOver()).toUpperCase());
		assertEquals(m.isMineFound(), board.isMineFound(), "Error eCC-B1: 'Mine Found' indicator is not the right value.  Expected: " + String.valueOf(m.isMineFound()).toUpperCase());
		assertEquals(m.getGameState(), board.getGameState(), "Error eCC-GS1: Game state is not the right value.  Expected: " + String.valueOf(m.getGameState()));
		
		int[][] m_vf = m.getVisibleField(),
				m_mf = m.getMineField(),
				b_vf = board.getVisibleField(),
				b_mf = board.getMineField();
		
		assertNotSame(m_vf, b_vf, "Error eCC-VF1: Visible Fields link to same reference.  Expected: Separate copy of the Visible Field.");
		assertTrue(Arrays.deepEquals(m_vf, b_vf), "Error eCC-VF2: Visible fields do not match. Expected: Exact copy of the Visible Field.");
		assertNotSame(m_mf, b_mf, "Error eCC-MF1: Mine Fields link to same reference.  Expected: Separate copy of the Mine Field.");
		assertTrue(Arrays.deepEquals(m_mf, b_mf), "Error eCC-MF2: Mine fields do not match. Expected: Exact copy of the Mine Field.");
		
		String ec = "";
		for(int r = 0; r < rows; r++) {
			for(int c = 0; c < columns; c++) {
				ec = "r" + String.valueOf(r) + "c" + String.valueOf(c);
				if(r < m_vf.length && c < m_vf[r].length)
					assertEquals(m_vf[r][c], board.getVisibleValue(r, c), "Error eCC-VF3" + ec +": Visible Field value does not match.  Expected: " + String.valueOf(m_vf[r][c]));
				if(r < m_mf.length && c < m_mf[r].length)
					assertEquals(m_mf[r][c], board.getMineValue(r, c), "Error eCC-MF3" + ec +": Mine Field value does not match.  Expected: " + String.valueOf(m_mf[r][c]));
			}
		}
		
		assertNotSame(m, board, "Error eCC-REF1: Model Objects link to same reference.  Expected: Separate instantiated copy of Model Object.");
	}
	
	private void checkBlankBoard(Model board) {
		int rows = board.getRows(),
			columns = board.getColumns();
		
		int[][] vf = board.getVisibleField(),
				mf = board.getMineField();
		
		assertNotNull(vf, "Error eBB-VF1: Visible Field is null.  Expected: Non-null 2D integer array for the Visible Field.");
		assertNotNull(mf, "Error eBB-MF1: Mine Field is null.  Expected: Non-null 2D integer array for the Mine Field.");
		
		boolean vfNull = vf == null,
				mfNull = mf == null;
		
		
		if(vfNull) {
			assertEquals(rows, -1, "Error eBB-VF2Ra: Visible Field has no rows.  Expected: " + String.valueOf(rows));
			assertEquals(columns, -1, "Error eBB-VF2Ca: Visible Field has no columns.  Expected: " + String.valueOf(columns));
		}
		else
			assertEquals(rows, vf.length, "Error eBB-VF2Rb: Number of rows in Visible Field do not match.  Expected: " + String.valueOf(rows));
			
		
		if(mfNull) {
			assertEquals(rows, -1, "Error eBB-MF2Ra: Mine Field has no rows.  Expected: " + String.valueOf(rows));
			assertEquals(columns, -1, "Error eBB-MF2Ca: Mine Field has no columns.  Expected: " + String.valueOf(columns));
		}
		else
			assertEquals(rows, mf.length, "Error eBB-MF2Rb: Number of rows in Mine Field do not match.  Expected: " + String.valueOf(rows));
		
		boolean uniformVF = !vfNull,
				uniformMF = !mfNull;
		
		String ec = "";
		
		for(int r = 0; r < rows; r++) {
			if(uniformVF && r < vf.length && vf[r].length != columns) {
				assertEquals(columns, vf[r].length, "Error eBB-VF2Cb: Number of columns in Visible Field do not match.  Expected: " + String.valueOf(columns));
				uniformVF = false;
			}
			
			if(uniformMF && r < mf.length && mf[r].length != columns) {
				assertEquals(columns, mf[r].length, "Error eBB-MF2Cb: Number of columns in Mine Field do not match.  Expected: " + String.valueOf(columns));
				uniformMF = false;
			}
			
			for(int c = 0; c < columns; c++) {
				ec = "r" + String.valueOf(r) + "c" + String.valueOf(c);
				
				if(!vfNull && c < vf[r].length) {
					assertEquals(Constants.CELL_TYPE_HIDDEN, vf[r][c],"Error eBB-VF3" + ec + "a: Value from local Visible Field is not right.  Expected: " + String.valueOf(Constants.CELL_TYPE_HIDDEN));
					assertEquals(Constants.CELL_TYPE_HIDDEN, board.getVisibleValue(r, c), "Error eBB-VF3" + ec + "b: Value from stored Visible Field is not right.  Expected: " + String.valueOf(Constants.CELL_TYPE_HIDDEN));
				}
				
				if(!mfNull && c < mf[r].length) {
					assertEquals(0, mf[r][c], "Error eBB-MF3" + ec + "a: Value from local Mine Field is not right.  Expected: 0");
					assertEquals(0, board.getMineValue(r, c), "Error eBB-VF3" + ec + "b: Value from stored Mine Field is not right.  Expected: 0");
				}
			}
		}
		
		if(uniformVF)
			assertEquals(columns, vf[0].length, "Error eBB-VF2Cb: Number of columns in Visible Field do not match.  Expected: " + String.valueOf(columns));
		if(uniformMF)
			assertEquals(columns, mf[0].length, "Error eBB-MF2Cb: Number of columns in Mine Field do not match.  Expected: " + String.valueOf(columns));	
	}
}

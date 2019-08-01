package ms.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;

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
	public void testConstructors(){
		boolean shouldFail = true;
		
	// Level Constructor Tests
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
		
	// Model Object Constructor Tests	
		Model board = new Minefield(2);
		board.setVisibleValue(8, 8, 2);
		board.setMineValue(8, 8, 5);
		
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
	
	// RCM Parameter Constructor Tests
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
							em = "ePC-Er" + code + ": Parameter Constructor should have failed - ";
							
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
	
	/*
	@Test
	public void testMethods() {
		Model m = new Minefield();
		int rand_cell = (int) Math.floor(Math.random() * (m.getRows() * m.getColumns())),
			x = rand_cell / m.getColumns(),
			y = rand_cell % m.getColumns();
		
		for(int v = Constants.CELL_TYPE_REVEAL; v <= Constants.CELL_TYPE_QUESTION; v++) {
			assertEquals(0, m.setVisibleValue(x, y, v), "Error eM-SVV" + String.valueOf(v + 1) + ": Failed to store visible value at [" + String.valueOf(x) + ", " + String.valueOf(y) + "].  Expected: 0 - \"Success\"");
			assertEquals(v, m.getVisibleValue(x, y), "Error eM-GVV" + String.valueOf(v + 1) + ": Retrieved visible value at [" + String.valueOf(x) + ", " + String.valueOf(y) + "] does not match.  Expected: " + String.valueOf(v));
		}
		
		//TODO: Test get/set pre-mods.
		//TODO: Test get/set post-mods.
		//TODO: Test remaining methods.
	}
	*/
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

package ms;
/**
 * this class contains constants used by 
 * @author lwang
 *
 */
public class Constants {
	
	/**
	 *  passed from View to Controllor
	 */
	public static final int CLICK_TYPE_LEFT = 1; 
	public static final int CLICK_TYPE_RIHGT = 2;
	public static final int CLICK_TYPE_BOTH = 3;
	
	/**
	 *  return from click. 	
	 */
	public static final int GAME_STATUS_READY = 0 ; 
	public static final int GAME_STATUS_ONGOING = 1 ;
	public static final int GAME_STATUS_WIN = 2 ;
	public static final int GAME_STATUS_LOSE  = 2 ;
	
	/**
	 * passed from Controller to view for cover
	 */
	public static final int FLAG_TYPE_NONE = 0; 
	public static final int FLAG_TYPE_RED = 1;
	public static final int FLAG_TYPE_QUESTION = 2 ;

	
	/**
	 * passed from Controller to view for cover
	 */
	public static final int SHOW_MINE = -1; 
	public static final int SHOW_MINE_FIRED = -2; // when lose 
	
	
	
	/*
	 * game level and parameters
	 */
	public static final int LEVEL_BEGINNER  = 0; 
	public static final int LEVEL_INTERMEDIATE  = 1;
	public static final int LEVEL_EXPERT  = 2;
												//   rows,	cols,	mines 			
	public static final int[][] LEVEL_PARAMETERS = { { 9,	9,		10 }, 
												     { 16,	16,		40}, 
												     { 16, 	30, 	99} }; 
	
	
}

package ms;

import ms.controller.*;
import ms.view.*;
import ms.model.*;

/**
 * Top class looking over the game
 * 
 * @author lwang
 *
 */
public class Game {

	Controller controller;
	View view;
	
	/**
	 * entry of the game  
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game ms = new Game();
	}
	
	/**
	 * Constructor
	 */
	public Game() {
		//controller = new DummyController();
		controller = new MSController();
		view = new BoardView();
		view.setController(controller);
		
		controller.setView(view);
		controller.start();
	}
}

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
	Model model;
	
	/**
	 * entry of the game  
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game ms = new Game();
	}
	
	/**
	 * Constructor
	 */
	public Game() {
		// model
		model = new Minefield();
		// controller
		controller = new MSController(model);
		// view
		view = new BoardView();
		view.setModel(model);
		view.setController(controller);
		
		controller.start();
	}
}

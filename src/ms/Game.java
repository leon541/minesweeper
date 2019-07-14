/**
 * 
 */
package ms;

/**
 * @author lwang
 *
 */
import ms.controller.*;
import ms.view.*;
import ms.model.*;

public class Game {

	Controller controller;
	View view;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game ms = new Game();
	}
	
	public Game() {
		controller = new DummyController();
		view = new BoardView();
		view.setController(controller);
		controller.start();
	}
}

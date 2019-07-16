package ms.controller;

import ms.view.View;

public class DummyController implements Controller {

	View view; 
	
	
	public void setView(View view) {
		this.view = view; 
	}
	
	@Override
	public int clickedGrid(int row, int col, int type) {
		// TODO Auto-generated method stub
		
		
		
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

}

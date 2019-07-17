package ms.controller;


import ms.Constants;
import ms.view.View;

public class DummyController implements Controller {

	View view; 
	
	
	public void setView(View view) {
		this.view = view; 
	}
	
	int cover = 1; 
	int under  = 0; 
	@Override
	public int clickedGrid(int row, int col, int type) {
		// TODO Auto-generated method stub
		if(type == Constants.CLICK_TYPE_RIGHT) {
			// rotate cover
			view.updateCell(row, col, cover, 0);
			if(++cover > 3) 
				cover = 1; 
			
		} else if( type == Constants.CLICK_TYPE_LEFT) {
			// rotate under
			view.updateCell(row, col, 0, under);
			if(++under > 8) 
				under = -2;
			
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

}

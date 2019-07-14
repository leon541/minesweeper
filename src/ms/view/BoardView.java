/**
 * 
 */
package ms.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ms.controller.Controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author lwang
 *
 */
public class BoardView extends JFrame implements View ,MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final int CELL_SIZE = 30;
	public final int MARGIN = 30;

	Controller controller;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BoardView mainView = new BoardView(); 

	}

	int rows, cols;
	CellButton [][] gridButtons;
	
	/**
	 * default view 
	 */
	public BoardView() {
		this(10, 10, 9);  // should use marcos
	}
	
	/**
	 * 
	 * @param rows
	 * @param cols
	 * @param mines
	 */
	public BoardView(int rows, int cols, int mines) {
		this.rows = rows; 
		this.cols = cols;
		gridButtons = new CellButton[rows][cols];

		this.setSize(this.rows * CELL_SIZE + MARGIN, this.cols * CELL_SIZE + MARGIN);
		this.getContentPane().setBackground(Color.CYAN);
		this.add(getMainPanel());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//this.pack();
		this.setLocationRelativeTo(null);  // center the frame
		this.setResizable(false);
		this.setVisible(true);	
	}
	
	/**
	 * 
	 * @param controller
	 */
	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	/**
	 * 
	 * @return
	 */
	JPanel getMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(getTopPanel(), BorderLayout.NORTH);
		mainPanel.add(getMinePanel(), BorderLayout.CENTER);
		mainPanel.add(new JPanel(), BorderLayout.SOUTH);
		mainPanel.add(new JPanel(), BorderLayout.EAST);
		mainPanel.add(new JPanel(), BorderLayout.WEST);
		mainPanel.addMouseListener(this);
		return mainPanel;
	}

	JPanel getTopPanel() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 3, 0, 0));
		topPanel.add(getCounterPanel());
		topPanel.add(new JButton("reset"));
		topPanel.add(getCounterPanel());

		return topPanel;
	}

	JPanel getCounterPanel() {
		JPanel countPanel = new JPanel();
		countPanel.setBackground(Color.BLUE);
		//countPanel.setPreferredSize(new Dimension(100, 50));
		return countPanel;
	}

	JPanel getMinePanel() {
		JPanel minePanel = new JPanel();
		minePanel.setBackground(Color.GRAY);
		minePanel.setPreferredSize(new Dimension(CELL_SIZE * rows, CELL_SIZE * cols));

		GridLayout gridLayout = new GridLayout(rows, cols);
		minePanel.setLayout(gridLayout);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				this.gridButtons[i][j] = new CellButton(i, j);
				this.gridButtons[i][j].addMouseListener(this);
				minePanel.add(this.gridButtons[i][j]);
			}
		}
		return minePanel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	private static boolean[] keys={ false, false}; // status for Left and right Mouse key
	
	@Override
	public void mousePressed(MouseEvent e) {
		if( e.getSource() instanceof CellButton ) {
			CellButton gridButton = (CellButton) e.getSource();

			if(e.getButton() == MouseEvent.BUTTON1 && !keys[0]){
				keys[0] = true;
			}
			else if(e.getButton() == MouseEvent.BUTTON3 && !keys[1]){
				keys[1] = true;
			}
			if(keys[0] && keys[1] ){
				System.out.println("Button: " + gridButton.getRow() + ","+ gridButton.getCol() +" Left+ right");
			}
		}

	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if( e.getSource() instanceof CellButton ) {
			CellButton gridButton = (CellButton) e.getSource();
			System.out.println("Button release: " + gridButton.getRow() + "," + gridButton.getCol());
			if(keys[0] && !keys[1] ){
				System.out.println(" left "); //  only left
				gridButton.changeIcon();
				keys[0] = false;
			}
			if(!keys[0]  && keys[1] ){
				System.out.println(" right"); // only right
				keys[1] = false;
			}
			if(keys[0] && keys[1]){
				System.out.println("Left+ right released!");
				keys[0] = false;
				keys[1] = false;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	

	@Override
	public int updateCell(int row, int col, int cover, int under) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void init(int rows, int cols, int mines) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}

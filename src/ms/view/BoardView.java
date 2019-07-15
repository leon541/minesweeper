/**
 * 
 */
package ms.view;

import ms.controller.Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ButtonGroup;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem ;
import javax.swing.JMenu;

/**
 * @author lwang
 *
 */
public class BoardView implements View, MouseListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int LEVEL_EASY  = 1; 
	private static final int LEVEL_MEDIUM  = 2;
	private static final int LEVEL_HARD  = 3;

	private final int CELL_SIZE = 25;
	
	private JFrame mainFrame; // main window
	
	Controller controller;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BoardView mainView = new BoardView(); 

	}

	int rows, cols, mines;
	CellButton [][] cellButtons;

	/**
	 * default view 
	 */
	public BoardView() {
		this(LEVEL_EASY);  // should use marcos
	}

	/**
	 * 
	 * @param rows
	 * @param cols
	 * @param mines
	 */
	public BoardView(int level) {
		init(level);
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
		minePanel.setPreferredSize(new Dimension(CELL_SIZE * cols, CELL_SIZE * rows));

		GridLayout gridLayout = new GridLayout(rows, cols);
		minePanel.setLayout(gridLayout);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				this.cellButtons[i][j] = new CellButton(i, j);
				this.cellButtons[i][j].addMouseListener(this);
				minePanel.add(this.cellButtons[i][j]);
			}
		}
		return minePanel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	// status for Left and right Mouse key, this is used to differentiate left from left + right. 
	private static boolean[] keys={ false, false}; 

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
	public void init(int level) {
		// TODO Auto-generated method stub
		switch (level) {
		case LEVEL_EASY: 
			init(level, 9, 9, 10);
			break;
		case LEVEL_MEDIUM:
			init(level, 16, 16, 40);
			break; 
		case LEVEL_HARD:
			init(level, 16, 30, 99);
			break;
		}
		
	}
	
	@Override
	public void init(int level,  int rows, int cols, int mines) {
		// TODO Auto-generated method stub
		this.rows = rows; 
		this.cols = cols;
		this.mines = mines;

		cellButtons = new CellButton[rows][cols];
		
		if(mainFrame != null)
			mainFrame.dispose();
		
		mainFrame = new JFrame("Mine Sweeper");
		mainFrame.setJMenuBar(buildMenu(level));
		//this.setSize(this.rows * CELL_SIZE + MARGIN, this.cols * CELL_SIZE + MARGIN);
		mainFrame.add(getMainPanel());
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);  // center the frame
		mainFrame.setResizable(false);

		mainFrame.setVisible(true);

	}

	/**
	 * Build MenuBar
	 * @return
	 */
	private JMenuBar buildMenu(int level) {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuLevels = new JMenu("Levels");

		JRadioButtonMenuItem levelEasy = new JRadioButtonMenuItem ("Easy"); // for dispaly
		levelEasy.setName("Easy"); // for action ID

		JRadioButtonMenuItem levelMedium  = new JRadioButtonMenuItem ("Medium");
		levelMedium.setName("Medium");
		JRadioButtonMenuItem levelHard  = new JRadioButtonMenuItem ("Hard");
		levelHard.setName("Hard"); 

		ButtonGroup btnGroup = new ButtonGroup();

		btnGroup.add(levelEasy);
		btnGroup.add(levelMedium);
		btnGroup.add(levelHard);

		switch (level) {
		case LEVEL_EASY: 
			levelEasy.setSelected(true);
			break;
		case LEVEL_MEDIUM:
			levelMedium.setSelected(true);
			break; 
		case LEVEL_HARD:
			levelHard.setSelected(true);
			break;
		}

		levelEasy.addActionListener(this);
		levelMedium.addActionListener(this);
		levelHard.addActionListener(this);

		menuLevels.add(levelEasy);
		menuLevels.add(levelMedium);
		menuLevels.add(levelHard);

		menuBar.add(menuLevels);
		return menuBar;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ( ((JRadioButtonMenuItem)e.getSource()).getName().indexOf("Easy") != -1) {
			System.out.println("easy");
			init(LEVEL_EASY);
		} else if (((JRadioButtonMenuItem)e.getSource()).getName().indexOf("Medium") != -1) {
			System.out.println("Medium");
			init(LEVEL_MEDIUM);
		} else if (((JRadioButtonMenuItem)e.getSource()).getName().indexOf("Hard") != -1) {
			System.out.println("Hard");
			init(LEVEL_HARD);
		}
	}

}

package ms.view;

import ms.Constants;
import ms.controller.Controller;
import ms.model.Minefield;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem ;
import javax.swing.JMenu;

/**
 * Main game view 
 * @author lwang
 *
 */
public class BoardView implements View, MouseListener, ActionListener {

	private final int CELL_SIZE = 25;

	public static ImageIcon [] ICON_FACES = { 
			new ImageIcon(ClassLoader.getSystemResource("images/facesmile.png")), // 0
			new ImageIcon(ClassLoader.getSystemResource("images/facesmile.png")), // 1
			new ImageIcon(ClassLoader.getSystemResource("images/facewin.png")),  // 2
			new ImageIcon(ClassLoader.getSystemResource("images/facelose.png"))   // 3
	};
	// UI components
	private JFrame mainFrame; // main Frame
	private Counter numberOfMines; 
	private Counter timerCounter; 
	CellButton [][] cellButtons;
	JButton resetButton ; 
	// controller
	Controller controller;
	Minefield model; 
	// game specs
	int rows, cols, mines;  
	
	private static final String LEVEL_BEGINNER = "Beginner";
	private static final String LEVEL_INTERMEDIATE = "Intermediate";
	private static final String LEVEL_EXPERT = "Expert";
	
	/**
	 * default constructor view 
	 */
	public BoardView() {
		this(Constants.LEVEL_INTERMEDIATE);  // should use marcos
	}

	/**
	 * constructor with level
	 * 
	 * @param level 0,1,2 according to Constants 
	 */
	public BoardView(int level) {
		init(level);
	}

	/**
	 * inject controller 
	 * @param controller
	 */
	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Main Panel in the frame
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
	/**
	 * Top Panel in main Panel, including number of mine counter and timer 
	 *
	 * @return
	 */
	JPanel getTopPanel() {
		JPanel topPanel = new JPanel();
		// cardLayout is mainly used to get some padding
		topPanel.setLayout(new CardLayout(15, 15));
 
		JPanel card = new JPanel();
		card.setLayout(new BorderLayout());
		
		JPanel counterPanel = getCounterPanel();
		card.add(counterPanel,  BorderLayout.WEST);
		resetButton = new JButton();
		resetButton.setIcon(ICON_FACES[0]);
		resetButton.setPreferredSize(new Dimension(50, 50));
		resetButton.addActionListener(this);

		JPanel resetPanel = new JPanel(); 
		resetPanel.add(resetButton);
		card.add(resetPanel,BorderLayout.CENTER );
		JPanel timerPanel = getTimerPanel();
		card.add(timerPanel,  BorderLayout.EAST);

		topPanel.add(card);
		return topPanel;
	}
	
	/**
	 * Mine Counter Panel
	 * @return
	 */
	JPanel getCounterPanel() {
		this.numberOfMines = new Counter(this.mines, 999, 0, false);
		return numberOfMines;
	}
	
	/**
	 * Timer Panel 
	 * @return
	 */
	JPanel getTimerPanel() {
		this.timerCounter = new Counter(0, 999, 0, true);
		return timerCounter;
	}

	
	/**
	 * Mine field panel
	 * @return
	 */
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
			int row = gridButton.getRow();
			int col = gridButton.getCol();

			if(e.getButton() == MouseEvent.BUTTON1 && !keys[0]){
				keys[0] = true;
			}
			else if(e.getButton() == MouseEvent.BUTTON3 && !keys[1]){
				keys[1] = true;
			}
			if(keys[0] && keys[1] ){
				System.out.println("Left + Right Pressed:");

		       controller.clickedGrid(row, col, Constants.CLICK_TYPE_BOTH);
				//handleClickResult(clickResult);
				updateView();
			}
		}

	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if( e.getSource() instanceof CellButton ) {
			CellButton gridButton = (CellButton) e.getSource();
			int row = gridButton.getRow();
			int col = gridButton.getCol();

			if(keys[0] && !keys[1] ){
				System.out.println("Left Button release: " + row + "," + col);
				controller.clickedGrid(row, col, Constants.CLICK_TYPE_LEFT);
				
				this.updateView();
				//handleClickResult(clickResult);
				keys[0] = false;
			}
			if(!keys[0]  && keys[1] ){
				System.out.println("Right Button release: " + row + "," + col);
				this.controller.clickedGrid(row, col, Constants.CLICK_TYPE_RIGHT);
				this.updateView();
				keys[1] = false;
			}
			if(keys[0] && keys[1]){
				System.out.println("Right + Left Button release: " + row + "," + col);
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

	
	public int updateCell(int row, int col, int cover, int under) {
		this.cellButtons[row][col].updateIcon(cover, under);
		return 0;
	}

	/**
	 * Initialize the board
	 * @param level: 0,1,2 for 
	 */
	public void init(int level) {
		// TODO Auto-generated method stub
		System.out.println("Level" + level);
		init(level, Constants.LEVEL_PARAMETERS[level][0],
				Constants.LEVEL_PARAMETERS[level][1],
				Constants.LEVEL_PARAMETERS[level][2]);
	}
	/**
	 * Initialize with spec 
	 * 
	 * @param level
	 * @param rows
	 * @param cols
	 * @param mines
	 */
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

		JRadioButtonMenuItem levelBeginner = new JRadioButtonMenuItem (LEVEL_BEGINNER); // for dispaly
		levelBeginner.setName(LEVEL_BEGINNER); // for action ID

		JRadioButtonMenuItem levelInterMediate  = new JRadioButtonMenuItem (LEVEL_INTERMEDIATE);
		levelInterMediate.setName(LEVEL_INTERMEDIATE);
		JRadioButtonMenuItem levelExpert  = new JRadioButtonMenuItem (LEVEL_EXPERT);
		levelExpert.setName(LEVEL_EXPERT); 

		ButtonGroup btnGroup = new ButtonGroup();

		btnGroup.add(levelBeginner);
		btnGroup.add(levelInterMediate);
		btnGroup.add(levelExpert);

		switch (level) {
		case Constants.LEVEL_BEGINNER: 
			levelBeginner.setSelected(true);
			break;
		case Constants.LEVEL_INTERMEDIATE:
			levelInterMediate.setSelected(true);
			break; 
		case Constants.LEVEL_EXPERT:
			levelExpert.setSelected(true);
			break;
		}

		levelBeginner.addActionListener(this);
		levelInterMediate.addActionListener(this);
		levelExpert.addActionListener(this);

		menuLevels.add(levelBeginner);
		menuLevels.add(levelInterMediate);
		menuLevels.add(levelExpert);

		menuBar.add(menuLevels);
		return menuBar;
	}

	/**
	 * Handle menu and reset event 
	 * @Override 
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JRadioButtonMenuItem) {  // leve in the menu
			if ( ((JRadioButtonMenuItem)e.getSource()).getName().indexOf(LEVEL_BEGINNER) != -1) {
				System.out.println(LEVEL_BEGINNER);
				init(Constants.LEVEL_BEGINNER);
				this.controller.configure(this.rows, this.cols, this.mines);
			} else if (((JRadioButtonMenuItem)e.getSource()).getName().indexOf(LEVEL_INTERMEDIATE) != -1) {
				System.out.println(LEVEL_INTERMEDIATE);
				init(Constants.LEVEL_INTERMEDIATE);
				this.controller.configure(this.rows, this.cols, this.mines);
			} else if (((JRadioButtonMenuItem)e.getSource()).getName().indexOf(LEVEL_EXPERT) != -1) {
				System.out.println(LEVEL_EXPERT);
				init(Constants.LEVEL_EXPERT);
				this.controller.configure(this.rows, this.cols, this.mines);
			}
		} else if(e.getSource() instanceof JButton) {
			System.out.println("reset");
			reset();
		}
		updateView();
		
	}

	
	
	/**
	 * reset the board when click the smily face
	 * this is done without condition or popup
	 */
	private void reset() {
		for(int row = 0; row < rows ; row++) {
			for (int col = 0; col < cols; col++) {
				this.cellButtons[row][col].updateIcon(1, 0);
			}
		}
		this.resetButton.setIcon(ICON_FACES[0]);
		this.controller.configure(this.rows, this.cols, this.mines);
		//this.timer = null;
		if(timerCounter != null) {
			timerCounter.reset();
		}
	}

	@Override
	public void setModel(Minefield model) {
		// TODO Auto-generated method stub
		this.model = model;		
	}
	

	/**
	 * update view according to the Module
	 * this is invoked after each click. 
	 * 
	 */
	public void updateView() {
		// update cells 
		for(int row = 0; row < model.getRows() ; row++ ) {
			for(int col = 0; col < model.getColumns(); col++) {
				this.cellButtons[row][col].updateIcon(model.getVisibleValue(row, col), model.getMineValue(row, col));
			}
		} 
		// counter 
		int flags = model.getNumFlags();
		if(flags != this.numberOfMines.getValue())  {
			this.numberOfMines.setValue(flags);
		}
		//  smile and timer
		int gameState = model.getGameState() ;
		if (gameState == Constants.GAME_STATUS_ONGOING && !timerCounter.isTimerStarted()) {
			timerCounter.startTimer();
		} else if (gameState  == Constants.GAME_STATUS_LOSE) { // 3
			this.resetButton.setIcon(ICON_FACES[Constants.GAME_STATUS_LOSE]);
			timerCounter.stopTimer();
		} else if (gameState == Constants.GAME_STATUS_WIN) { // 2
			this.resetButton.setIcon(ICON_FACES[Constants.GAME_STATUS_WIN]);
			timerCounter.stopTimer();
		}
	}
}

/**
 * 
 */
package ms.view;

import ms.Constants;
import ms.controller.Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
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

	private final int CELL_SIZE = 25;

	public static ImageIcon [] ICON_FACES = { 
			new ImageIcon("images/facesmile.png"), // 0
			new ImageIcon("images/facesmile.png"), // 1
			new ImageIcon("images/facewin.png"),  // 2
			new ImageIcon("images/facelose.png")   // 3
	};

	// temp;
	int faceIndex = 0; 

	private JFrame mainFrame; // main window
	private Counter numberOfMines; 
	private Counter timerCounter; 
	Controller controller;

	private Timer timer;
	private int seconds ; 
	
	int rows, cols, mines;
	CellButton [][] cellButtons;
	JButton resetButton ; 

	/**
	 * default view 
	 */
	public BoardView() {
		this(Constants.LEVEL_INTERMEDIATE);  // should use marcos
	}

	/**
	 * 
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
		//topPanel.setLayout(new FlowLayout());
		topPanel.setLayout(new BorderLayout());
		topPanel.add(getCounterPanel(),  BorderLayout.WEST);

		resetButton = new JButton();
		resetButton.setIcon(ICON_FACES[0]);
		resetButton.setPreferredSize(new Dimension(50, 50));
		resetButton.addActionListener(this);

		JPanel resetPanel = new JPanel(); 
		resetPanel.add(resetButton);
		topPanel.add(resetPanel,BorderLayout.CENTER );
		topPanel.add(getCounterPanel(),  BorderLayout.EAST);

		return topPanel;
	}

	JPanel getCounterPanel() {
		JPanel countPanel = new JPanel();
		countPanel.setBackground(Color.BLUE);
		countPanel.setPreferredSize(new Dimension(50, 50));
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

				int clickResult = this.controller.clickedGrid(row, col, Constants.CLICK_TYPE_BOTH);
				handleClickResult(clickResult);
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
				int clickResult = controller.clickedGrid(row, col, Constants.CLICK_TYPE_LEFT);
				handleClickResult(clickResult);
				keys[0] = false;
			}
			if(!keys[0]  && keys[1] ){
				System.out.println("Right Button release: " + row + "," + col);
				this.controller.clickedGrid(row, col, Constants.CLICK_TYPE_RIGHT);
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

	@Override
	public int updateCell(int row, int col, int cover, int under) {
		this.cellButtons[row][col].updateIcon(cover, under);
		return 0;
	}

	@Override
	public void init(int level) {
		// TODO Auto-generated method stub
		System.out.println("Level" + level);
		init(level, Constants.LEVEL_PARAMETERS[level][0],
				Constants.LEVEL_PARAMETERS[level][1],
				Constants.LEVEL_PARAMETERS[level][2]);


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

		JRadioButtonMenuItem levelBeginner = new JRadioButtonMenuItem ("Beginner"); // for dispaly
		levelBeginner.setName("Beginner"); // for action ID

		JRadioButtonMenuItem levelInterMediate  = new JRadioButtonMenuItem ("InterMediate");
		levelInterMediate.setName("InterMediate");
		JRadioButtonMenuItem levelExpert  = new JRadioButtonMenuItem ("Expert");
		levelExpert.setName("Expert"); 

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


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JRadioButtonMenuItem) {  // leve in the menu
			if ( ((JRadioButtonMenuItem)e.getSource()).getName().indexOf("Beginner") != -1) {
				System.out.println("Beginner");
				init(Constants.LEVEL_BEGINNER);
				this.controller.configure(this.rows, this.cols, this.mines);
			} else if (((JRadioButtonMenuItem)e.getSource()).getName().indexOf("InterMediate") != -1) {
				System.out.println("InterMediate");
				init(Constants.LEVEL_INTERMEDIATE);
				this.controller.configure(this.rows, this.cols, this.mines);
			} else if (((JRadioButtonMenuItem)e.getSource()).getName().indexOf("Expert") != -1) {
				System.out.println("Expert");
				init(Constants.LEVEL_EXPERT);
				this.controller.configure(this.rows, this.cols, this.mines);
			}
		} else if(e.getSource() instanceof JButton) {
			System.out.println("reset");
			reset();

		}
	}

	@Override
	public void updateCounter(int numberOfMines) {
		// TODO Auto-generated method stub

	}
	private void handleClickResult(int result) {
		if(result == Constants.GAME_STATUS_ONGOING) {
			if(this.timer == null) {
				timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask(){
					public void run() {
						System.out.println("Timer:"+ seconds++);
					}
				},1000,1000);
			}
		}else if(result == Constants.GAME_STATUS_LOSE) {  // 3
			this.resetButton.setIcon(ICON_FACES[Constants.GAME_STATUS_LOSE]);
			if(timer != null)
				timer.cancel();
		} else if(result == Constants.GAME_STATUS_WIN) { // 2
			this.resetButton.setIcon(ICON_FACES[Constants.GAME_STATUS_WIN]);
			if(timer != null)
				timer.cancel();
		}
	}

	/**
	 * 
	 */
	private void reset() {
		for(int row = 0; row < rows ; row++) {
			for (int col = 0; col < cols; col++) {
				this.cellButtons[row][col].updateIcon(1, 0);
			}
		}
		this.resetButton.setIcon(ICON_FACES[0]);
		this.controller.configure(this.rows, this.cols, this.mines);
		this.timer = null;
	}
}
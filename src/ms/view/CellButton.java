package ms.view;

import ms.Constants;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * This class is Button
 * representing a cell on borad.
 * @author lwang
 *
 */
public class CellButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int col;   // col location  
	private int row;   // row localtion
	private int iconIndex; 
	
	public static ImageIcon [] ICONS_COVERED = {
			new ImageIcon("images/blank.png"),  //
			new ImageIcon("images/blank.png"),  // 1 
			new ImageIcon("images/flag.png"),   // 2
			new ImageIcon("images/unknown.png"), // 3
	};

	public static ImageIcon[] ICONS_MINES = {
			new ImageIcon("images/mine.png"),
			new ImageIcon("images/mine_click.png")
	};

	public static ImageIcon[] ICONS_REVEALED = {		
			new ImageIcon("images/m0.png"),
			new ImageIcon("images/m1.png"),
			new ImageIcon("images/m2.png"),
			new ImageIcon("images/m3.png"),
			new ImageIcon("images/m4.png"),
			new ImageIcon("images/m5.png"),
			new ImageIcon("images/m6.png"),
			new ImageIcon("images/m7.png"),
			new ImageIcon("images/m8.png"),
	};

	public CellButton(int row, int col ) {
		this (row, col, ICONS_COVERED[0]);
	}
	
	public CellButton(int row, int col, ImageIcon icon) {
		super(icon);
		this.row = row;
		this.col = col;
		this.setIcon(icon);
	}

	public int getCol() {
		return this.col;
	}

	public int getRow() {
		return this.row;
	}
	
	
	/**
	 * 
	 * @param cover
	 * @param under
	 */
	public void updateIcon(int cover, int under) {
		if(cover > 0 ) {
			setIcon(ICONS_COVERED[cover]);
		} else if (under > 0){
			setIcon(ICONS_REVEALED[under]);
		} else if (under == Constants.SHOW_MINE) {
			setIcon(ICONS_MINES[0]);
		} else if(under ==  Constants.SHOW_MINE_FIRED) {
			setIcon(ICONS_MINES[1]);
		}
	}
	
	/**
	 * just for testing, 
	 * rotate all possible images
	 */
	public void changeIcon() {
		iconIndex++; 
		if(iconIndex >= ICONS_REVEALED.length) 
			iconIndex = 0; 
		setIcon(ICONS_REVEALED[iconIndex]);
	}
}
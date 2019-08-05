package ms.view;

import java.awt.BorderLayout;
/**
 * This is a reusable UI component as a counter 
 * showing the number of mines and time.
 *
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * This is a reusable UI component as a counter 
 * showing the number of mines and time.
 * @author harika
 *
 */
public class Counter extends JPanel {
	private static final long serialVersionUID = 1L;

	private int value;
	private int initValue;
	private int upper;
	private int lower;
	private String format;
	private JLabel label;
	private Timer timer;
	private boolean timerStarted;

	private ActionListener listener;
	private static Font font;

	/**
	 * constructor
	 * @param initValue  initial value
	 * @param upper      upper bound
	 * @param lower      lower bound
	 * @param isTimer    whether this is a Timer
	 */
	public Counter(int initValue, int upper, int lower, boolean isTimer) {
		this.initValue = initValue;
		this.upper = upper;
		this.lower = lower;
		
		format = "%0" + String.valueOf(upper).length() + "d";
		if(font == null)
			loadFont();
		label = new JLabel(String.format(format, initValue));
		label.setForeground(Color.red);
		label.setFont(font);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		this.setPreferredSize(new Dimension(90, 40));
		this.setBackground(Color.black);
		this.add(label);
		
		if(isTimer) {
			createActionListener(999);
		}
	}

	/**
	 * Set the value
	 * @param value  vale to be set
	 */
	public void setValue(int value) {
		if (value <= upper && value >= lower) {
			setLabelText(value);
			this.value = value;
		}
	}

	/**
	 * Get the value
	 * @return   current value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Increase the counter
	 */
	public void increase() {
		if (value < upper) {
			value++;
			setLabelText(value);
		}
	}

	/**
	 * Decrease the counter
	 */
	public void decrease() {
		if (value > lower) {
			value--;
			setLabelText(value);
		}
	}

	/**
	 * Checks if the timer has been started and returns true or false
	 * @return    whether the timer is started
	 */
	public boolean isTimerStarted() {
		return timerStarted;
	}

	/**
	 * Resets the counter with initValue
	 */
	public void reset() {
		if (timer != null) {
			timer.removeActionListener(listener);
			int delay = 999;
			createActionListener(delay);
		}
		timerStarted = false;
		setValue(initValue);
		setLabelText(initValue);
	}

	/**
	 * Create action listener for timer
	 * @param delay   the delay timer
	 */
	public void createActionListener(int delay) {
		listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				increase();
			}
		};
		
		timer = new Timer(delay, listener);
		timer.setInitialDelay(0);
	}
	/**
	 * Start the timer
	 */
	public void startTimer() {
		timer.start();
		timerStarted = true;
	}

	/**
	 * Stop the timer
	 */
	public void stopTimer() {
		timer.stop();
		timerStarted = false;
	}

	/**
	 * Set label text
	 * @param val  value to be set
	 */
	private void setLabelText(int val) {
		label.setText(String.format(format, val));
	}

	/**
	 * Load font
	 */
	public static void loadFont() {
		// Load font from file
		//File file = new File(ClassLoader.getResourceAsStream("./fonts/digital-7 (mono).ttf"));  
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemResourceAsStream("fonts/digital-7 (mono).ttf"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		font = font.deriveFont(Font.BOLD, 60);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(font);
	}

	public static void main(String[] argv) {
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.add(new Counter(10, 999, 0, false), BorderLayout.WEST);
		frame.add(new JPanel(), BorderLayout.CENTER );
		frame.add(new Counter(0, 999, 0, true), BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
		
	}
}

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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Counter extends JPanel {
	private static final long serialVersionUID = 1L;

	private int value;
	private int initValue;
	private int upper;
	private int lower;
	private String format;
	private JLabel label;
	private Timer timer;

	private ActionListener listener;
	private static Font font;

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
		this.setPreferredSize(new Dimension(90, 40));
		this.setBackground(Color.black);
		this.add(label);
		
		if(isTimer) {
			createActionListener(999);
		}
	}

	public void setValue(int value) {
		if (value <= upper && value >= lower) {
			setLabelText(value);
			this.value = value;
		}
	}

	public int getValue() {
		return value;
	}

	public void increase() {
		if (value < upper) {
			value++;
			setLabelText(value);
		}
	}

	public void decrease() {
		if (value > lower) {
			value--;
			setLabelText(value);
		}
	}

	public void reset() {
		if(timer != null) {
			timer.removeActionListener(listener);
			int delay = 999;
			createActionListener(delay);
		}
		setValue(initValue);
		setLabelText(initValue);
	}

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
	public void startTimer() {
		timer.start();
	}

	public void stopTimer() {
		timer.stop();
	}

	private void setLabelText(int val) {
		label.setText(String.format(format, val));
	}

	public static void loadFont() {
		// Load font from file
		File file = new File("./fonts/DJB Get Digital.ttf");  
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		font = font.deriveFont(Font.BOLD, 50);
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

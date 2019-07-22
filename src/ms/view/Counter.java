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

import javax.sound.sampled.LineUnavailableException;
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
	private JFrame frame;
	private JPanel panel;
	private JLabel label;
	private Timer timer;

	private ActionListener listener;
	private static Font font;

	public Counter(int initValue, int upper, int lower) {
		this.initValue = initValue;
		this.upper = upper;
		this.lower = lower;
	}

	public void setValue(int value) {
		this.value = value;
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

	public JPanel getPanel() {
		format = "%0" + String.valueOf(upper).length() + "d";
		//frame = new JFrame();
		label = new JLabel(String.format(format, initValue));
		label.setForeground(Color.red);
		label.setFont(font);

		panel = new JPanel();
		panel.setBackground(Color.black);
		panel.add(label);
		/*frame.add(panel);
		// set the size of frame
		frame.setSize(50, 50);
		frame.pack();
		frame.setVisible(true);*/
		setValue(initValue);
		return panel;

	}

	public static void loadFont() {
		// Load font from file
		File file = new File("./fonts/DJB Get Digital.ttf");  
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		font = font.deriveFont(Font.BOLD, 38);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(font);
	}
	
}



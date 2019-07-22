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
	private JLabel label;
	private Timer timer;
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
		setLabelText(initValue);
	}

	public void createTimerAction(int delay) {
		
		timer = new Timer(delay, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				increase();
			}
		});
		timer.setInitialDelay(0);
		timer.start();
	}

	private void setLabelText(int val) {
		label.setText(String.format(format, val));
	}

	public void draw() {
		format = "%0" + String.valueOf(upper).length() + "d";
		frame = new JFrame();
		label = new JLabel(String.format(format, initValue));
		label.setForeground(Color.red);
		label.setFont(font);

		JPanel panel = new JPanel();
		panel.setBackground(Color.black);
		panel.add(label);
		frame.add(panel);
		// set the size of frame
		frame.setSize(50, 50);
		frame.pack();
		frame.setVisible(true);
		setValue(initValue);

	}

	private static void loadFont() {
		// Load font from file
		File file = new File("./fonts/DJB Get Digital.ttf");  
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		font = font.deriveFont(Font.BOLD, 30);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(font);
	}
/*
	public static void main(String[] args) {
		loadFont();
		Counter counter = new Counter(15, 15, 0);
		counter.draw();

		Counter timer = new Counter(0, 999, 0);
		timer.draw();
		timer.createTimerAction(999);
	}
*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.add(new Counter2(10), BorderLayout.EAST);
		frame.add(new JPanel(), BorderLayout.CENTER);
		frame.add(new Counter2(20), BorderLayout.WEST);
		frame.pack();
		frame.setVisible(true);
	}
	
}

class Counter2 extends JPanel {
	int value; 
	public Counter2 (int initValue) {
		this.value = initValue;
		setPreferredSize(new Dimension(100,  50));
		setBackground(Color.BLACK);
		JLabel label = new JLabel(String.valueOf(value));
		label.setFont(new Font(Font.DIALOG, Font.BOLD, 20 ));
		label.setForeground(Color.RED);
		add(label);
	}
}

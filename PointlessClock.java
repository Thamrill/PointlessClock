package pointlessClock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class PointlessClock extends JDialog {
	private static final long serialVersionUID = 1L;
	JLabel timeDisplay;
	JLabel dateDisplay;

	public static String getTime(boolean date) {
		double time = System.currentTimeMillis() / 1000.0 / 3600 / 24;
		if (date) {
			time = Math.floor(time);
			return String.format("%d", (int)time).replaceFirst(",", ".");
		}else{
			time = time - Math.floor(time);
			return String.format("%.6f", time).replaceFirst(",", ".");
		}
		
	}

	public PointlessClock() {
		timeDisplay = new JLabel(getTime(false));
		dateDisplay=new JLabel(getTime(true));
		timeDisplay.setFont(timeDisplay.getFont().deriveFont(Font.BOLD, 60));
		dateDisplay.setFont(dateDisplay.getFont().deriveFont(Font.ITALIC));
		timeDisplay.setHorizontalAlignment(JLabel.CENTER);
		dateDisplay.setHorizontalAlignment(JLabel.CENTER);
		timeDisplay.setForeground(new Color(0f, 0f, 0f, 0.9f));
		dateDisplay.setForeground(new Color(0f, 0f, 0f, 0.9f));
		timeDisplay.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		dateDisplay.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		new Thread(new Updater() {
			@Override
			public void update() {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						timeDisplay.setText(getTime(false));
						dateDisplay.setText(getTime(true));
					}
				});

			}
		}).start();

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && isUndecorated()) {
					dispose();
					setBackground(Color.LIGHT_GRAY);
					setUndecorated(false);
					pack();
					setVisible(true);
				}

			}
		});
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getExtendedKeyCode() == KeyEvent.VK_ESCAPE) {
					dispose();
					setUndecorated(true);
					setBackground(new Color(1f, 1f, 1f, 0.5f));
					pack();
					setVisible(true);
				}else if (e.getExtendedKeyCode() == KeyEvent.VK_T) {
					setAlwaysOnTop(!isAlwaysOnTop());
					if(isAlwaysOnTop()) {
						getRootPane().setBorder(BorderFactory.createLineBorder(timeDisplay.getForeground()));
					}else {
						getRootPane().setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
					}
				}
				
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		add(timeDisplay, BorderLayout.CENTER);
		add(dateDisplay, BorderLayout.PAGE_END);
		setAlwaysOnTop(true);
		getRootPane().setBorder(BorderFactory.createLineBorder(timeDisplay.getForeground()));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public abstract static class Updater implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep((long) (Math.random() * 1000 + 500));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				update();
			}
		}

		public abstract void update();

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new PointlessClock();
			}
		});
	}
}

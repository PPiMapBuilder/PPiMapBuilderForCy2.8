package ppimapbuilder.gui;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;

public abstract class LoadingWindow implements Runnable{
	
	private JDialog loadingwindow;
	private JLabel text;
	
	private String message;
	
	/**
	 * Create a new loading window with a displayed message
	 * @param message
	 */
	public LoadingWindow(String message) {
		this.message = message;
		run();
	}
	
	/**
	 * Creates the JDialog with spinner 
	 * @return the dialog window
	 */
	private void initialize() {
		loadingwindow = new JDialog();
		loadingwindow.setUndecorated(true);
		
		// Boder adding a 5px margin
		EmptyBorder margin = new EmptyBorder(5, 5, 5, 5);
		
		text = new JLabel(message);
		text.setBorder(margin);
		
//		JLabel spinner =  new JLabel();
//		spinner.setBorder(margin);
//		
//		ImageIcon gif = null;
//		try {
//			gif = new ImageIcon(getClass().getResource("/spinner.gif"));
//		} catch (Exception e) {}
//		
//		spinner.setMinimumSize(new Dimension(gif.getIconWidth(), gif.getIconHeight()));
//		spinner.setPreferredSize(new Dimension(gif.getIconWidth(), gif.getIconHeight()));
//		spinner.setMaximumSize(new Dimension(gif.getIconWidth(), gif.getIconHeight()));
//		spinner.setIcon(gif);
//		
//		loadingwindow.getContentPane().add(spinner, BorderLayout.CENTER);
		
		loadingwindow.getContentPane().add(text, BorderLayout.CENTER);
		
		loadingwindow.pack();
		loadingwindow.setLocationRelativeTo(null);
	}
	

	/**
	 * When launch the loading window appear, run the treatment and close
	 */
	@Override
	public void run() {
		initialize();

		loadingwindow.setVisible(true);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				process();
				loadingwindow.setVisible(false);
			}
		});
	}
	
	public abstract void process();
}

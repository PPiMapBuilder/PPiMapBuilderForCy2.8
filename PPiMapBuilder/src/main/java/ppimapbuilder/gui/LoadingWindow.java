package ppimapbuilder.gui;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

public abstract class LoadingWindow implements Runnable{
	
	private JDialog loadingwindow;
	private JLabel text;
	
	private String message;
	
	private JProgressBar progressbar;
	
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
		
		
		JPanel center_panel = new JPanel(); // New panel
		
		
		text = new JLabel(message);
		text.setBorder(margin);
		
		
		this.progressbar = new JProgressBar(); // New progress bar
		progressbar.setIndeterminate(true); // Infinite progress bar
		progressbar.setStringPainted(true); // Progress bar with text ?
		progressbar.setString("Work in progress"); // Text ?
		
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
		
		center_panel.add(text); // Add the text to the panel
		center_panel.add(progressbar); // Add the progress bar to the panel
		
		
		loadingwindow.getContentPane().add(center_panel, BorderLayout.CENTER); // Add the panel to the JDialog
		
		loadingwindow.pack();
		loadingwindow.setLocationRelativeTo(null);
		loadingwindow.toFront();
	}
	

	/**
	 * When launch the loading window appear, run the treatment and close
	 */
	@Override
	public void run() {
		initialize();

		loadingwindow.setVisible(true);
		
		/*SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				process();
				//loadingwindow.setVisible(false);
			}
		});*/
		
		new Thread(new Runnable() { // Create a new thread for the treatment to keep the progress bar running.
			@Override
			public void run(){
				process();
				loadingwindow.setVisible(false);
			}
	    }).start();
	}
	
	public abstract void process();
}

package ppimapbuilder.gui;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import cytoscape.Cytoscape;

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
		loadingwindow.setAlwaysOnTop(true);
		
		JPanel center_panel = new JPanel(); // New panel
		center_panel.setLayout(new BorderLayout(0, 0));
		center_panel.setBorder(new EmptyBorder(10,10,10,10)); // Boder adding a 10px margin
		
		text = new JLabel(message);
		text.setBorder(new EmptyBorder(5,5,5,5));
		
		this.progressbar = new JProgressBar(); // New progress bar
		progressbar.setIndeterminate(true); // Infinite progress bar
		
		center_panel.add(text, BorderLayout.CENTER); // Add the text to the panel
		center_panel.add(progressbar, BorderLayout.SOUTH); // Add the progress bar to the panel
		
		loadingwindow.getContentPane().add(center_panel, BorderLayout.CENTER); // Add the panel to the JDialog
		
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
		
		loadingwindow.pack();
		loadingwindow.setLocationRelativeTo(Cytoscape.getDesktop());
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

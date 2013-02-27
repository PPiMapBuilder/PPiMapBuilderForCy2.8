package PPiMapBuilder;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PPiMapBuilderCredits extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel copyright;
	
	public PPiMapBuilderCredits() {
		
		copyright = new JLabel("Copyright 2013");
		this.getContentPane().add(copyright);
		
		this.setTitle("About PPiMapBuilder");
		this.setPreferredSize(new Dimension(400,250));
		this.setMinimumSize(new Dimension(400,250));
		this.setMaximumSize(new Dimension(400,250));
		this.setResizable(false);

		this.setLocationRelativeTo(null);
	}
}

package ppimapbuilder.gui.label;

import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import javax.swing.JLabel;

public class JLinkLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	public JLinkLabel(String text, final String url) {
		super(text);
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (Desktop.isDesktopSupported()) {
        			Desktop desktop = Desktop.getDesktop();
        			try {
        				desktop.browse(new URI(url));
        			} catch (Exception ex) {}
        		}
			}
		});
	}
}

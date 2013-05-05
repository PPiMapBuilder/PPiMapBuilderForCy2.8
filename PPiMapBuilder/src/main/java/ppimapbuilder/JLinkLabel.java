package ppimapbuilder;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import javax.swing.JLabel;

public class JLinkLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	private String url;

	public JLinkLabel(String text, final String webUrl) {
		super(text);
		this.url = webUrl;

		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {setCursor(new Cursor(Cursor.DEFAULT_CURSOR));}
			@Override
			public void mouseEntered(MouseEvent arg0) {setCursor(new Cursor(Cursor.HAND_CURSOR));}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					if (Desktop.isDesktopSupported()) {
						Desktop.getDesktop().browse(new URI(url));
					} else {
						String OS = System.getProperty("os.name").toLowerCase();

						if(OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 )
							Runtime.getRuntime().exec("x-www-browser " + url);
						else if (OS.indexOf("win") >= 0)
							Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
						if(OS.indexOf("mac") >= 0)
							Runtime.getRuntime().exec("open " + url);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	public JLinkLabel() {
		this("", "");
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

package ppimapbuilder.gui.label;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * PPiMapBuilder logo icon label
 */
public class LogoIcon extends JLinkLabel{
	private static final long serialVersionUID = 1L;

	public LogoIcon() {
		super("", "http://github.com/PPiMapBuilder/");
		try {
			setIcon(new ImageIcon(LogoIcon.class.getResource("/img/logo.png")));
		} catch (Exception e) {
			setText("[LOGO]");
		}
	}
}

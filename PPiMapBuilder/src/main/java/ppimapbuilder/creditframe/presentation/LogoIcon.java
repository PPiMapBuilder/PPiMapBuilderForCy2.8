package ppimapbuilder.creditframe.presentation;

import javax.swing.ImageIcon;

import ppimapbuilder.JLinkLabel;

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

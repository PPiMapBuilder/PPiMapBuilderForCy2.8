package ppimapbuilder.panel.presentation;

import javax.swing.Icon;
import javax.swing.JLabel;

public class JTextLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	public JTextLabel() {
		super();
	}
	
	public JTextLabel(String arg0) {
		super(wrapText(arg0));
	}

	public JTextLabel(String arg0, int arg1) {
		super(wrapText(arg0), arg1);
	}

	public JTextLabel(String arg0, Icon arg1, int arg2) {
		super(wrapText(arg0), arg1, arg2);
	}

	@Override
	public void setText(String arg0) {
		super.setText(arg0);
	}
	
	private static String wrapText(String in) {
		return	"<html><p style=\"width:100%;padding:2px;background-color:white\">"+in+"</p></html>";
	}
}

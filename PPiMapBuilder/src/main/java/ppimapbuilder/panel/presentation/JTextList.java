package ppimapbuilder.panel.presentation;

import java.util.ArrayList;
import javax.swing.JTextPane;

/**
 * A swing component used to display a list of String as a html list
 */
public class JTextList extends JTextPane {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a JTextList with an ArrayList of String 
	 * @param list
	 */
	public JTextList(ArrayList<String> list) {
		String content = "";
		
		for(String text : list)
			content += "<div style=\"margin-left:10px;width:100%\">• "+ Character.toUpperCase(text.charAt(0)) + text.substring(1)+"</div>";
			
		setContentType("text/html");
		setText("<html>"+content+"</html>");
		setEditable(false);
		setBackground(null);
		setBorder(null);
	}
}

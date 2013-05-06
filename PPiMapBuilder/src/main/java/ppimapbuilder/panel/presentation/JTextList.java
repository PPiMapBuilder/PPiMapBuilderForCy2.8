package ppimapbuilder.panel.presentation;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;

/**
 * A swing component used to display a list of String as a html list
 */
public class JTextList extends JTextPane {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public JTextList() {
		super();
	}
	
	/**
	 * Constructs a JTextList with an ArrayList of String 
	 * @param list
	 */
	public JTextList(String[] list, String[] url) {
		setText(genHtml(list, url));
		setContentType("text/html");
		setEditable(false);
		setBackground(null);
		setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
	}
	
	/**
	 * 
	 * @param list
	 */
	public void setText(String[] list) {
		this.setText(list, null);
	}
	
	/**
	 * 
	 * @param list
	 */
	public void setText(String[] list, String[] url) {
		setContentType("text/html");
		setEditable(false);
		setBackground(null);
		setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		setText(genHtml(list, url));
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	private static String genHtml(String[] list, String[] url) {
		String content = "";
		
		for(int i = 0; i < list.length; i++) {
			content += "<div style=\"margin-left:10px;width:100%\">• ";
			if(url != null) content += "<a href=\""+url[i]+"\">";
			content += Character.toUpperCase(list[i].charAt(0)) + list[i].substring(1);
			if(url != null) content += "</a>";
			content += "</div>";
		}
		
		return "<html>"+content+"</html>";
	}
}

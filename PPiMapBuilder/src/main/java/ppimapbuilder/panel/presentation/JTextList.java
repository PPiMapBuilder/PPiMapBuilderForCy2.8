package ppimapbuilder.panel.presentation;

import java.awt.Desktop;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * A swing component used to display a list of String as a html list
 */
public class JTextList extends JTextPane {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public JTextList() {
		this(null, null);
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
		addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent event) {
				if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		            try {
		            	String url = event.getURL().toString();
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
		            } catch (final Exception e) {
		            	e.printStackTrace();
		            }
		        }
			}
		});
	}
	
	@Override
	public void setText(String arg0) {
		super.setText(arg0);
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
		
		if(list != null) {
			for(int i = 0; i < list.length; i++) {
				content += "<div style=\"margin-left:10px;width:100%\">• ";
				if(url != null) content += "<a href=\""+url[i]+"\">";
				content += Character.toUpperCase(list[i].charAt(0)) + list[i].substring(1);
				if(url != null) content += "</a>";
				content += "</div>";
			}
		}
		
		
		return "<html>"+content+"</html>";
	}
}

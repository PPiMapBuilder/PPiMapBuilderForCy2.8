package PPiMapBuilder;


import javax.swing.*;

public class PPiMapBuilderMenu extends JMenu{

	private static final long serialVersionUID = 1L;
	private static PPiMapBuilderMenu _instance = null;
	private JMenuItem menuItem_create_network, menuItem_credits; // Menu item to create a new network
	
	private PPiMapBuilderMenu() {
		super("PPiMapBuilder"); // Change the name of the plugin menu

		menuItem_create_network = new JMenuItem("Create a new network");
		menuItem_create_network.addActionListener(new PPiMapBuilderCreateNetworkAction());
		
		menuItem_credits = new JMenuItem("About PPiMapBuilder");
		menuItem_credits.addActionListener(new PPiMapBuilderCreditsAction());
		
		this.add(menuItem_create_network);
		this.add(menuItem_credits);
	}
	
	public static PPiMapBuilderMenu Instance() {
		if (_instance == null)
			_instance = new PPiMapBuilderMenu();
		return _instance;
	}

}

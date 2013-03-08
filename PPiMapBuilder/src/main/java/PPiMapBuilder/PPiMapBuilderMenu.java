package PPiMapBuilder;

import javax.swing.*;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class PPiMapBuilderMenu extends JMenu{

	private static final long serialVersionUID = 1L;
	
	private static PPiMapBuilderMenu _instance = null; // Instance of the PPiMapBuilder menu to prevent several instances 
	
	private JMenuItem menuItem_create_network, menuItem_credits; // Menu item to create a new network and to display the credits
	private PPiMapBuilderCreateNetworkAction myCreateNetworkAction; // Action to create a network
	private PPiMapBuilderCreditsAction myCreditsAction; // Action to display the credit
	
	/**
	 * Default constructor which is private to prevent several instances
	 * Create the main menu and add the differents menu item
	 */
	private PPiMapBuilderMenu() {
		super("PPiMapBuilder"); // Creates the PPiMapBuilder menu as a JMenu and change the name of the plugin menu

		menuItem_create_network = new JMenuItem("Create a new network"); // Create the menu item corresponding to the create network action
		myCreateNetworkAction = new PPiMapBuilderCreateNetworkAction(); // Create the action
		menuItem_create_network.addActionListener(myCreateNetworkAction); // Link this action to the menu item
		
		menuItem_credits = new JMenuItem("About PPiMapBuilder"); // Create the menu item corresponding to the credits display
		myCreditsAction = new PPiMapBuilderCreditsAction(); // Create the action
		menuItem_credits.addActionListener(myCreditsAction); // Link this action to the menu item
		
		this.add(menuItem_create_network); // Add the menu item to the main menu
		this.add(menuItem_credits);
	}
	
	/**
	 * Method to access the unique instance of PPiMapBuilderMenu
	 * @return _instance
	 */
	public static PPiMapBuilderMenu Instance() {
		if (_instance == null)
			_instance = new PPiMapBuilderMenu();
		return _instance;
	}

}

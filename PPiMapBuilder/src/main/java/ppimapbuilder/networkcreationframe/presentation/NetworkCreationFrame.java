package ppimapbuilder.networkcreationframe.presentation;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import java.awt.Color;
import javax.swing.border.MatteBorder;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.BoxLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/** Network creation frame */
public class NetworkCreationFrame {
	/** Instance of the PPiMapBuilder frame to prevent several instances */
	private static NetworkCreationFrame _instance = null; 
	
	// The create network window
	private JFrame window;

	// Hash map of databases and organisms loaded from the database
	private LinkedHashMap<Integer, JCheckBox> organisms;
	private LinkedHashMap<String, JCheckBox> databases;

	// Uniprot identifiers text area and reference organism combobox
	private JTextArea txaIdentifiers;
	private JComboBox cbReferenceOrganism;

	// Databases and organism panels containing all checkbox
	private JPanel panSourceDatabases;
	private JPanel panOtherOrganims;

	// Fancy design element
	private Color darkForeground;
	private CompoundBorder panelBorder;
	private CompoundBorder fancyBorder;

	/** Create the network creation frame */
	private NetworkCreationFrame() {
		window = new JFrame("PPiMapBuilder - Create a network");

		// Create all component in the window
		initialize();
	}

	/**
	 * Method to access the unique instance of NetworkCreationFrame
	 * @return _instance
	 */
	public static NetworkCreationFrame Instance() {
		if (_instance == null)
			_instance = new NetworkCreationFrame();
		return _instance;
	}

	/** Initialize the contents of the frame */
	private void initialize() {
		// Slightly darker color than window background color
		darkForeground = UIManager.getColor("Panel.background");
		float hsbVals[] = Color.RGBtoHSB(darkForeground.getRed(), darkForeground.getGreen(), darkForeground.getBlue(), null);
		darkForeground = Color.getHSBColor(hsbVals[0], hsbVals[1], 0.9f * hsbVals[2]);
		
		// Simple border around panel and text area
		fancyBorder = new CompoundBorder(
			// Outside border 1px bottom light color
			new MatteBorder(0, 0, 1, 0, (Color) new Color(255, 255, 255)),
			// Border all around panel 1px dark grey 
			new LineBorder(new Color(154, 154, 154), 1)
		);
		// Border for left and right panel
		panelBorder = new CompoundBorder(
			// Dark margin around panel
			new MatteBorder(5, 0, 0, 0, (Color) darkForeground), 	
			new CompoundBorder(
				fancyBorder,
				new EmptyBorder(5, 5, 5, 5)
		));
		
		// Split panel
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(5);
		splitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		splitPane.setContinuousLayout(true);
		window.getContentPane().add(splitPane, BorderLayout.CENTER);

		// $hide>>$
		// Redraw the split panel divider
		try {
			splitPane.setUI(new BasicSplitPaneUI() {
				public BasicSplitPaneDivider createDefaultDivider() {
					return new BasicSplitPaneDivider(this) {
						private static final long serialVersionUID = 1L;
						
						@Override
						public void paint(Graphics g) {
							super.paint(g);
							g.setColor(darkForeground);
							g.fillRect(0, 0, getSize().width, getSize().height);

							Graphics2D g2d = (Graphics2D) g;
							int h = 12; int w = 2;
							int x = (getWidth() - w) / 2;
							int y = (getHeight() - h) / 2;
							g2d.setColor(new Color(154, 154, 154));
							g2d.drawOval(x, y, w, h);
						}
					};
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		// $hide<<$

		// Left part
		JPanel panIndentifiers = initIndentifiersPanel();
		splitPane.setLeftComponent(panIndentifiers);

		// Right part
		JPanel panMainForm = initMainFormPanel();
		splitPane.setRightComponent(panMainForm);

		// Bottom part
		JPanel panBottomForm = initBottomPanel();
		window.getContentPane().add(panBottomForm, BorderLayout.SOUTH);
		
		// Resize window
		window.setMinimumSize(new Dimension(500, 300));
		window.setSize(new Dimension(550, 500));
		
		// Center window
		window.setLocationRelativeTo(null);
	}

	/**
	 * Creating indentifiers panel with label and text area used to fill the
	 * uniprot identifiers
	 * @return the generated JPanel
	 */
	private JPanel initIndentifiersPanel() {
		// Uniprot identifiers left panel
		JPanel panIndentifiers = new JPanel();
		panIndentifiers.setBorder(new CompoundBorder(new MatteBorder(0, 5, 0, 0, (Color) darkForeground), panelBorder));
		panIndentifiers.setLayout(new MigLayout("inset 10", "[129px,grow][14px:14px:14px,right]", "[20px][366px,grow][35px]"));

		// Label "Uniprot identifiers"
		JLabel lblIdentifiers = new JLabel("Uniprot Identifiers\n");
		panIndentifiers.add(lblIdentifiers, "flowx,cell 0 0,alignx left,aligny top");

		// Uniprot identifiers Help Icon
		JLabel lblHelpUniprotIdentifiers = new HelpIcon("Please enter Uniprot identifiers (one per line)");
		lblHelpUniprotIdentifiers.setHorizontalAlignment(SwingConstants.RIGHT);
		panIndentifiers.add(lblHelpUniprotIdentifiers, "cell 1 0");

		// Text area uniprot identifiers
		txaIdentifiers = new JTextArea(); 
		txaIdentifiers.setFont(new Font("Monospaced", Font.PLAIN, 13));
		txaIdentifiers.setBorder(new EmptyBorder(5, 5, 5, 5));

		// Scroll pane around the text area
		JScrollPane scrollPane = new JScrollPane(txaIdentifiers);
		scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPane.setBorder(fancyBorder);
		panIndentifiers.add(scrollPane, "cell 0 1 2 1,grow");

		// Cancel button
		JButton button = new JButton("Clear");
		button.setMnemonic(KeyEvent.VK_CLEAR);
		button.setAlignmentX(1.0f);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txaIdentifiers.setText("");
			}
		});
		panIndentifiers.add(button, "cell 0 2 2 1,alignx right,aligny center");

		return panIndentifiers;
	}

	/**
	 * Creating the main form panel containing the organism selector and the
	 * source database selector
	 * @return the generated JPanel
	 */
	private JPanel initMainFormPanel() {
		// Main form panel
		JPanel panMainForm = new JPanel();
		panMainForm.setMinimumSize(new Dimension(290, 10));
		panMainForm.setBorder(new CompoundBorder(new MatteBorder(0, 0, 0, 5, (Color) darkForeground), panelBorder));
		panMainForm.setLayout(new MigLayout("inset 10", "[49.00,grow][14px:14px:14px,right]", "[][][][grow][][grow]"));

		// Reference organism label
		JLabel lblReferenceOrganism = new JLabel("Reference organism:");
		panMainForm.add(lblReferenceOrganism, "cell 0 0");

		// Reference organism combobox
		cbReferenceOrganism = new JComboBox();
		cbReferenceOrganism.addActionListener(new ReferenceOrganismListener(this));

		// Reference organism Help Icon
		JLabel lblHelpRefOrganism = new HelpIcon("Select here the organism from which the protein you entered come from");
		lblHelpRefOrganism.setHorizontalAlignment(SwingConstants.RIGHT);
		panMainForm.add(lblHelpRefOrganism, "cell 1 0");
		panMainForm.add(cbReferenceOrganism, "cell 0 1 2 1,growx");

		// Other organisms label
		JLabel lblHomologOrganism = new JLabel("Other organisms:");
		panMainForm.add(lblHomologOrganism, "cell 0 2");

		// Other organisms Help Icon
		JLabel lblHelpOtherOrganism = new HelpIcon("Select here the other organism in which you want to search homologous interactions");
		lblHelpOtherOrganism.setHorizontalAlignment(SwingConstants.RIGHT);
		panMainForm.add(lblHelpOtherOrganism, "cell 1 2");

		// Other organisms scrollpane containing a panel that will contain checkbox at display
		JScrollPane scrollPaneOtherOrganisms = new JScrollPane();
		scrollPaneOtherOrganisms.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPaneOtherOrganisms.setBorder(fancyBorder);
		panMainForm.add(scrollPaneOtherOrganisms, "cell 0 3 2 1,grow");

		// Other organisms panel that will contain checkbox at display
		panOtherOrganims = new JPanel();
		panOtherOrganims.setBorder(new EmptyBorder(0, 0, 0, 0));
		panOtherOrganims.setBackground(Color.WHITE);
		scrollPaneOtherOrganisms.setViewportView(panOtherOrganims);
		panOtherOrganims.setLayout(new BoxLayout(panOtherOrganims,BoxLayout.Y_AXIS));

		// Source databases label
		javax.swing.JLabel lblSourceDatabases = new javax.swing.JLabel("Source databases:");
		panMainForm.add(lblSourceDatabases, "cell 0 4");

		// Source databases Help Icon
		JLabel lblHelpSourceDatabase = new HelpIcon("Select here the databases from which the interactions will be retrieved");
		lblHelpSourceDatabase.setHorizontalAlignment(SwingConstants.RIGHT);
		panMainForm.add(lblHelpSourceDatabase, "cell 1 4");

		// Source databases scrollpane containing a panel that will contain checkbox at display
		JScrollPane scrollPaneSourceDatabases = new JScrollPane();
		scrollPaneSourceDatabases.setBorder(fancyBorder);
		scrollPaneSourceDatabases.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		panMainForm.add(scrollPaneSourceDatabases, "cell 0 5 2 1,grow");

		// Source databases panel that will contain checkbox at display
		panSourceDatabases = new JPanel();
		panSourceDatabases.setBackground(Color.white);
		panSourceDatabases.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPaneSourceDatabases.setViewportView(panSourceDatabases);
		panSourceDatabases.setLayout(new BoxLayout(panSourceDatabases, BoxLayout.Y_AXIS));

		return panMainForm;
	}

	/**
	 * Creating bottom panel with cancel and submit button
	 * @return the generated JPanel
	 */
	private JPanel initBottomPanel() {
		JPanel panBottomForm = new JPanel();
		
		//Bottom Panel
		panBottomForm.setBackground(darkForeground);
		panBottomForm.setPreferredSize(new Dimension(0, 42));
		panBottomForm.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		//Cancel Button
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setMnemonic(KeyEvent.VK_CANCEL);
		//Cancel action listener
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		panBottomForm.setLayout(new MigLayout("inset 5", "[grow][100px][][100px]", "[29px]"));
		//Add cancel to panel
		panBottomForm.add(btnCancel, "cell 1 0,alignx center,aligny center");

		//Submit Button
		JButton btnSubmit = new JButton("Submit");
		//Submit action listener
		btnSubmit.addActionListener(new SubmitListener());
		//Add submit to panel
		panBottomForm.add(btnSubmit, "cell 3 0,alignx center,aligny center");

		//Set submit as default button
		window.getRootPane().setDefaultButton(btnSubmit);

		return panBottomForm;
	}

	/**
	 * Databases hash accessor
	 * @return the databases hash
	 */
	public LinkedHashMap<String, JCheckBox> getDatabases() {
		return databases;
	}

	/**
	 * Organism hash accessor
	 * @return the organism hash
	 */
	public LinkedHashMap<Integer, JCheckBox> getOrganisms() {
		return organisms;
	}

	/**
	 * Get the list of selected databases
	 * @return list of database values
	 */
	public ArrayList<String> getSelectedDatabases() {
		ArrayList<String> databaseList = new ArrayList<String>();
		
		// For each entry of the database linkedHashmap
		for (Entry<String, JCheckBox> entry : this.getDatabases().entrySet())
			if (entry.getValue().isSelected()) // If the checkbox is selected
				databaseList.add(entry.getKey()); // The database name is add into the list to be returned
		
		return databaseList;
	}
	
	/**
	 * Get the list of selected organisms
	 * @return list of organism taxid
	 */
	public ArrayList<Integer> getSelectedOrganisms() {
		ArrayList<Integer> organismList = new ArrayList<Integer>();
		
		// For each entry of the organism linkedHashmap
		for (Entry<Integer, JCheckBox> entry : this.getOrganisms().entrySet())
			if (entry.getValue().isSelected()) // If the checkbox is selected
				organismList.add(entry.getKey()); // The organism taxid is added into the list to be returned

		return organismList;
	}
	
	/**
	 * Get the selected reference organism
	 * @return the reference organism taxid
	 */
	public Integer getSelectedReferenceOrganism() {
		//TODO: Test this method
		String refOrganism = (String)this.cbReferenceOrganism.getSelectedItem();
		
		// For each entry of the organism linkedHashmap
		for (Entry<Integer, JCheckBox> entry : this.getOrganisms().entrySet())
			if (entry.getValue().getName() == refOrganism) // If the organism is the organism selected 
				return entry.getKey(); // The organism taxid is returned
		return null; //error (shouldn't happen)
	}
	

	/**
	 * Get the list of protein entered by the user in the text area.
	 * @return list of protein identifiers
	 */
	public ArrayList<String> getIdentifiers() throws ArrayStoreException {
		//If text area is empty (excluding spaces, tabulation and new line)
		if (txaIdentifiers.getText().trim().equals(""))
			throw new ArrayStoreException();
		
		//Load each line in an ArrayList
		ArrayList<String> identifierList = new ArrayList<String>();
		for (String str : txaIdentifiers.getText().split("\n")) {
			if(!str.equals("")) identifierList.add(str.trim());
		}
		return identifierList;
	}
	
	/** Close the create network frame */
	public void close() {
		window.setVisible(false);
	}
	
	/**
	 * Get the JFrame window
	 * @return the JFrame window
	 */
	public JFrame getWindow() {
		return window;
	}
	
	/** Clearing all fields and lists of the form displayed in the frame */
	public void clearFormFields() {
		// Emptying form fields
		txaIdentifiers.setText("Q49A88\nQ9VI74");
		cbReferenceOrganism.removeAllItems();
		panOtherOrganims.removeAll();
		panSourceDatabases.removeAll();
	}

	/** 
	 * Update the database list, organism list and organism combobox with the given lists
	 * @param orga a LinkedHashMap of organisms with names in key and taxid in value
	 * @param dbs an ArrayList of database name
	 */
	public void updateLists(LinkedHashMap<String, Integer> orga, ArrayList<String> dbs) {
		// Creation of the organism list
		organisms = new LinkedHashMap<Integer, JCheckBox>();
		for (Entry<String, Integer> entry : orga.entrySet()) {
			JCheckBox j = new JCheckBox(entry.getKey(), true);
			j.setBackground(Color.white);
			organisms.put(entry.getValue(), j);
		}

		// Creation of the database list
		databases = new LinkedHashMap<String, JCheckBox>();
		for (String str : dbs) {
			JCheckBox j = new JCheckBox(str, true);
			j.setBackground(Color.white);
			databases.put(str, j);
		}

		// Filling reference organism Combobox and adding all organism checkbox
		for (JCheckBox checkOrga : organisms.values()) {
			cbReferenceOrganism.addItem(checkOrga.getText());
			panOtherOrganims.add(checkOrga);
		}
		// selecting Homo sapiens in the organism list
		cbReferenceOrganism.setSelectedItem("Homo sapiens");

		// Adding all database checkbox
		for (JCheckBox cbxDb : databases.values())
			panSourceDatabases.add(cbxDb);
	}
}

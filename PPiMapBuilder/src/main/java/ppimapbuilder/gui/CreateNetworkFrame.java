package ppimapbuilder.gui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;
import javax.swing.SpringLayout;
import java.awt.Component;
import javax.swing.border.EmptyBorder;

import cytoscape.Cytoscape;
import ppimapbuilder.gui.listener.CreateNetworkFrameReferenceOrganismListener;
import ppimapbuilder.gui.listener.CreateNetworkFrameSubmitListener;
import ppimapbuilder.ppidb.api.DBConnector;

import javax.swing.JCheckBox;
import java.awt.Color;
import javax.swing.border.MatteBorder;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.BoxLayout;
import javax.swing.ScrollPaneConstants;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Network creation window
 */
public class CreateNetworkFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static CreateNetworkFrame _instance = null; // Instance of the PPiMapBuilder frame to prevent several instances

	private LinkedHashMap<Integer, JCheckBox> organisms;
	private LinkedHashMap<String, JCheckBox> databases;

	private JTextArea txaIdentifiers;
	private JComboBox<String> comboBox;

	private JPanel panSourceDatabases;
	private JPanel panOtherOrganims;

	private Color darkForeground;

	private DBConnector myDBConnector = DBConnector.Instance();


	/**
	 * Create the application.
	 */
	private CreateNetworkFrame() {
		super("PPiMapBuilder - Create a network");

		// Create all component in the window
		initialize();
	}

	/**
	 * Method to access the unique instance of CreateNetworkFrame
	 * 
	 * @return _instance
	 */
	public static CreateNetworkFrame Instance() {
		if (_instance == null)
			_instance = new CreateNetworkFrame();
		return _instance;
	}

	/**
	 * Initialize the contents of the frame
	 */
	private void initialize() {
		// Slightly darker color than window background color
		darkForeground = UIManager.getColor("Panel.background");
		float hsbVals[] = Color.RGBtoHSB(darkForeground.getRed(), darkForeground.getGreen(), darkForeground.getBlue(), null);
		darkForeground = Color.getHSBColor(hsbVals[0], hsbVals[1], 0.9f * hsbVals[2]);
		
		// Split panel
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(5);
		splitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		splitPane.setContinuousLayout(true);
		this.getContentPane().add(splitPane, BorderLayout.CENTER);

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

		// Left part
		JPanel panIndentifiers = initIndentifiersPanel();
		splitPane.setLeftComponent(panIndentifiers);

		// Right part
		JPanel panMainForm = initMainFormPanel();
		splitPane.setRightComponent(panMainForm);

		// Bottom part
		JPanel panBottomForm = initBottomPanel();
		this.getContentPane().add(panBottomForm, BorderLayout.SOUTH);
		
		// Resize window
		this.setMinimumSize(new Dimension(500, 300));
		this.setSize(new Dimension(550, 500));
		
		// Center window
		this.setLocationRelativeTo(null);
	}

	/**
	 * Creating indentifiers panel with label and text area used to fill the
	 * uniprot identifiers
	 * 
	 * @return the generated JPanel
	 */
	private JPanel initIndentifiersPanel() {
		// Left panel
		JPanel panIndentifiers = new JPanel();
		panIndentifiers.setBorder(new CompoundBorder(new MatteBorder(5, 5, 0,
				0, (Color) darkForeground), new CompoundBorder(new MatteBorder(
				0, 0, 1, 0, (Color) new Color(238, 238, 238)),
				new CompoundBorder(new LineBorder(new Color(154, 154, 154), 1),
						new EmptyBorder(5, 5, 5, 5)))));
		panIndentifiers.setLayout(new BorderLayout(0, 0));

		// Label "Uniprot identifiers"
		JLabel lblIdentifiers = new JLabel("Uniprot Identifiers\n");
		lblIdentifiers.setToolTipText("Please enter here uniprot protein indentifier(s) (one per line)");
		panIndentifiers.add(lblIdentifiers, BorderLayout.NORTH);
		lblIdentifiers.setBorder(new EmptyBorder(2, 5, 2, 5));

		// Text area uniprot identifiers
		txaIdentifiers = new JTextArea();
		txaIdentifiers.setFont(new Font("Monospaced", Font.PLAIN, 13));
		txaIdentifiers.setBorder(new EmptyBorder(5, 5, 5, 5));
		txaIdentifiers.setMargin(new Insets(10, 10, 10, 10));

		// Scroll pane around the text area
		JScrollPane scrollPane = new JScrollPane(txaIdentifiers);
		scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPane.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0,
				(Color) new Color(255, 255, 255)), new LineBorder(new Color(
				192, 192, 192), 1)));
		panIndentifiers.add(scrollPane, BorderLayout.CENTER);

		// Panel with clear button
		JPanel panClear = new JPanel();
		panClear.setPreferredSize(new Dimension(0, 35));
		FlowLayout fl_panClear = (FlowLayout) panClear.getLayout();
		fl_panClear.setAlignment(FlowLayout.RIGHT);
		panClear.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panClear.setBorder(new EmptyBorder(0, 0, 0, 0));
		panIndentifiers.add(panClear, BorderLayout.SOUTH);

		// Clear button
		JButton btnClear = new JButton("Clear");
		btnClear.setMnemonic(KeyEvent.VK_CLEAR);
		btnClear.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panClear.add(btnClear);
		btnClear.setPreferredSize(new Dimension(90, 27));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txaIdentifiers.setText("");
			}
		});

		return panIndentifiers;
	}

	/**
	 * Creating the main form panel containing the organism selector and the
	 * source database selector
	 * @return the generated JPanel
	 */
	private JPanel initMainFormPanel() {
		JPanel panMainForm = new JPanel();
		panMainForm.setMinimumSize(new Dimension(200, 10));
		panMainForm.setBorder(new CompoundBorder(new MatteBorder(5, 0, 0, 5,
				(Color) darkForeground), new CompoundBorder(new MatteBorder(0,
				0, 1, 0, (Color) new Color(238, 238, 238)), new CompoundBorder(
				new LineBorder(new Color(154, 154, 154), 1), new EmptyBorder(5,
						5, 5, 5)))));
		panMainForm.setLayout(new MigLayout("", "[49.00,grow]",
				"[][][][grow][][grow]"));

		javax.swing.JLabel lblReferenceOrganism = new javax.swing.JLabel(
				"Reference organism:");
		panMainForm.add(lblReferenceOrganism, "cell 0 0");

		comboBox = new JComboBox<String>();
		comboBox.addActionListener(new CreateNetworkFrameReferenceOrganismListener(
				this));
		panMainForm.add(comboBox, "cell 0 1,growx");

		javax.swing.JLabel lblHomologOrganism = new javax.swing.JLabel(
				"Other organisms:");
		panMainForm.add(lblHomologOrganism, "cell 0 2");

		JScrollPane scrollPaneOtherOrganisms = new JScrollPane();
		scrollPaneOtherOrganisms
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneOtherOrganisms.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPaneOtherOrganisms.setBorder(new CompoundBorder(new MatteBorder(
				0, 0, 1, 0, (Color) new Color(255, 255, 255)), new LineBorder(
				new Color(192, 192, 192), 1)));

		panMainForm.add(scrollPaneOtherOrganisms, "cell 0 3,grow");

		panOtherOrganims = new JPanel();
		panOtherOrganims.setBorder(new EmptyBorder(0, 0, 0, 0));
		panOtherOrganims.setBackground(Color.WHITE);
		scrollPaneOtherOrganisms.setViewportView(panOtherOrganims);
		panOtherOrganims.setLayout(new BoxLayout(panOtherOrganims,
				BoxLayout.Y_AXIS));

		javax.swing.JLabel lblSourceDatabases = new javax.swing.JLabel(
				"Source databases:");
		panMainForm.add(lblSourceDatabases, "cell 0 4");

		JScrollPane scrollPaneSourceDatabases = new JScrollPane();
		scrollPaneSourceDatabases
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneSourceDatabases.setBorder(new CompoundBorder(new MatteBorder(
				0, 0, 1, 0, (Color) new Color(255, 255, 255)), new LineBorder(
				new Color(192, 192, 192), 1)));
		scrollPaneSourceDatabases
				.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		panMainForm.add(scrollPaneSourceDatabases, "cell 0 5,grow");

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
		panBottomForm.setPreferredSize(new Dimension(10, 39));
		panBottomForm.setBorder(new EmptyBorder(0, 0, 0, 0));
		SpringLayout sl_panBottomForm = new SpringLayout();
		panBottomForm.setLayout(sl_panBottomForm);

		//Cancel Button
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setMnemonic(KeyEvent.VK_CANCEL);
		btnCancel.setPreferredSize(new Dimension(100, 29));
		sl_panBottomForm.putConstraint(SpringLayout.NORTH, btnCancel, 5, SpringLayout.NORTH, panBottomForm);
		sl_panBottomForm.putConstraint(SpringLayout.EAST, btnCancel, -180, SpringLayout.EAST, panBottomForm);
		//Cancel action listener
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		//Add cancel to panel
		panBottomForm.add(btnCancel);

		//Submit Button
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setPreferredSize(new Dimension(100, 29));
		sl_panBottomForm.putConstraint(SpringLayout.NORTH, btnSubmit, 5, SpringLayout.NORTH, panBottomForm);
		sl_panBottomForm.putConstraint(SpringLayout.EAST, btnSubmit, -50, SpringLayout.EAST, panBottomForm);
		//Submit action listener
		btnSubmit.addActionListener(new CreateNetworkFrameSubmitListener(this));
		//Add submit to panel
		panBottomForm.add(btnSubmit);

		//Set submit as default button
		this.getRootPane().setDefaultButton(btnSubmit);

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
	 * 
	 * @return list of database values
	 */
	public ArrayList<String> getDatabaseValues() {
		ArrayList<String> databaseList = new ArrayList<String>();
		
		// For each entry of the database linkedHashmap
		for (Entry<String, JCheckBox> entry : this.getDatabases().entrySet())
			if (entry.getValue().isSelected()) // If the checkbox is selected
				databaseList.add(entry.getKey()); // The database name is add into the list to be returned
		
		return databaseList;
	}

	/**
	 * Organism hash accessor
	 * @return the organism hash
	 */
	public LinkedHashMap<Integer, JCheckBox> getOrganisms() {
		return organisms;
	}

	/**
	 * 
	 * @return list of organism values
	 */
	public ArrayList<Integer> getOrganismValues() {
		ArrayList<Integer> organismList = new ArrayList<Integer>();
		
		// For each entry of the organism linkedHashmap
		for (Entry<Integer, JCheckBox> entry : this.getOrganisms().entrySet())
			if (entry.getValue().isSelected()) // If the checkbox is selected
				organismList.add(entry.getKey()); // The organism name is add into the list to be returned

		return organismList;
	}

	/**
	 * 
	 * @return list of protein identifiers
	 */
	public ArrayList<String> getIdentifiers() throws ArrayStoreException {
		//If text area is empty (excluding spaces, tabulation and new line)
		if (txaIdentifiers.getText().trim().equals(""))
			throw new ArrayStoreException();

		//Load each line in an ArrayList
		ArrayList<String> identifierList = new ArrayList<String>();
		for (String str : txaIdentifiers.getText().split("\n")) {
			identifierList.add(str);
		}
		return identifierList;
	}

	/**
	 * Close the create network frame
	 */
	public void close() {
		this.setVisible(false);
	}

	/**
	 * Overrided setVisible method which reload data from database to update the window
	 */
	@Override
	public void setVisible(boolean b) {
		// Updating window
		if (b) {
			// Emptying form fields
			txaIdentifiers.setText("");
			comboBox.removeAllItems();
			panOtherOrganims.removeAll();
			panSourceDatabases.removeAll();

			// Creation of the organism list
			organisms = new LinkedHashMap<Integer, JCheckBox>();
			try {
				for (Entry<String, Integer> entry : myDBConnector.getOrganisms().entrySet()) {
					JCheckBox j = new JCheckBox(entry.getKey(), true);
					j.setBackground(Color.white);
					organisms.put(entry.getValue(), j);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "Connection to database failed", "Connection error", JOptionPane.ERROR_MESSAGE);
				b = false; // We do not display the frame
			}

			// Creation of the database list
			databases = new LinkedHashMap<String, JCheckBox>();
			try {
				for (String str : myDBConnector.getDatabases()) {
					JCheckBox j = new JCheckBox(str, true);
					j.setBackground(Color.white);
					databases.put(str, j);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "Connection to database failed", "Connection error", JOptionPane.ERROR_MESSAGE);
				b = false;
			}

			// Filling reference organism Combobox and adding all organism checkbox
			for (JCheckBox cbxOrga : organisms.values()) {
				comboBox.addItem(cbxOrga.getText());
				panOtherOrganims.add(cbxOrga);
			}
			comboBox.setSelectedIndex(0);
			
			// Disabling first organism checkbox 
			organisms.get(organisms.keySet().toArray()[0]).setEnabled(false);
				
			// Adding all database checkbox
			for (JCheckBox cbxDb: databases.values())
				panSourceDatabases.add(cbxDb);
		}

		super.setVisible(b);
	}

}

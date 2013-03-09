package ppimapbuilder.gui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JLabel;
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
import org.eclipse.wb.swing.FocusTraversalOnArray;

import ppimapbuilder.gui.listener.CreateNetworkFrameReferenceOrganismListener;

import javax.swing.JCheckBox;
import java.awt.Color;

import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.util.LinkedHashMap;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.BoxLayout;
import javax.swing.ScrollPaneConstants;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

/**
 * Network creation window
 */
public class CreateNetworkFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private LinkedHashMap<String, JCheckBox> organisms;
	private LinkedHashMap<String, JCheckBox> databases;
	
	private JTextArea txaIndentifiers;
	private JComboBox comboBox;
	
	private JPanel panSourceDatabases;
	private JPanel panOtherOrganims;
	
	private Color darkForeground;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		new PPiMapBuilderCreateNetworkFrame();
//	}

	/**
	 * Create the application.
	 */
	public CreateNetworkFrame() {
		super("PPiMapBuilder - Create a network");
		
		
		organisms = new LinkedHashMap<String, JCheckBox>();
		for(int i = 1; i<=3; i++) {
			JCheckBox j = new JCheckBox("Orga "+i, true);
			j.setBackground(Color.white);
			organisms.put("Orga "+i, j);
		}
		
		databases = new LinkedHashMap<String, JCheckBox>();
		for(int i = 1; i<=3; i++) {
			JCheckBox j = new JCheckBox("DB "+i, true);
			j.setBackground(Color.white);
			databases.put("DB "+i, j);
		}
		
		initialize();
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame
	 */
	private void initialize() {
		// Look and feel adapted to each system
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//Slightely darker color than window background color
		darkForeground = UIManager.getColor("Panel.background");
		float hsbVals[] = Color.RGBtoHSB(darkForeground.getRed(), darkForeground.getGreen(), darkForeground.getBlue(), null );
		darkForeground = Color.getHSBColor( hsbVals[0], hsbVals[1], 0.9f * hsbVals[2]);
		
		this.setMinimumSize(new Dimension(500, 300));
		this.setBounds(100, 100, 507, 445);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBorder(null);
		splitPane.setDividerSize(5);
		splitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		splitPane.setContinuousLayout(true);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		BasicSplitPaneDivider divider = (BasicSplitPaneDivider) splitPane.getComponent(2);
		
		try {
			splitPane.setUI(new BasicSplitPaneUI() {
				public BasicSplitPaneDivider createDefaultDivider() {
					return new BasicSplitPaneDivider(this) {
						public void setBorder(Border b) {}
	 
						@Override
						public void paint(Graphics g) {
							super.paint(g);
							g.setColor(darkForeground);
							g.fillRect(0, 0, getSize().width, getSize().height);
							
							Graphics2D g2d = (Graphics2D) g;
							int h = 12;
							int w = 2;
	    					int x = (this.getWidth() - w) / 2;
	    					int y = (this.getHeight() - h) / 2;
	    					g2d.setColor(new Color(154, 154, 154));
	    					g2d.drawOval(x, y, w, h);
						}
					};
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Left part
		JPanel panIndentifiers = initIndentifiersPanel();
		splitPane.setLeftComponent(panIndentifiers);
		
		//Right part
		JPanel panMainForm = initMainFormPanel();
		splitPane.setRightComponent(panMainForm);
		
		//Bottom part
		JPanel panBottomForm = initBottomPanel();
		getContentPane().add(panBottomForm, BorderLayout.SOUTH);
		
		this.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txaIndentifiers, panOtherOrganims, panSourceDatabases, panBottomForm.getComponent(0), panBottomForm.getComponent(1)}));
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	/**
	 * Creating indentifiers panel with label and text area used to fill the uniprot identifiers
	 * @return the generated JPanel
	 */
	private JPanel initIndentifiersPanel() {
		JFrame f = this;
		JPanel panIndentifiers = new JPanel();
		panIndentifiers.setBorder(
		new CompoundBorder(
				new MatteBorder(5, 5, 0, 0, (Color) darkForeground),
				new CompoundBorder(
					new MatteBorder(0, 0, 1, 0, (Color) new Color(238, 238, 238)),
					new CompoundBorder(
						new LineBorder(new Color(154, 154, 154), 1),
						new EmptyBorder(5, 5, 5, 5)
					)
				)
			)
		);
		panIndentifiers.setLayout(new BorderLayout(0, 0));
		
		JLabel lblIdentifiers = new JLabel("Uniprot Identifiers\n");
		lblIdentifiers.setToolTipText("Please enter here uniprot protein indentifier(s) (one per line)");
		panIndentifiers.add(lblIdentifiers, BorderLayout.NORTH);
		lblIdentifiers.setBorder(new EmptyBorder(2, 5, 2, 5));
		
		txaIndentifiers = new JTextArea();
		txaIndentifiers.setFont(new Font("Monospaced", Font.PLAIN, 13));
		txaIndentifiers.setBorder(new EmptyBorder(5, 5, 5, 5));
		txaIndentifiers.setMargin(new Insets(10,10,10,10));
		
		JScrollPane scrollPane = new JScrollPane(txaIndentifiers);
		scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPane.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(255, 255, 255)), new LineBorder(new Color(192, 192, 192), 1)));
		panIndentifiers.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panClear = new JPanel();
		panClear.setPreferredSize(new Dimension(0, 35));
		FlowLayout fl_panClear = (FlowLayout) panClear.getLayout();
		fl_panClear.setAlignment(FlowLayout.RIGHT);
		panClear.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panClear.setBorder(new EmptyBorder(0, 0, 0, 0));
		panIndentifiers.add(panClear, BorderLayout.SOUTH);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setMnemonic(KeyEvent.VK_CLEAR);
		btnClear.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panClear.add(btnClear);
		btnClear.setPreferredSize(new Dimension(90, 27));
		
		return panIndentifiers;
	}
	
	/**
	 * Creating the main form panel containing the organism selector and the source database selector
	 * @return the generated JPanel
	 */
	private JPanel initMainFormPanel() {
		JPanel panMainForm = new JPanel();
		panMainForm.setMinimumSize(new Dimension(200, 10));
		panMainForm.setBorder(
			new CompoundBorder(
					new MatteBorder(5, 0, 0, 5, (Color) darkForeground),
					new CompoundBorder(
						new MatteBorder(0, 0, 1, 0, (Color) new Color(238, 238, 238)),
						new CompoundBorder(
							new LineBorder(new Color(154, 154, 154), 1),
							new EmptyBorder(5, 5, 5, 5)
						)
					)
				)
			);
		panMainForm.setLayout(new MigLayout("", "[49.00,grow]", "[][][][grow][][grow]"));
		
		javax.swing.JLabel lblReferenceOrganism = new javax.swing.JLabel("Reference organism:");
		panMainForm.add(lblReferenceOrganism, "cell 0 0");
		
		comboBox = new JComboBox(organisms.keySet().toArray());
		comboBox.addActionListener(new CreateNetworkFrameReferenceOrganismListener(this));
		panMainForm.add(comboBox, "cell 0 1,growx");
		
		javax.swing.JLabel lblHomologOrganism = new javax.swing.JLabel("Other organisms:");
		panMainForm.add(lblHomologOrganism, "cell 0 2");
		
		JScrollPane scrollPaneOtherOrganisms = new JScrollPane();
		scrollPaneOtherOrganisms.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneOtherOrganisms.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPaneOtherOrganisms.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(255, 255, 255)), new LineBorder(new Color(192, 192, 192), 1)));
		
		panMainForm.add(scrollPaneOtherOrganisms, "cell 0 3,grow");
		
		panOtherOrganims = new JPanel();
		panOtherOrganims.setBorder(new EmptyBorder(0, 0, 0, 0));
		panOtherOrganims.setBackground(Color.WHITE);
		scrollPaneOtherOrganisms.setViewportView(panOtherOrganims);
		panOtherOrganims.setLayout(new BoxLayout(panOtherOrganims, BoxLayout.Y_AXIS));
		
		for(int i=0;i<organisms.size();i++) {
			JCheckBox j = organisms.get(organisms.keySet().toArray()[i]);
			if(i==0) {
				j.setEnabled(false); 
				j.setSelected(true); 
			}
			panOtherOrganims.add(j);
		}
		
		javax.swing.JLabel lblSourceDatabases = new javax.swing.JLabel("Source databases:");
		panMainForm.add(lblSourceDatabases, "cell 0 4");
		
		JScrollPane scrollPaneSourceDatabases = new JScrollPane();
		scrollPaneSourceDatabases.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneSourceDatabases.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(255, 255, 255)), new LineBorder(new Color(192, 192, 192), 1)));
		scrollPaneSourceDatabases.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
		panMainForm.add(scrollPaneSourceDatabases, "cell 0 5,grow");
		
		panSourceDatabases = new JPanel();
		panSourceDatabases.setBackground(Color.white);
		panSourceDatabases.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPaneSourceDatabases.setViewportView(panSourceDatabases);
		panSourceDatabases.setLayout(new BoxLayout(panSourceDatabases, BoxLayout.Y_AXIS));
		panMainForm.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{comboBox, panOtherOrganims, panSourceDatabases}));
		
		for(int i=0;i<databases.size();i++) {
			panSourceDatabases.add(databases.get(databases.keySet().toArray()[i]));
		}
		
		return panMainForm;
	}
	
	/**
	 * Creating bottom panel with cancel and submit button
	 * @return the generated JPanel
	 */
	private JPanel initBottomPanel() {
		JPanel panBottomForm = new JPanel();
		panBottomForm.setBackground(darkForeground);
		panBottomForm.setPreferredSize(new Dimension(10, 39));
		panBottomForm.setBorder(new EmptyBorder(0, 0, 0, 0));
		SpringLayout sl_panBottomForm = new SpringLayout();
		panBottomForm.setLayout(sl_panBottomForm);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setMnemonic(KeyEvent.VK_CANCEL);
		btnCancel.setPreferredSize(new Dimension(100, 29));
		sl_panBottomForm.putConstraint(SpringLayout.NORTH, btnCancel, 5, SpringLayout.NORTH, panBottomForm);
		sl_panBottomForm.putConstraint(SpringLayout.EAST, btnCancel, -180, SpringLayout.EAST, panBottomForm);
		panBottomForm.add(btnCancel);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setPreferredSize(new Dimension(100, 29));
		sl_panBottomForm.putConstraint(SpringLayout.NORTH, btnSubmit, 5, SpringLayout.NORTH, panBottomForm);
		sl_panBottomForm.putConstraint(SpringLayout.EAST, btnSubmit, -50, SpringLayout.EAST, panBottomForm);
		panBottomForm.add(btnSubmit);
		panBottomForm.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{btnCancel, btnSubmit}));
		
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
	 * Organism hash accessor
	 * @return the organism hash
	 */
	public LinkedHashMap<String, JCheckBox> getOrganisms() {
		return organisms;
	}
}

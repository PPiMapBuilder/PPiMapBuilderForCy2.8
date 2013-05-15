package ppimapbuilder.changelog.presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Changelog frame listener. Used the 'Close' button is clicked.
 * @author Keuv
 */
public class ChangeLogCloseListener implements ActionListener {

    private ChangelogFrame myFrame;

    public ChangeLogCloseListener(ChangelogFrame myFrame) {
        this.myFrame = myFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (this.myFrame.getChkBoxShowAgain().isSelected()) {
            System.out.println("J'ai envie de revoir le changelog la prochaine fois que je lance la fenetre de creation de réseau de PPiMapBuilder.");
        } else {
            System.out.println("Je n'ai pas envie de revoir le changelog la prochaine fois que je lance la fenetre de creation de réseau de PPiMapBuilder (du moins, pas avant la prochaine MàJ).");
        }

        ChangelogFrame.Instance().setVisible(false);
    }
}

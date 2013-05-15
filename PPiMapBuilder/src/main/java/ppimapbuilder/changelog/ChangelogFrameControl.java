package ppimapbuilder.changelog;

import ppimapbuilder.changelog.presentation.ChangelogFrame;

/**
 * Controlleur de la fenetre de changelog.
 *
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 */
public class ChangelogFrameControl {

    /**
     * Set the changelog text
     * @param myFrame 
     */
    public static void setChangelogText(ChangelogFrame myFrame) {
        myFrame.getTxtareaChnglog().setText("Changelog du 16/05/2013\n\nCeci est un changelog tout à fait complet\n\t- 01\n\t- 02\n");
    }

    /**
     * Open the frame.
     */
    public static void open() {
        ChangelogFrame.Instance().setVisible(true);
    }
}

package gui;

import personnel.GestionPersonnel;
import personnel.SauvegardeImpossible;

public class Gui {
    private GestionPersonnel gestionPersonnel;

    public Gui(GestionPersonnel gestionPersonnel)
    {
        this.gestionPersonnel = gestionPersonnel;
    }
    public static void main(String[] args) throws SauvegardeImpossible
    {
        Gui gui = new Gui(GestionPersonnel.getGestionPersonnel());
        Frame connexionFrame = Frame.connexionFrame(gui.gestionPersonnel);
        showFrame(connexionFrame);
    }

    /**
     * Show a frame
     * @param frame frame to show
     */
    public static void showFrame(Frame frame)
    {
        frame.setVisible(true);
    }

    /**
     * Close a frame, show an other
     * @param frameToClose frame to close
     * @param frameToShow frame to show
     */
    public static void swapFrame(Frame frameToClose, Frame frameToShow) {
        frameToClose.dispose();
        frameToShow.setVisible(true);
    }
}

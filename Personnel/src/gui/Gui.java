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
        Frame connexion = SessionFrame.connexion(gui.gestionPersonnel);
        Frame.show(connexion);
    }
}

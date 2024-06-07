package gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import personnel.GestionPersonnel;

public class Style extends Frame{

    public Style(GestionPersonnel gestionPersonnel) {
        super(gestionPersonnel);
    }

        protected static void makeFrameFullSize(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
    }
}

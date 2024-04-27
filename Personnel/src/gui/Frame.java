package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import personnel.GestionPersonnel;

public class Frame extends JFrame{
    private GestionPersonnel gestionPersonnel;

    public Frame(GestionPersonnel gestionPersonnel)
    {
        this.gestionPersonnel = gestionPersonnel;
        setTitle("VDNGestion");
        makeFrameFullSize(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    protected static Frame connexionFrame(GestionPersonnel gestionPersonnel)
    {
        Frame connexionFrame = new Frame(gestionPersonnel);

        JPanel panel = new JPanel();
        JButton validateBtn = new JButton("Valider");
        JTextField mail = new JTextField("mail");
        JTextField passWord = new JPasswordField("mot de passe");
        JLabel pleaseConnect = new JLabel("Veuillez vous connecter :");
    
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(pleaseConnect);
        panel.add(mail);
        panel.add(passWord);
        panel.add(validateBtn);

        connexionFrame.setContentPane(panel);

        return connexionFrame;
    }

    protected static void makeFrameFullSize(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
    }
}

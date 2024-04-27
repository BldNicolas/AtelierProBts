package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.SortedSet;

import javax.swing.*;

import personnel.GestionPersonnel;
import personnel.Ligue;

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

        validateBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String password = passWord.getText();
                if(verifiePassword(password, gestionPersonnel))
                {
                    Frame welcomeFrame = welcomeFrame(gestionPersonnel);
                    Gui.showFrame(welcomeFrame);
                } else
                {
                    JLabel wrongPassword = new JLabel("Mot de passe ou email incorrect !");
                    
                    panel.add(wrongPassword);

                    connexionFrame.setContentPane(panel);

                    Gui.showFrame(connexionFrame);
                }
            }
        });
    
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(pleaseConnect);
        panel.add(mail);
        panel.add(passWord);
        panel.add(validateBtn);

        connexionFrame.setContentPane(panel);

        return connexionFrame;
    }

    protected static Frame welcomeFrame(GestionPersonnel gestionPersonnel)
    {
        Frame welcomeFrame = new Frame(gestionPersonnel);

        JPanel panel = new JPanel();
        JLabel title = new JLabel("Gestionnaire de ligue");
        JButton ligueBtn;
        JButton backBtn = new JButton("Quitter");
        JButton addBtn = new JButton("Ajouter une ligue");

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(title);
        panel.add(backBtn);
        panel.add(addBtn);

        for (Ligue ligue : gestionPersonnel.getLigues()) {
            ligueBtn = new JButton(ligue.getNom());
            panel.add(ligueBtn);
        }

        welcomeFrame.setContentPane(panel);

        return welcomeFrame;
    }

    protected static void makeFrameFullSize(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
    }

    private static boolean verifiePassword(String password, GestionPersonnel gestionPersonnel)
	{
		boolean isCorrect = gestionPersonnel.getRoot().checkPassword(password);
		return isCorrect;
	}

}

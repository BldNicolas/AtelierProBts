package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import personnel.GestionPersonnel;
import personnel.SauvegardeImpossible;

public class SessionFrame extends Frame{

    public SessionFrame(GestionPersonnel gestionPersonnel) {
        super(gestionPersonnel);
    }

    protected static Frame connexion(GestionPersonnel gestionPersonnel)
    {
        Frame connexion = new Frame(gestionPersonnel);

        JPanel panel = new JPanel();
        JButton validateBtn = new JButton("Valider");
        JTextField nom = new JTextField("nom");
        JTextField passWord = new JPasswordField();
        JLabel pleaseConnect = new JLabel("Veuillez vous connecter :");

        validateBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String password = passWord.getText();
                if(verifiePassword(password, gestionPersonnel))
                {
                    Frame picker = LigueFrame.picker(gestionPersonnel);
                    Frame.swap(connexion, picker);
                } else
                {
                    JLabel wrongPassword = new JLabel(" Nom ou Mot de passe incorrect !");
                    
                    panel.add(wrongPassword);

                    connexion.setContentPane(panel);

                    Frame.show(connexion);
                }
            }
        });
    
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(pleaseConnect);
        panel.add(nom);
        panel.add(passWord);
        panel.add(validateBtn);

        connexion.setContentPane(panel);

        return connexion;
    }

        protected static Frame exit(GestionPersonnel gestionPersonnel)
    {
        Frame exit = new Frame(gestionPersonnel);

        JPanel panel = new JPanel();
        JLabel exitTxt = new JLabel("Quitter");
        JButton backButton = new JButton("Revenir en arrière");
        JButton exitWithSaveBtn = new JButton("Quitter en enrengistrant");
        JButton exitWithoutSaveBtn = new JButton("Quitter sans enregistrer");

        backButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Frame.swap(exit, LigueFrame.picker(gestionPersonnel));
            }
        });

        exitWithSaveBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    gestionPersonnel.sauvegarder();
                    System.exit(0);
                }
                catch (SauvegardeImpossible e1)
                {
                JLabel impossibleToSaveTxt = new JLabel("Impossible de sauvegarder !");
                
                panel.add(impossibleToSaveTxt);

                exit.setContentPane(panel);

                Frame.show(exit);
                }
            }
        });

        exitWithoutSaveBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        panel.add(exitTxt);
        panel.add(exitWithSaveBtn);
        panel.add(exitWithoutSaveBtn);
        panel.add(backButton);

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        exit.setContentPane(panel);

        return exit;
    }
}

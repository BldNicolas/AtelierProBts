package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

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
        JLabel selectLigueToEditTxt = new JLabel("Sélectionnez une ligue à modifier :");
        JButton ligueBtn;
        JButton backBtn = new JButton("Quitter");
        JButton addLigueBtn = new JButton("Ajouter une ligue");

        addLigueBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Frame addLigueFrame = addLigueFrame(gestionPersonnel);
                Gui.showFrame(addLigueFrame);
            }
        });

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(title);
        
        if (gestionPersonnel.getLigues() != null) {
            panel.add(selectLigueToEditTxt);
            for (Ligue ligue : gestionPersonnel.getLigues()) {
                ligueBtn = new JButton(ligue.getNom());

                ligueBtn.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                       Gui.showFrame(selectLigueFrame(gestionPersonnel, ligue));
                    }
                });

                panel.add(ligueBtn);
            }
        }
        
        panel.add(backBtn);
        panel.add(addLigueBtn);

        welcomeFrame.setContentPane(panel);

        return welcomeFrame;
    }

    protected static Frame addLigueFrame(GestionPersonnel gestionPersonnel)
    {
        Frame addLigueFrame = new Frame(gestionPersonnel);

        JPanel panel = new JPanel();
        JLabel addLigueTxt = new JLabel("Ajouter une ligue");
        JLabel ligueNameTxt = new JLabel("Nom de la ligue : ");
        JTextField ligueNameField = new JTextField();
        JButton validateBtn = new JButton("Valider");

        validateBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String ligueName = ligueNameField.getText();
                try
                {
                    gestionPersonnel.addLigue(ligueName);
                    Frame welcomeFrame = welcomeFrame(gestionPersonnel);
                    Gui.showFrame(welcomeFrame);
                }
                catch(SauvegardeImpossible exception)
                {
                    JLabel impossibleToSaveTxt = new JLabel("Impossible de sauvegarder cette ligue !");
                    
                    panel.add(impossibleToSaveTxt);

                    addLigueFrame.setContentPane(panel);

                    Gui.showFrame(addLigueFrame);
                }
            }
        });

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(addLigueTxt);
        panel.add(ligueNameTxt);
        panel.add(ligueNameField);
        panel.add(validateBtn);

        addLigueFrame.setContentPane(panel);

        return addLigueFrame;
    }

    protected static Frame selectLigueFrame(GestionPersonnel gestionPersonnel, Ligue ligue)
    {
        Frame selectLigueFrame = new Frame(gestionPersonnel);

        JPanel panel = new JPanel();
        JLabel ligueNameTxt = new JLabel(ligue.getNom());
        JLabel adminLigueTxt = new JLabel("Administrateur de la ligue :");
        JButton backBtn = new JButton("Revenir en arrière");
        JButton editLigueBtn = new JButton("Modifier la ligue");
        JButton deleteLigueBtn = new JButton("Supprimer la ligue");
        JButton addEmployeBtn = new JButton("Ajouter un employé");
        JButton employeBtn;

        backBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Gui.showFrame(welcomeFrame(gestionPersonnel));
            }
        });

        editLigueBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Gui.showFrame(editLigueFrame(gestionPersonnel));
            }
        });

        deleteLigueBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    ligue.remove();
                    Gui.showFrame(welcomeFrame(gestionPersonnel));
                }
                catch (SauvegardeImpossible e1)
                {
                    JLabel impossibleToSaveTxt = new JLabel("Impossible de sauvegarder cette ligue !");
                    
                    panel.add(impossibleToSaveTxt);

                    selectLigueFrame.setContentPane(panel);

                    Gui.showFrame(selectLigueFrame);
                }
            }
        });

        addEmployeBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Gui.showFrame(addEmployeFrame(gestionPersonnel, ligue));
            }
        });

        panel.add(ligueNameTxt);
        panel.add(editLigueBtn);
        panel.add(deleteLigueBtn);

        for (Employe employe : ligue.getEmployes()) {
            if (employe.estAdmin(ligue)) {
                panel.add(adminLigueTxt);
            }
            employeBtn = new JButton(employe.getNom() + employe.getPrenom());

            employeBtn.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Gui.showFrame(editEmployeFrame(gestionPersonnel, employe));
                }
            });

            panel.add(employeBtn);
        }

        panel.add(addEmployeBtn);
        panel.add(backBtn);

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        selectLigueFrame.setContentPane(panel);

        return selectLigueFrame;
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

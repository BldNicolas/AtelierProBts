package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

public class LigueFrame extends Frame {

    public LigueFrame(GestionPersonnel gestionPersonnel) {
        super(gestionPersonnel);
    }

    protected static Frame picker(GestionPersonnel gestionPersonnel)
    {
        Frame picker = new Frame(gestionPersonnel);

        JPanel panel = new JPanel();
        JLabel title = new JLabel("Gestionnaire de ligue");
        JLabel selectLigueToEditTxt = new JLabel("Sélectionnez une ligue à modifier :");
        JButton ligueBtn;
        JButton backBtn = new JButton("Quitter");
        JButton addLigueBtn = new JButton("Ajouter une ligue");

        backBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Frame.swap(picker, SessionFrame.exit(gestionPersonnel));
            }
        });

        addLigueBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Frame add = add(gestionPersonnel);
                Frame.swap(picker(gestionPersonnel), add);
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
                        Frame.swap(picker, ligue(gestionPersonnel, ligue));
                    }
                });

                panel.add(ligueBtn);
            }
        }
        
        panel.add(backBtn);
        panel.add(addLigueBtn);

        picker.setContentPane(panel);

        return picker;
    }

    protected static Frame add(GestionPersonnel gestionPersonnel)
    {
        Frame add = new Frame(gestionPersonnel);

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
                    Frame picker = picker(gestionPersonnel);
                    Frame.swap(add, picker);
                }
                catch(SauvegardeImpossible exception)
                {
                    JLabel impossibleToSaveTxt = new JLabel("Impossible de sauvegarder cette ligue !");
                    
                    panel.add(impossibleToSaveTxt);

                    add.setContentPane(panel);

                    Frame.show(add);
                }
            }
        });

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(addLigueTxt);
        panel.add(ligueNameTxt);
        panel.add(ligueNameField);
        panel.add(validateBtn);

        add.setContentPane(panel);

        return add;
    }
    
    protected static Frame ligue(GestionPersonnel gestionPersonnel, Ligue ligue)
    {
        Frame ligue = new Frame(gestionPersonnel);

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
                Frame.swap(ligue, picker(gestionPersonnel));
            }
        });

        editLigueBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Frame.swap(ligue, editLigueFrame(gestionPersonnel));
            }
        });

        deleteLigueBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    ligue.remove();
                    Frame.swap(ligue, picker(gestionPersonnel));
                }
                catch (SauvegardeImpossible e1)
                {
                    JLabel impossibleToSaveTxt = new JLabel("Impossible de sauvegarder cette ligue !");
                    
                    panel.add(impossibleToSaveTxt);

                    ligue.setContentPane(panel);

                    Frame.show(ligue);
                }
            }
        });

        addEmployeBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Frame.swap(ligue, EmployeFrame.add(gestionPersonnel, ligue));
            }
        });

        panel.add(ligueNameTxt);
        panel.add(editLigueBtn);
        panel.add(deleteLigueBtn);

        for (Employe employe : ligue.getEmployes()) {
            if (employe.estAdmin(ligue)) {
                panel.add(adminLigueTxt);
            }
            employeBtn = new JButton(employe.getNom() + " " + employe.getPrenom());

            employeBtn.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Frame.swap(ligue, editEmployeFrame(gestionPersonnel, employe));
                }
            });

            panel.add(employeBtn);
        }

        panel.add(addEmployeBtn);
        panel.add(backBtn);

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        ligue.setContentPane(panel);

        return ligue;
    }
}

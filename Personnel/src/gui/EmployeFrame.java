package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

public class EmployeFrame extends Frame {

    public EmployeFrame(GestionPersonnel gestionPersonnel) {
        super(gestionPersonnel);
    }

    public static Frame add(GestionPersonnel gestionPersonnel, Ligue ligue) {
        Frame add = new Frame(gestionPersonnel);

        JPanel panel = new JPanel();
        JLabel titleTxt = new JLabel("Ajouter un employé");
        JLabel firstNameTxt = new JLabel("Prénom :");
        JLabel lastNameTxt = new JLabel("Nom de famille :");
        JLabel mailTxt = new JLabel("Mail :");
        JLabel arrivalDateTxt = new JLabel("Date d'arrivée (Format: dd-mm-yyyy) :");
        JLabel departureDateTxt = new JLabel("Date de départ (Format: dd-mm-yyyy)  :");
        JLabel passwordTxt = new JLabel("Mot de passe :");
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField mailField = new JTextField();
        JTextField arrivalDateField = new JTextField();
        JTextField departureDateField = new JTextField();
        JTextField passwordField = new JPasswordField();
        JButton backBtn = new JButton("Revenir en arrière");
        JButton validateBtn = new JButton("Valider");
        JCheckBox adminCheckBox = new JCheckBox("Définir comme administrateur");
        
        validateBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String mail = mailField.getText();
                String arrivalDate = arrivalDateField.getText();
                String departureDate = departureDateField.getText();
                String password = passwordField.getText();
                boolean isAdmin = adminCheckBox.isSelected();

                try {
                    // Ajouter l'employé à la ligue et sauvegarder dans la base de données
                    ligue.addEmploye(lastName, firstName, mail, password, add.strToLocalDate(arrivalDate), add.strToLocalDate(departureDate));
                    
                    // Vérifier si l'employé doit être administrateur
                    if (isAdmin) {
                        ligue.setAdministrateur(ligue.getEmployes().last());
                    }
                    
                    Frame.swap(add, LigueFrame.select(gestionPersonnel, ligue));
                } catch (SauvegardeImpossible e1) {
                    e1.printStackTrace();
                }
            }
        });

        backBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Frame.swap(add, LigueFrame.select(gestionPersonnel, ligue));
            }
        });

        panel.add(titleTxt);
        panel.add(firstNameTxt);
        panel.add(firstNameField);
        panel.add(lastNameTxt);
        panel.add(lastNameField);
        panel.add(mailTxt);
        panel.add(mailField);
        panel.add(arrivalDateTxt);
        panel.add(arrivalDateField);
        panel.add(departureDateTxt);
        panel.add(departureDateField);
        panel.add(passwordTxt);
        panel.add(passwordField);

        panel.add(backBtn);
        panel.add(adminCheckBox);
        panel.add(validateBtn);

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        add.setContentPane(panel);

        return add;
    }
    
    public static Frame edit(GestionPersonnel gestionPersonnel, Employe employe) {
        Frame edit = new Frame(gestionPersonnel);

        JPanel panel = new JPanel();
        JLabel titleTxt = new JLabel("Modifier un employé");
        JLabel firstNameTxt = new JLabel("Prénom :");
        JLabel lastNameTxt = new JLabel("Nom de famille :");
        JLabel mailTxt = new JLabel("Mail :");
        JLabel arrivalDateTxt = new JLabel("Date d'arrivée :");
        JLabel departureDateTxt = new JLabel("Date de départ :");
        JLabel passwordTxt = new JLabel("Mot de passe :");
        JTextField firstNameField = new JTextField(employe.getPrenom());
        JTextField lastNameField = new JTextField(employe.getNom());
        JTextField mailField = new JTextField(employe.getMail());
        JTextField arrivalDateField = new JTextField(employe.getDateArrive().toString());
        JTextField departureDateField = new JTextField(employe.getDateDepart() != null ? employe.getDateDepart().toString() : "");
        JTextField passwordField = new JPasswordField(employe.getPassword());
        JCheckBox adminCheckBox = new JCheckBox("Définir comme administrateur", employe.estAdmin(employe.getLigue()));
        JButton backBtn = new JButton("Revenir en arrière");
        JButton validateBtn = new JButton("Valider");

        validateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String mail = mailField.getText();
                String arrivalDate = arrivalDateField.getText();
                String departureDate = departureDateField.getText();
                String password = passwordField.getText();
                boolean isAdmin = adminCheckBox.isSelected();

                try {
                    employe.setNom(lastName);
                    employe.setPrenom(firstName);
                    employe.setMail(mail);
                    employe.setPassword(password);
                    employe.setDateArrive(edit.strToLocalDate(arrivalDate));
                    employe.setDateDepart(departureDate.isEmpty() ? null : edit.strToLocalDate(departureDate));
                    
                    if (isAdmin) {
                        employe.getLigue().setAdministrateur(employe);
                    }

                    gestionPersonnel.update(employe);
                    Frame.swap(edit, LigueFrame.select(gestionPersonnel, employe.getLigue()));
                } catch (SauvegardeImpossible e1) {
                    e1.printStackTrace();
                }
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame.swap(edit, LigueFrame.select(gestionPersonnel, employe.getLigue()));
            }
        });

        panel.add(titleTxt);
        panel.add(firstNameTxt);
        panel.add(firstNameField);
        panel.add(lastNameTxt);
        panel.add(lastNameField);
        panel.add(mailTxt);
        panel.add(mailField);
        panel.add(arrivalDateTxt);
        panel.add(arrivalDateField);
        panel.add(departureDateTxt);
        panel.add(departureDateField);
        panel.add(passwordTxt);
        panel.add(passwordField);
        panel.add(adminCheckBox);
        panel.add(validateBtn);
        panel.add(backBtn);

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        edit.setContentPane(panel);

        return edit;
    }
}


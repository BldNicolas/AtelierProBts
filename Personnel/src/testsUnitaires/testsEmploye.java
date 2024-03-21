package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import personnel.*;

public class testsEmploye {
	
	//Initiaisation d'un employe
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
	
    //Test des setters
    @Test
    void SetNom() throws SauvegardeImpossible{
		Ligue ligue = gestionPersonnel.addLigue("TestNom");
		Employe employe = ligue.addEmploye("Dupont", "Jean", "employe@coucou.com", "mdp", LocalDate.of(1, 1, 2018), LocalDate.of(1, 1, 2020));
		employe.setNom("Martin");
        assertEquals("Martin", employe.getNom());
    }

    @Test
    void SetPrenom() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("TestPrenom");
    	Employe employe = ligue.addEmploye("Durand", "Pierre", "employe@coucou.com", "mdp", LocalDate.of(1, 1, 2018), LocalDate.of(1, 1, 2020));
        employe.setPrenom("Paul");
        assertEquals("Paul", employe.getPrenom());
    }

    @Test
    void SetMail() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("TestMail");
    	Employe employe = ligue.addEmploye("Leclerc", "Philippe", "employe@coucou.com", "mdp", LocalDate.of(1, 1, 2018), LocalDate.of(1, 1, 2020));
        employe.setMail("lemail@gmal.com");
        assertEquals("lemail@gmal.com", employe.getMail());
    }

    @Test
    void SetPassword() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("TestPassword");
    	Employe employe = ligue.addEmploye("Lefebvre", "Sophie", "employe@coucou.com", "mdp", LocalDate.of(1, 1, 2018), LocalDate.of(1, 1, 2020));
        employe.setPassword("gngngn12!++A");
        assertTrue(employe.checkPassword("gngngn12!++A"));
    }

    @Test
    void SetDateArrive() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("TestDateArrive");
    	Employe employe = ligue.addEmploye("Petit", "Lucie", "employe@coucou.com", "mdp", LocalDate.of(1, 1, 2018), LocalDate.of(1, 1, 2020));
        LocalDate testDate = LocalDate.of(18, 1, 2024);
        employe.setDateArrive(testDate);
        assertEquals(testDate, employe.getDateArrive());
    }

    @Test
    void SetDateDepart() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("TestDateDepart");
    	Employe employe = ligue.addEmploye("Jacques", "Marie", "employe@coucou.com", "mdp", LocalDate.of(1, 1, 2018), LocalDate.of(1, 1, 2020));
        LocalDate nouvelleDateDepart = LocalDate.of(1, 1, 2026);
        employe.setDateDepart(nouvelleDateDepart);
        assertEquals(nouvelleDateDepart, employe.getDateDepart());
    }

    @Test
    void setAdmin() throws SauvegardeImpossible {
    	Ligue ligue = gestionPersonnel.addLigue("TestSetAdmin");
        Employe employe1 = ligue.addEmploye("Riviere", "Julien", "cristiano@gmail.com", "cr7", LocalDate.now(), null);
        ligue.setAdministrateur(employe1);
        assertEquals(employe1, ligue.getAdministrateur());
    }
    
    //Test des getters
    @Test
    void getNom() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("TestGetNom");
    	Employe employe = ligue.addEmploye("Moreau", "Luc", "employe@coucou.com", "mdp", LocalDate.of(1, 1, 2018), LocalDate.of(1, 1, 2020));
    	assertEquals("Moreau", employe.getNom());
    }

    @Test
    void getPrenom() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("TestGetPrenom");
    	Employe employe = ligue.addEmploye("Gauthier", "Alice", "employe@coucou.com", "mdp", LocalDate.of(1, 1, 2018), LocalDate.of(1, 1, 2020));
        assertEquals("Alice", employe.getPrenom());
    }

    @Test
    void getMail() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("TestGetMail");
    	Employe employe = ligue.addEmploye("Roussel", "Eric", "employe@coucou.com", "mdp", LocalDate.of(1, 1, 2018), LocalDate.of(1, 1, 2020));
        assertEquals("employe@coucou.com", employe.getMail());
    }

    @Test
    void getLigue() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("TestGetLigue");
    	Employe employe = ligue.addEmploye("Blanchard", "Nicolas", "employe@coucou.com", "mdp", LocalDate.of(1, 1, 2018), LocalDate.of(1, 1, 2020));
        assertEquals("TestGetLigue", employe.getLigue().getNom());
    }

    @Test
    void getDateArrive() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("TestGetDateArrive");
    	Employe employe = ligue.addEmploye("Girard", "Julie", "employe@coucou.com", "mdp", LocalDate.of(1, 1, 2018), LocalDate.of(1, 1, 2020));
        assertEquals(LocalDate.of(1, 1, 2018), employe.getDateArrive());
    }

    @Test
    void getDateDepart() throws SauvegardeImpossible{
    	Ligue ligue = gestionPersonnel.addLigue("TestGetDateDepart");
    	Employe employe = ligue.addEmploye("Fournier", "Thomas", "employe@coucou.com", "mdp", LocalDate.of(1, 1, 2018), LocalDate.of(1, 1, 2020));
        assertEquals(LocalDate.of(1, 1, 2020), employe.getDateDepart());
    }
    
    //Test suppression
    @Test
    void removeEmploye() throws SauvegardeImpossible {
    	Ligue ligue = gestionPersonnel.addLigue("TestRemoveEmploye");
    	Employe employe1 = ligue.addEmploye("Lambert", "Nathalie", "mjordan@gmail.com", "airjordan", LocalDate.now(), null);
        employe1.remove();
        assertNull(ligue.getEmployes());
    }

    @Test
    void removeAdmin() throws SauvegardeImpossible {
    	Ligue ligue = gestionPersonnel.addLigue("TestRemoveAdmin");
        Employe employe1 = ligue.addEmploye("Bonnard", "Thierry", "cristiano@gmail.com", "cr7", LocalDate.now(), null);
        ligue.setAdministrateur(employe1);
        ligue.setAdministrateur(null);
        assertNull(ligue.getAdministrateur());
        assertEquals(gestionPersonnel.getRoot(), ligue.getAdministrateur());
    }
}

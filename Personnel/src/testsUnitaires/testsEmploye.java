package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import personnel.*;

public class testsEmploye {
	
	//Initiaisation d'un employe
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
	static Employe employe;
	
	@BeforeAll
    public void setUp() throws SauvegardeImpossible {
    	Ligue ligue = gestionPersonnel.addLigue("Panda2");
    	employe = ligue.addEmploye("Le Chant", "Léo", "employe@coucou.com", "mdp", LocalDate.of(2018, 1, 1), LocalDate.of(2020, 1, 1));
    }

    //Test des setters
    @Test
    void SetNom() throws SauvegardeImpossible{
		employe.setNom("Loup");
        assertEquals("Loup", employe.getNom());
    }

    @Test
    void SetPrenom() throws SauvegardeImpossible{
        employe.setPrenom("Garou");
        assertEquals("Garou", employe.getPrenom());
    }

    @Test
    void SetMail() throws SauvegardeImpossible{
        employe.setMail("lemail@gmal.com");
        assertEquals("lemail@gmal.com", employe.getMail());
    }

    @Test
    void SetPassword() throws SauvegardeImpossible{
        employe.setPassword("gngngn12!++A");
        assertTrue(employe.checkPassword("gngngn12!++A"));
    }

    @Test
    void SetDateArrive() throws SauvegardeImpossible{
        LocalDate testDate = LocalDate.of(18, 1, 2024);
        employe.setDateArrive(testDate);
        assertEquals(testDate, employe.getDateArrive());
    }

    @Test
    void SetDateDepart() throws SauvegardeImpossible{
        LocalDate nouvelleDateDepart = LocalDate.of(1, 1, 2026);
        employe.setDateDepart(nouvelleDateDepart);
        assertEquals(nouvelleDateDepart, employe.getDateDepart());
    }
    
    //Test des getters
    @Test
    void getNom() throws SauvegardeImpossible{
    	assertEquals("Le Chant", employe.getNom());
    }

    @Test
    void getPrenom() throws SauvegardeImpossible{
        assertEquals("Léo", employe.getPrenom());
    }

    @Test
    void getMail() throws SauvegardeImpossible{
        assertEquals("employe@coucou.com", employe.getMail());
    }

    @Test
    void getLigue() throws SauvegardeImpossible{
        assertEquals("Panda2", employe.getLigue());
    }

    @Test
    void getDateArrive() throws SauvegardeImpossible{
        assertEquals(LocalDate.of(2018, 1, 1), employe.getDateArrive());
    }

    @Test
    void getDateDepart() throws SauvegardeImpossible{
        assertEquals(LocalDate.of(2020, 1, 1), employe.getDateDepart());
    }
}
package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import personnel.*;

public class testsEmploye {
	private Employe employe = new Employe();

	//Initiaisation d'un employe
    @Before
    public void setUp() {
        employe = new Employe(
                new GestionPersonnel(),
                new Ligue("Elvis is the best"),
                "TheRock",
                "Ibrahimovic",
                "zoumbacafe@pitch.com",
                "original",
                LocalDate.of(2002, 1, 25),
                LocalDate.of(2002, 10, 1));
    }

    //Test des setters
    @Test
    public void SetNom() throws SauvegardeImpossible{
        employe.setNom("Loup");
        assertEquals("Loup", employe.getNom());
    }

    @Test
    public void SetPrenom() throws SauvegardeImpossible{
        employe.setPrenom("Garou");
        assertEquals("Garou", employe.getPrenom());
    }

    @Test
    public void SetMail() throws SauvegardeImpossible{
        employe.setMail("lemail@gmal.com");
        assertEquals("lemail@gmal.com", employe.getMail());
    }

    @Test
    public void SetPassword() throws SauvegardeImpossible{
        employe.setPassword("gngngn12!++A");
        assertTrue(employe.checkPassword("gngngn12!++A"));
    }

    @Test
    public void SetDateArrive() throws SauvegardeImpossible{
        LocalDate testDate = LocalDate.of(2024, 1, 18);
        employe.setDate_arrive(testDate);
        assertEquals(testDate, employe.getDateArrive());
    }

    @Test
    public void SetDateDepart() throws SauvegardeImpossible{
        LocalDate nouvelleDateDepart = LocalDate.of(2026, 1, 1);
        employe.setDate_depart(nouvelleDateDepart);
        assertEquals(nouvelleDateDepart, employe.getDateDepart());
    }
    
    //Test des exceptions
    @Test
    public void 
}

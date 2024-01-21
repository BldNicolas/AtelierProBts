package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import personnel.*;

class testLigue
{
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();

	@Test
	void createLigue() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		assertEquals("Fléchettes", ligue.getNom());
	}

	@Test
	void getNom() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Bowling");
		assertEquals("Bowling", ligue.getNom());
	}

	@Test
	void setNom() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Lancer de hache");
		ligue.setNom("Lancer de hache sur cibles vivantes");
		assertEquals("Lancer de hache sur cibles vivantes", ligue.getNom());
	}

	@Test
	void getAdministrateur() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Barbecue");
		Employe employe = ligue.addEmploye("D'Arc", "Jeanne", "j.arc@weber.com", "gécho", LocalDate.now(), null);
		ligue.setAdministrateur(employe);
		assertEquals("D'Arc", ligue.getAdministrateur().getNom());
	}

	@Test
	void setAdministrateur() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Tir à l'arc");
		Employe employe = ligue.addEmploye("DesBois", "Robin", "je.tire@pommes.com", "feu", LocalDate.now(), null);
		ligue.setAdministrateur(employe);
		assertEquals(employe, ligue.getAdministrateur());
	}

	@Test
	void addEmploye() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", LocalDate.now(), null);
		assertEquals(employe, ligue.getEmployes().first());
	}
}

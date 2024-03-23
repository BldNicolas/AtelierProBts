package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import commandLineMenus.ListOption;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import personnel.Employe;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

public class EmployeConsole
{
	private Option afficher(final Employe employe)
	{
		return new Option("Afficher l'employé", "l", () -> {System.out.println(employe);});
	}

	ListOption<Employe> editerEmploye()
	{
		return (employe) -> editerEmploye(employe);
	}

	Option editerEmploye(Employe employe)
	{
		Menu menu = new Menu("Gérer le compte " + employe.getNom(), "c");
		menu.add(afficher(employe));
		menu.add(changerNom(employe));
		menu.add(changerPrenom(employe));
		menu.add(changerMail(employe));
		menu.add(changerPassword(employe));
		menu.add(changerAdministrateur(employe));
		menu.add(supprimerEmploye(employe));
		menu.addBack("q");
		return menu;
	}

	private Option changerAdministrateur(Employe employe)
	{
		Ligue ligue = employe.getLigue();
		return new Option
		(
			"Définir " + employe.getNom() + " administrateur",
			"c", () -> {
				try {
					ligue.setAdministrateur(employe);
				} catch (SauvegardeImpossible e) {
					System.err.println("Erreur lors du changement de l'administrateur");
				}
			}
		);
	}

	private Option changerNom(final Employe employe)
	{
		return new Option
		(
			"Changer le nom",
			"n",
			() -> {try {
				employe.setNom(getString("Nouveau nom : "));
			} catch (SauvegardeImpossible e) {
				e.printStackTrace();
				System.err.println("Erreur lors de la mise a jour de l'utilisateur (nom)");
				
			}}
		);
	}
	
	private Option changerPrenom(final Employe employe)
	{
		return new Option
		(
			"Changer le prénom",
			"p",
			() -> {try {
				employe.setPrenom(getString("Nouveau prénom : "));
			} catch (SauvegardeImpossible e) {
				e.printStackTrace();
				System.err.println("Erreur lors de la mise a jour de l'utilisateur (prenom)");
			}}
		);
	}
	
	private Option changerMail(final Employe employe)
	{
		return new Option
		(
			"Changer le mail",
			"e",
			() -> {try {
				employe.setMail(getString("Nouveau mail : "));
			} catch (SauvegardeImpossible e) {
				e.printStackTrace();
				System.err.println("Erreur lors de la mise a jour de l'utilisateur (mail)");
			}}
		);
	}
	
	private Option changerPassword(final Employe employe)
	{
		return new Option
		(
			"Changer le password",
			"x",
			() -> {try {
				employe.setPassword(getString("Nouveau password : "));
			} catch (SauvegardeImpossible e) {
				e.printStackTrace();
				System.err.println("Erreur lors de la mise a jour de l'utilisateur (pass)");
			}}
		);
	}

	private Option supprimerEmploye(Employe employe)
	{
		return new Option
		(
			"Supprimer",
			"d",
			() -> {try {
				employe.remove();
			} catch (SauvegardeImpossible e) {
				System.err.println("Impossible de supprimer cet employé");
			}}
		);
	}
}

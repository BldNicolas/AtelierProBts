package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.util.ArrayList;

import commandLineMenus.List;
import commandLineMenus.ListOption;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import personnel.Employe;
import personnel.Ligue;

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

	ListOption<Employe> changerAdministrateur(Ligue ligue)
	{
		return (employe) -> changerAdministrateur(ligue, employe);
	}

	Option editerEmploye(Employe employe)
	{
		Menu menu = new Menu("Gérer le compte " + employe.getNom(), "c");
		menu.add(afficher(employe));
		menu.add(changerNom(employe));
		menu.add(changerPrenom(employe));
		menu.add(changerMail(employe));
		menu.add(changerPassword(employe));
		menu.add(supprimerEmploye(employe));
		menu.addBack("q");
		return menu;
	}

	private Option changerAdministrateur(Ligue ligue, Employe employe)
	{
		return new Option
		(
			"Définir " + employe.getNom() + " administrateur",
			"c",
			() -> {ligue.setAdministrateur(employe);}
			);
	}

	private Option changerNom(final Employe employe)
	{
		return new Option
		(
			"Changer le nom",
			"n",
			() -> {employe.setNom(getString("Nouveau nom : "));}
		);
	}
	
	private Option changerPrenom(final Employe employe)
	{
		return new Option
		(
			"Changer le prénom",
			"p",
			() -> {employe.setPrenom(getString("Nouveau prénom : "));}
		);
	}
	
	private Option changerMail(final Employe employe)
	{
		return new Option
		(
			"Changer le mail",
			"e",
			() -> {employe.setMail(getString("Nouveau mail : "));}
		);
	}
	
	private Option changerPassword(final Employe employe)
	{
		return new Option
		(
			"Changer le password",
			"x",
			() -> {employe.setPassword(getString("Nouveau password : "));}
		);
	}

	private Option supprimerEmploye(Employe employe)
	{
		return new Option
		(
			"Supprimer",
			"d",
			() -> {employe.remove();}
		);
	}
}

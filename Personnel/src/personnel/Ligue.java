package personnel;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Représente une ligue. Chaque ligue est reliée à une liste
 * d'employés dont un administrateur. Comme il n'est pas possible
 * de créer un employé sans l'affecter à une ligue, le root est
 * l'administrateur de la ligue jusqu'à ce qu'un administrateur
 * lui ait été affecté avec la fonction {@link #setAdministrateur}.
 */

public class Ligue implements Serializable, Comparable<Ligue>
{
	private static final long serialVersionUID = 1L;
	public int id = -1;
	private String nom;
	private SortedSet<Employe> employes;
	private Employe administrateur;
	private GestionPersonnel gestionPersonnel;
	
	/**
	 * Crée une ligue dans la base de donnée.
	 * @param nom le nom de la ligue.
	 */
	
	Ligue(GestionPersonnel gestionPersonnel, String nom) throws SauvegardeImpossible
	{
		this(gestionPersonnel, -1, nom);
		this.id = gestionPersonnel.insert(this);
	}
	
	/**
	 * Crée une ligue localement.
	 * @param nom le nom de la ligue.
	 */

	Ligue(GestionPersonnel gestionPersonnel, int id, String nom)
	{
		this.nom = nom;
		employes = new TreeSet<>();
		this.gestionPersonnel = gestionPersonnel;
		administrateur = gestionPersonnel.getRoot();
		this.id = id;
	}

	/**
	 * Retourne le nom de la ligue.
	 * @return le nom de la ligue.
	 */

	public String getNom()
	{
		return nom;
	}

	/**
	 * Change le nom localement et en base de donnée.
	 * @param nom le nouveau nom de la ligue.
	 * @throws SauvegardeImpossible
	 */
	public void setNom(String nom) throws SauvegardeImpossible
	{
		this.nom = nom;
		gestionPersonnel.update(this);
	}

	/**
	 * Retourne l'administrateur de la ligue.
	 * @return l'administrateur de la ligue.
	 */
	
	public Employe getAdministrateur()
	{
		return administrateur;
	}

	/**
	 * Fait de administrateur l'administrateur de la ligue.
	 * Lève DroitsInsuffisants si l'administrateur n'est pas
	 * un employé de la ligue ou le root. Révoque les droits de l'ancien
	 * administrateur. Modification en local et en base de donnée.
	 * @param administrateur le nouvel administrateur de la ligue.
	 * @throws SauvegardeImpossible
	 */
	public void setAdministrateur(Employe administrateur) throws SauvegardeImpossible
	{
		Employe root = gestionPersonnel.getRoot();
		if (administrateur != root && administrateur.getLigue() != this)
			throw new DroitsInsuffisants();
		this.administrateur = administrateur;
		gestionPersonnel.update(this);
	}

	/**
	 * Retourne les employés de la ligue.
	 * @return les employés de la ligue dans l'ordre alphabétique.
	 */
	
	public SortedSet<Employe> getEmployes()
	{
		return Collections.unmodifiableSortedSet(employes);
	}

	/**
	 * Insère l'employé en base de donnée et l'ajoute localement. Cette méthode
	 * est le seul moyen de créer un employé.
	 * @param nom le nom de l'employé.
	 * @param prenom le prénom de l'employé.
	 * @param mail l'adresse mail de l'employé.
	 * @param password le password de l'employé.
	 * @param dateArrive la date d'arrivé.
	 * @param dateDepart la date de départ.
	 * @return l'employé créé.
	 * @throws SauvegardeImpossible 
	 */

	public Employe addEmploye(String nom, String prenom, String mail, String password, LocalDate dateArrive, LocalDate dateDepart) throws SauvegardeImpossible
	{
		if (dateArrive != null && dateDepart != null && dateArrive.isAfter(dateDepart)) {
			throw new IllegalArgumentException("La date d'arrivée ne peut pas être après la date de départ.");
		}

		Employe employe = new Employe(this.gestionPersonnel, this, false, nom, prenom, mail, password, dateArrive, dateDepart);
		employes.add(employe);
		return employe;
	}

	/**
	 * Ajoute un employé localement
	 * @param id de l'employé
	 * @param nom de l'employé
	 * @param prenom de l'employé
	 * @param mail de l'employé
	 * @param password de l'employé
	 * @param dateArrive de l'employé
	 * @param dateDepart de l'employé
	 * @return l'employé créé
	 * @throws SauvegardeImpossible 
	 */
	public Employe addEmploye(int id, Ligue ligue, String nom, String prenom, String mail, String password, LocalDate dateArrive, LocalDate dateDepart) throws SauvegardeImpossible
	{
		Employe employe = new Employe(gestionPersonnel, id, false,ligue, nom, prenom, mail, password, dateArrive, dateDepart);
		employes.add(employe);
		return employe;
	}

	/**
	 * Supprime un employé dans la ligue.
	 * @param employe le nom de l'employe
	 * @throws SauvegardeImpossible
	 */

	void remove(Employe employe) throws SauvegardeImpossible
	{
		employes.remove(employe);
		gestionPersonnel.remove(employe);
	}
	
	/**
	 * Supprime la ligue, entraîne la suppression de tous les employés
	 * de la ligue.
	 */
	
	public void remove() throws SauvegardeImpossible
	{
		gestionPersonnel.remove(this);
	}
	

	@Override
	public int compareTo(Ligue autre)
	{
		return getNom().compareTo(autre.getNom());
	}
	
	public int getId()
	{
		return this.id;
	}
	
	@Override
	public String toString()
	{
		return nom;
	}
}
package personnel;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Gestion du personnel. Un seul objet de cette classe existe.
 * Il n'est pas possible d'instancier directement cette classe,
 * la méthode {@link #getGestionPersonnel getGestionPersonnel}
 * le fait automatiquement et retourne toujours le même objet.
 * Dans le cas où {@link #sauvegarder()} a été appelé lors
 * d'une exécution précédente, c'est l'objet sauvegardé qui est
 * retourné.
 */

public class GestionPersonnel implements Serializable
{
	private static final long serialVersionUID = -105283113987886425L;
	private static GestionPersonnel gestionPersonnel = null;
	private SortedSet<Ligue> ligues;
	private Employe root;
	private SortedSet<Employe> employes;
	public final static int SERIALIZATION = 1, JDBC = 2,
			TYPE_PASSERELLE = JDBC; 
	private static Passerelle passerelle = TYPE_PASSERELLE == JDBC ? new jdbc.JDBC() : new serialisation.Serialization();
	
	/**
	 * Retourne l'unique instance de cette classe.
	 * Crée cet objet s'il n'existe déjà.
	 * @return l'unique objet de type {@link GestionPersonnel}.
	 * @throws SauvegardeImpossible 
	 */
	
	public static GestionPersonnel getGestionPersonnel() throws SauvegardeImpossible
	{
		if (gestionPersonnel == null)
		{
			gestionPersonnel = passerelle.getGestionPersonnel();
			if (gestionPersonnel == null)
				gestionPersonnel = new GestionPersonnel();
		}
		return gestionPersonnel;
	}

	public GestionPersonnel() throws SauvegardeImpossible
	{
		if (gestionPersonnel != null)
			throw new RuntimeException("Vous ne pouvez créer qu'une seuls instance de cet objet.");
		ligues = new TreeSet<>();
		gestionPersonnel = this;
	}
	
	public void sauvegarder() throws SauvegardeImpossible
	{
		passerelle.sauvegarderGestionPersonnel(this);
	}
	
	/**
	 * Retourne la ligue dont administrateur est l'administrateur,
	 * null s'il n'est pas un administrateur.
	 * @param administrateur l'administrateur de la ligue recherchée.
	 * @return la ligue dont administrateur est l'administrateur.
	 */
	
	public Ligue getLigue(Employe administrateur)
	{
		if (administrateur.estAdmin(administrateur.getLigue()))
			return administrateur.getLigue();
		else
			return null;
	}

	/**
	 * Retourne toutes les ligues enregistrées.
	 * @return toutes les ligues enregistrées.
	 */
	
	public SortedSet<Ligue> getLigues()
	{
		return Collections.unmodifiableSortedSet(ligues);
	}

	/**
	 * Insère la ligue en base de donnée et l'ajoute localement
	 * @param nom de la ligue
	 * @return ligue créée
	 * @throws SauvegardeImpossible
	 */
	public Ligue addLigue(String nom) throws SauvegardeImpossible
	{
		Ligue ligue = new Ligue(this, nom);
		ligues.add(ligue);
		return ligue;
	}
	
	/**
	 * Ajoute la ligue localement
	 * @param id de la ligue
	 * @param nom de la ligue
	 * @return ligue créée
	 */
	public Ligue addLigue(int id, String nom)
	{
		Ligue ligue = new Ligue(this, id, nom);
		ligues.add(ligue);
		return ligue;
	}

	/**
	 * Insère la ligue en base de donnée
	 * @param ligue
	 * @return
	 * @throws SauvegardeImpossible
	 */
	int insert(Ligue ligue) throws SauvegardeImpossible
	{
		return passerelle.insert(ligue);
	}

	/**
	 * Supprime la ligue localement et en base de donnée
	 * @param ligue à supprimer
	 * @throws SauvegardeImpossible
	 */
	void remove(Ligue ligue) throws SauvegardeImpossible
	{
		ligues.remove(ligue);
		passerelle.remove(ligue);
	}

	/**
	 * Met à jour la base de donnée
	 * @param ligue
	 * @throws SauvegardeImpossible
	 */
	void update(Ligue ligue) throws SauvegardeImpossible
	{
		passerelle.update(ligue);
	}
	void update(Employe employe) throws SauvegardeImpossible
	{
		passerelle.update(employe);
	}
	
	/**
	 * Insère un employé en base de donnée
	 * @param employe à ajouter en base de donnée
	 * @return l'employé ajouté
	 * @throws SauvegardeImpossible
	 */
	int insert(Employe employe) throws SauvegardeImpossible
	{
		return passerelle.insert(employe);
	}


	/**
	 * Supprimer l'employé en base de donnée
	 * @param employe
	 * @throws SauvegardeImpossible
	 */
	void remove(Employe employe) throws SauvegardeImpossible
	{
		passerelle.remove(employe);
	}
	
	/**
	 * Retourne le root (super-utilisateur).
	 * @return le root.
	 */
	
	public Employe getRoot()
	{
		return root;
	}

	/**
	 * Ajoute le root localement et en base de donnée
	 * @param gestionPersonnel
	 * @throws SauvegardeImpossible
	 */
	public void addRoot(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible {
		LocalDate dateArrive = LocalDate.now();
		this.root = new Employe(gestionPersonnel, null, true ,"root", "", "", "toor", dateArrive, null);
	}

	/**
	 * Ajoute le root localement
	 * @param gestionPersonnel
	 * @param id du root
	 * @param nom du root
	 * @param prenom du root
	 * @param mail du root
	 * @param password du root
	 * @param dateArrive du root
	 * @param dateDepart du root
	 * @throws SauvegardeImpossible
	 */
	public void addRoot(GestionPersonnel gestionPersonnel, int id, String nom, String prenom, String mail, String password, LocalDate dateArrive, LocalDate dateDepart) throws SauvegardeImpossible {
		this.root = new Employe(gestionPersonnel, id , true, null , nom, prenom, mail, password, dateArrive, dateDepart);
	}
}
package personnel;

public interface Passerelle
{
	public GestionPersonnel getGestionPersonnel();
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel)  throws SauvegardeImpossible;
	public int insertLigue(Ligue ligue) throws SauvegardeImpossible;
	public int insertEmploye(Employe employe) throws SauvegardeImpossible;
	public void removeLigue(Ligue ligue) throws SauvegardeImpossible;
	public void remove(Employe employe, Ligue ligue) throws SauvegardeImpossible;
}

package personnel;

/**
 * Levée si l'on tente de supprimer le super-utilisateur.
 */

public class ImpossibleDeSupprimerRoot extends RuntimeException
{
	private static final long serialVersionUID = 6850643427556906205L;
}

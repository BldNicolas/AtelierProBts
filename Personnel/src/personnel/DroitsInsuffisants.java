package personnel;

/**
 * Levée si un administrateur tente d'effectuer une opération sur une 
 * autre ligue que la sienne.
 */

public class DroitsInsuffisants extends RuntimeException
{
	private static final long serialVersionUID = -7047171662944223002L;	
}

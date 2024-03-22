package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

import personnel.*;

public class JDBC implements Passerelle
{
	Connection connection;

	public JDBC()
	{
		try
		{
			Class.forName(Credentials.getDriverClassName());
			connection = DriverManager.getConnection(Credentials.getUrl(), Credentials.getUser(), Credentials.getPassword());
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Pilote JDBC non installé.");
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	@Override
	public GestionPersonnel getGestionPersonnel()
	{
		GestionPersonnel gestionPersonnel = new GestionPersonnel();
		getRoot(gestionPersonnel);
		getLigue(gestionPersonnel);
		return gestionPersonnel;
	}

	@Override
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible 
	{
		close();
	}
	
	public void close() throws SauvegardeImpossible
	{
		try
		{
			if (connection != null)
				connection.close();
		}
		catch (SQLException e)
		{
			throw new SauvegardeImpossible(e);
		}
	}
	
	@Override
	public int insert(Ligue ligue) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into ligue (nom) values(?)", Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, ligue.getNom());
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

	public void remove(Ligue ligue) throws SauvegardeImpossible {
		try {
			PreparedStatement instruction = connection.prepareStatement("DELETE FROM ligue WHERE nom=?");
			instruction.setString(1, ligue.getNom());
			instruction.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

	public int insert(Employe employe) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement(
			"INSERT INTO employe (id_ligue, droit, prenom, nom, password, mail, date_arrive, date_depart) VALUES ((SELECT l.id_ligue FROM ligue l WHERE l.nom = ?), ?, ?, ?, ?, ?, ?, ?);",
			Statement.RETURN_GENERATED_KEYS
			);
			instruction.setString(1, employe.getLigue().getNom());
			instruction.setObject(2, false);
			instruction.setString(3, employe.getPrenom());
			instruction.setString(4, employe.getNom());
			instruction.setString(5, employe.getPassword());
			instruction.setString(6, employe.getMail());
			instruction.setDate(7, java.sql.Date.valueOf(employe.getDateArrive()));
			instruction.setDate(8, java.sql.Date.valueOf(employe.getDateDepart()));
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

	public void remove(Employe employe) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction = connection.prepareStatement("DELETE FROM employe WHERE id_employe = ?");
			instruction.setInt(1, employe.id);
			instruction.executeUpdate();
		} catch (SQLException exception)
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

	/**
	 * Récupère les ligues et les employés stocké en base de donnée et les stocke localement dans
	 * l'objet gestionPersonnel
	 * @param gestionPersonnel
	 * @return gestionPersonnel
	 */
	public GestionPersonnel getLigue(GestionPersonnel gestionPersonnel)
	{
		try
		{
			Ligue ligue = null;
			String requete = "select * from ligue";
			Statement instruction = connection.createStatement();
			ResultSet ligues = instruction.executeQuery(requete);
			while (ligues.next())
			{
				ligue = gestionPersonnel.addLigue(ligues.getInt(1), ligues.getString(3));
				getEmploye(gestionPersonnel, ligue);
			}
		} catch (SQLException e)
		{
			System.out.println(e);
		}
		return gestionPersonnel;
	}

	/**
	 * Récupère les employés stocké en base de donnée et les stocke localement dans l'objet
	 * gestionPersonnel
	 * @param gestionPersonnel
	 * @param ligue dans laquelle on doit chercher l'employé
	 * @return gestionPersonnel
	 */
	public GestionPersonnel getEmploye(GestionPersonnel gestionPersonnel, Ligue ligue)
	{
		try
		{
			String requete = "SELECT * FROM employe WHERE id_ligue = (SELECT ligue.id_ligue FROM ligue WHERE ligue.nom = \"" + ligue.getNom() + "\");";
			Statement instruction = connection.createStatement();
			ResultSet employes = instruction.executeQuery(requete);
			while (employes.next()) {
				ligue.addEmploye(employes.getInt(1), ligue, employes.getString(4), employes.getString(5), employes.getString(7), employes.getString(6), sqlDateToLocalDate(employes.getDate(8)), sqlDateToLocalDate(employes.getDate(9)));
			}
		} catch (SQLException e)
		{
			System.out.println(e);
		}
		return gestionPersonnel;
	}

	/**
	 * Si non présent, insert le root dans la BDD, puis l'ajoute localement
	 * @param gestionPersonnel
	 * @return
	 */
	public GestionPersonnel getRoot(GestionPersonnel gestionPersonnel)
	{
		try
		{
			String requete = "SELECT * FROM employe WHERE id_ligue IS NULL;";
			Statement instruction = connection.createStatement();
			ResultSet root = instruction.executeQuery(requete);
			if (!root.next())
			{
				requete = "INSERT INTO employe (id_employe, droit, nom, prenom, password, mail, date_arrive) VALUES (1, TRUE, \"root\", \"\", \"toor\", \"\", \"0001-01-01\");";
				instruction.executeUpdate(requete);
			}
			gestionPersonnel.addRoot();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return gestionPersonnel;
	}

	/**
	 * Convertit une date de base de donnée vers date java
	 * @param dateFromDB format SQL
	 * @return date format java
	 */
	public LocalDate sqlDateToLocalDate(Date dateFromDB)
	{
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dateFromDB);
		LocalDate localDate = LocalDate.of(
			calendar.get(Calendar.YEAR),
			calendar.get(Calendar.MONTH) + 1,
			calendar.get(Calendar.DAY_OF_MONTH)
		);
		return localDate;
	}
}

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
	public GestionPersonnel getGestionPersonnel() throws SauvegardeImpossible 
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
			PreparedStatement instruction = connection.prepareStatement("DELETE FROM employe WHERE id_ligue=?;");
			instruction.setInt(1, ligue.id);
			instruction.executeUpdate();
			instruction = connection.prepareStatement("DELETE FROM ligue WHERE id_ligue=?");
			instruction.setInt(1, ligue.id);
			instruction.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

	/**
	 * Récupère les ligues et les employés stocké en base de donnée et les stocke localement dans
	 * l'objet gestionPersonnel
	 * @param gestionPersonnel
	 * @return gestionPersonnel
	 * @throws SauvegardeImpossible
	 */
	public GestionPersonnel getLigue(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible
	{
		try
		{
			Ligue ligue = null;
			String requete = "SELECT * FROM ligue";
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
	 * Met à jour la ligue en base de donnée
	 * @param ligue
	 * @throws SauvegardeImpossible
	 */
	public void update(Ligue ligue) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction = connection.prepareStatement(
				"UPDATE ligue SET id_admin = ?, nom = ? WHERE id_ligue = ?"
				);
			instruction.setInt(1, ligue.getAdministrateur().getId());
			instruction.setString(2, ligue.getNom());
			instruction.setInt(3, ligue.id);
			instruction.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

	/**
	 * Insère l'employé dans la base de donnée
	 * @param employe à ajouter dans la base de donnée
	 * @return id de l'employé créé
	 */
	public int insert(Employe employe) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement(
			"INSERT INTO employe (id_ligue, droit, nom, prenom, mail, password, date_arrive, date_depart) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
			Statement.RETURN_GENERATED_KEYS
			);
			instruction.setInt(1, employe.getLigue() != null ? employe.getLigue().getId() : null);
			instruction.setObject(2, employe.getDroit());
			instruction.setString(3, employe.getNom());
			instruction.setString(4, employe.getPrenom() != null ? employe.getPrenom() : null);
			instruction.setString(5, employe.getMail() != null ? employe.getMail() : null);
			instruction.setString(6, employe.getPassword());
			instruction.setDate(7, java.sql.Date.valueOf(employe.getDateArrive()));
			instruction.setDate(8, employe.getDateDepart() != null ? java.sql.Date.valueOf(employe.getDateDepart()) : null);
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

	/**
	 * Met à jour l'employé en base de donnée
	 * @param employe mis à jour dans la base de donnée
	 * @throws SauvegardeImpossible
	 */
	public void update(Employe employe) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction = connection.prepareStatement(
				"UPDATE employe SET droit = ?, nom = ?, prenom = ?, mail = ?, password = ?, date_arrive = ?, date_depart = ? WHERE id_employe = ?"
				);
			instruction.setObject(1, employe.getDroit());
			instruction.setString(2, employe.getNom());
			instruction.setString(3, employe.getPrenom());
			instruction.setString(4, employe.getMail());
			instruction.setString(5, employe.getPassword());
			instruction.setDate(6, employe.getDateArrive()!= null ? java.sql.Date.valueOf(employe.getDateArrive()) : null);
			instruction.setDate(7, employe.getDateDepart() != null ? java.sql.Date.valueOf(employe.getDateDepart()) : null);
			instruction.setInt(8, employe.getId());
			instruction.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

	/**
	 * Supprime un employé de la base de donnée
	 * @param employe supprimé de la base de donnée
	 * @throws SauvegardeImpossible
	 */
	public void remove(Employe employe) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction = connection.prepareStatement("DELETE FROM employe WHERE id_employe = ?");
			instruction.setInt(1, employe.getId());
			instruction.executeUpdate();
		} catch (SQLException exception)
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

	/**
	 * Récupère les employés stocké en base de donnée et les stocke localement dans l'objet
	 * gestionPersonnel
	 * @param gestionPersonnel
	 * @param ligue dans laquelle on doit chercher l'employé
	 * @return gestionPersonnel
	 * @throws SauvegardeImpossible
	 */
	public GestionPersonnel getEmploye(GestionPersonnel gestionPersonnel, Ligue ligue) throws SauvegardeImpossible
	{
		try
		{
			String requete = "SELECT * FROM employe WHERE id_ligue = (SELECT ligue.id_ligue FROM ligue WHERE ligue.nom = \"" + ligue.getNom() + "\");";
			Statement instruction = connection.createStatement();
			ResultSet employes = instruction.executeQuery(requete);
			while (employes.next()) {
				ligue.addEmploye(employes.getInt(1), ligue, employes.getString(4), employes.getString(5), employes.getString(6), employes.getString(7), sqlDateToLocalDate(employes.getDate(8)), sqlDateToLocalDate(employes.getDate(9)));
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
			if (root.next())
				gestionPersonnel.addRoot(gestionPersonnel,root.getInt(1), root.getString(4), root.getString(5), root.getString(6), root.getString(7), sqlDateToLocalDate(root.getDate(8)), sqlDateToLocalDate(root.getDate(9)));
			else
				gestionPersonnel.addRoot(gestionPersonnel);
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
		if (dateFromDB == null)
			return null;
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

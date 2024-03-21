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
		//TODO getRoot(gestionPersonnel);
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
			if (employe.getLigue() == null)
			{
				instruction = connection.prepareStatement(
				"INSERT INTO employe (droit, prenom, nom, password, mail, date_arrive, date_depart) VALUES(?, ?, ?, ?, ?, ?, ?);",
				Statement.RETURN_GENERATED_KEYS
				);
				instruction.setObject(1, true);
			} else
			{
				instruction = connection.prepareStatement(
				"INSERT INTO employe (id_ligue, droit, prenom, nom, password, mail, date_arrive, date_depart) SELECT l.id_ligue, ?, ?, ?, ?, ?, ?, ? FROM ligue l WHERE l.nom = ?;",
				Statement.RETURN_GENERATED_KEYS
				);
				instruction.setObject(1, false);
				instruction.setString(8, employe.getLigue().getNom());
			}
			instruction.setString(2, employe.getPrenom());
			instruction.setString(3, employe.getNom());
			instruction.setString(4, employe.getPassword());
			instruction.setString(5, employe.getMail());
			instruction.setDate(6, java.sql.Date.valueOf(employe.getDateArrive()));
			instruction.setDate(7, java.sql.Date.valueOf(employe.getDateDepart()));
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

	//TODO: enlever le param Ligue ligue
	public void remove(Employe employe, Ligue ligue) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction = connection.prepareStatement("DELETE FROM employe WHERE nom = ? AND prenom = ? AND id_ligue = (SELECT id_ligue FROM ligue WHERE ligue.nom = ?)");
			instruction.setString(1, employe.getNom());
			instruction.setString(2, employe.getPrenom());
			instruction.setString(3, employe.getLigue().getNom());
			instruction.executeUpdate();
		} catch (SQLException exception)
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

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

	public GestionPersonnel getEmploye(GestionPersonnel gestionPersonnel, Ligue ligue)
	{
		try
		{
			String requete = "SELECT * FROM employe WHERE id_ligue = (SELECT ligue.id_ligue FROM ligue WHERE ligue.nom = \"" + ligue.getNom() + "\");";
			Statement instruction = connection.createStatement();
			ResultSet employes = instruction.executeQuery(requete);
			while (employes.next()) {
				ligue.addEmploye(employes.getInt(1),employes.getString(4), employes.getString(5), employes.getString(7), employes.getString(6), sqlDateToLocalDate(employes.getDate(8)), sqlDateToLocalDate(employes.getDate(9)));
			}
		} catch (SQLException e)
		{
			System.out.println(e);
		}
		return gestionPersonnel;
	}

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

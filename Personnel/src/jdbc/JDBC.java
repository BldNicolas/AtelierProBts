package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
		try 
		{
			String requete = "select * from ligue";
			Statement instruction = connection.createStatement();
			ResultSet ligues = instruction.executeQuery(requete);
			while (ligues.next())
				gestionPersonnel.addLigue(ligues.getInt(1), ligues.getString(3));
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
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
	public int insertLigue(Ligue ligue) throws SauvegardeImpossible
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

	public void removeLigue(Ligue ligue) throws SauvegardeImpossible {
		try {
			PreparedStatement instruction = connection.prepareStatement("DELETE FROM ligue WHERE nom=?");
			instruction.setString(1, ligue.getNom());
			instruction.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

	public int insertEmploye(Employe employe) throws SauvegardeImpossible
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

	public void remove(Employe employe) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction = connection.prepareStatement("DELETE FROM employe WHERE nom = ? AND prenom = ?;");
			instruction.setString(1, employe.getNom());
			instruction.setString(2, employe.getPrenom());
			instruction.executeUpdate();
		} catch (SQLException exception)
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}
}

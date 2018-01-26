package com.kuryshee.safehome.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * The abstraction of interactions with the database.
 * @author Ekaterina Kurysheva.
 */
public interface DatabaseAccessInterface {
	
	/**
	 * Adds user credentials to the database.
	 * @param login
	 * @param password
	 */
	public void addUserCredentials(String login, String password) throws SQLException;
	
	/**
	 * Adds user token for communication authorization with the token.
	 * @param login
	 * @param token
	 */
	public void addUserToken(String login, String token) throws SQLException;
	
	/**
	 * Deletes user credentials and all data connected to the user from the database.
	 * @param login
	 */
	public void deleteUserCredentials(String login) throws SQLException;
	
	/**
	 * Validates user's login and password.
	 * @param login
	 * @param password
	 * @return true, if data are valid, false otherwise.
	 */
	public boolean validateUserCredentials(String login, String password) throws SQLException;
	
	/**
	 * Validates user's token for communication.
	 * @param login
	 * @param token
	 * @return true, if token is valid, false otherwise.
	 */
	public boolean validateUserToken(String login, String token) throws SQLException;
	
	/**
	 * Gets the user, which is identified be the token.
	 * @param token
	 * @return user name or empty string, if the user does not exist.
	 */
	public String getUserByToken(String token) throws SQLException;
	
}

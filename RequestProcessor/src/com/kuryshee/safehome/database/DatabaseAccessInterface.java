package com.kuryshee.safehome.database;

import java.io.IOException;
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
	
	/**
	 * Returns list of most recent actions on Raspberry Pi registered in database.
	 * @param rpiId
	 * @param numberOfActions
	 * @return byte array of data
	 * @throws SQLException
	 * @throws IOException when converting the database output to byte array.
	 */
	public String getLatestRpiActionTime(String rpiId) throws SQLException, IOException;
	
	/**
	 * Returns list of most recent actions on Raspberry Pi registered in database after the given date.
	 * @param rpiId
	 * @param date
	 * @param numberOfActions
	 * @return byte array of data
	 * @throws SQLException
	 * @throws IOException when converting the database output to byte array.
	 */
	public byte[] getRpiActionsAfterDate(String rpiId, String date, int numberOfActions) throws SQLException, IOException;
	
	/**
	 * Retrieves information about user and Rpi relation.
	 * @param user
	 * @return Rpi ID for the given user
	 * @throws SQLException
	 */
	public String getRpiByUser(String user) throws SQLException;
	
}

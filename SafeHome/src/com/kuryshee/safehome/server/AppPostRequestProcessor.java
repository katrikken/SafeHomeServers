package com.kuryshee.safehome.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;

import com.kuryshee.safehome.appcommunicationconsts.AppCommunicationConsts;
import com.kuryshee.safehome.database.DatabaseAccessInterface;

public class AppPostRequestProcessor{
	
	private DatabaseAccessInterface database;
	
	public AppPostRequestProcessor() {
		try {
			database = new DatabaseAccessImpl();
		} catch (SQLException ex) {
			Logger.getLogger(AppPostRequestProcessor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
	
	/**
	 * Simple token creation.
	 * @param login
	 * @param password
	 * @return token for user authorization.
	 */
	private String createToken(String login, String password) {
		
		return login + System.currentTimeMillis();
	}

	/**
	 * Creates a token for user authorization upon logging in.
	 * Writes the token to the output stream.
	 * @param output is the HTTP response output stream.
	 * @param login
	 * @param password
	 */
	public void getToken(ServletOutputStream output, String login, String password) {
		try {
			Boolean userExists = database.validateUserCredentials(login, password);
			
			if(userExists) {
				String token = createToken(login, password);
				database.addUserToken(login, token);
				output.println(token);
			}
			else
				output.println(AppCommunicationConsts.INVALID_USER_ERROR);
		}
		catch(Exception e) {
			Logger.getLogger(AppPostRequestProcessor.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			try {
				output.println(AppCommunicationConsts.REQUEST_PROCESS_ERROR);
			}
			catch(IOException ex) {
				Logger.getLogger(AppPostRequestProcessor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
	}
	
	/**
	 * Validates that the token is valid. 
	 * Writes {@link AppCommunicationConsts.TRUE} or {@link AppCommunicationConsts.FALSE} in the output.
	 * @param output is the HTTP response output stream
	 * @param token
	 */
	public void validateToken(ServletOutputStream output, String token) {
		try {
			String user = database.getUserByToken(token);
			
			if(user.length() > 0)
				output.println(AppCommunicationConsts.TRUE);
			else
				output.println(AppCommunicationConsts.FALSE);
		}
		catch(Exception e) {
			Logger.getLogger(AppPostRequestProcessor.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			try {
				output.println(AppCommunicationConsts.REQUEST_PROCESS_ERROR);
			}
			catch(IOException ex) {
				Logger.getLogger(AppPostRequestProcessor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
	}
}

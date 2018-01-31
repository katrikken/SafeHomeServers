package com.kuryshee.safehome.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import oracle.jdbc.pool.OracleDataSource;

import com.kuryshee.safehome.appcommunicationconsts.AppCommunicationConsts;
import com.kuryshee.safehome.database.DatabaseAccessInterface;
import com.kuryshee.safehome.requestprocessorinterface.RequestProcessor;

import javax.sql.DataSource;

import java.util.logging.Logger;


public class AppGetRequestProcessor implements RequestProcessor{
	
	private DatabaseAccessInterface database;
	
	private String user;
	
	public AppGetRequestProcessor(InitialContext context) {
		try {
			Context envContext  = (Context) context.lookup("java:/comp/env");
			database = new DatabaseAccessImpl((DataSource)  envContext.lookup("jdbc/xe"));
		} catch (Exception ex) {
			Logger.getLogger(AppGetRequestProcessor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
	
	/**
	 * Gets the user from the database.
	 * @param token
	 * @return true, if the user is found, false otherwise.
	 * @throws SQLException 
	 */
	private boolean getUserByToken(String token) throws SQLException {
		user = database.getUserByToken(token);
		
		if(user.length() > 0)
			return true;
		
		return false;
	}
	
	private void getDataFromDatabase(ServletOutputStream output, String action) throws IOException {
		output.println("data"); //todo
	}
	
	@Override
	public void process(ServletOutputStream output, String... parameters) {
		try {
			if(parameters[1] != null) {
				Map<String, String> params = parseQuery(parameters[1]);
				String action = params.get(AppCommunicationConsts.ACTION);
				
				if(action != null && action.equals(AppCommunicationConsts.PING)) {
					output.println(AppCommunicationConsts.PONG);
				}
				else if (action != null){
					if(parameters[0] != null && getUserByToken(parameters[0])) {
						//todo action process
					}
					else {
						output.println(AppCommunicationConsts.REQUEST_FORMAT_ERROR);
					}
				}
				else {
					output.println(AppCommunicationConsts.REQUEST_FORMAT_ERROR);
				}
			}
		}
		catch(Exception e) {
			Logger.getLogger(AppGetRequestProcessor.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			try {
				output.println(AppCommunicationConsts.ERROR);
			}
			catch(IOException ex) {
				Logger.getLogger(AppPostRequestProcessor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
	}
}

package com.kuryshee.safehome.server;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.ServletOutputStream;
import com.kuryshee.safehome.appcommunicationconsts.AppCommunicationConsts;
import com.kuryshee.safehome.database.DatabaseAccessInterface;
import com.kuryshee.safehome.requestprocessorinterface.RequestProcessor;
import com.sun.istack.internal.logging.Logger;


public class AppGetRequestProcessor implements RequestProcessor{
	
	private DatabaseAccessInterface database = new MockDatabaseAccess();
	
	private String user;
	
	/**
	 * Gets the user from the database.
	 * @param token
	 * @return true, if the user is found, false otherwise.
	 */
	private boolean getUserByToken(String token) {
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
			Logger.getLogger(AppGetRequestProcessor.class).log(Level.SEVERE, e.getMessage(), e);
		}
	}
}

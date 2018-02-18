package com.kuryshee.safehome.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;

import com.kuryshee.safehome.database.DatabaseAccessInterface;
import com.kuryshee.safehome.httprequestsender.AnswerConstants;
import com.kuryshee.safehome.requestprocessorinterface.RequestProcessor;
import com.kuryshee.safehome.rpicommunicationconsts.RpiCommunicationConsts;

/**
 * Processes HTTP requests from Raspberry Pi.
 * @author Ekaterina Kurysheva.
 *
 */
public class RpiHttpRequestProcessor implements RequestProcessor{
	
	private String rpiId;
	private DatabaseAccessInterface database;
	
	public RpiHttpRequestProcessor(InitialContext context) {
		try {
			Context envContext  = (Context) context.lookup("java:/comp/env");
			database = new DatabaseAccessImpl((DataSource)  envContext.lookup("jdbc/xe"));
		} catch (Exception ex) {
			Logger.getLogger( RpiHttpRequestProcessor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
	
	public void closeConnection() {
		try {
			database.closeConnection();
		}
		catch(SQLException ex) {
			Logger.getLogger(RpiHttpRequestProcessor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
	
	private boolean checkRpi(String rpiId) {
		//TODO
		return true;
	}
	
	/**
	 * Registers user for the given Raspberry Pi to the database.
	 * @param output
	 * @param rpiId
	 * @param login
	 * @param password
	 */
	public void registerUser(ServletOutputStream output, String rpiId, String login, String password) {
		try {
			if ( checkRpi(rpiId)) { //Rpi is identified
				database.addUserCredentials(login, password);
				database.addRpiUserRelation(rpiId, login);
				
				output.println(AnswerConstants.OK_ANSWER);
			}
			else {
				output.println(AnswerConstants.ERROR_ANSWER);
			}
		}
		catch(Exception e) {
			Logger.getLogger(RpiHttpRequestProcessor.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			
			try {
				output.println(AnswerConstants.ERROR_ANSWER);
			}
			catch(IOException ex) {
				Logger.getLogger(RpiHttpRequestProcessor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
	}
	
	public void registerAction(ServletOutputStream output, String rpiId, String info, String time, String level) {
		try {
			if ( checkRpi(rpiId)) { //Rpi is identified
				database.addRpiAction(rpiId, time, info, level);
				
				output.println(AnswerConstants.OK_ANSWER);
			}
			else {
				output.println(AnswerConstants.ERROR_ANSWER);
			}
		}
		catch(Exception e) {
			Logger.getLogger(RpiHttpRequestProcessor.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			
			try {
				output.println(AnswerConstants.ERROR_ANSWER);
			}
			catch(IOException ex) {
				Logger.getLogger(RpiHttpRequestProcessor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
	}
	
	public void savePhoto(ServletOutputStream output, String rpiId, String time, String name, byte[] photo) {
		try {
			if ( checkRpi(rpiId)) { //Rpi is identified
				database.addRpiPhoto(rpiId, time, name, photo);
				
				output.println(AnswerConstants.OK_ANSWER);
			}
			else {
				output.println(AnswerConstants.ERROR_ANSWER);
			}
		}
		catch(Exception e) {
			Logger.getLogger(RpiHttpRequestProcessor.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			
			try {
				output.println(AnswerConstants.ERROR_ANSWER);
			}
			catch(IOException ex) {
				Logger.getLogger(RpiHttpRequestProcessor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
	}
	
	@Override
	public void process(ServletOutputStream output, String... parameters) {
		try {
			if(parameters != null && parameters.length >= 2) {
				if(checkRpi(parameters[0])) { //check existence of such Raspberry Pi in the database
					Map<String, String> params = parseQuery(parameters[1]); //getting query parameters
					
					String action = params.get(RpiCommunicationConsts.ACTION);
					switch(action) {
						case RpiCommunicationConsts.GET_TASK: //TODO
							break;
						default: output.println(AnswerConstants.ERROR_ANSWER);
							break;
					}
				}
			}
		}
		catch(Exception e) {
			Logger.getLogger(RpiHttpRequestProcessor.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			try {
				output.println(AnswerConstants.ERROR_ANSWER);
			}
			catch(IOException ex) {
				Logger.getLogger(RpiHttpRequestProcessor.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
		finally {
			closeConnection();
		}
	}

}

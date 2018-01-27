package com.kuryshee.safehome.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.kuryshee.safehome.database.DatabaseAccessInterface;

import oracle.jdbc.pool.OracleDataSource;

/**
 * This class implements the mock database access.
 * It writes information to the files instead of writing to the actual database.
 * @author Ekaterina Kurysheva
 */
public class DatabaseAccessImpl implements DatabaseAccessInterface{
	
	OracleDataSource ds;
	Connection conn;
	
	public DatabaseAccessImpl() throws SQLException {
		ds = new OracleDataSource();
		ds.setURL("jdbc:oracle:thin:katrikken/952009@localhost:1521:XE");
		conn = ds.getConnection("katrikken", "952009");
	}
	
	@Override
	public void addUserCredentials(String login, String password) throws SQLException {
		CallableStatement callStmt = null;
		try {
			callStmt = conn.prepareCall("{add_user_credentials(?, ?)}");
	        callStmt.setString(1, login);
	        callStmt.setString(2, password);
	        callStmt.execute();
        } 
        finally {
        	try {
	        	callStmt.close();
	        	conn.close();
        	}
        	catch(Exception e) {
        		Logger.getLogger(DatabaseAccessImpl.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        	}
        }
	}


	@Override
	public void addUserToken(String login, String token) throws SQLException {
		CallableStatement callStmt = null;
		try {
			callStmt = conn.prepareCall("{add_user_token(?, ?)}");
	        callStmt.setString(1, login);
	        callStmt.setString(2, token);
	        callStmt.execute();
        } 
        finally {
        	try {
	        	callStmt.close();
	        	conn.close();
        	}
        	catch(Exception e) {
        		Logger.getLogger(DatabaseAccessImpl.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        	}
        }
	}


	@Override
	public void deleteUserCredentials(String login) throws SQLException {
		CallableStatement callStmt = null;
		try {
			callStmt = conn.prepareCall("{delete_user_credentials(?)}");
	        callStmt.setString(1, login);
	        callStmt.execute();
        } 
        finally {
        	try {
	        	callStmt.close();
	        	conn.close();
        	}
        	catch(Exception e) {
        		Logger.getLogger(DatabaseAccessImpl.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        	}
        }
	}
	
	@Override
	public String getUserByToken(String token) throws SQLException {
		CallableStatement callStmt = null;
		String user;
		try {
			callStmt = conn.prepareCall("{? = call get_user_by_token(?)}");
			callStmt.registerOutParameter(1, java.sql.Types.VARCHAR);
	        callStmt.setString(2, token);
	        callStmt.execute();
	        
	        user = callStmt.getString(1);
        } 
        finally {
        	try {
	        	callStmt.close();
	        	conn.close();
        	}
        	catch(Exception e) {
        		Logger.getLogger(DatabaseAccessImpl.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        	}
        }
		
		if(user != null && user.length() > 0) {
			return user;
		}
		
		return "";
	}


	@Override
	public boolean validateUserCredentials(String login, String password) throws SQLException {
		CallableStatement callStmt = null;
		int user = -1;
		try {
			callStmt = conn.prepareCall("{? = call validate_user_credentials(?,?)}");
			callStmt.registerOutParameter(1, java.sql.Types.NUMERIC);
	        callStmt.setString(2, login);
	        callStmt.setString(3, password);
	        callStmt.execute();
	        
	        user = callStmt.getInt(1);
        } 
        finally {
        	try {
	        	callStmt.close();
	        	conn.close();
        	}
        	catch(Exception e) {
        		Logger.getLogger(DatabaseAccessImpl.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        	}
        }
		
		if(user != -1) {
			if (user == 1) return true;
		}
		
		return false;
	}


	@Override
	public boolean validateUserToken(String login, String token) throws SQLException {
		CallableStatement callStmt = null;
		int user = -1;
		try {
			callStmt = conn.prepareCall("{? = call validate_user_token(?,?)}");
			callStmt.registerOutParameter(1, java.sql.Types.NUMERIC);
	        callStmt.setString(2, login);
	        callStmt.setString(3, token);
	        callStmt.execute();
	        
	        user = callStmt.getInt(1);
        } 
        finally {
        	try {
	        	callStmt.close();
	        	conn.close();
        	}
        	catch(Exception e) {
        		Logger.getLogger(DatabaseAccessImpl.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        	}
        }
		
		if(user != -1) {
			if (user == 1) return true;
		}
		
		return false;
	}
	
	public static String DB_PATH = "Database\\";
	
	public static final String LIST = "list";


	public Boolean connect(String url, Map<String, String> properties) {
		return true;
	}


	public Boolean insert(String table, Map<String, String> values) {
		String id = values.get(SafeHomeServer.RPI_PARAM);
		String command = values.get(SafeHomeServer.COMMAND_PARAM);
		String time = values.get(SafeHomeServer.TIME_PARAM);
		if (id != null){
			try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DB_PATH + id, true)));
					PrintWriter out_p = new PrintWriter(new BufferedWriter(new FileWriter(DB_PATH + id + LIST, true)))){
				if(command != null){
					if(time != null){
						out.print(time.trim() + " ");
					}
					
					if(command.equals(SafeHomeServer.REQ_MOTIONDETECTED)){
					    out.println("Motion was detected.");
					    return true;
					}
					else if (command.equals(SafeHomeServer.REQ_PHOTOTAKEN)){
						out.println("Photo was made.");
						return true;
					}
					else if (command.equals(SafeHomeServer.REQ_RFIDSWITCH)){
						String user = values.get(SafeHomeServer.RFID_PARAM);
						if(user != null){
							out.print(user + " used the token. ");
						}
						out.println("State was switched.");
						return true;
					}
					else if (command.equals(SafeHomeServer.UPLOAD_PHOTO)){
						String path = values.get(SafeHomeServer.PHOTO_PARAM);
						if(path != null){
							out.print(path + " ");
							out_p.println(path);
						}
						out.println("Photo was saved.");
						return true;
					}
				}
			} catch (IOException e) {
				Logger.getLogger("Mock Database").severe(e.getMessage());	
			}
		}
		return false;
	}
	


	public Boolean close() {
		return true;
	}
}

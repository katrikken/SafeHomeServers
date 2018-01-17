package com.kuryshee.safehome.server;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.ServletOutputStream;
import com.kuryshee.safehome.appcommunicationconsts.AppCommunicationConsts;
import com.kuryshee.safehome.requestprocessorinterface.RequestProcessor;
import com.sun.istack.internal.logging.Logger;


public class AppGetRequestProcessor implements RequestProcessor{
	
	private String user;
	
	private boolean validateToken(String token) {
		user = "rpi"; //todo 
		
		return true;
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
					if(parameters[0] != null && validateToken(parameters[0])) {
						
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

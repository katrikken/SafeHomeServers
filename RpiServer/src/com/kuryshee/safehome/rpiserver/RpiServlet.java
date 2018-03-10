package com.kuryshee.safehome.rpiserver;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kuryshee.safehome.httprequestsender.AnswerConstants;
import com.kuryshee.safehome.requestdataretriever.PostDataRetriever;
import com.kuryshee.safehome.rpicommunicationconsts.RpiCommunicationConsts;


/**
 * Servlet implementation class RpiServlet.
 * This servlet is placed locally on the Raspberry Pi and is accessed from the local network.
 * @author Ekaterina Kurysheva
 */
@WebServlet(loadOnStartup = 1, urlPatterns = {"/RpiServlet/*"})
@MultipartConfig
public class RpiServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * The constant path of this servlet.
	 */
	private static final String SERVLET_PATH = "/RpiServer/RpiServlet";
	
	
	/**
	 * The constant contains path to the file with registered tokens for a card reader.
	 */
	//public static final String USERCONFIG = "/home/pi/NetBeansProjects/com.kuryshee.safehome.rpi/dist/keys.txt";
	public static final String USERCONFIG = "keys.txt";
	
	
	/**
	 * The queue for the tasks to the Raspberry Pi logic part application.
	 * The queue is being polled when {@link #REQ_CHECKTASK} arrives.
	 */
	public static ConcurrentLinkedQueue<String> tasks = new ConcurrentLinkedQueue<>();
	
	private static Logger log = Logger.getLogger(RpiServlet.class.getName());
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String query = request.getQueryString();
		log.log(Level.INFO, "-- Got request {0}", query);
		
		RpiLocalRequestProcessor processor = new RpiLocalRequestProcessor();
		processor.process(response.getOutputStream(), query);
	}

	/**
	 * This method processes POST request and sends relevant data to the Servlet context.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		PostDataRetriever db = new PostDataRetriever();
		String action = db.getTextPart(request, RpiCommunicationConsts.ACTION);
		
		if(action.equals(RpiCommunicationConsts.COMMAND_READTOKEN)){ //The servlet gets POST request with the token data.
			try{		
				String token = db.getTextPart(request, RpiCommunicationConsts.CARD_PARAM); //Fetch token data from the request.
				
				if(token!= null && !token.isEmpty()){
					ServletContext sc = getServletContext();
					sc.setAttribute(RpiCommunicationConsts.CARD_PARAM, token.trim());
					
					log.log(Level.INFO, "-- Token is passed to the servlet context");
					
					response.getWriter().println(AnswerConstants.OK_ANSWER);
				}
				else{
					log.log(Level.INFO, "-- Token is not in the request");
					response.getWriter().println(AnswerConstants.ERROR_ANSWER);
				}
			}
			catch(Exception e){
				response.getWriter().println(AnswerConstants.ERROR_ANSWER);
				log.log(Level.WARNING, "--Failed to fetch token data with an exception", e);
			}
		}
		else{
			log.log(Level.WARNING, "--Invalid POST request");
		}		 
	}
}

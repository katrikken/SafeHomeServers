package com.kuryshee.safehome.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kuryshee.safehome.appcommunicationconsts.AppCommunicationConsts;
import com.kuryshee.safehome.postrequestretriever.DataRetriever;

/**
 * Servlet implementation class AndroidAppServlet
 */
@WebServlet(loadOnStartup = 1, urlPatterns = {"/SafeHomeServer/app/*"})
@MultipartConfig
public class AndroidAppServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AndroidAppServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger.getLogger(AndroidAppServlet.class.getName()).log(Level.INFO, "--Android GET request registered");
		
		String token = request.getHeader(AppCommunicationConsts.TOKEN);
		String query = request.getQueryString();
		
		AppGetRequestProcessor processor;
		try {
			processor = new AppGetRequestProcessor(new InitialContext());
			processor.process(response.getOutputStream(), token, query);
		} catch (NamingException e) {
			response.getWriter().println(AppCommunicationConsts.REQUEST_PROCESS_ERROR);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger.getLogger(AndroidAppServlet.class.getName()).log(Level.INFO, "--Android POST request registered");
		
		String token = request.getHeader(AppCommunicationConsts.TOKEN);
		
		DataRetriever db = new DataRetriever();
		String action = db.getTextPart(request, AppCommunicationConsts.ACTION);
		
		AppPostRequestProcessor processor;
		try {
			processor = new AppPostRequestProcessor(new InitialContext());
			
			switch (action) {
				case AppCommunicationConsts.GET_TOKEN: processor.getToken(response.getOutputStream(),
						db.getTextPart(request, AppCommunicationConsts.LOGIN),
						db.getTextPart(request, AppCommunicationConsts.PASSWORD));
					break;
				case AppCommunicationConsts.VALIDATE: processor.validateToken(response.getOutputStream(), token);
					break;
			}
		} catch (NamingException e) {
			response.getWriter().println(AppCommunicationConsts.REQUEST_PROCESS_ERROR);
		}
	}
}

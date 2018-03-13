package com.kuryshee.safehome.rpiserver;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.kuryshee.safehome.httprequestsender.AnswerConstants;
import com.kuryshee.safehome.rpicommunicationconsts.RpiCommunicationConsts;
import com.kuryshee.safehome.sanitizer.Sanitizer;

/**
 * This class implements managed bean for the "newuser" page.
 * @author Ekaterina Kurysheva
 */
@ManagedBean(name="newUserPage")
@RequestScoped
public class NewUserPage implements Serializable{
	
	private String name;

	private String token;
	
	private String password;
	
	public String getPassword() {
		if(password == null) {
			return "";
		}
		return password;
	}

	public void setPassword(String password) {
		this.password = Sanitizer.sanitize(password);
	}

	/**
	 * The property is bounded to the index page and contains user name entered during logging in.
	 */
	@ManagedProperty("#{indexPage.userName}")
	private String userName;
	
	private UIComponent errorMsgComponent;
	
	/**
	 * Getter for the property errorMsgComponent where {@link FacesMessage} is displayed.
	 * @return the component for error messages.
	 */
    public UIComponent getErrorMsgComponent() {
        return errorMsgComponent;
    }

    /**
     * Setter for the property errorMsgComponent.
     * @param errorMsgComponent is the component to display messages in.
     */
    public void setErrorMsgComponent(UIComponent errorMsgComponent) {
        this.errorMsgComponent = errorMsgComponent;
    }

	/**
	 * Getter for the property token, which is passed to the bean through the Servlet Context.
	 * @return string with a token code. In case it has not been set, returns empty string.
	 */
	private String getToken() {
		if(token == null){
			return "";
		}
		return token;
	}

	/**
	 * Setter for the property token.
	 * @param token is provided through the Servlet Context.
	 */
	private void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * Getter for the property {@link #userName}
	 * @return user name provided during logging in.
	 */
	public String getUserName() {
		return userName;
	}
	
	
	/**
	 * Setter for the property {@link #userName}
	 * @param userName is provided during logging in.
	 */
	public void setUserName(String userName) {	
		this.userName = userName;
	}
	
	/**
	 * Getter for the property name.
	 * @return name for the new user defined in the HTML input. In case the name is not set, returns empty string. 
	 */
	public String getName() {
		if(name == null){
			return "";
		}
		return name;
	}

	/**
	 * Setter for the property name.
	 * @param name is provided via HTML input.
	 */
	public void setName(String name) {
		this.name = Sanitizer.sanitize(name);	
	}
	
	/**
	 * This method checks whether the Servlet got the token information from a logic part.
	 * In case the token was read and the new user name was provided, it writes information about new user to the configuration file.
	 * @return userpage in case of successful creation of a new user. Otherwise redirects to the current page.
	 */
	public String createNewUser(){
		try{
			ServletContext servletContext = 
				(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			
			String key = servletContext.getAttribute(RpiCommunicationConsts.CARD_PARAM).toString();
			if(!key.equals(AnswerConstants.ERROR_ANSWER)){
				setToken(key);
			}
			servletContext.removeAttribute(RpiCommunicationConsts.CARD_PARAM);
		}
		catch(Exception ex){
			Logger.getLogger(NewUserPage.class.getName()).log(Level.SEVERE, ex.getMessage());
		}
		
		if(!getToken().isEmpty() && !getName().isEmpty() && !getPassword().isEmpty()){
			Logger.getLogger(NewUserPage.class.getName()).log(Level.INFO, "User can be stored.");
			
			try{
				UserConfigManager reader = new UserConfigManager(new File(RpiServlet.readConfig()));
				List<UserBean> beans = reader.readUsersToUserBeans();
				
				for(UserBean bean: beans) {
					if(bean.getToken().equals(token)) {
						bean.setName(name);
						
						saveChanges(reader, beans);
						return PageNames.USERPAGE;	
					}
				}
				
				UserBean bean = new UserBean();
				bean.setName(name);
				bean.setToken(token);
				bean.setPassword(password);
				
				beans.add(bean);
				
				saveChanges(reader, beans);
				return PageNames.USERPAGE;	
			
			} catch (Exception e) {
				Logger.getLogger(NewUserPage.class.getName()).log(Level.SEVERE, e.getMessage());
			} 	
		}
		else{
			FacesContext.getCurrentInstance().addMessage(
					errorMsgComponent.getClientId(), 
					new FacesMessage("Error! Check validity of the name and ensure the token has been read."));
			if(!RpiServlet.tasks.contains(RpiCommunicationConsts.COMMAND_READTOKEN)){
				RpiServlet.tasks.add(RpiCommunicationConsts.COMMAND_READTOKEN);
			}
		}
		
		return PageNames.NEWUSER;
	}
	
	private void saveChanges(UserConfigManager reader, List<UserBean> beans){
		reader.writeBeansToJson(beans);
		if(!RpiServlet.tasks.contains(RpiCommunicationConsts.COMMAND_UPDATEUSERS)){
			RpiServlet.tasks.add(RpiCommunicationConsts.COMMAND_UPDATEUSERS);
		}
	}
}

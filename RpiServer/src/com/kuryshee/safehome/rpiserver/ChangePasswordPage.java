package com.kuryshee.safehome.rpiserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.kuryshee.safehome.rpicommunicationconsts.RpiCommunicationConsts;

/**
 * Managed bean for change password page.
 * @author Ekaterina Kurysheva
 *
 */
@ManagedBean(name="changePasswordPage")
@RequestScoped
public class ChangePasswordPage implements Serializable{
	
	@ManagedProperty("#{userPage.changePswdBean}")
	private UserBean changePswdBean;
	
	@ManagedProperty("#{indexPage.userName}")
	private String userName;
	
	private String changePswdName;
	
	public String getChangePswdName() {
		return changePswdName;
	}

	public void setChangePswdName(String changePswdName) {
		this.changePswdName = changePswdName;
	}

	public void setChangePswdBean(UserBean changePswdBean) {
		this.changePswdBean = changePswdBean;
		this.changePswdName = this.changePswdBean.getName();
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserBean getChangePswdBean() {
		return changePswdBean;
	}

	public String getUserName() {
		return userName;
	}

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Saves new password to configuration file.
	 * @return redirect to the user page.
	 */
	public String saveNewPassword() {
		try{
			UserConfigManager reader = new UserConfigManager(new File(RpiServlet.readConfig()));
			List<UserBean> beans = reader.readUsersToUserBeans();
			
			for(UserBean bean: beans) {
				if(bean.getToken().equals(changePswdBean.getToken())) {
					bean.setPassword(password);
					
					reader.writeBeansToJson(beans);
					if(!RpiServlet.tasks.contains(RpiCommunicationConsts.COMMAND_UPDATEUSERS)){
						RpiServlet.tasks.add(RpiCommunicationConsts.COMMAND_UPDATEUSERS);
					}
				}
			}
		
		} catch (Exception e) {
			Logger.getLogger(NewUserPage.class.getName()).log(Level.SEVERE, e.getMessage());
		} 	
		return PageNames.USERPAGE;
	}
}

package com.kuryshee.safehome.rpicommunicationconsts;

public class RpiCommunicationConsts {
	
	public static final String REGISTER_USER = "registeruser";
	public static final String DELETE_USER = "deleteuser";
	public static final String REGISTER_ACTION = "registeraction";
	
	/**
     * The constant for the POST request to the server when sending a photo.
     */
	public static final String SAVE_PHOTO = "savephoto";
	
	/**
     * The constant for the server to ask for the program state.
     */
	public static final String POST_STATE = "poststate";
	
	/**
     * The constant for the Raspberry Pi to ask for new tasks from server.
     */
	public static final String GET_TASK = "gettask";
	
	
	/**
     * The constant for commanding and reporting switching the program state to on.
     */
	public static final String TURN_ON = "turnon";
	
	/**
     * The constant for commanding and reporting switching the program state to off.
     */
	public static final String TURN_OFF = "turnoff";
	
	/**
     * The constant for the server to command taking a photo.
     */
	public static final String TAKE_PICTURE = "takepicture";
	
	public static final String USER_LOGIN = "login";
	public static final String USER_PASSWORD = "password";
	public static final String ACTION = "action";
	public static final String RPI_ACTION_INFO = "info";
	
	/**
     * The constant for the POST request parameter of time.
     */
	public static final String TIME = "time";
	public static final String LEVEL = "level";
	public static final String PHOTO_NAME = "name";
	
	/**
     * The constant for the POST request parameter of photo.
     */
	public static final String PHOTO = "photo";
	public static final String STATE = "state";
	
	/**
     * The POST request parameter for passing the id data.
     */
	public static final String RPI_ID = "id";
	
	/**
	 * The POST request parameter for passing the token data.
	 */
	public static final String CARD_PARAM = "card";
	
	/**
	 * The constant for the server to tell the Raspberry Pi to read a token.
	 */
	public static final String COMMAND_READTOKEN = "read";
	
	/**
	 * The constant for the server to tell the Raspberry Pi to update user list.
	 */
    public static final String COMMAND_UPDATEUSERS = "saveuser";
    
	/**
	 * The constant contains key word for the configuration file.
	 */
	public static final String KEY = "key";
	
	public static final String NAME = "name";
	public static final String TOKEN = "token";
	public static final String PASSWORD = "password";
}


package com.kuryshee.safehome.rpicommunicationconsts;

public class RpiCommunicationConsts {
	
	public static final String REGISTER_USER = "registeruser";
	public static final String REGISTER_ACTION = "registeraction";
	public static final String SAVE_PHOTO = "savephoto";
	public static final String POST_STATE = "poststate";
	public static final String GET_TASK = "gettask";
	
	public static final String TURN_ON = "turnon";
	public static final String TURN_OFF = "turnoff";
	public static final String TAKE_PICTURE = "takepicture";
	
	public static final String USER_LOGIN = "login";
	public static final String USER_PASSWORD = "password";
	public static final String ACTION = "action";
	public static final String RPI_ACTION_INFO = "info";
	public static final String TIME = "time";
	public static final String LEVEL = "level";
	public static final String PHOTO_NAME = "name";
	public static final String PHOTO = "photo";
	public static final String STATE = "state";
	public static final String RPI_ID = "id";
	
	/**
	 * The POST request parameter for passing the token data.
	 */
	public static final String CARD_PARAM = "card";
	
	/**
	 * The constant for the Raspberry Pi to ask for new tasks from server.
	 */
	public static final String REQ_CHECKTASK = "checktask";
	
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


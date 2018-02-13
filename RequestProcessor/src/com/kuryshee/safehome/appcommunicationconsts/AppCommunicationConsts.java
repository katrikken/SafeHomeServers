package com.kuryshee.safehome.appcommunicationconsts;

public class AppCommunicationConsts {
	
	public static final String TOKEN = "token";
	public static final String ACTION = "action";
	public static final String PING = "ping";
	public static final String PONG = "pong";
	public static final String COUNT = "count";
	public static final String TIME = "time";
	
	public static final String GET_TOKEN = "gettoken";
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	public static final String VALIDATE = "validatetoken";
	public static final String GET_ACTIONS = "getactions";
	public static final String GET_LATEST_TIME = "getlatestactiontime";
	
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public static final String DATE_FORMAT_DB = "DD-MON-YYYY HH24:MI:SS.FF";
	public static final String DATE_FORMAT_APP = "dd-MM-YYYY HH:mm:ss.FF";
	
	public static final String REQUEST_FORMAT_ERROR = "Invalid request";
	
	/**
     * The definition of the answer when error occurred during request processing.
     */
    public static final String REQUEST_PROCESS_ERROR = "Process error";
    
    /**
     * The definition of the answer when user credentials are invalid.
     */
    public static final String INVALID_USER_ERROR = "Invalid user data";
    
    /**
     * The definition of the answer, when not specified error occurred.
     */
    public static final String ERROR = "error";
}

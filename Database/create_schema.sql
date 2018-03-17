-- TABLE OF USERS AND PASSWORDS
CREATE TABLE USER_CREDENTIALS(
  USER_LOGIN CHARACTER VARYING (15) NOT NULL
	PRIMARY KEY,

  USER_PASSWORD CHARACTER VARYING(15) NOT NULL
);

--INSERTS A ROW WITH NEW USER DATA OR CHANGES THE EXISTING ROW.
CREATE PROCEDURE ADD_USER_CREDENTIALS(
  U_LOGIN USER_CREDENTIALS.USER_LOGIN%TYPE, -- USER LOGIN
  U_PASSWORD USER_CREDENTIALS.USER_PASSWORD%TYPE -- USER PASSWORD
)
AS
BEGIN
  INSERT INTO USER_CREDENTIALS(
      USER_LOGIN,
      USER_PASSWORD
      ) 
    VALUES(
      U_LOGIN,
      U_PASSWORD
      );
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
      UPDATE USER_CREDENTIALS
	  SET USER_PASSWORD=U_PASSWORD
	  WHERE USER_LOGIN=U_LOGIN;
END ADD_USER_CREDENTIALS;
/

--DELETES THE ROW WITH USER DATA.
CREATE PROCEDURE DELETE_USER_CREDENTIALS(
  U_LOGIN USER_CREDENTIALS.USER_LOGIN%TYPE -- USER LOGIN
)
AS
BEGIN
  DELETE FROM USER_CREDENTIALS U 
    WHERE U_LOGIN=U.USER_LOGIN;
	
END DELETE_USER_CREDENTIALS;
/


COMMIT;


-- RETURNS TRUE IF A USER EXISTS WITH THE GIVEN PASSWORD.
CREATE FUNCTION VALIDATE_USER_CREDENTIALS(
    U_LOGIN USER_CREDENTIALS.USER_LOGIN%TYPE, -- USER LOGIN
    U_PASSWORD USER_CREDENTIALS.USER_PASSWORD%TYPE -- USER PASSWORD
  ) RETURN INTEGER  -- 1 IF THE DATA ARE VALID, 0 OTHERWISE
AS
  U_COUNT NUMBER(1,0);
BEGIN
  SELECT COUNT(*) INTO U_COUNT
	FROM USER_CREDENTIALS UC
	 WHERE UC.USER_LOGIN = U_LOGIN AND UC.USER_PASSWORD = U_PASSWORD; 

  RETURN U_COUNT;
END;
/

-- TABLE OF USERS' AUTHORIZATION TOKENS
CREATE TABLE USER_TOKENS(
  USER_LOGIN CHARACTER VARYING (15) NOT NULL,
  
  CONSTRAINT UT_USER_LOGIN 
    FOREIGN KEY(USER_LOGIN)
	  REFERENCES USER_CREDENTIALS(USER_LOGIN)
	    ON DELETE CASCADE,

  USER_TOKEN CHARACTER VARYING(40) NOT NULL
    CONSTRAINT UT_USER_TOKEN
      UNIQUE
);

COMMIT;

--INSERTS A ROW WITH NEW USER TOKEN OR CHANGES THE TOKEN IF THE ROW EXISTS.
CREATE PROCEDURE ADD_USER_TOKEN(
  U_LOGIN USER_TOKENS.USER_LOGIN%TYPE, -- USER LOGIN
  U_TOKEN USER_TOKENS.USER_TOKEN%TYPE -- USER TOKEN
)
AS
BEGIN
  DELETE FROM USER_TOKENS UT 
    WHERE U_LOGIN = UT.USER_LOGIN; -- DELETE PREVIOUS TOKENS.
	
  INSERT INTO USER_TOKENS(
      USER_LOGIN,
      USER_TOKEN
      ) 
    VALUES(
      U_LOGIN,
      U_TOKEN
      );
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
      RAISE_APPLICATION_ERROR(
      00001, 
      'THE TOKEN IS NOT UNIQUE!'
      );
END ADD_USER_TOKEN;
/

-- VALIDATES USER TOKEN.
CREATE FUNCTION VALIDATE_USER_TOKEN(
    U_LOGIN USER_TOKENS.USER_LOGIN%TYPE, -- USER LOGIN
    U_TOKEN USER_TOKENS.USER_TOKEN%TYPE -- USER TOKEN
  ) RETURN INTEGER  -- 1 IF THE DATA ARE VALID, 0 OTHERWISE
AS
  U_COUNT NUMBER(1,0);
BEGIN
  SELECT COUNT(*) INTO U_COUNT
	FROM USER_TOKENS UT
	 WHERE UT.USER_LOGIN = U_LOGIN AND UT.USER_TOKEN = U_TOKEN;
  
  RETURN U_COUNT;
END;
/

-- RETURNS THE USER, WHICH OWNS THE TOKEN.
CREATE FUNCTION GET_USER_BY_TOKEN(
    U_TOKEN USER_TOKENS.USER_TOKEN%TYPE -- USER TOKEN
  ) RETURN USER_TOKENS.USER_LOGIN%TYPE  -- USER LOGIN
AS
  RETVAL USER_TOKENS.USER_LOGIN%TYPE;
BEGIN
  SELECT USER_LOGIN INTO RETVAL
	FROM USER_TOKENS UT
	 WHERE UT.USER_TOKEN = U_TOKEN;
  
  RETURN RETVAL;
  
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN '';
END;
/

COMMIT;

-- TABLE OF RASPBERRY PI DEVICES
CREATE TABLE RPI_DEVICES(
  RPI_ID CHARACTER VARYING (10) NOT NULL
	PRIMARY KEY,

  RPI_STATE CHARACTER VARYING(15) DEFAULT 'INACTIVE',
    CONSTRAINT RPI_STATE_VALUE CHECK (RPI_STATE IN ('INACTIVE', 'ACTIVE'))
);

COMMIT;

-- ADDS NEW RPI TO THE TABLE
CREATE PROCEDURE ADD_NEW_RPI(
  P_RPI_ID RPI_DEVICES.RPI_ID%TYPE -- RPI ID
)
AS
BEGIN
  INSERT INTO RPI_DEVICES(
      RPI_ID
      ) 
    VALUES(
      P_RPI_ID
      );
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
      RAISE_APPLICATION_ERROR(
      00001, 
      'THE TOKEN IS NOT UNIQUE!'
      );
END ADD_NEW_RPI;
/

-- CHANGES STATE OF THE RPI
CREATE PROCEDURE CHANGE_RPI_STATE(
  P_RPI_ID RPI_DEVICES.RPI_ID%TYPE, -- RPI ID
  P_RPI_STATE RPI_DEVICES.RPI_STATE%TYPE  -- RPI STATE
)
AS
BEGIN
  UPDATE RPI_DEVICES
    SET RPI_STATE = P_RPI_STATE
	WHERE RPI_ID = P_RPI_ID;
	
  EXCEPTION
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(
      00002, 
      'NO DEVICES WITH SUCH ID WERE FOUND!'
      );
END CHANGE_RPI_STATE;
/

-- RETURNS STATE OF THE DEVICE
CREATE FUNCTION GET_RPI_STATE(
    P_RPI_ID RPI_DEVICES.RPI_ID%TYPE -- RPI ID
  ) RETURN RPI_DEVICES.RPI_STATE%TYPE  -- RPI STATE
AS
  RETVAL RPI_DEVICES.RPI_STATE%TYPE;
BEGIN
  SELECT RPI_STATE INTO RETVAL
	FROM RPI_DEVICES RD
	 WHERE RD.RPI_ID = P_RPI_ID;
  
  RETURN RETVAL;
  
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN '';
END;
/

-- RETURNS STATE OF THE DEVICE
CREATE FUNCTION VERIFY_RPI_REGISTERED(
    P_RPI_ID RPI_DEVICES.RPI_ID%TYPE -- RPI ID
  ) RETURN INTEGER  -- 1 IF THE DATA ARE VALID, 0 OTHERWISE
AS
  U_COUNT NUMBER(1,0);
BEGIN
  SELECT COUNT(*) INTO U_COUNT
	FROM RPI_DEVICES RD
	 WHERE RD.RPI_ID = P_RPI_ID AND RD.RPI_STATE = 'ACTIVE';
  
  RETURN U_COUNT;
END;
/

-- TABLE OF RELATIONS BETWEEN RASPBERRY PI COMPUTERS AND USERS.
-- USER IS REGISTERED FOR ONE RASPBERRY PI, ONE RASPBERRY PI CAN HAVE SEVERAL USERS.
CREATE TABLE RPI_USERS_RELATIONS(
  USER_LOGIN CHARACTER VARYING (15) NOT NULL
    CONSTRAINT RPI_UNIQUE_RELATION
	  UNIQUE,
  
  CONSTRAINT UL_RELATIONS
    FOREIGN KEY(USER_LOGIN)
	  REFERENCES USER_CREDENTIALS(USER_LOGIN)
	    ON DELETE CASCADE,
		
  RPI_ID CHARACTER VARYING (10) NOT NULL,
  
  CONSTRAINT RI_RELATIONS
    FOREIGN KEY(RPI_ID)
	  REFERENCES RPI_DEVICES(RPI_ID)
	    ON DELETE CASCADE,

  CONSTRAINT RPI_USERS_RELATIONS_KEY
    PRIMARY KEY(USER_LOGIN, RPI_ID)
);

COMMIT;

-- CHANGES STATE OF THE RPI
CREATE PROCEDURE ADD_RPI_USER_RELATION(
  P_USER_LOGIN USER_CREDENTIALS.USER_LOGIN%TYPE,  -- USER  LOGIN
  P_RPI_ID RPI_DEVICES.RPI_ID%TYPE -- RPI ID
)
AS
BEGIN
  INSERT INTO RPI_USERS_RELATIONS(
      USER_LOGIN,
      RPI_ID
      ) 
    VALUES(
	  P_USER_LOGIN,
      P_RPI_ID
      );
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN NULL; -- IGNORE
	
END ADD_RPI_USER_RELATION;
/

-- RETURNS THE RPI ID WHICH IS ASSOCIATED WITH THE USER
CREATE FUNCTION GET_RPI_BY_USER(
    P_USER_LOGIN USER_CREDENTIALS.USER_LOGIN%TYPE  -- USER  LOGIN
  ) RETURN RPI_DEVICES.RPI_ID%TYPE -- RPI ID
AS
  RETVAL RPI_DEVICES.RPI_ID%TYPE; -- RPI ID
BEGIN
  SELECT RPI_ID INTO RETVAL
	FROM RPI_USERS_RELATIONS
	 WHERE USER_LOGIN = P_USER_LOGIN;
  
  RETURN RETVAL;
  
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN '';
END;
/

COMMIT;

-- TABLE WITH REGISTERED ACTIONS ON THE RASPBERRY PI COMPUTER.
CREATE TABLE RPI_ACTIONS(
		
  RPI_ID CHARACTER VARYING (10) NOT NULL,
  
  CONSTRAINT RA_RPI_ID
    FOREIGN KEY(RPI_ID)
	  REFERENCES RPI_DEVICES(RPI_ID)
	    ON DELETE CASCADE,

  ACTION_DATE TIMESTAMP(3) NOT NULL,
  
  CONSTRAINT RPI_ACTIONS_KEY
    PRIMARY KEY(RPI_ID, ACTION_DATE),
  
  ACTION_INFO CHARACTER VARYING (100) NOT NULL,
  
  ACTION_LEVEL CHARACTER VARYING(1) DEFAULT 'N',
    CONSTRAINT ACTION_LEVEL_VALUE CHECK (ACTION_LEVEL IN ('N', 'D')) -- N FOR NORMAL, D FOR DANGEROUS
);

COMMIT;

-- ADDS ACTION ON THE RPI
CREATE PROCEDURE ADD_RPI_ACTION(
  P_RPI_ID RPI_DEVICES.RPI_ID%TYPE, -- RPI ID
  P_ACTION_DATE RPI_ACTIONS.ACTION_DATE%TYPE, -- ACTION DATE
  P_ACTION_INFO RPI_ACTIONS.ACTION_INFO%TYPE, -- ACTION DESCRIPTION
  P_ACTION_LEVEL RPI_ACTIONS.ACTION_LEVEL%TYPE -- ACTION LEVEL
)
AS
BEGIN
  INSERT INTO RPI_ACTIONS(
      RPI_ID,
      ACTION_DATE,
	  ACTION_INFO,
	  ACTION_LEVEL
      ) 
    VALUES(
	  P_RPI_ID,
	  P_ACTION_DATE,
	  P_ACTION_INFO,
	  P_ACTION_LEVEL
      );
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN NULL; -- DUPLICATE VALUES SHOULD BE HANDLED BY SECONDS PRECISION OF DATE.
	
END ADD_RPI_ACTION;
/

-- RETURNS ACTIONS ON RASPBERRY PI BEFORE A GIVEN TIMESTAMP
create FUNCTION GET_RPI_ACTIONS_BEFORE(
    P_RPI_ID RPI_DEVICES.RPI_ID%TYPE, -- RPI ID,
	P_ACTION_DATE RPI_ACTIONS.ACTION_DATE%TYPE, -- TIMESTAMP OF THE LATEST KNOWN EVENT
	P_COUNT NUMBER -- NUMBER OF ACTIONS TO RETURN, USE MAXIMUM OF 10
  ) RETURN CLOB -- RETURNS JSON OF TIMES AND ACTIONS [{"t":"TIME","a":"ACTION","l":"LEVEL"},{"t":"TIME","a":"ACTION","l":"LEVEL"},...]
AS
  CURSOR C IS
  select * from (
    SELECT TO_CHAR(ACTION_DATE, 'DD-MM-YYYY HH24:MI:SS.FF') AS ACTION_DATE_STR, ACTION_INFO, ACTION_LEVEL
        FROM RPI_ACTIONS
          WHERE RPI_ID = P_RPI_ID AND ACTION_DATE <= P_ACTION_DATE
            ORDER BY ACTION_DATE DESC)
    where ROWNUM <= P_COUNT;

  RETVAL VARCHAR2(2000);
  RETVALCLOB CLOB;
  TIMEV VARCHAR2(5);
  ACTIONV VARCHAR2(4);
  QUOTM VARCHAR2(1);
  DELIM VARCHAR2(1);
  BRACE VARCHAR2(1);
  SQBRACE VARCHAR2(1);
  ACTIONL VARCHAR2(4);
BEGIN
  RETVALCLOB := '[';
  RETVAL := '';
  TIMEV := '{"t":';
  ACTIONV := '"a":';
  ACTIONL := '"l":';
  QUOTM := '"';
  BRACE := '}';
  DELIM := '';
  SQBRACE := ']';

  FOR R IN C LOOP
    RETVAL := RETVAL || DELIM 
	  || TIMEV || QUOTM || R.ACTION_DATE_STR || QUOTM;
	DELIM := ',';
	RETVAL := RETVAL || DELIM
	  || ACTIONV || QUOTM || R.ACTION_INFO || QUOTM || DELIM
	  || ACTIONL || QUOTM || R.ACTION_LEVEL || QUOTM
	  || BRACE;
  END LOOP;
  RETVAL := RETVAL || SQBRACE;

  DBMS_LOB.APPEND(RETVALCLOB, RETVAL);
  RETURN RETVALCLOB;
END;
/

-- RETURNS THE TIMESTAMP OF THE LATEST REGISTERED ACTION ON RPI
create or replace FUNCTION GET_LATEST_DATE_ON_ACTIONS(
    P_RPI_ID RPI_DEVICES.RPI_ID%TYPE -- RPI ID
  ) RETURN RPI_ACTIONS.ACTION_DATE%TYPE  -- TIMESTAMP
AS
  RETVAL RPI_ACTIONS.ACTION_DATE%TYPE;
BEGIN
select ACTION_DATE into RETVAL from(
  SELECT ACTION_DATE
	FROM RPI_ACTIONS
	 WHERE RPI_ID = P_RPI_ID
	   ORDER BY ACTION_DATE DESC)
  where ROWNUM = 1;

  RETURN RETVAL;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN '';
END;
/
    
COMMIT;

CREATE TABLE RPI_PHOTOS(
		
  RPI_ID CHARACTER VARYING (10) NOT NULL,
  
  CONSTRAINT RPH_RPI_ID
    FOREIGN KEY(RPI_ID)
	  REFERENCES RPI_DEVICES(RPI_ID)
	    ON DELETE CASCADE,

  PHOTO_DATE TIMESTAMP(3) NOT NULL,
  
  CONSTRAINT RPI_PHOTOS_KEY
    PRIMARY KEY(RPI_ID, PHOTO_DATE),
  
  PHOTO_NAME CHARACTER VARYING(30) NOT NULL,
  
  PHOTO_BLOB BLOB NOT NULL
);

COMMIT;

CREATE PROCEDURE ADD_RPI_PHOTO(
  P_RPI_ID RPI_DEVICES.RPI_ID%TYPE, -- RPI ID
  P_PHOTO_DATE RPI_PHOTOS.PHOTO_DATE%TYPE, -- PHOTO DATE
  P_PHOTO_NAME RPI_PHOTOS.PHOTO_NAME%TYPE, -- PHOTO NAME
  P_PHOTO_BLOB RPI_PHOTOS.PHOTO_BLOB%TYPE -- PHOTO BINARY
)
AS
BEGIN
  INSERT INTO RPI_PHOTOS(
      RPI_ID,
      PHOTO_DATE,
	  PHOTO_NAME,
	  PHOTO_BLOB
      ) 
    VALUES(
	  P_RPI_ID,
	  P_PHOTO_DATE,
	  P_PHOTO_NAME,
	  P_PHOTO_BLOB
      );
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN NULL; -- DUPLICATE VALUES SHOULD BE HANDLED BY PRECISION OF DATE.
	
END ADD_RPI_PHOTO;
/

-- DELETES THE PHOTO FROM THE TABLE
CREATE PROCEDURE DELETE_RPI_PHOTO(
  P_RPI_ID RPI_DEVICES.RPI_ID%TYPE, -- RPI ID
  P_PHOTO_DATE RPI_PHOTOS.PHOTO_DATE%TYPE -- PHOTO DATE
)
AS
BEGIN
  DELETE FROM RPI_PHOTOS
    WHERE RPI_ID = P_RPI_ID AND PHOTO_DATE = P_PHOTO_DATE;
	
END DELETE_RPI_PHOTO;
/

COMMIT;

-- RETURNS TIMESTAMPS OF PHOTOS REGISTERED BEFORE THE GIVEN DATE
CREATE FUNCTION GET_PHOTO_TIMES_BEFORE(
    P_RPI_ID RPI_DEVICES.RPI_ID%TYPE, -- RPI ID,
	P_PHOTO_DATE RPI_PHOTOS.PHOTO_DATE%TYPE, -- TIMESTAMP OF THE LATEST KNOWN EVENT
	P_COUNT NUMBER -- NUMBER OF ACTIONS TO RETURN, USE MAXIMUM OF 5
  ) RETURN CLOB -- RETURNS JSON ARRAY OF TIMESTAMPS [{"t":"TIME1","n":"NAME"},{"t":"TIME2","n":"NAME"},...]
AS
  CURSOR C IS
  select * from (
    SELECT TO_CHAR(PHOTO_DATE, 'DD-MM-YYYY HH24:MI:SS.FF') AS PHOTO_DATE_STR, PHOTO_NAME
        FROM RPI_PHOTOS
          WHERE RPI_ID = P_RPI_ID AND PHOTO_DATE <= P_PHOTO_DATE
            ORDER BY PHOTO_DATE DESC)
  where ROWNUM <= P_COUNT;

  RETVAL VARCHAR2(1000);
  RETVALCLOB CLOB;
  TIMEV VARCHAR2(5);
  PHOTON VARCHAR2(4);
  QUOTM VARCHAR2(1);
  DELIM VARCHAR2(1);
  BRACE VARCHAR2(1);
  SQBRACE VARCHAR2(1);
BEGIN
  RETVALCLOB := '[';
  RETVAL := '';
  TIMEV := '{"t":';
  PHOTON := '"n":';
  QUOTM := '"';
  BRACE := '}';
  DELIM := '';
  SQBRACE := ']';

  FOR R IN C LOOP
    RETVAL := RETVAL || DELIM 
	  || TIMEV || QUOTM || R.PHOTO_DATE_STR || QUOTM;
	DELIM := ',';
	RETVAL := RETVAL || DELIM
	  || PHOTON || QUOTM || R.PHOTO_NAME || QUOTM
	  || BRACE;
  END LOOP;
  RETVAL := RETVAL || SQBRACE;
  
  DBMS_LOB.APPEND(RETVALCLOB, RETVAL);
  RETURN RETVALCLOB;
END;
/

-- RETURNS PHOTO OF THE GIVEN IDENTIFIER
CREATE FUNCTION GET_PHOTO(
    P_RPI_ID RPI_DEVICES.RPI_ID%TYPE, -- RPI ID
	P_PHOTO_DATE RPI_PHOTOS.PHOTO_DATE%TYPE -- TIMESTAMP TO IDENTIFY THE PHOTO
  ) RETURN BLOB -- PHOTO FILE
AS
  RETVAL BLOB;
BEGIN
  SELECT PHOTO_BLOB INTO RETVAL
	FROM RPI_PHOTOS
	 WHERE RPI_ID = P_RPI_ID AND PHOTO_DATE = P_PHOTO_DATE;
  
  RETURN RETVAL;
  
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
END;
/

-- RETURNS THE TIMESTAMP OF THE LATEST REGISTERED PHOTO ON RPI
create or replace FUNCTION GET_LATEST_DATE_ON_PHOTOS(
    P_RPI_ID RPI_DEVICES.RPI_ID%TYPE -- RPI ID
  ) RETURN RPI_PHOTOS.PHOTO_DATE%TYPE  -- TIMESTAMP
AS
  RETVAL  RPI_PHOTOS.PHOTO_DATE%TYPE;
BEGIN
select PHOTO_DATE into retval
  from (
    SELECT PHOTO_DATE
	FROM RPI_PHOTOS
	 WHERE RPI_ID = P_RPI_ID
	   ORDER BY PHOTO_DATE DESC
       )
  where ROWNUM = 1;

  RETURN RETVAL;

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN '';
END;
/
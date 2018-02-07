DROP PROCEDURE ADD_RPI_USER_RELATION;
DROP TABLE RPI_USERS_RELATIONS;

DROP PROCEDURE ADD_RPI_ACTION;
DROP FUNCTION GET_LATEST_RPI_ACTIONS;
DROP TABLE RPI_ACTIONS;

DROP PROCEDURE ADD_RPI_PHOTO;
DROP TABLE RPI_PHOTOS;

DROP FUNCTION GET_USER_BY_TOKEN;
DROP FUNCTION VALIDATE_USER_TOKEN;
DROP PROCEDURE ADD_USER_TOKEN;
DROP TABLE USER_TOKENS;

DROP PROCEDURE ADD_USER_CREDENTIALS;
DROP PROCEDURE DELETE_USER_CREDENTIALS;
DROP FUNCTION VALIDATE_USER_CREDENTIALS;

DROP TABLE USER_CREDENTIALS;

DROP PROCEDURE ADD_NEW_RPI;
DROP PROCEDURE CHANGE_RPI_STATE;
DROP FUNCTION GET_RPI_STATE;
DROP TABLE RPI_DEVICES;

COMMIT;
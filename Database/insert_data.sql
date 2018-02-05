exec ADD_USER_CREDENTIALS('admin', 'admin');

exec ADD_NEW_RPI('rpi');
exec CHANGE_RPI_STATE('rpi', 'ACTIVE');

exec ADD_RPI_USER_RELATION('admin','rpi');

COMMIT;
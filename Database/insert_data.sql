exec ADD_USER_CREDENTIALS('admin', 'admin');

exec ADD_NEW_RPI('rpi');
exec CHANGE_RPI_STATE('rpi', 'ACTIVE');

exec ADD_RPI_USER_RELATION('admin','rpi');

exec ADD_RPI_ACTION('rpi', to_timestamp('07-02-2018 12:00:00.00', 'DD-MM-YYYY HH24:MI:SS.FF'), 'first action');

COMMIT;
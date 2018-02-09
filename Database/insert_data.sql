exec ADD_USER_CREDENTIALS('admin', 'admin');

exec ADD_NEW_RPI('rpi');
exec CHANGE_RPI_STATE('rpi', 'ACTIVE');

exec ADD_RPI_USER_RELATION('admin','rpi');

exec ADD_RPI_ACTION('rpi', to_timestamp('07-02-2018 12:00:00.00', 'DD-MM-YYYY HH24:MI:SS.FF'), 'first action');
exec ADD_RPI_ACTION('rpi', to_timestamp('07-02-2018 12:01:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'second action');
exec ADD_RPI_ACTION('rpi', to_timestamp('07-02-2018 12:02:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'third action');
exec ADD_RPI_ACTION('rpi', to_timestamp('07-02-2018 12:03:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'fourth action');
exec ADD_RPI_ACTION('rpi', to_timestamp('07-02-2018 12:04:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'fifth action');
exec ADD_RPI_ACTION('rpi', to_timestamp('07-02-2018 12:05:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'sixth action');
exec ADD_RPI_ACTION('rpi', to_timestamp('07-02-2018 12:06:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'seventh action');
exec ADD_RPI_ACTION('rpi', to_timestamp('08-02-2018 12:00:00.00', 'DD-MM-YYYY HH24:MI:SS.FF'), 'first action');
exec ADD_RPI_ACTION('rpi', to_timestamp('08-02-2018 12:01:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'second action');
exec ADD_RPI_ACTION('rpi', to_timestamp('08-02-2018 12:02:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'third action');
exec ADD_RPI_ACTION('rpi', to_timestamp('08-02-2018 12:03:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'fourth action');
exec ADD_RPI_ACTION('rpi', to_timestamp('08-02-2018 12:04:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'fifth action');
exec ADD_RPI_ACTION('rpi', to_timestamp('08-02-2018 12:05:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'sixth action');
exec ADD_RPI_ACTION('rpi', to_timestamp('08-02-2018 12:06:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'seventh action');
exec ADD_RPI_ACTION('rpi', to_timestamp('06-02-2018 12:00:00.00', 'DD-MM-YYYY HH24:MI:SS.FF'), 'first action');
exec ADD_RPI_ACTION('rpi', to_timestamp('06-02-2018 12:01:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'second action');
exec ADD_RPI_ACTION('rpi', to_timestamp('06-02-2018 12:02:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'third action');
exec ADD_RPI_ACTION('rpi', to_timestamp('06-02-2018 12:03:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'fourth action');
exec ADD_RPI_ACTION('rpi', to_timestamp('06-02-2018 12:04:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'fifth action');
exec ADD_RPI_ACTION('rpi', to_timestamp('06-02-2018 12:05:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'sixth action');
exec ADD_RPI_ACTION('rpi', to_timestamp('06-02-2018 12:06:00.02', 'DD-MM-YYYY HH24:MI:SS.FF'), 'seventh action');

COMMIT;
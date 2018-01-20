-- Table of users and passwords
create table user_credentials(
  user_login character varying (15) not null
	primary key,

  user_password character varying(15) not null
);

--Inserts a row with new user data or changes the existing row.
create procedure add_user_credentials(
  u_login user_credentials.user_login%type, -- user login
  u_password user_credentials.user_password%type -- user password
)
as
begin
  insert into user_credentials(
      user_login,
      user_password
      ) 
    values(
      u_login,
      u_password
      );
  exception
    when dup_val_on_index then
      update user_credentials
	  set user_password=u_password
	  where user_login=u_login;
end add_user_credentials;
/

--Deletes the row with user data.
create procedure delete_user_credentials(
  u_login user_credentials.user_login%type -- user login
)
as
begin
  delete from user_credentials u 
    where u_login=u.user_login;
	
end delete_user_credentials;
/


commit;


-- Table of users' authorization tokens
create table user_tokens(
  user_login character varying (15) not null,
  
  constraint ut_user_login 
    foreign key(user_login)
	  references user_credentials(user_login)
	    on delete cascade,

  user_token character varying(40) not null
    constraint ut_user_token
      unique
);

commit;

--Inserts a row with new user token or changes the token if the row exists.
create procedure add_user_token(
  u_login user_tokens.user_login%type, -- user login
  u_token user_tokens.user_token%type -- user token
)
as
begin
  delete from user_tokens ut 
    where u_login = ut.user_login; -- Delete previous tokens.
	
  insert into user_tokens(
      user_login,
      user_token
      ) 
    values(
      u_login,
      u_token
      );
  exception
    when dup_val_on_index then
      raise_application_error(
      00001, 
      'The token is not unique!'
      );
end add_user_token;
/

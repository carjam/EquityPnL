ALTER USER carjam IDENTIFIED WITH mysql_native_password BY 'password';
USE equity;
GRANT ALL ON equity TO carjam ;

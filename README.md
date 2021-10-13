# equity

Template project for quick-start of a POC java REST API w/ mysql flyway integration.

steps to get here:
```
mysql -u root
 > CREATE USER 'carjam'@'localhost' IDENTIFIED BY 'password'
 > GRANT ALL PRIVILEGES ON *.* TO 'carjam'@'localhost';
 > FLUSH PRIVILEGES;
 > CREATE DATABASE equity;
 > USE equity;
 > GRANT ALL ON equity TO carjam@localhost ;
 > GRANT ALL PRIVILEGES ON `equity`.* TO 'carjam'@'localhost';
 > ALTER USER 'carjam'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';
```

create tables & data:
Install Maven: https://maven.apache.org/install.html
Follow directions to assure JAVA_HOME and the maven bin directory are in your path.
Build: 
```
> mvn package
```
 run MainApplicationClass and flyway will automatically run:
 ```
 > mvn spring-boot:run
 ```
 verify running at http://localhost:8080/Equity/1
 (or whatever port you've specified in the application.properties file)

 to manually run flyway:
   from /equity directory > mvn compile flyway:migrate

Create .env and add your environment values like:
```
MYSQL_ROOT_PASSWORD=root1234
MYSQL_USER=carjam
MYSQL_PASSWORD=password
MYSQL_ALLOW_EMPTY_PASSWORD=1
MYSQL_DATABASE=equity

MAVEN_OPTS=-Xmx1024m
DATABASE_HOST=equity-db
DATABASE_USER=carjam
DATABASE_PASSWORD=password
DATABASE_NAME=equity
DATABASE_PORT=3306
SPRING_PROFILES_ACTIVE=dev
LOG_LEVEL=INFO
```


To setup docker, first follow instructions here:
  https://spring.io/guides/gs/spring-boot-docker/
```
From equity directory:
> mkdir -p target/dependency
> cd target/dependency
> jar -xf ../*.jar
> docker build -t springio/gs-spring-boot-docker .
> docker run -p 8080:8080 springio/gs-spring-boot-docker
```
To run with docker-compose:
```
> docker-compose build
> docker-compose up
```
verify running at http://localhost:8080/Equity/1

# Equity PnL

A prototype PnL snapshot calculation engine for Equity positions based on free-tier Finhub integration:
- https://finnhub.io/docs/api/quote
- https://finnhub.io/docs/api/stock-candles

Desirable Future Enhancements:
  - Tech
    - expose webhook for near realtime ingestion of Transactions
        - calculate and store PnL at ingestion time (rather than on demand)
  	- persist finhub historical marks asynchronously and cache query first - add 2nd lvl cache LRU for latest dates
    - add business day logic for price inquiries - lookup last biz day on mkt close day
  	- add unit tests
  	- validate input on receipt in controllers
  	- authentication/security
  	- general clean up/refactor
  - Features
  	- transaction costs, tax impliciation (short term/long term)
    - margin
    - store ITD, YTD, MTD, daily (nightly persist)
  		- base queries off this for faster lookup and less reliance on Finhub
  	- triple ledger for accounting
  	- attribution to greeks - beta & alpha
 - Upstream
    - account for settled, pending, fails, etc
  	- fractional shares in Transactions
  	- Transactions specifying positions by lot for loss harvesting (fifo/lifo)
  	- more events: dividends, split, reverse split, delisting, etc



<h4>Setup Steps:</h4>
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

FINHUB_KEY=[your Finhub key value here]
```


Create mysql DB & user:
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

Build
```
From equity directory:
> . build.sh
OR
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
To setup docker, first follow instructions here:
  https://spring.io/guides/gs/spring-boot-docker/


Or...manually run maven migrations:
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

Verify running using
- ./postman/*.json endopint access specifications</h5>
- http://localhost:8080/actuator/health

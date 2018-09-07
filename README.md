# Z Invoices API
This project is a very simple REST API storing data in a SQL database.  The database is a H2 in-memory database.

To build the project:
From the root of the project, run the following command:  ./mvnw clean package
This will compile, run unit tests and package the jar file.

To run the project (java 8 required):
From the root of the project, run the following command:  java -jar target/coding-challenge-0.0.1-SNAPSHOT.jar

To interact with this REST API: 
A Swagger-based interface is built-in: http://localhost:8080/swagger-ui.html#/
From that point, you can consult the documentation and use the API via the "Try it out!" button.  This function also provides the curl command for future use.

Also, for your convenience, the database can be browsed directly from the built-in Web interface: http://localhost:8080/h2-console
Connection parameters:
JDBC URL: jdbc:h2:mem:invoices
User Name: zola
No password required.

# Thank you

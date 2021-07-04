# Phase 3 Technical Report

## Introduction

This document contains the relevant design and implementation aspects of LS project's first phase.

## Modeling the database

### Conceptual model ###

The following diagram holds the Entity-Relationship model for the information managed by the system.

![alt text](https://github.com/konradw98/private-g01/blob/main/docs/templates/DB_SCHEMA_LS_phase2.png)

We highlight the following aspects:

* Problem with mapping relationships between tables. (phase1)

The conceptual model has the following restrictions:

* Does not map how pk behaves. (phase1)
* Does not show format at date. (phase1)

    
### Physical Model ###

The physical model of the database is available in [DB SCRYPT](https://github.com/konradw98/private-g01/blob/main/src/test/sql/scryptDb.sql).

We highlight the following aspects of this model:

* Problem with application of self-incrementing PK. (phase1)
* Select appropriate data types. (phase1)
* Select appropriate constraints for example not null type. (phase1)

## Software organization

### Command Processing

PHASE 1
* Command Processing begins in InteractiveMode class, input is validated there and prepared for the following actions.
* If command is correct, the corresponding Handler is called. Otherwise, the user is informed about the mistake in command.
* We have 15 handler, each for desired database queries. 
* In addition, PostHandlers have ArrayList of parameters taken from an input. In these handlers parameters are also validated.

PHASE 2
* Now, we have a CommandExecutor class, which is used to differ every part of the command, determine whether one part is a parameter or header and choose the corresponding CommandRequest.
* We have another structure of handlers - there is an abstract superclass of all get-handlers in order to not repeat common parts of header features.
We also have another abstract superclass for a sequence result handlers, which is a subclass of GetHandler. It's used to extract common parts from the classes,
  that should implement paging feature.
* Every header returns an instance or an ArrayList of a model class, which are - User, Route, Sport and Activity. 
* There are also classes that are used to format and store parameters from command - Parameters, PathParameters and Headers.
Each of them has a Hashmap with name of corresponding parameter and value.
They are passed and validated in every handler according to handler's specific conditions.
* Headers are passed to CommandResults, in order to determine how to return our Models passed by a handler.

PHASE 3
* Command processing through the console works the same as in the previous Phases.
* The new functionality, which is http request processing was added. According to it, we have now special command - LISTEN / 
which starts listening the server. We're processing http requests in AppServlet. It is the bridge between server and the rest of our application.
After this, everything works exactly the same as in the console version.
* We can now navigate through application with URI and also using Postman.

PHASE 4
* Now, except the simple GET commands obtained from the URI or Postman, we are capable of processing the POST commands.
We are using html forms and buttons to write and submit the parameters needed for each posting and then retrieving them from the body of http request.
* Then, we are using the Parameter class to write each parameter down to the map from the whole input string.

### Command routing

PHASE 1

* The router holds a multimap of Method and Tuple composed with PathTemplate and corresponding handler.
* Using the multimap feature, after extracting values corresponding to the key, we receive an ArrayList of Tuples. Then, iterating through this list, we are looking for the correct PathTemplate.
* Whole process of matching Path to PathTemplate is done in Router - because we didn't want Path to have any information about PathTemplates held in Router.
* Process is simple - we are splitting Path and PathTemplate parameters according to regex "/" and then checking if every segment, except those, which starts with "{" and ends with "}", is the same in Path and PathTemplate. In the case with "{...}", we are adding the corresponding parameter from Path segment to te return ArrayList.

PHASE 2
* The router works exactly the same as in Phase 1.

PHASE 3
* The router works exactly the same as in previous phases. 
* We also have two Servlets, which are used to ensure the http connection. One is responsible for GET commands for the only one response information, e.x. getUserById and the other one for multiple response information, e.x. getUsers.
* They are responsible for finding the route for the path passed in the http request and then pass it to CommandExecutor to process the whole command.

PHASE 4
* The router functionality is the same as in previous phases.
* Now, we have only one Servlet responsible for all the http request. To make that happen we are using the Router functionality, which helps us to simplify the code without any need to repeat the same actions in servlets.
* Servlet is now ensuring the connection between http server and the application.


### Connection Management

PHASE 1

* We use PGSimpleDataSource as DataSource, we create an object of Connection class using getConnection() method, we make queries on the database using PreparedStatement, we close the connection each time in handler.

PHASE 2
* Data access is handled the same way as in the phase 1.

PHASE 3
* Data access works exactly the same as in previous phases.

PHASE 4
* The connection management works the same as in previous phases.

### Data Access

PHASE 1
* Connection to the database is prepared in main method, but the process of opening and closing connection is held only in Handlers.
* Queries to database are made in handlers directly.

PHASE 2 
* Now we've added classes which are responsible for processing strings corresponding to each segment of the command.
* We have Parameters, PathParameters and Headers classes. They are holding all information needed to run the command properly.
* They consist of HashMap with key being a string name of the parameter, and value being the string value corresponding to the parameter name.
* Then, in each handler the parameters are being validated and check if for example, parsing to int is possible.

PHASE 3
* It is done the same way as it was in the previous phase.

PHASE 4
* It works the same way as in the previous phases.

### Error Handling/Processing

PHASE 1

* Only exceptions (SQLException) in our application can be thrown in Handlers. Every Handler execution is surrounded by try-catch construction.
* In addition, if the application receive wrong arguments from the user, it always displays the message to the users and waits for another input. If it is possible, it also displays which arguments caused the error.

PHASE 2
* Now in almost every handler (except the delete-one) we are using try with resources - that's how we are avoiding bugs resulting from not closing the connection.
* Also, every execution of handler is surrounded by try-catch construction.
* In every handler, we are validating parameters - we are checking if parsing is possible for the given input string. 
If it is not, we are adding the name of parameter to the printing message.

PHASE 3
* While application is running, we're checking the possible errors (the same as in the previous phases).
  If any occurs, we return the appropriate result with the message about the error.
* According to new functionality, we handle http response status codes - we are checking the result handler and sending the appropriate code to the server.

PHASE 4
* In this phase we've added the Log functionality which ensures the communication between the client, and the app without any need to break the control flow.
* In the cases when some error occurs, e.x. the server has already been started the information about that is being printed.
* It is not only used to inform the client about errors, but also to inform about some ongoing actions - e.x. information about starting or stopping the whole application.

## Critical Evaluation

(_enumerate the functionality that is not concluded and the identified defects_)

(_identify improvements to be made on the next phase_)

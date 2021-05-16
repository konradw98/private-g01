# Phase 2 Technical Report

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

(_describe the command handling interface_)

(_describe any additional classes used internally by the command handlers_)

(_describe how command parameters are obtained and validated_)

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
* Headers are passed to CommandResults, in order to determine how to return our Models passed by handler.

### Command routing

(_describe how the router works and how path parameters are extracted_)

PHASE 1

* The router holds a multimap of Method and Tuple composed with PathTemplate and corresponding handler.
* Using the multimap feature, after extracting values corresponding to the key, we receive an ArrayList of Tuples. Then, iterating through this list, we are looking for the correct PathTemplate.
* Whole process of matching Path to PathTemplate is done in Router - because we didn't want Path to have any information about PathTemplates held in Router.
* Process is simple - we are splitting Path and PathTemplate parameters according to regex "/" and then checking if every segment, except those, which starts with "{" and ends with "}", is the same in Path and PathTemplate. In the case with "{...}", we are adding the corresponding parameter from Path segment to te return ArrayList.

PHASE 2
* The router works exactly the same as in Phase 1.
### Connection Management

(_describe how connections are created, used and disposed_, namely its relation with transaction scopes).

PHASE 1

* We use PGSimpleDataSource as DataSource, we create an object of Connection class using getConnection() method, we make queries on the database using PreparedStatement, we close the connection each time in handler.

PHASE 2
* Data access is handled the same way as in the phase 1.

### Data Access

(_describe any created classes to help on data access_).

PHASE 1
* Connection to the database is prepared in main method, but the process of opening and closing connection is held only in Handlers.
* Queries to database are made in handlers directly.

PHASE 2 
* It is done the same way as it was in the phase 1.
### Error Handling/Processing

(_describe how errors are handled and communicated to the application user_).

PHASE 1

* Only exceptions (SQLException) in our application can be thrown in Handlers. Every Handler execution is surrounded by try-catch construction.
* In addition, if the application receive wrong arguments from the user, it always displays the message to the users and waits for another input. If it is possible, it also displays which arguments caused the error.

PHASE 2
* Now in almost every handler (except the delete-one) we are using try with resources - that's how we are avoiding bugs resulting from not closing the connection.
* Also, every execution of handler is surrounded by try-catch construction.
* In every handler, we are validating parameters - we are checking if parsing is possible for the given input string. 
If it is not, we are adding the name of parameter to the printing message.
## Critical Evaluation

(_enumerate the functionality that is not concluded and the identified defects_)

(_identify improvements to be made on the next phase_)

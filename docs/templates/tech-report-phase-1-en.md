# Phase 1 Technical Report

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

* Command Processing begins in InteractiveMode class, input is validated there and prepared for the following actions. (phase1)
* If command is correct, the corresponding Handler is called. Otherwise, the user is informed about the mistake in command. (phase1)
* We have 15 handler, each for desired database queries. (phase1)
* In addition, PostHandlers have ArrayList of parameters taken from an input. In these handlers parameters are also validated. (phase1)

### Command routing

(_describe how the router works and how path parameters are extracted_)
* The router holds a multimap of Method and Tuple composed with PathTemplate and corresponding handler. (phase1)
* Using the multimap feature, after extracting values corresponding to the key, we receive an ArrayList of Tuples. Then, iterating through this list, we are looking for the correct PathTemplate. (phase1)
* Whole process of matching Path to PathTemplate is done in Router - because we didn't want Path to have any information about PathTemplates held in Router. (phase1)
* Process is simple - we are splitting Path and PathTemplate parameters according to regex "/" and then checking if every segment, except those, which starts with "{" in PathTemplate. In that case, we are adding the corresponding parameter from Path segment to te return ArrayList. (phase1)

### Connection Management

(_describe how connections are created, used and disposed_, namely its relation with transaction scopes).

We use PGSimpleDataSource as DataSource, we create an object of Connection class using getConnection() method, we make queries on the database using PreparedStatement, we close the connection each time in handler. (phase1)


### Data Access

(_describe any created classes to help on data access_).
* Connection to the database is prepared in main method, but the process of opening and closing connection is held only in Handlers. (phase1)
* Queries to database are made in handlers directly. (phase1)

### Error Handling/Processing

(_describe how errors are handled and communicated to the application user_).
* Only exceptions (SQLException) in our application can be thrown in Handlers. Every Handler execution is surrounded by try-catch construction.  (phase1)
* In addition, if the application receive wrong arguments from the user, it always displays the message to the users and waits for another input. If it is possible, it also displays which arguments caused the error. (phase1)

## Critical Evaluation

(_enumerate the functionality that is not concluded and the identified defects_)

(_identify improvements to be made on the next phase_)

# Phase 1 Technical Report

## Introduction

This document contains the relevant design and implementation aspects of LS project's first phase.

## Modeling the database

### Conceptual model ###

The following diagram holds the Entity-Relationship model for the information managed by the system.

![alt text](https://github.com/konradw98/private-g01/docs/DB_SCHEMA_LS.png)

We highlight the following aspects:

* Problem with mapping relationships between tables.

The conceptual model has the following restrictions:

* Does not map how pk behaves. 
* Does not show format at date.

    
### Physical Model ###

The physical model of the database is available in [DB SCRYPT](https://github.com/konradw98/private-g01/src/test/sql/scryptDb.sql).

We highlight the following aspects of this model:

* Problem with application of self-incrementing PK.
* Select appropriate data types.
* Select appropriate constraints for example not null type.

## Software organization

### Command Processing

(_describe the command handling interface_)

(_describe any additional classes used internally by the command handlers_)

(_describe how command parameters are obtained and validated_)

### Command routing

(_describe how the router works and how path parameters are extracted_)

### Connection Management

(_describe how connections are created, used and disposed_, namely its relation with transaction scopes).

### Data Access

(_describe any created classes to help on data access_).

(_identify any non-trivial used SQL statements_).

### Error Handling/Processing

(_describe how errors are handled and communicated to the application user_).

## Critical Evaluation

(_enumerate the functionality that is not concluded and the identified defects_)

(_identify improvements to be made on the next phase_)

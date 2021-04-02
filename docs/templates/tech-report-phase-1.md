# Phase 1 Technical Report

## Introduction

This document contains the relevant aspects of the design and implementation of phase 1 of the LS project.

## Modelling the database

### Conceptual modelling ###

The following diagram shows the entity-association model for the information managed by the system. 

(_include an image or a link to the conceptual diagram_)

The following aspects of this model stand out:

* (_include a list of relevant design issues_)

The conceptual model also has the following constraints:

* (_include a list of relevant design issues_)
    
### Physical modelling ###

The physical model of the database is present in (_link to the SQL script with the schema definition_).

The following aspects of this model stand out:

* (_include a list of relevant design issues_)

## Software organisation

### Command processing

(_describe the command handling interface_)

(_describe any additional classes used internally by the command handlers_)

(_describe how command parameters are obtained and validated_)

### Routing of commands

(_describe how the router works and how path parameters are extracted_)

### Connection management

(_describe how connections are created, used and disposed_, namely its relation with transaction scopes).

### Data access

(_describe any created classes to help on data access_).

(_identify any non-trivial used SQL statements_).

### Error processing

(_describe how errors are handled and communicated to the application user_).

## Critical evaluation

(_enumerate the functionality that is not concluded and the identified defects_)

(_identify improvements to be made on the next phase_)

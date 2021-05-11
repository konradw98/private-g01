package pt.isel.ls.commandresults;

public class OptionResult implements CommandResult{
    @Override
    public void print() {
        System.out.println("""
                   AVAILABLE COMMANDS:
                   COMMAND STRUCTURE: {method} {path} {headers} {parameters}
                       Where :
                           METHOD defines the type of action to perform
                           PATH defines the resource on which the command is executed
                               Example: /users or /users/1500
                           ACCEPT HEADER defines the format for the outputted representation:
                               accept:text/plain- plain text, printed in teh standard output
                               accept:text/html - Hypertext Markup Language (HTML).
                               accept:application/json - Javascript Object Notation (JSON)
                           FILE-NAME HEADER defines the file system location for the outputted representation.
                               If this header is absent, the representation is written into the standard output.
                           The headers should be separated by '|' char.
                           Example: accept:text/plain|file-name:users.txt
                           PARAMETERS are specific values defined for every command, separated by '&' char
                                
                   GET commands:
                       GET command structure: GET {path} {headers} {paging}
                           Where:
                               PAGING is defined by two parameters:
                                   top - length of the subsequence to return,
                                   skip - start position of the subsequence to return.
                               Example: skip=6&top=3
                                
                       1. GET /users - returns the list of all users    
                       2. GET /users/{uid} - returns the details for the user identified by uid
                       3. GET /routes - returns the list of all routes
                       4. GET /routes/{rid} - returns the details for the route identified by rid
                       5. GET /sports - returns a list with all sports
                       6. GET /sports/{sid} - returns the detailed information for the sport identified by sid
                       7. GET /sports/{sid}/activities - returns all the activities for the sport identified by sid
                       8. GET /sports/{sid}/activities/{aid} - returns the full information for the activity aid
                       9. GET /users/{uid}/activities - returns all the activities made from the user identified by uid
                       10. GET /tops/activities - returns a list with the activities, given the following parameters:
                                sid - sport identifier
                                orderBy - order by duration time, this parameter only has two possible values - ASC/DESC
                                date - activity date (optional)
                                rid - route identifier (optional)
                                distance - return the activities associated to a route 
                                    with a distance larger than the value of the parameter (optional)
                                
                   POST COMMANDS:
                        POST command structure: POST {path} {headers} {parameters}
                        
                       1. POST /users - creates a new user, given the following parameters:
                               name - the user's name
                               email - the user's unique email
                          The command returns the user's unique identifier
                       2. POST /routes - creates a new route, given the following parameters:
                               startLocation - the route start location name
                               endLocation - the route end location name
                               distance - the route distance in km
                          The command returns the route unique identifier
                       3. POST /sports - creates a new sport, given the following parameters:
                               name - the sport's name
                               description - the sport's description
                          The command returns the sport's unique identifier 
                       4. POST /sports/{sid}/activities - creates a new activity for the sport identified by sid, 
                          given the following parameters:
                               uid - user identifier
                               duration - duration time in the format hh:mm:ss.fff
                               date - activity date in the format yyyy-mm-dd
                               rid - route identifier (optional)
                          The command returns the physical activity unique identifier
                               
                   OTHER COMMANDS:
                       1. DELETE /users/{uid}/activities - removes the identified activities from user uid, 
                           given the following additional parameter:
                               activity - the set of activities to remove
                       2. EXIT / - terminates the application
                       3. OPTION / - presents a list of available commands\s"""
                );

    }
}

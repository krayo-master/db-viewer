# db-viewer simple

IN PROGRESS..

Small springboot/react "db browser" app that I was asked to do in the past (specific description of what it should do was provided). 
I was able to pick one database of my choosing to implement this for and I went with PostgreSQL.

Server url path redirects you to swagger (default) doc page with the list of apis.

//TODO notes

Logging

possibly refactor complex (or duplicated) metadata controllers logic and move to service layer

prevent sql injection in metadata controller (feed list of valid options and compare)

security (users, authentication, password encryption)

Tests - invalid scenarios

Client - fix toggling, error handling, formatting

...

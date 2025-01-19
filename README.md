# Note taking REST Api with Spring Webflux + H2 file DB + Security basic auth
REST API that allows users to post notes which are saved in a database.<br>
Users need to register or login before being allowed to add new notes.<br>
When logged in users can change their username and delete or edit their notes.<br>
Admins can delete notes regardless of their author and can delete any other user who are not also admins.<br>
Admins can also grant admin privilege.<br>
# Project structure
     Main
         Config
            PageableWebFluxConfiguration - Configures Pagination
            R2dbcConfig - Configures the database to work with non-blocking reactive drivers
            SecurityConfig - Configures password encoder and paths which require authentication
            SecurityUtils - Contains method that returns currently logged in user's username
            
         Controller
            AdminController - Handles requests about admins
            NoteController - Handles requests about notes
            UserController - Handles requests about users
            
         DTO
            NoteDTO
            UserDTO
            
         Exception
            DifferentUserException - Custom exception thrown when a user tries to edit/delete another user's notes
            
         Model
            Note - Models the note table in the database
            Role - Defines User and Admin roles
            User - Models the user table in the database
            
         Repository
            NoteRepository - handles communication between the note table in the database and the application
            UserRepository - handles communication between the user table in the database and the application
            
         Service
            AdminService - Interface for all methods that handle the business logic on admins
            AdminServiceImpl - Implementation of the interface
            CustomUserDetailsService - Returns UserDetails from a username
            NoteService - Interface for all methods that handle the business logic on notes
            NoteServiceImpl - Implementation of the interface
            UserService - Interface for all methods that handle the business logic on users
            UserServiceImpl - Implementation of the interface
            
     Test - Unit tests
         Service
            AdminServiceUnitTests
            NoteServiceUnitTests
            UserServiceUnitTests
            
         Controller
            NoteControllerUnitTests
            UserControllerUnitTests
# Paths and Methods
    Server running on port 8080
     Users
        GET /users - Returns users, supports pagination (default is size = 10, page = 0). Unauthenticated
        GET /users/{id} - Returns user by their ID - Unauthenticated
        POST /users - Adds user, requires name, email and password where name and email should be unique - Unathenticated
        PATCH /users/{id} - Updates username. Users can't update other users' usernames.
        DELETE /users/{id} - Deletes user. Users can't update other users' usernames.
    
     Notes
        GET /notes - Returns notes, supports pagination (default is size = 10, page = 0). Unauthenticated
        GET /notes/{id} - Returns notes by their ID - Unauthenticated
        POST /notes - Adds note and associates it with it's user, requires title and text
        PUT /notes/{id} - Edits note. Users can't update other users' notes.
        DELETE /notes/{id} - Deletes note. Users can't delete other users' notes.

     Admin - requires role Admin
        DELETE /admin/users/{id} - Deletes any user except other admins by id.
        DELETE /admin/notes/{id} - Deletes any note.
        POST /admin/users/{id} - Makes user admin.
### Dependencies

* Spring boot webflux
* Spring boot r2dbc
* Spring boot security
* H2 Database
* Lombok
* JUnit
* Mockito

## Initial DB
Admin account has username = "kaloyan" and password = "1".<br>
Database has no other entries.

## License
Distributed under the MIT License. See LICENSE.md for more information.

## Authors

[Kaloyan Pavlov](https://github.com/KaloyanPavlov4)

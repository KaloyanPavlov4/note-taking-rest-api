# Note taking REST Api with Spring Webflux + H2 file DB + Security basic auth
    REST Api for taking notes. Only registered users can post notes and delete them. Supports changing usernames and editing notes. Admins can delete any user (except other admins) and any note. They can also make other users admins.
# Paths and Methods
    Server running on port 8080
    ## Users
        GET /users - Returns users, supports pagination (default is size = 10, page = 0). Unauthenticated
        GET /users/{id} - Returns user by their ID - Unauthenticated
        POST /users - Adds user, requires name, email and password where name and email should be unique - Unathenticated
        PATCH /users/{id} - Updates username. Users can't update other users' usernames.
        DELETE /users/{id} - Deletes user. Users can't update other users' usernames.
    
    ## Notes
        GET /notes - Returns notes, supports pagination (default is size = 10, page = 0). Unauthenticated
        GET /notes/{id} - Returns notes by their ID - Unauthenticated
        POST /notes - Adds note and associates it with it's user, requires title and text
        PUT /notes/{id} - Edits note. Users can't update other users' notes.
        DELETE /notes/{id} - Deletes note. Users can't delete other users' notes.

    ## Admin - requires role Admin
        DELETE /admin/users/{id} - Deletes any user except other admins by id.
        DELETE /admin/notes/{id} - Deletes any note.
        POST /admin/users/{id} - Makes user admin.
# Initial DB
    ## Admin account has username = "kaloyan" and password = "1".
    ## Database has no other entries.

# uber-rent
Car rental &amp; reservation service &amp; GUI. 
Final project deliverable for relational databases course @UBC.

### Logging In

You will need to provide your UBC SQLPlus credentials in order to run database transactions.
Provide your username and password in the `login()` located in `src/database/DBConnectionHandler.java`.

In the root directory run `sqlplus` to start the sql interpreter

`user-name: ora_[cwl]@stu`

`password: a[student_number]`

### To make the environment more user friendly run...
`rlwrap sqlplus`

`set linesize 2000`

`set wrap off`

### From root you can run commands...
`start database/ubr_create_tables.sql;`

`start database/ubr_drop_tables.sql;`

`start database/populate.sql;`

### To see what tables currently exist in the DB run...

`SELECT table_name from user_tables;`

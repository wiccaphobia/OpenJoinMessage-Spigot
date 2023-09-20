# OpenJoinMessage-Spigot
An open source plugin that lets you customize server messages whenever a player joins or leaves

!! SETUP !!
Since this is my first time making plugins in java i've had to rely on a database instead of a config file (i'm trying to make a script that will setup "almost" everything automatically).

- Step 1: you need to download MariaDB Server from https://downloads.mariadb.org/ and make sure you have any SQL database software like HeidiSQL that comes installed along with MariaDB.
- Step 2: once you have opened your database software you will then create a new session and inside it you need to create a new table (named as you like, ex: ojm), then create another table
          but this time you have to name it "custommessages" which will contain two VARCHAR values: first one named "Type" (AND IT HAS TO BE SET AS PRIMARY + SET AS NO DEFAULT); and the second one "Value".
  ![image](https://github.com/Hxlixd/OpenJoinMessage-Spigot/assets/90792340/7fb462f7-c06a-4c35-b87c-a4a58d13a115)
- Step 3: of course as there is no setup command in the plugin yet, you will have to create two environment variables and name them "OJM_DB_USER" and "OJM_DB_PW".
  ![image](https://github.com/Hxlixd/OpenJoinMessage-Spigot/assets/90792340/890cb2a8-3eb6-4cf1-a0d9-43b4b23610f3)
- Step 4: at this point you're basically good to go, start the database and then the server to avoid any error.



If you have followed the instructions correctly you should achieve this result as you start the plugin:
![image](https://github.com/Hxlixd/OpenJoinMessage-Spigot/assets/90792340/446810e0-199e-4842-b73b-c0f5423c5652)

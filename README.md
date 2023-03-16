Java Console Framework that uses a MySQL 8.0 DB to store the data for a Car Rental Stystem.

Architectrure is based on the MVC pattern with the model being represented by POJOs and DAOs. 

Database architecture:

![db2](https://user-images.githubusercontent.com/91593275/225699036-044a98e8-f1ec-47f0-85f9-61e9411fc743.jpg)

There are 2 types of accounts:

>Admin

>Client

Clients can rent vehicles and access their own rental history.

Admins can access all cars and registered clients, add or delete entries.

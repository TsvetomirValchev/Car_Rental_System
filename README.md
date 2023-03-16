Java Console Framework that uses a MySQL 8.0 DB to store the data for a Car Rental Stystem.

Architectrure is based on the MVC pattern with the model being represented by POJOs and DAOs. 

Database architecture:

![db](https://user-images.githubusercontent.com/91593275/225697794-7da60b1e-7e17-47f2-be44-92a23e9a6b89.png)

There are 2 types of accounts:

>Admin

>Client

Clients can rent vehicles and access their own rental history.

Admins can access all cars and registered clients, add or delete entries.

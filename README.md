# Frorace E-Commerce Store

This project was created with Spring Boot (https://spring.io/projects/spring-boot).

## Development server

Service run and is exposed on  `http://localhost:8080/`. 

## Back-End Service

Service uses and response with Json + HAL (Hypertext Application Language) using Hateoas library.

## Front-End client

The front-end application can be found at: https://github.com/nucleus210/florance-client

## Security

Service uses Jwt token filter that intercept all incoming requests. This way unauthorized users are not allowed to access 
to protected resources. 
On second level is implemented Cors Filter that intercept all incoming requests from unknown sources.
Http Only is disabled. Using Jwt token we can store token data into the browser localStorage or sessionStorage.
Jtw token expires after a certain amount of time. 

## Database

Project is built on relational database using MySQL server. Database is stores only data without any Blob format. 
Files are stored in HDD storage.

## Main classes

Application has Product, Category, SubCategory, Order, OrderItems, User, Login, Register, Profile, Address, Supplier, 
BlogPost, Slider etc. classes for maintaining database.

//TODO: Adding Interceptors classes

## Running application

First need to be edited application.yml file and to be entered username and password for local MySQL database.




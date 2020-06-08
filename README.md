# Quiz-Service
is an implementation of some educational project called 'Web Quiz Engine' 
from hyperskill.org (https://hyperskill.org/projects/91)

Used technologies: Spring Rest, Spring Data JPA, Spring Security, h2 database, JSON

Quiz service is a rest service that can be used for handling quizzes.

To start using the service, first, you need to register.
It may be done by sending a request to "/api/register" with a json request body 
with the following String attributes: "email", "password".

The email must conform to the regex "^\\w+@\\w?mail\\.\\w{2,3}",
and the password must consist of five or more characters. 
In case the email and passsword meet the requirements and there are no registered users 
with the same email, the server responds with the status code 200, otherwise - 400.



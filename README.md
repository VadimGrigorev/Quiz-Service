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

All of the endpoints the service provides require basic authentication. To create a 
quiz you must send a post request to "api/quizzes" with a json request body containing
the following keys: "title"(String), "text"(String), "options"(String[]) and "answer"(int[]).
The server responds with a json with the same fields you entered and an id of the created quiz.

To see the quiz you have created you need to send a get request to "api/quizzes/{id}".
To solve the quiz you must send a post request to "api/quizzes/{id}/solve" with a json containing
a field "answer"(int[]) which may be empty if none of the provided cases are true.

The service also provide some functions like:
#getting all of the quizzes at GET "api/quizzes" sent with a "page" parameter specifying the page number
#getting the info about compeltions of quizzes at GET "api/quizzes/completed" with a "page" parameter
#deleting a quiz at DELETE "api/quizzes/{id}

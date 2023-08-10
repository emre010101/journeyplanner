# journeyplanner
This project is a journey planner application that integrates Google Map API and GPT API. Users can create, view, and share their journeys, comment on other user's journeys, and like them.

![image](https://github.com/emre010101/journeyplanner/assets/118063573/4ca92b18-1259-40e0-9f52-01e9832ff4c0)

Getting Started
Follow these instructions to get a copy of the project up and running on your local machine.

Prerequisites
Before you begin, ensure you have the following installed:

Java (version-17)
Spring Boot (version 3.0.1)
Any SQL database server (MySQL, PostgreSQL, etc.) It should support JSON for column type
Configuration
Database Configuration:

Configure your database by editing the following properties in the application.properties (or whichever config file you have):

properties
Copy code
spring.datasource.url=YOUR_DATABASE_URL
spring.datasource.username=YOUR_DATABASE_USER
spring.datasource.password=YOUR_DATABASE_PASSWORD
spring.jpa.hibernate.ddl-auto=update
API Keys:

Google Map API: Acquire your API key from Google Cloud Console.
It needs to be replaced with my api key in community.html file.
GPT API: Obtain your API key from OpenAI. GPT-3 text-davinci-003 is used.
Insert these keys in the appropriate sections in your application's configuration.

Installation
Clone the repository:

bash
Copy code
git clone https://github.com/emre010101/journeyPlanner.git
Navigate to the project directory:

bash
Copy code
cd JourneyPlanner
Install dependencies and run:

bash
Copy code
./mvnw spring-boot:run
This command will fetch all necessary dependencies and start the server.

Usage
WebPage is designed to communicate with this API and it's in resources directory.
The endpoints are need to replaced if you are running locally
journey-planner.azurewebsites.net it's deployed so you can refer to this url as well.

Contributing
If you'd like to contribute, please fork the repository and use a feature branch. Pull requests are warmly welcome.

License
This project is licensed under the MIT License - see the LICENSE.md file for details.

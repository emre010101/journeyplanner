# journeyplanner
This project is a journey planner application that integrates Google Map API and GPT API. Users can create, view, and share their journeys, comment on other user's journeys, and like them.

![ezgif com-video-to-gif](https://github.com/emre010101/journeyplanner/assets/118063573/52a1f556-549e-4aa0-91c6-af0c6dd0ff11)

<h1>Getting Started</h1>
<p></p>Follow these instructions to get a copy of the project up and running on your local machine.</p>

<h2>Prerequisites</h2>
<p style="bold">Before you begin, ensure you have the following installed:</p>
<ul>
<li>Java (version-17)</li>
<li>Spring Boot (version 3.0.1)</li>
<li>>Any SQL database server (MySQL, PostgreSQL, etc.) It should support JSON for column type</li>
<li> Gpt API Key</li>
<li>Google Map API Key</li>
</ul>
<h2>Configuration</h2>
<h3>>Database Configuration:</h3>
<p style="bold">Configure your database by editing the following properties in the application.properties (or whichever config file you have):</p>

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

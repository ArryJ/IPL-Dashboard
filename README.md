# IPL DashBoard Demo Backend
## Overview
This is a backend for the IPL Dashboard Demo on the Java Brains youtube channel.
The backend is developed as a REST API such that it can be integrated easily with 
any front end application. 
## Database Decisions
Here, the database chosen was an in-memory embedded H2 rdbms. The database is 
populated from a CSV file using Spring Batch. Since, the dataset was not that
huge, it can be loaded into the database from the csv file every time the application
starts in a very less time using spring batch.
## Functions
This API will be returning -
1. Match List for every team. 
   1. REST Endpoint &#8594; `/team/{teamName}/matches?year='year'`
   2. Returns `List<Match>` data for a particular year.
   3. {teamName} and 'year' are PathVariables and Query parameter respectively.
2. Team List
   1. REST Endpoint &#8594; `/team`
   2. Returns `List<Team>`
3. Fetches team with latest 4 matches played.
   1. REST Endpoint &#8594; `/team/{teamName}`
   2. Returns `Team` object
## Architecture
The spring boot backend has been divided into just 2 layers.
1. Controller layer &#8594; This layer maps the resources to call the
repository method that returns the appropriate response. There is only 1 controller that is
`TeamController` present.
2. Data Layer &#8594; This layer consists of 2 entities - Team & Match. 
Consecutively there are 2 repositories `MatchRepository` and `TeamRepository` to 
connect to the in-memory database using Spring JPA hibernate. Which in
turn runs JPQL queries to extract desired data from the H2 database.
3. The `data` package consists of the Spring batch configurations, jobs instructions,
Reader, writer and processor logic for each step. Spring batch reads data in chunks, processes them, and 
then writes them all at once into the database by mapping `MatchInput` to `Match` object. 
4. The `Team` gets populated after the job finishes.
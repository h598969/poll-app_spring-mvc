****DAT250 Assignment 4****
This assignment/experiment went rather smoothly in my opinion.

I did have to put in a little bit of reasearch to get a hang of JPA and it's notations. I went a little back and forth testing out stuff before figuring out how to make it work.
Eventually I managed to add all the necessary annotations that made the test cases pass.

The biggest headache for this assignment was figuring out how to inspect the database tables and how they were created. I managed to find out about some properties I could include in the tests:

- .property("hibernate.show_sql", "true")
- .property("hibernate.format_sql", "true")
- .property("hibernate.use_sql_comments", "true")

These properties made it possible to inspect information about the database and tables created in the logs.

Also you could see the tables that were being created and the votes given if you went into debug mode when running the tests.

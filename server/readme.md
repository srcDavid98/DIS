# Exam 2019

## How to initialize
1. Open your terminal (or git bash or whatever) and navigate to the folder you want to store your project in.
2. Clone this project
3. Open IntelliJ and import project with existing sources and maven. Remember to import maven. This can be done by clicking the pop’up and selecting `import`. Else you can right click the `pom.xml` file and select `maven` and `import`
4. Add new configuration in the top of IntelliJ. Select `maven`. Set the commandline to 'tomcat:run'
5. Edit config.json to fit with your database setup.
6. Open a connection to you MySQL database (can be done directly through the terminal, or Workbench or Sequel Pro ) and run the `init.sql` script which can be found in the resources folder.
7. Run your server
8. If everything works as it should you can go to `localhost:8080/exam/customers` which should retrieve one customer.

## Fix the To Do’s 
In IntelliJ you can select the TODO tab in the bottom (or search for TODO) which will list all the items, that you have to complete.
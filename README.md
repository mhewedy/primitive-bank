#Primitive bank application

It supports following functionalists:

* check balance of account
* deposit money to account
* withdraw money from account
  
> Real world bank applications should enforce heavy security rules, but due to this app is a demo, 
I just implemented the required functions.
  
### Backend
Backend is written in Java (Spring-boot) application and it uses h2 in memory database.


### Frontend
Frontend is an Angular 5 *angular-cli* Application, it communicate to the Backend via REST api

The front end source code resides at: `src/main/resources/static/web`

#### Software requriements
* JDK 1.8
* Angular CLI ([https://cli.angular.io/](https://cli.angular.io/)) (which requires node and npm installed)
* Lombok ([https://projectlombok.org/](https://projectlombok.org/)) to be installed in the IDE (recommended 
to run the app in the IDE)

#### Start the demo app
the script `start-demo.sh` uses to start the application (backand and frontend) in demo mode.

Then open the browser at: [http://localhost:4200/](http://localhost:4200/)
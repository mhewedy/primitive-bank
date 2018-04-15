#Primitive bank application

It supports following functionalists:

* check balance of account
* deposit money to account
* withdraw money from account
  
> Real world bank applications should enforce heavy security rules, but due to this app is a sample, 
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
use the script `start-demo.sh` to start the application (Backend and Frontend) in demo mode where 2 accounts with ID
1 and 2 are already created and ready to operate against.

The app will be running over: [http://localhost:4200/](http://localhost:4200/).

#### Deployment
Usually, Frontend and backend are written as separate modules. But in this sample app, I choose not to separate them for simplicty.

Moreover, when the `mvn clan install` I did not consider running `ng dist` to do the `AOT` compilation.

There are maven plugins out there are used to call the `ng dist` as part of the maven lifecycle, but again since this app 
is not indented for production deployment, I didn't put any effort for archiving the angular artifacts with Java 
(also because some deployment methods prefere to separate the frontend from the backed with allows greater scalability).


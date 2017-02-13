##tracking-rest
==============

A restful tracking server is a JEE application

##Getting the code
You can clone the latest code onto your local machine with the following command.

    https://github.com/git@github.com:PavelZhelnov/tracking.git
    
##Building the application
Tracking uses Maven (http://maven.apache.org/) for building and packaging the project for deployment.
    mvn --version
    
To build the Web Application make sure you are in the project directory and type the following command:
    mvn clean install
    
This will create the deployable tracking.war file

##Deploying to Tomcat (Contiuous Integration/Development/Local)

Either using the Tomcat Deployment Manager UI or copying the WAR into the Tomcat webapps directory should be sufficent
to deploy onto Tomcat. If using Eclipse please refer to the instructions above about Building in Eclipse


## NOTE  DEV Enviroment args
-XX:PermSize=256M -XX:MaxPermSize=256M -Xms2000m -Xmx2000m -Dtracking.debug.mode=false

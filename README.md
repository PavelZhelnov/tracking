##tailoring-rest
==============

A restful tailoring server is a JEE application

##Getting the code
You can clone the latest code onto your local machine with the following command.

    https://github.com/MensWearhouse/tailoring-rest.git
    
##Building the application
Tailoring uses Maven (http://maven.apache.org/) for building and packaging the project for deployment.
    mvn --version
    
To build the Web Application make sure you are in the project directory and type the following command:
    mvn clean install
    
This will create the deployable tracking.war file

##Deploying to Tomcat (Contiuous Integration/Development/Local)

Either using the Tomcat Deployment Manager UI or copying the WAR into the Tomcat webapps directory should be sufficent to deploy onto Tomcat. If using Eclipse please refer to the instructions above about Building in Eclipse


## NOTE  DEV Enviroment args
-XX:PermSize=256M -XX:MaxPermSize=256M -Xms2000m -Xmx2000m -Dtailoring.debug.mode=false
-Dtailoring.env=dev -Dlog4j.configuration=log4j-dev.xml

## NOTE precommit hooks 
each commit version is autoincrements via .git/hooks/pre-commit , put file  config/pre-commit  to .git/hooks and make it executable

## NOTE Tomcat SETUP
Tomcat must contain following jars in endorsed dir :
ucp.jar - find it in config/ dir
copy ojdbc-16.jar to tomcat lib dir

config/test_tomcat contains configuration files
 
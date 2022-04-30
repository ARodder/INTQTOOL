# Interactive quizzing-tool backend
This is the backend server application for an interactive quizzing-tool.
Hosting the api and serving any content needed for the frontend application.

## Installation
Clone the application from github with the following command to the host machine
```bash
git clone https://github.com/ARodder/INTQTOOL-Backend.git
```
Then cd in to the project folder and add a prod.env file containing the two environment-variables below.
```env
MYSQL_DEVELOPER_USERNAME= (username for mysql user)
MYSQL_DEVELOPER_PASSWORD= (password for mysql user)
MYSQL_DEVELOPER_IP= (Server ip for the mysql server, with port, and full path)
```

If you are using custom domain you should go into te SecurityConfig.java and in the CORSFilter add your domain to the 
allowed origins.

Depending on if you are enabling HTTPS or not you need to uncomment the following lines from the application.properties
file and generate the required keystore.p12 file. (A tutorial for this can be found at 
https://web-tek.ninja/cookbook/https-spring-boot/) 
```properties
server.port=8443
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=
server.ssl.keyStoreType= PKCS12
server.ssl.keyAlias= tomcat

server.ssl.enabled=true
```
Leave the password field empty if the keystore.p12-file has no password.

Next install docker.io and run the following command:
```bash
docker run --env-file /INTQTOOL/INTQTOOL-Backend/prod.env -it --name intqtool-backend -d -p 8443:8443 -v /INTQTOOL/INTQTOOL-Backend:/usr/src/mymaven -w /usr/src/mymaven maven mvn spring-boot:run
```

Certain paths such ass --env-file and -v will vary based on the placement of your folder. The -d flag can also be toggled
based on if you require the process to be daemonised or not.

## Usage
This application is meant to serve as an api for the interactive quizzing-tool frontend.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

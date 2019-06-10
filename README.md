# waes-diff
**WAES - Assignment Scalable Web**


## Application description and requirements
1. Provide 2 http endpoints that accepts JSON base64 encoded binary data on both endpoints
**{{host}}/v1/diff/<ID>/left and <host>/v1/diff/<ID>/right**
2. The provided data needs to be diff-ed and the results shall be available on a third endpoint
**{{host}}/v1/diff/<ID>**
3. The results shall provide the following info in JSON format
4. If equal return that
5. If not of equal size just return that
6. If of same size provide insight in where the diffs are, actual diffs are not needed.
7. So mainly offsets + length in the data


# Build
Application has been built using **Maven**, **java8** and **SpringBoot**. and requires **JDK8** and **Maven**

### Build command that needs to be executed from project root:
`maven clean install` 

This action will compile, run all the unit and integration tests and generate the **waes-diff.jar** artifact inside **/target** folder.


# Runtime
**waes-diff.jar** has been built to be runnable as:
 
1. stand alone java application using the command:  `java -jar target/waes-diff.jar`
2. run it in a **docker** (look at [*Docker Commands*](#docker-commands) section)
3. from any Java development IDE like Eclipse. Runnable class: **com.waes.application.WebDiffApplication** 

### Logging
Application logs will be generated inside **/logs** folder at the same directory level of the **waes-diff.jar**  
Log file pattern name is: **waes-diff-apl-{yyyy-MM-dd}-{sequencial}.log** 


# API Swagger
### JSON: {{host}}:9080/v2/api-docs
### HTML: {{host}}:9080/swagger-ui.html
 

# Application Testing

Once running, the application can be tested using either any REST client as Postman or from HTML Swagger page **{{host}}:9080/swagger-ui.html**


# Persistent layer

In order to be easily runnable, this version is using in memory data repository.    
For scalable production environment, it is strongly recommended to use some third part database. 
This can be a no-sql database due the data format, for example: MongoDB, Google Datastore, Google BigTable, etc...


# Docker Commands

Those are useful docker commands and pre-requires docker installation.
They should be run from project root.
 
### Build and register docker image from jar
`docker build -f Dockerfile -t "waes-diff:v1" .`

### remove docker image from docker repository
```
docker images
docker rmi {{IMAGE ID}}
```

### run docker image in container
`docker run  -p 9080:9080 --name waes-diff  -t "waes-diff:v1"` 

### remove running application from docker
`docker rm -f waes-diff`

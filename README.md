# waes-diff

WAES - Assignment Scalable Web

The assignment
• Provide 2 http endpoints that accepts JSON base64 encoded binary data on both
endpoints
o <host>/v1/diff/<ID>/left and <host>/v1/diff/<ID>/right
• The provided data needs to be diff-ed and the results shall be available on a third end
point
o <host>/v1/diff/<ID>
• The results shall provide the following info in JSON format
o If equal return that
o If not of equal size just return that
o If of same size provide insight in where the diffs are, actual diffs are not needed.
o So mainly offsets + length in the data

# Build
Application has been built using maven, java8 and SpringBoot.

build command executed from project root:
 -> maven clean install 

This action will compile, run all the unit and integration tests and generate the waes-diff.war artifact inside target folder.


# Runtime

waes-diff.war has been built to be runnable as:
 
 1- stand alone java application using the command: -> java -jar waes-diff.war
 2- deploy it in a application server like JBOSS
 3- run it in a docker

# API Swagger
Json format: <host>:8080/v2/api-docs
Html format: <host>:8080/swagger-ui.html


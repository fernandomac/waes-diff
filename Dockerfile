FROM openjdk:8-jre-alpine
EXPOSE 9080
ADD /target/waes-diff.jar waes-diff.jar
ENTRYPOINT ["java","-jar","waes-diff.jar"]
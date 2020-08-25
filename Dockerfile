FROM java:8
ADD target/*.jar translate.jar
VOLUME /tmp
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "translate.jar"]


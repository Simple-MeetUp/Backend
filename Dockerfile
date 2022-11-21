FROM amazoncorretto:11.0.14

EXPOSE 1998

ARG JAR_FILE=build/libs/*.jar

ADD ${JAR_FILE} walkreen_be_image.jar

ENTRYPOINT ["java","-jar","/walkreen_be_image.jar"]
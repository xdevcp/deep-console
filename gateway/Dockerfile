FROM 10.82.73.68:5000/openjdk:8-jre-alpine
MAINTAINER "tao.lv <tao.lv@datavaluable.com>"

ARG JAR_FILE=target/APP.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=Asia/Shanghai","-Dspring.profiles.active=dev","-Dserver.port=8080","-jar","/app.jar"]

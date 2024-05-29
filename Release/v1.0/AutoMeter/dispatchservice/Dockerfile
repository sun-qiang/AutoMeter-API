FROM openjdk:8
ENV TZ=Asia/Shanghai
USER root
WORKDIR /home/dispatchservice
COPY dispatchservice.jar dispatchservice.jar
COPY config/application.yml application.yml
ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx256m -Djava.security.egd=file:/dev/./urandom"
EXPOSE 8082
ENTRYPOINT java -jar $JAVA_TOOL_OPTIONS dispatchservice.jar --spring.config.location=application.yml
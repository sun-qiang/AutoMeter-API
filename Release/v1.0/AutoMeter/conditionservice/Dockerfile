FROM openjdk:8
ENV TZ=Asia/Shanghai
USER root
WORKDIR /home/conditionservice
COPY conditionservice.jar conditionservice.jar
COPY config/application.yml application.yml
ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx256m -Djava.security.egd=file:/dev/./urandom"
EXPOSE 8083
ENTRYPOINT java -jar $JAVA_TOOL_OPTIONS conditionservice.jar --spring.config.location=application.yml
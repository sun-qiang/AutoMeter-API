FROM openjdk:8
USER root
WORKDIR /home/slaverservice
COPY ./slaverservice.jar slaverservice.jar
COPY ./config/application.yml application.yml
COPY ./apache-jmeter-5.3 apache-jmeter-5.3
COPY ./servicejmxcase servicejmxcase
RUN chmod 777 /home/slaverservice/apache-jmeter-5.3/bin/jmeter
sed -i 's/\r$//' /home/slaverservice/apache-jmeter-5.3/bin/jmeter
#在环境准备好后修改apache-jmeter-5.3/bin/jmeter的文件格式为unix
#ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx256m -Djava.security.egd=file:/dev/./urandom"
ENV JAVA_TOOL_OPTIONS="-Djava.security.egd=file:/dev/./urandom"
EXPOSE 8081
ENTRYPOINT java -jar $JAVA_TOOL_OPTIONS slaverservice.jar --spring.config.location=application.yml
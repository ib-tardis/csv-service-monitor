FROM openjdk:15

LABEL MAINTAINER=imranbagwan
LABEL APPLICATION=MonitoringService
LABEL VERSION=0.1

WORKDIR /apps/monitor-service/

COPY monitor-service/target/monitor-service.jar monitor-service.jar

ENTRYPOINT ["java","-jar","monitor-service.jar"]
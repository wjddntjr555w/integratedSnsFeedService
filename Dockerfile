FROM openjdk:11.0.15-oraclelinux8

COPY wait-for-it.sh /wait-for-it.sh

COPY SnsFeedService.jar /app/

CMD java -jar /app/SnsFeedService.jar


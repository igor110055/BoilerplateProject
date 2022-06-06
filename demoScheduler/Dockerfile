FROM openjdk:11.0.10-jre
VOLUME /tmp
RUN pwd
#RUN chmod +x gradlew
#RUN pwd
#내가 계속 착각했었다.
#docker 컴파일이 실행되는 곳은 jenkins가 설치된 서버이다.
#RUN gradlew build
#jenkins 스크립트에 넣자.
#도커는 실행환경만이다.

#ARG JAR_FILE=demoWeb/build/libs/*.war
COPY build/libs/*.jar app.jar
EXPOSE 8090
# 외부에 노출할 포트 지정
ENTRYPOINT ["java","-jar","-Xms1024m","-Xmx1024m","/app.jar"]
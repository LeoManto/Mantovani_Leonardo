FROM openjdk:1.8.0

ADD ./build/distributions/it.unibo.ParkManagerService_s4-1.0
ENTRYPOINT ["./build/distributions/it.unibo.ParkManagerService_s4-1.0/it.unibo.ParkManagerService_s4-1.0/bin/it.unibo.ParkManagerService_s4.bat"]

CMD ["bash", "parkingSystem"]

### gradlew distTar
#### docker build -t clientgui:1.0.1 .
### run the image to test
### docker run -p 8081:8081 -ti --rm clientgui:1.0.1 /bin/bash //to use the console
### docker-compose -f virtualrobotguistepper.yaml up

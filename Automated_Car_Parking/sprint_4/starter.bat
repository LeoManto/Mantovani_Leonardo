start cmd.exe /k "call robot.bat"
pause
start cmd.exe /k "cd it.unibo.ParkManagerService_s4/build/distributions/it.unibo.ParkManagerService_s4-1.0/it.unibo.ParkManagerService_s4-1.0/bin & call it.unibo.ParkManagerService_s4.bat"
pause
start cmd.exe /k "cd ClientGui\out\artifacts\ClientGui_main_jar & java -Dserver.port=8081 -jar ClientGui.main.jar"
timeout 1
start cmd.exe /k "cd ParkManagerGui\out\artifacts\ParkManagerGui_main_jar & java -Dserver.port=8083 -jar ParkManagerGui.main.jar"
timeout 10
start "" "http://localhost:8081"
pause
start "" "http://localhost:8083"
Rem 



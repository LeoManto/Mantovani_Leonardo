start cmd.exe /k "call robot.bat"
echo Let start TestPlans
pause
start cmd.exe /k "cd ClientGui\out\artifacts\ClientGui_main_jar & java -Dserver.port=8081 -jar ClientGui.main.jar"
timeout 1
start cmd.exe /k "cd ParkManagerGui\out\artifacts\ParkManagerGui_main_jar & java -Dserver.port=8083 -jar ParkManagerGui.main.jar"
timeout 5
start "" "http://localhost:8081"
pause
start "" "http://localhost:8083"




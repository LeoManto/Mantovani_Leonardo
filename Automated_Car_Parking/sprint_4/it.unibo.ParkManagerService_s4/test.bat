start cmd.exe /k "java -version & java -jar ./gui/ParkManagerGui.main.jar"
start "" http://www.localhost:8083
echo "When Manager starts press a button"
pause
start cmd.exe /k java -jar ./gui/ClientGui.main.jar
start "" http://www.localhost:8081
echo "system ready. Click a button to start test ..."
pause
cd .\test\it\unibo\parkingmanagerservice\test\utils
npx playwright test --config=my.config.js 
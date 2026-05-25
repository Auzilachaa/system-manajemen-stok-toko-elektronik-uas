@echo off
setlocal

set FX_PATH=lib
set CP=bin;lib\gson-2.13.2.jar

echo [1/3] Compiling...
javac --module-path %FX_PATH% ^
      --add-modules javafx.controls,javafx.fxml,itextpdf ^
      -cp %CP% ^
      -d bin ^
      src\services\*.java src\gui\*.java src\Main.java

if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)

echo [2/3] Copying resources...
xcopy /E /Y /I src\gui bin\gui >nul 2>&1
xcopy /E /Y /I src\data bin\data >nul 2>&1

echo [3/3] Running application...
java --module-path %FX_PATH% ^
     --add-modules javafx.controls,javafx.fxml,itextpdf ^
     -cp %CP% ^
     Main

endlocal
pause

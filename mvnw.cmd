@REM Maven Wrapper script for Windows
@REM See https://maven.apache.org/wrapper/

setlocal
set "MAVEN_PROJECTBASEDIR=%~dp0"
set "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%"
set "WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"

if not exist "%WRAPPER_JAR%" (
  echo Downloading Maven Wrapper...
  powershell -NoProfile -Command "& { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '%WRAPPER_JAR%' -UseBasicParsing }"
  if not exist "%WRAPPER_JAR%" (
    echo ERROR: Could not download maven-wrapper.jar. Install Maven and run: mvn -N wrapper:wrapper
    exit /b 1
  )
)

where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
  echo ERROR: java not found. Set JAVA_HOME or add Java to PATH.
  exit /b 1
)

java ^
  -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" ^
  -cp "%WRAPPER_JAR%" %WRAPPER_LAUNCHER% %*
endlocal & exit /b %ERRORLEVEL%

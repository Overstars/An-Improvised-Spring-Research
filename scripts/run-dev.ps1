Param(
    [int]$Port = 9003
)

$ErrorActionPreference = "Stop"

Push-Location "$PSScriptRoot/../spring-research"
try {
    ./gradlew.bat clean bootJar --info
    java -jar ./build/libs/spring-research-0.0.1-SNAPSHOT.jar `
      -Dspring.profiles.active=dev `
      -Dserver.port=$Port `
      -Dfile.encoding=UTF-8
}
finally {
    Pop-Location
}

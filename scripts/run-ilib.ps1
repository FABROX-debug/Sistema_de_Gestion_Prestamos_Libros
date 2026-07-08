$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot

$outputPath = Join-Path $projectRoot "target\vscode-classes"
New-Item -ItemType Directory -Force $outputPath | Out-Null

$flatlaf = Join-Path $env:USERPROFILE ".m2\repository\com\formdev\flatlaf\2.4\flatlaf-2.4.jar"
$flatlafThemes = Join-Path $env:USERPROFILE ".m2\repository\com\formdev\flatlaf-intellij-themes\2.4\flatlaf-intellij-themes-2.4.jar"
$mysql = Join-Path $env:USERPROFILE ".m2\repository\mysql\mysql-connector-java\5.1.13\mysql-connector-java-5.1.13.jar"
$absoluteLayout = Join-Path $projectRoot "lib\unknown\binary\AbsoluteLayout\SNAPSHOT\AbsoluteLayout-SNAPSHOT.jar"

$classpathItems = @(
    $outputPath,
    (Join-Path $projectRoot "src\main\resources"),
    $flatlaf,
    $flatlafThemes,
    $mysql,
    $absoluteLayout
) | Where-Object { Test-Path $_ }

$classpath = [string]::Join(";", $classpathItems)
$sourceFiles = Get-ChildItem -Recurse (Join-Path $projectRoot "src\main\java") -Filter "*.java" | ForEach-Object { $_.FullName }

Write-Host "Compilando iLib..."
javac -encoding UTF-8 -cp $classpath -d $outputPath $sourceFiles

Write-Host "Ejecutando iLib..."
java -cp $classpath com.mycompany.ilib.Dashboard

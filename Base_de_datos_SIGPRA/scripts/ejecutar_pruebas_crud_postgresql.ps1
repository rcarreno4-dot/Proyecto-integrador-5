param(
    [string]$PsqlPath = "C:\Program Files\PostgreSQL\17\bin\psql.exe",
    [string]$Database = "sigpra",
    [string]$User = "postgres"
)

$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..\..")
$sqlFile = Join-Path $repoRoot "Base_de_datos_SIGPRA\scripts\pruebas_crud_basicas.sql"
$outputFile = Join-Path $repoRoot "Base_de_datos_SIGPRA\documentos\Evidencia_CRUD_PostgreSQL_SIGPRA.txt"

if (-not (Test-Path -LiteralPath $PsqlPath)) {
    throw "No se encontro psql.exe en: $PsqlPath"
}

if (-not (Test-Path -LiteralPath $sqlFile)) {
    throw "No se encontro el script CRUD en: $sqlFile"
}

$securePassword = Read-Host "Clave del usuario PostgreSQL $User" -AsSecureString
$plainPassword = [Runtime.InteropServices.Marshal]::PtrToStringAuto(
    [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
)

try {
    $env:PGPASSWORD = $plainPassword

    "SIGPRA - Evidencia de montaje y pruebas CRUD PostgreSQL" | Set-Content -LiteralPath $outputFile -Encoding UTF8
    "Fecha de ejecucion: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')" | Add-Content -LiteralPath $outputFile -Encoding UTF8
    "Base de datos: $Database" | Add-Content -LiteralPath $outputFile -Encoding UTF8
    "Usuario: $User" | Add-Content -LiteralPath $outputFile -Encoding UTF8
    "Script: $sqlFile" | Add-Content -LiteralPath $outputFile -Encoding UTF8
    "" | Add-Content -LiteralPath $outputFile -Encoding UTF8

    & $PsqlPath -U $User -d $Database -v ON_ERROR_STOP=1 -f $sqlFile 2>&1 |
        Tee-Object -FilePath $outputFile -Append

    if ($LASTEXITCODE -ne 0) {
        throw "La prueba CRUD termino con errores. Revisar $outputFile"
    }

    Write-Host "Evidencia generada en: $outputFile"
}
finally {
    Remove-Item Env:\PGPASSWORD -ErrorAction SilentlyContinue
    $plainPassword = $null
}

Write-Host "🚀 Frontend Application Starting..."
Write-Host "-------------------------------------------"
Write-Host "Target URL: http://localhost:3000"
Write-Host "Stop Command: Ctrl+C"
Write-Host "-------------------------------------------"

# 이미 포트가 사용 중인지 확인하고 정리 안내
$port3000 = Get-NetTCPConnection -LocalPort 3000 -ErrorAction SilentlyContinue
if ($port3000) {
    Write-Warning "⚠️ Port 3000 is already in use!"
    Write-Warning "Please run 'kill-frontend.ps1' to clean up previous processes."
    exit
}

# Gradle을 통해 프론트엔드 실행
./gradlew :frontend:start

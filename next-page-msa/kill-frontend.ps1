Get-NetTCPConnection -LocalPort 3000, 3001, 3002, 3003, 3004, 3005 -ErrorAction SilentlyContinue | Select-Object LocalPort, OwningProcess, State | ForEach-Object { 
    Write-Host "Killing process ID $($_.OwningProcess) on port $($_.LocalPort)..."
    Stop-Process -Id $_.OwningProcess -Force
}
Write-Host "All frontend processes cleared."

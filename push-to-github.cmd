@echo off
REM Run this in CMD (or double-click) after Git is installed.
REM Repository: https://github.com/jssechoi/samsung-wallet-atw-integration

cd /d "%~dp0"

where git >nul 2>nul || (
  echo Git is not installed or not in PATH. Install from https://git-scm.com/download/win
  pause
  exit /b 1
)

echo [1/6] git init
git init

echo [2/6] git add .
git add .

echo [3/6] git commit
git commit -m "Initial commit: Samsung Wallet ATW integration demo"

echo [4/6] git remote add origin
git remote add origin https://github.com/jssechoi/samsung-wallet-atw-integration.git 2>nul
if errorlevel 1 (
  echo Remote already exists. Updating URL...
  git remote set-url origin https://github.com/jssechoi/samsung-wallet-atw-integration.git
)

echo [5/6] git branch -M main
git branch -M main

echo [6/6] git pull then push (remote may have README)
git pull origin main --allow-unrelated-histories --no-edit 2>nul
git push -u origin main

echo.
echo Done. Check https://github.com/jssechoi/samsung-wallet-atw-integration
pause

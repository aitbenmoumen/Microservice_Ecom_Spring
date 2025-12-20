@echo off
echo ========================================
echo Starting Microservices in correct order
echo ========================================
echo.

echo [1/4] Starting Discovery Service (Eureka)...
start "Discovery Service" cmd /k "cd discovery_service && mvnw spring-boot:run"
echo Waiting 30 seconds for Eureka to start...
timeout /t 30 /nobreak

echo.
echo [2/4] Starting Customer Service...
start "Customer Service" cmd /k "cd customer_service && mvnw spring-boot:run"
echo Waiting 20 seconds for Customer Service to register...
timeout /t 20 /nobreak

echo.
echo [3/4] Starting Inventory Service...
start "Inventory Service" cmd /k "cd inventory_service && mvnw spring-boot:run"
echo Waiting 20 seconds for Inventory Service to register...
timeout /t 20 /nobreak

echo.
echo [4/4] Starting Billing Service...
start "Billing Service" cmd /k "cd billing-service && mvnw spring-boot:run"

echo.
echo ========================================
echo All services are starting!
echo ========================================
echo.
echo Check Eureka Dashboard: http://localhost:8761
echo.
echo Services:
echo   - Discovery Service: http://localhost:8761
echo   - Customer Service:  http://localhost:8081
echo   - Inventory Service: http://localhost:8082
echo   - Billing Service:   http://localhost:8083
echo.
echo Press any key to exit...
pause > nul


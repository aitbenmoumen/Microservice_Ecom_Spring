# Microservices Startup Guide

## Problem Summary
The billing-service was failing with error: 
```
Load balancer does not contain an instance for the service customer-service
```

This happens when the billing-service tries to call other microservices that are not yet running or not registered with Eureka.

## Solution Applied

### 1. Enhanced Configuration
- Added Feign client timeout settings
- Improved Eureka registry fetch intervals for faster service discovery
- Added retry logic with delays in BillingServiceApplication

### 2. Correct Startup Order

**IMPORTANT:** Start the services in this exact order:

#### Step 1: Start Discovery Service (Eureka)
```bash
cd discovery_service
mvnw spring-boot:run
```
Wait until you see: `Started DiscoveryServiceApplication` (usually on port 8761)
Verify at: http://localhost:8761

#### Step 2: Start Customer Service
```bash
cd customer_service
mvnw spring-boot:run
```
Wait until you see: `Started CustomerServiceApplication` (port 8081)

#### Step 3: Start Inventory Service
```bash
cd inventory_service
mvnw spring-boot:run
```
Wait until you see: `Started InventoryServiceApplication` (port 8082)

#### Step 4: Start Billing Service
```bash
cd billing-service
mvnw spring-boot:run
```
Now billing-service will automatically retry connecting to customer-service and inventory-service.
You should see messages like:
- "Waiting for customer-service and inventory-service to be available..."
- "Successfully connected to customer-service"
- "Successfully connected to inventory-service"
- "Bills created successfully!"

## Service Ports
- **Discovery Service (Eureka)**: 8761
- **Customer Service**: 8081
- **Inventory Service**: 8082
- **Billing Service**: 8083
- **Gateway Service** (if used): Check gateway_service/application.properties

## Verification
1. Check Eureka Dashboard: http://localhost:8761
   - All services should be registered and show as UP
   
2. Test endpoints:
   - Customers: http://localhost:8081/api/customers
   - Products: http://localhost:8082/api/products
   - Bills: http://localhost:8083/api/bills

## Troubleshooting

### If billing-service still fails:
1. Ensure all services are running
2. Check Eureka dashboard to confirm all services are registered
3. Review logs for each service
4. Verify H2 databases are accessible:
   - http://localhost:8081/h2-console (customer-service)
   - http://localhost:8082/h2-console (inventory-service)
   - http://localhost:8083/h2-console (billing-service)

### Common Issues:
- **Port already in use**: Kill the process using that port or change the port in application.properties
- **Maven build fails**: Run `mvnw clean install` first
- **Services not registering with Eureka**: Check eureka.client.service-url.defaultZone in application.properties

## Quick Start Script (Windows)
You can create a batch file to start all services in order:

```batch
@echo off
echo Starting Discovery Service...
start cmd /k "cd discovery_service && mvnw spring-boot:run"
timeout /t 30

echo Starting Customer Service...
start cmd /k "cd customer_service && mvnw spring-boot:run"
timeout /t 20

echo Starting Inventory Service...
start cmd /k "cd inventory_service && mvnw spring-boot:run"
timeout /t 20

echo Starting Billing Service...
start cmd /k "cd billing-service && mvnw spring-boot:run"

echo All services starting...
```

Save this as `start-all-services.bat` in the root project directory.


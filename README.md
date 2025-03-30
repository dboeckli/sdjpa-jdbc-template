# Spring Data JPA - Introduction to Spring Data JDBC

This repository contains source code examples to support my course Spring Data JPA and Hibernate Beginner to Guru

This projects demonstrates the old way with the DAO (Data Access Object) pattern

## Java DAO Pattern

- DAO - Data Access Object
- Pattern was a precursor to JPA, before ORMs become popular
- Older and uses JDBC for data access
- Common to see in legacy J2EE applications
- While not common in use anymore, it is a good way to utilize JDBC
- Very similar to the Repository Pattern used by Spring Data
- DAO Pattern - Purpose is to isolate persistence operations from the application layer
- For example, when the application needs to persist an object, it should not need to understand the underlying persistence technology
- Domain Class - Simple POJOs, same as JPA entities
- NOTE: DAO Pattern will not utilize JPA annotations
- DAO API - Provide interface for CRUD operations (similar to Repository)
- DAO Implementation - Implement persistence functionality

![img.png](img.png)

## Flyway

To enable Flyway in the MySQL profile, override the following properties when starting the application:
- `spring.flyway.enabled = true`
- `spring.docker.compose.file = compose-mysql.yaml`

This profile starts MySQL on port 3306 using the Docker Compose file `compose-mysql-.yaml`.

## Docker

Docker Compose file initially use the startup script located in `src/scripts`. These scripts create the database and users.

## Kubernetes

### Generate Config Map for mysql init script

When updating 'src/scripts/init-mysql-mysql.sql', apply the changes to the Kubernetes ConfigMap:
```bash
kubectl create configmap mysql-init-script --from-file=init.sql=src/scripts/init-mysql.sql --dry-run=client -o yaml | Out-File -Encoding utf8 k8s/mysql-init-script-configmap.yaml
```

### Deployment

To deploy all resources:
```bash
kubectl apply -f k8s/
```

To remove all resources:
```bash
kubectl delete -f k8s/
```

Check
```bash
kubectl get deployments -o wide
kubectl get pods -o wide
```

## Running the Application
1. Choose between h2 or mysql for database schema management. (you can use one of the preconfigured intellij runners)
2. Start the application with the appropriate profile and properties.
3. The application will use Docker Compose to start MySQL and apply the database schema changes.
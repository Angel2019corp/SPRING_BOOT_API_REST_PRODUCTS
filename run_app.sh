#!/bin/bash

set -a
source prop.env
set +a

echo "Compilando..."
./mvnw clean package -DskipTests

echo "Iniciando aplicacion..."
java -jar target/productos_api-0.0.1-SNAPSHOT.jar

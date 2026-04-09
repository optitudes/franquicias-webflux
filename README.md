## REQUERIMIENTOS DEL PROYECTO
Este proyecto requiere tener instalado `aws cli`, `docker` con `docker compose`
## PROYECTO EN PRODUCCION
Se puede ver el swagger del proyecto e interactuar con el desde el siguiente enlace  `http://54.236.7.169:8080/swagger-ui/index.html`
## EJECUCIÓN DEL PROYECTO EN UN AMBIENTE LOCAL
### CONFIGURACION DEL ENVIRONMENT
Para ejecutar el proyecto en un ambiente local es requerido tener instalado docker con compose
se debe copiar el .env.example  y generar un .env, definir la variable  ENVIRONMENT=LOCAL
y definir la variable AWS_DYNAMODB_ENDPOINT=http://dynamodb-local:8000, dichas instrucciones esta'n tambien presentes en el 
archivo .env.example
### EJECUCION DEL CONTENEDOR 
se debe ejecutar el comando `docker compose --profile local up -d` esto creara' dos contenedores, uno de spring-webflux y otro
con una instancia local de dynamo.
### CREACION DE LA SINGLE TABLE
una vez creados los contenedores se debe ejecutar el siguiente comando para crear la tabla de franquicias
`aws dynamodb create-table \
  --table-name franchise \
  --attribute-definitions \
    AttributeName=uuid,AttributeType=S \
    AttributeName=sk,AttributeType=S \
  --key-schema \
    AttributeName=uuid,KeyType=HASH \
    AttributeName=sk,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST \
  --endpoint-url http://localhost:8000
`
al terminar la ejecucion del comando si el resultado es exitoso  mostrara' un texto el cual se puede quitar presionando la tecla 'q'
### ACCEDER AL SWAGGER PARA PROBAR EL API
Si todo resulto' bien, espero que si, se puede acceder al enpoint `http://localhost:8080/swagger-ui/index.html` 
y desde ahi probar los CRUD y el endpint para obtener los productos con mayor stock.

## Arquitectura de la Solución

Esta solución está diseñada siguiendo los principios de **Clean Architecture**, buscando separar responsabilidades y facilitar la mantenibilidad, escalabilidad y testeo del sistema. A continuación se describe la estructura principal del proyecto:

```
application/
├─ service/                # Servicios del sistema que coordinan casos de uso
│  ├─ implementation/      # Implementación concreta de los servicios
│  └─ interfaces/          # Interfaces requeridas por los servicios del sistema
├─ repository/             # Interfaces de los repositorios
domain/
├─ model/                  # Modelos del dominio del negocio
infraestructure/
├─ configuration/          # Configuraciones necesarias para conexiones entrantes y salientes
├─ inbound/                # Conexiones entrantes al sistema
│  └─ api/                 # API REST
│     ├─ dto/              # Data Transfer Objects del API REST
│     └─ exception/        # Manejo de errores y excepciones globales
└─ outbound/               # Conexiones salientes desde el sistema
   └─ dynamodb/            # Conexión a la base de datos DynamoDB
       ├─ entity/          # Entidades de cara a la base de datos
       ├─ mapper/          # Mapeadores de entidades
       └─ repository/      # Implementación de repositorios para DynamoDB
```

### Descripción de capas

* **Domain**: Contiene los modelos de negocio y la lógica central del sistema, sin depender de frameworks externos.
* **Application**: Define los casos de uso del sistema, coordinando la lógica de negocio con los servicios y repositorios.
* **Application Service**: Contiene los servicios del sistema, sus interfaces y sus implementaciones concretas.
* **Infrastructure**: Maneja la comunicación con sistemas externos y configuraciones. Se divide en:

  * **Inbound**: Conexiones entrantes al sistema, como APIs REST.
  * **Outbound**: Conexiones salientes hacia sistemas externos, como bases de datos (DynamoDB).

Esta arquitectura permite que el **dominio y los casos de uso** permanezcan independientes de las tecnologías externas, facilitando cambios, pruebas unitarias y escalabilidad futura.

## Notas del desarrollador
 - Si bien se me asignaron dos días para resolver el reto técnico realmente pude usar muy poco tiempo debido a mi jornada laboral de 7:30am a 6pm presencial y las clases de la universidad que estoy viendo en la noche entre otros factores, sin embargo fue muy entretenido para mí realizar esta prueba, muchas gracias
 - Por temas de tiempo tuve que omitir las pruebas unitarias y el usar terraform para el despliegue


# Detalles técnicos 

**Versión:** `0.0.1-SNAPSHOT`  
**Java:** `17`  (Inicialmente lo queria hacer con una version superior (java25-21) pero tuve inconvenientes con swagger) 
**Framework Base:** Spring Boot `3.4.4` (Inicialmente queria usar la version 4 pero tambien tuve inconvenientes con swagger)

---

### Descripción Breve
Este proyecto es una aplicación reactiva basada en **Spring WebFlux** diseñada para integrarse con **Amazon DynamoDB**. Está orientada a ofrecer un alto rendimiento mediante programación no bloqueante y cuenta con documentación automática de APIs.

---

### Dependencias y Versiones

A continuación se detallan los paquetes principales utilizados en el desarrollo:

| Categoría | Paquete / Dependencia | Versión | Descripción |
| :--- | :--- | :--- | :--- |
| **Core** | `spring-boot-starter-webflux` | *Managed* | Motor para aplicaciones web reactivas y no bloqueantes. |
| **Persistencia** | `aws-sdk: dynamodb` | `2.29.35` | SDK oficial de AWS para interactuar con la base de datos NoSQL DynamoDB. |
| **Persistencia** | `dynamodb-enhanced` | `2.29.35` | Biblioteca para mapear clases Java (POJOs) a tablas de DynamoDB. |
| **Validación** | `spring-boot-starter-validation` | *Managed* | Soporte para validación de datos mediante anotaciones. |
| **Utilidad** | `lombok` | *Managed* | Biblioteca para reducir código repetitivo (Getters, Setters). |
| **Configuración** | `spring-dotenv` | `4.0.0` | Carga de variables de entorno desde archivos `.env`. |
| **Documentación** | `springdoc-openapi-ui` | `2.8.5` | Generación automática de Swagger UI para WebFlux. |
| **Testing** | `spring-boot-starter-test` | *Test Scope* | Herramientas estándar para pruebas unitarias.(por temas de tiempo no la pude implementar) |
| **Testing** | `reactor-test` | *Test Scope* | Utilidades para verificar flujos reactivos (Flux/Mono). (por temas de tiempo no se pudo implementar pero esta' en el pom.xml para futuras actualizaciones)|

> **Nota:** Las versiones marcadas como *"Managed"* se heredan de `spring-boot-starter-parent:3.4.4` para garantizar la estabilidad del ecosistema.

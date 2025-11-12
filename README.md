# TaskHub

TaskHub este o aplicație web simplă pentru gestionarea proiectelor și task-urilor, construită cu Spring Boot 3 și Java 21. Include un UI realizat cu Thymeleaf și Bootstrap, suport pentru autentificare demo și funcționalități CRUD complete pentru proiecte, task-uri și comentarii.

## Cerințe

- Java 21
- Maven 3.9+

## Rulare

1. Instalați dependențele și porniți aplicația:

   ```bash
   mvn spring-boot:run
   ```

2. Accesați aplicația la [http://localhost:8080](http://localhost:8080).

3. Autentificați-vă cu unul dintre utilizatorii demo:

   - `user` / `password`
   - `admin` / `admin`

Consola H2 este disponibilă la [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL `jdbc:h2:mem:taskhub`).

## Teste

Pentru a rula testele unitare:

```bash
mvn test
```

## Structură

- `com.example.taskhub.domain` – entitățile JPA și enum-urile pentru status/prioritate
- `com.example.taskhub.repo` – repository-urile Spring Data JPA
- `com.example.taskhub.dto` – DTO-uri și mapper-e simple pentru layer-ul web
- `com.example.taskhub.service` – logica de business și validările aplicației
- `com.example.taskhub.web` – controllerele MVC și vizualizările Thymeleaf
- `src/main/resources/templates` – layout-uri și pagini HTML

Date demo (un proiect și câteva task-uri) sunt preîncărcate din `data.sql`, iar utilizatorii demo sunt creați automat la pornire.

## Docker

Rularea aplicației și a unei baze de date PostgreSQL se poate face cu Docker Compose:

```bash
docker-compose up --build
```

Profilul `postgres` configurează conexiunea către baza de date din container și poate fi activat cu `SPRING_PROFILES_ACTIVE=postgres`.

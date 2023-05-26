FROM eclipse-temurin:17-jdk-jammy

WORKDIR /sirup/service

COPY ./target /sirup/service/target

CMD ["java","-jar","./target/DiagnosticsService-1.0-SNAPSHOT-shaded.jar"]
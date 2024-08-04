# Используем базовый образ OpenJDK
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Скопируйте JAR-файл в контейнер
COPY target/tink-0.0.1-SNAPSHOT.jar /app/tink-0.0.1-SNAPSHOT.jar

# Открываем порт, на котором будет работать ваше приложение
EXPOSE 8080

# Укажите команду для запуска JAR-файла
ENTRYPOINT ["java", "-jar", "/app/tink-0.0.1-SNAPSHOT.jar"]

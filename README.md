# Translation Service

## Описание

Это Spring Boot приложение предоставляет API для перевода текста. Использует внешний переводческий сервис Яндекс Translate для перевода текста и сохраняет запросы и переводы в базе данных.

## Требования

- **Java 17** (для разработки и запуска)
- **Docker** (для контейнеризации приложения)
- **Postman** (для тестирования API)

## Установка

### 1. Клонирование репозитория

Сначала клонируйте репозиторий на ваш локальный компьютер:

```sh
git clone https://github.com/chlrn/tink.git
cd translation-service
```
### 2. Сборка приложения
Соберите приложение с помощью Maven.

```sh
./mvnw package
```
### 3. Создание Docker образа
Соберите Docker образ из Dockerfile:
```sh
docker build -t translation-service .
```

### 4. Запуск Docker контейнера
Запустите контейнер с приложением
```sh
docker run -p 8080:8080 translation-service
```

### Использование API

### Перевод текста

URL: http://localhost:8080/api/translate

Метод: POST

### Заголовки:

Content-Type: application/json

### Авторизация: 

Type: API key

Key: Authorization 

Value: API-key AQVN11Q7idZUf5oaswdsv_nnZTiqkp2Us050iLSW

### Тело запроса: 
raw JSON
```json
{
"folderId": "b1gva6hkd65vrionjthb",
"texts": ["hello"],
"targetLanguageCode": "ru",
"sourceLanguageCode": "en"
}
```

### База данных
### 1. Запуск приложения:
Запустите Spring Boot приложение. База данных H2 будет автоматически создана в памяти при запуске приложения.

### 2. Доступ к H2 консоли:
После запуска приложения откройте веб-браузер и перейдите по следующему URL:
```bash
http://localhost:8080/h2-console
```
На странице входа в консоль H2 используйте следующие параметры подключения:

JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password: оставьте пустым
Нажмите "Connect", чтобы подключиться к базе данных.

# OnlineStore
Полностью переписанное CRUD приложения для магазина с каталогами, товарами и возможностью оформлять заказы для пользователя. Пользовательский UI пока что отсутствует. Панель администратора на 100% готова и выполняет свой функционал. В новой версии магазина по мере возможностей использовались принципы чистого кода, SOLID, KIS и DRY. [Старая весия проекта для сравнения кода.](https://github.com/fuz3Tilt/OnlineStore/tree/old_store)
## Скриншот
![store](https://github.com/fuz3Tilt/OnlineStore/assets/113349741/65b254c7-058d-4c0c-9222-1d41855bfaac)
## Особенности
- Регистрация и сброс пароля с помощью почты.
- Используется компонентый фреймвор ZK OSS для быстрой разработки простого веб интерфейса.
- Небольшое количество говнокода и странных решений по сравнению с прошлой версией проекта.
- Отказ от сохранения фотографий на сервере в пользу стороннего хранилища. Требуется ссылка на изображение.
- Отказ от использования unit тестов.
## Стек технологий
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring Email
- Spring Shedul
- Docker
- ZK OSS
- Thymeleaf
- Hibernate
- PostgreSQL
- Maven
## Как запустить
### Maven
  1. Введите актуальные значения для вашей базы данных в `application.properties`.
  2. [Настройте почту](https://yandex.ru/support/mail/mail-clients/others.html), введите логин и пароль в `application.properties`.
  3. Выполните `mvnw spring-boot:run` в папке проекта.
  4. Приложение запустится по адресу [localhost:8080](http://localhost:8080).
### Docker
  1. Выполните `docker-compose up --build` в папке проекта.
  2. Приложение запустится по адресу [localhost:8080](http://localhost:8080).

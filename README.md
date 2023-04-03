# OnlineStore
CRUD приложение для "общажного магазина" с каталогами и товарами внутри. Для рядовго пользователя никакого функционала, кроме просмотра ассортимента, не предусмотрено. Все CRUD операции производятся в панели администратора. Также, к аккаунту администратора можно привязать почту, а после её подтверждения, использовать для сброса пароля.
## Особенности
- Верификация почты с помощью уникального сообщения.
- Сброс пароля по верефицированной почте.
- Автоудаление истёкших токенов верификации.
- Сохранение фотографий каталогов и товаров в папке проекта.
## Стек технологий
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Junit
## Схема базы данных
![Схема базы данных](https://user-images.githubusercontent.com/113349741/229635930-b63b8021-58bc-46ac-86cb-859ed1dccb13.png)
## Как запустить

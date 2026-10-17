# star-recommendation

Рекомендательный сервис банка «Стар» (MVP). Spring Boot 3, Java 17, JdbcTemplate, H2 (только чтение).

## Запуск

1. Файл базы `transaction.mv.db` уже лежит в корне проекта (рядом с `pom.xml`).
2. `mvn spring-boot:run`
3. `GET http://localhost:8080/recommendation/{user_id}`

Пример: `http://localhost:8080/recommendation/cd515076-5d8a-44be-930e-8d4fcb79f42d`

```json
{
  "user_id": "cd515076-5d8a-44be-930e-8d4fcb79f42d",
  "recommendations": [
    {"name": "Invest 500", "id": "147f6a0f-3b91-413b-ab99-87f081d60d5a", "text": "..."}
  ]
}
```

Если рекомендаций нет, `recommendations` — пустой массив.

## Архитектура

- `RecommendationController` — единственный эндпоинт.
- `RecommendationService` — собирает непустые результаты всех `RecommendationRuleSet`.
- `RecommendationRuleSet` — интерфейс `Optional<RecommendationDto> check(UUID userId)`.
  Реализации: `Invest500RuleSet`, `TopSavingRuleSet`, `SimpleCreditRuleSet`.
  Новая рекомендация = новый `@Component`.
- `TransactionsRepository` — два общих параметризованных запроса (`usesProductType`, `sumAmount`),
  из которых строятся все правила.

## Допущения о схеме БД

Схема сверена с выданной БД: `TRANSACTIONS(ID, PRODUCT_ID, USER_ID, TYPE, AMOUNT)`, `PRODUCTS(ID, TYPE, NAME)`.
Типы транзакций: `DEPOSIT` и `WITHDRAW` (см. `Types`). Если в вашей БД названия отличаются,
поправьте SQL в `TransactionsRepository` и константы в `Types`.

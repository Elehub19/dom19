package ru.star.recommendation.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Общие параметризованные запросы к БД транзакций (только чтение).
 * Все правила рекомендаций строятся из этих двух запросов.
 */
@Repository
public class TransactionsRepository {

    private static final String USES_PRODUCT_SQL = """
            SELECT COUNT(*) > 0
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ?
            """;

    private static final String SUM_SQL = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ? AND t.type = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    public TransactionsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Пользователь использует продукт типа productType, если по нему есть хотя бы одна транзакция.
     */
    public boolean usesProductType(UUID userId, String productType) {
        Boolean result = jdbcTemplate.queryForObject(USES_PRODUCT_SQL, Boolean.class, userId, productType);
        return Boolean.TRUE.equals(result);
    }

    /**
     * Сумма транзакций типа transactionType по всем продуктам типа productType пользователя.
     */
    public BigDecimal sumAmount(UUID userId, String productType, String transactionType) {
        BigDecimal result = jdbcTemplate.queryForObject(SUM_SQL, BigDecimal.class,
                userId, productType, transactionType);
        return result == null ? BigDecimal.ZERO : result;
    }
}

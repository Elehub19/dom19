package ru.star.recommendation.service;

import org.junit.jupiter.api.Test;
import ru.star.recommendation.dto.RecommendationDto;
import ru.star.recommendation.rules.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecommendationServiceTest {

    private final UUID userId = UUID.randomUUID();

    @Test
    void collectsOnlyNonEmptyResults() {
        RecommendationDto dto = new RecommendationDto("A", UUID.randomUUID(), "text");
        RecommendationRuleSet matching = id -> Optional.of(dto);
        RecommendationRuleSet notMatching = id -> Optional.empty();

        var service = new RecommendationService(List.of(matching, notMatching));

        assertEquals(List.of(dto), service.getRecommendations(userId));
    }

    @Test
    void returnsEmptyListWhenNothingMatches() {
        var service = new RecommendationService(List.of(id -> Optional.empty()));

        assertTrue(service.getRecommendations(userId).isEmpty());
    }
}

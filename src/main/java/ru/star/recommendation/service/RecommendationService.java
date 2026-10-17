package ru.star.recommendation.service;

import org.springframework.stereotype.Service;
import ru.star.recommendation.dto.RecommendationDto;
import ru.star.recommendation.rules.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;

    public RecommendationService(List<RecommendationRuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }

    public List<RecommendationDto> getRecommendations(UUID userId) {
        return ruleSets.stream()
                .map(ruleSet -> ruleSet.check(userId))
                .flatMap(Optional::stream)
                .toList();
    }
}

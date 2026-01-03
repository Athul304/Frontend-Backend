package com.aitool.finder.Specification;

import com.aitool.finder.Entity.Tool;
import org.springframework.data.jpa.domain.Specification;

public class ToolSpecification {

    public static Specification<Tool> filterBy(String category, String pricing, Double minRating) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (category != null && !category.isEmpty()) {
                predicates = cb.and(predicates, cb.equal(root.get("category"), Tool.Category.valueOf(category.toUpperCase())));
            }

            if (pricing != null && !pricing.isEmpty()) {
                predicates = cb.and(predicates, cb.equal(root.get("pricing"), Tool.PricingType.valueOf(pricing.toUpperCase())));
            }

            if (minRating != null) {
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get("averageRating"), minRating));
            }

            return predicates;
        };
    }
}

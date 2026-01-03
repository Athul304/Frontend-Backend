package com.example.ai_tool_finder.specification;

import com.example.ai_tool_finder.model.PricingType;
import com.example.ai_tool_finder.model.Tool;
import org.springframework.data.jpa.domain.Specification;

public class ToolSpecification {

    public static Specification<Tool> filterBy(String category, String pricing, Double minRating) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (category != null && !category.isEmpty()) {
                // sanitize surrounding quotes and normalize case
                String c = category.trim();
                c = c.replaceAll("^[\'\"]+|[\'\"]+$", "");
                String cat = c.toLowerCase();
                predicates = cb.and(predicates, cb.equal(cb.lower(root.get("category")), cat));
            }

            if (pricing != null && !pricing.isEmpty()) {
                try {
                    // sanitize surrounding quotes if present (e.g. "free" or 'free')
                    String p = pricing.trim();
                    p = p.replaceAll("^[\'\"]+|[\'\"]+$", "");
                    PricingType pt = PricingType.valueOf(p.toUpperCase());
                    predicates = cb.and(predicates, cb.equal(root.get("pricingType"), pt));
                } catch (IllegalArgumentException e) {
                    // throw so caller (controller) can return a 400 and the client knows the input was invalid
                    throw new IllegalArgumentException("Invalid pricing type: " + pricing);
                }
            }

            if (minRating != null) {
                // averageRating may be NULL for some rows; use COALESCE to treat NULL as 0.0 for comparison
                var avgRatingExpr = cb.coalesce(root.get("averageRating"), 0.0);
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(avgRatingExpr, minRating));
            }

            return predicates;
        };
    }
}

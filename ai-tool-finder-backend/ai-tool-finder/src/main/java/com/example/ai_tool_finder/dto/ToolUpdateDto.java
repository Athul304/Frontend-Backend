package com.example.ai_tool_finder.dto;

import com.example.ai_tool_finder.model.PricingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ToolUpdateDto {

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotBlank(message = "category must not be blank")
    private String category;

    @NotNull(message = "pricingType must be provided")
    private PricingType pricingType;

    @NotBlank(message = "useCase must not be blank")
    private String useCase;

    public ToolUpdateDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public PricingType getPricingType() { return pricingType; }
    public void setPricingType(PricingType pricingType) { this.pricingType = pricingType; }

    public String getUseCase() { return useCase; }
    public void setUseCase(String useCase) { this.useCase = useCase; }
}


package com.example.ai_tool_finder.controller;

import com.example.ai_tool_finder.dto.ToolCreateDto;
import com.example.ai_tool_finder.dto.ToolUpdateDto;
import com.example.ai_tool_finder.model.Tool;
import com.example.ai_tool_finder.repository.ToolRepository;
import com.example.ai_tool_finder.specification.ToolSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tools")
@CrossOrigin(origins = "http://localhost:3000")
public class ToolController {

    private static final Logger log = LoggerFactory.getLogger(ToolController.class);

    private final ToolRepository toolRepository;

    public ToolController(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    @GetMapping
    public List<Tool> getTools(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String pricing,
            @RequestParam(required = false, name = "rating") Double rating
    ) {
        try {
            String cat = category == null ? null : category.trim();
            String pr = pricing == null ? null : pricing.trim();

            // debug log to help diagnose filtering issues
            log.debug("getTools called with raw category='{}', raw pricing='{}', rating='{}'", category, pricing, rating);

            Specification<Tool> spec = ToolSpecification.filterBy(cat, pr, rating);

            // also log sanitized values used by the specification
            log.debug("Using sanitized filters category='{}', pricing='{}', rating='{}'", cat, pr, rating);

            return toolRepository.findAll(spec);
        } catch (IllegalArgumentException e) {
            // translate invalid pricing enum (or other spec errors) to a 400 so the client knows input was bad
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tool> getToolById(@PathVariable Long id) {
        return toolRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tool not found"));
    }

    @PostMapping
    public ResponseEntity<Tool> createTool(@Valid @RequestBody ToolCreateDto dto) {
        Tool tool = new Tool();
        tool.setName(dto.getName());
        tool.setCategory(dto.getCategory());
        tool.setPricingType(dto.getPricingType());
        tool.setUseCase(dto.getUseCase());
        tool.setAverageRating(0.0);

        Tool saved = toolRepository.save(tool);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tool> updateTool(@PathVariable Long id, @Valid @RequestBody ToolUpdateDto dto) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tool not found"));

        // update mutable fields
        tool.setName(dto.getName());
        tool.setCategory(dto.getCategory());
        tool.setPricingType(dto.getPricingType());
        tool.setUseCase(dto.getUseCase());

        Tool saved = toolRepository.save(tool);
        return ResponseEntity.ok(saved);
    }
}

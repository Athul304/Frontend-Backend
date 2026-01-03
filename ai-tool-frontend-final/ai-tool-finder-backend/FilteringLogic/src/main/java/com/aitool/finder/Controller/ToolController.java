package com.aitool.Controller;

import com.aitool.Entity.Tool;
import com.aitool.Repository.ToolRepository;
import com.aitool.Specification.ToolSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@RestController
@RequestMapping("/tools")
public class ToolController {

    @Autowired
    private ToolRepository toolRepository;

    @GetMapping
    public List<Tool> getTools(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String pricing,
            @RequestParam(required = false) Double rating
    ) {
        Specification<Tool> spec = ToolSpecification.filterBy(category, pricing, rating);
        return toolRepository.findAll(spec);
    }
}

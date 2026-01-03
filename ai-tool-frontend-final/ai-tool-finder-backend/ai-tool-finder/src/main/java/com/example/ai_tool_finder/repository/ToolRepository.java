package com.example.ai_tool_finder.repository;

import com.example.ai_tool_finder.model.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ToolRepository extends JpaRepository<Tool, Long>, JpaSpecificationExecutor<Tool> {
}

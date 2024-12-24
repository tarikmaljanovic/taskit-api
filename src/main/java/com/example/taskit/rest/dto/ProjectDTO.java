package com.example.taskit.rest.dto;

import com.example.taskit.core.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    @JsonProperty("created_by")
    private Long createdBy;
    private Set<Long> members;

    public ProjectDTO() {}

    public ProjectDTO(Long id, String name, String description, Long createdBy, Set<Long> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Set<Long> getMembers() {
        return members;
    }

    public void setMembers(Set<Long> members) {
        this.members = members;
    }
}

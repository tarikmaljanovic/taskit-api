package com.example.taskit.core.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String role;

    @JsonIgnore
    @ManyToMany(mappedBy = "members")
    private Set<Project> projects = new HashSet<>();

    public User() {
    }

    public User(Long id, String name, String email, String password, String  role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjectsIn(Set<Project> projects) {
        this.projects = projects;
    }
}

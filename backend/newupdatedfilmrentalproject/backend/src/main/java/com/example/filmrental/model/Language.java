package com.example.filmrental.model;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "language")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "languageId", nullable = false)
    private Integer languageId;

    @NotBlank
    @Column(name = "name", length = 20, nullable = false)
    private String name;  // was 'Name' – change to 'name'

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate = LocalDateTime.now(); // was 'last_Update'
}

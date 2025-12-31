
package com.example.filmrental.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;


@Data
@Entity
@Table(name = "category")
public class Category {
	@Schema(accessMode = AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false) 
    private Integer categoryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;
}

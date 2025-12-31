
package com.example.filmrental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.Instant;

@Entity
@Table(name = "film")
@Data
public class Film {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "film_id", nullable = false)
  private Integer filmId; // maps to BIGINT (MySQL/MariaDB) or NUMBER(22) (Oracle)

  @Column(name = "title", nullable = false, length = 255)
  private String title;

  @Lob
  @Column(name = "description")
  private String description;

  @Column(name = "release_year")
  private Integer releaseYear;


    @NotNull
    @ManyToOne
    @JoinColumn(name = "languageId", nullable = false)
    private Language language;


  @Column(name = "rental_duration")
  private Integer rentalDuration;

  @Column(name = "rental_rate")
  private int rentalRate;

  @Column(name = "length")
  private int length;

  @Column(name = "replacement_cost")
  private Integer replacementCost;

  @Column(name = "rating", length = 10)
  private Integer rating;

  @Column(name = "special_features")
  private String specialFeatures;

  @Column(name = "last_update", nullable = false)
  private Instant lastUpdate;

  @PrePersist @PreUpdate
  void touch() { this.lastUpdate = Instant.now(); }
}

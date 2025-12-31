package com.example.filmrental.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity 
@Table(name = "actor")
@Getter 
@Setter 
public class Actor {
  @Id
  @Column(name = "actor_id")
  private Integer id;

  @Column(name = "first_name", nullable = false, length = 45)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 45)
  private String lastName;

  @Column(name = "last_update", nullable = false)
  private Instant lastUpdate;

  @PrePersist @PreUpdate
  void touch() 
  { 
	  this.lastUpdate = Instant.now(); 
  }
}


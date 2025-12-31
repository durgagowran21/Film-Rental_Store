
package com.example.filmrental.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "film_category")
@Getter
@Setter
@NoArgsConstructor
public class FilmCategory {

    @EmbeddedId
    private FilmCategoryId id = new FilmCategoryId();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("filmId")
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate = LocalDateTime.now();

    public void setFilm(Film film) {
        this.film = film;
        if (film != null) {
            if (this.id == null) this.id = new FilmCategoryId();
            this.id.setFilmId(film.getFilmId());
        }
    }

    public void setCategory(Category category) {
        this.category = category;
        if (category != null) {
            if (this.id == null) this.id = new FilmCategoryId();
            this.id.setCategoryId(category.getCategoryId());
        }
    }

    @PrePersist
    @PreUpdate
    void touch() {
        this.lastUpdate = LocalDateTime.now();
    }
}

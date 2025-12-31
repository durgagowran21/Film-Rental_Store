
package com.example.filmrental.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmCategoryId implements Serializable {

    @Column(name = "film_id", nullable = false)
    private Integer filmId;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilmCategoryId)) return false;
        FilmCategoryId that = (FilmCategoryId) o;
        return Objects.equals(filmId, that.filmId) && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filmId, categoryId);
    }
}

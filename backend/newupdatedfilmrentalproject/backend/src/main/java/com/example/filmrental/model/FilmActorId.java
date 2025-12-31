
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
public class FilmActorId implements Serializable {

    @Column(name = "actor_id", nullable = false)
    private Integer actorId;

    @Column(name = "film_id", nullable = false)
    private Integer filmId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilmActorId)) return false;
        FilmActorId that = (FilmActorId) o;
        return Objects.equals(actorId, that.actorId) && Objects.equals(filmId, that.filmId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorId, filmId);
    }
}

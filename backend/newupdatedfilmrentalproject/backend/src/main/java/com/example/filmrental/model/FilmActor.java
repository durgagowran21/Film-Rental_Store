
package com.example.filmrental.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "film_actor")
@Getter
@Setter
@NoArgsConstructor
public class FilmActor {

    @EmbeddedId
    private FilmActorId id = new FilmActorId();

    @ManyToOne( optional = false)
    @MapsId("actorId")
    @JoinColumn(name = "actor_id", nullable = false)
    private Actor actor;

    @ManyToOne( optional = false)
    @MapsId("filmId")
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @Column(name = "last_update", nullable = false)
    private Instant lastUpdate = Instant.now();

    public void setActor(Actor actor) {
        this.actor = actor;
        if (actor != null) {
            if (this.id == null) this.id = new FilmActorId();
            this.id.setActorId(actor.getId());
        }
    }

    public void setFilm(Film film) {
        this.film = film;
        if (film != null) {
            if (this.id == null) this.id = new FilmActorId();
            this.id.setFilmId(film.getFilmId());
        }
    }

    @PrePersist
    @PreUpdate
    void touch() {
        this.lastUpdate = Instant.now();
    }
}

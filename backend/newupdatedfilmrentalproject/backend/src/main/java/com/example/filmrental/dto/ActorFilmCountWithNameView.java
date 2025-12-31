package com.example.filmrental.dto;


//package com.film.rental.store.actor.actor.dto;

/**
 * Projection used for "top actors by film count" including first/last name.
 * Getter names must match the SELECT aliases in the repository query.
 */
public interface ActorFilmCountWithNameView {
    Long getActorId();
    String getFirstName();
    String getLastName();
    Long getFilmCount();
}

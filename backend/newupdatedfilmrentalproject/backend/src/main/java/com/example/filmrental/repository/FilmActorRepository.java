package com.example.filmrental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.filmrental.dto.FilmDto;
import com.example.filmrental.model.*;


public interface FilmActorRepository extends JpaRepository<FilmActor, FilmActorId> {


    @Query("select fa.actor from FilmActor fa where fa.film.id = :filmId")
    List<Actor> findActorsByFilmId(Integer filmId);
	boolean existsByFilmAndActor(Film film, Actor actor);
}


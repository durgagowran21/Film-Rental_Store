

package com.example.filmrental.repository;


import com.example.filmrental.model.Actor;
import com.example.filmrental.model.Film;
import com.example.filmrental.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
	
	public List<Actor> findByLastName(String lastName);
   public List<Actor> findByFirstName(String firstName);

//	@Query("SELECT a FROM Actor a WHERE a.lastName = :lastName")
//	List<Actor> findByLastName(@Param("lastName") String lastName);
//	@Query("SELECT a FROM Actor a WHERE a.firstName = :firstName")
//	List<Actor> findByFirstName(@Param("firstName") String firstName);
//	 
////	@Query("SELECT a FROM Actor a WHERE a.actorId = :id")
////	Optional<Actor> findActorById(@Param("id") Long id);
//	
//	 
//	@Query("SELECT a FROM Actor a ORDER BY SIZE(a.filmActors) DESC")
//	List<Actor> findTopActors(Pageable pageable);
//	 
//	 
//	@Query("SELECT 1f FROM Film f WHERE f.filmid IN :filmIds")
//	List<Film> findFilmsByIds(@Param("filmIds") Collection<Long> filmIds);
//	 
//	@Query("SELECT fa.film FROM FilmActor fa WHERE fa.actor.actorId = :id")
//	List<Film> findFilmsByActorId(@Param("id") Long id);
	
}




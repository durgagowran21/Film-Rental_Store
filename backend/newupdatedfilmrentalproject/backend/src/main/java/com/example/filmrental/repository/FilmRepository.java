package com.example.filmrental.repository;



import com.example.filmrental.model.Film;
import com.example.filmrental.repository.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Integer> {
	public  List<Film> findFilmByTitle(String title);

}


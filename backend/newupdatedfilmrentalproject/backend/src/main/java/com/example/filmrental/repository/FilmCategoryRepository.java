
package com.example.filmrental.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.filmrental.model.Category;
import com.example.filmrental.model.Film;
import com.example.filmrental.model.FilmCategory;
import com.example.filmrental.model.FilmCategoryId;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.filmrental.model.FilmCategory;
@Repository
public interface FilmCategoryRepository extends  JpaRepository<FilmCategory,Integer>
{
	@Query("select fc.film from FilmCategory fc where lower(fc.category.name) = lower(:categoryName)")
    List<Film> findFilmsByCategoryName(String categoryName);
	boolean existsByFilmAndCategory(Optional<Film> film, Optional<Category> category);

}
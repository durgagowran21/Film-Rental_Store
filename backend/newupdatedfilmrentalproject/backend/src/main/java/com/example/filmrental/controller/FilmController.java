package com.example.filmrental.controller;
 
import java.util.Collection;

import java.util.List;

import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
 
import com.example.filmrental.dto.*;

import com.example.filmrental.exception.*;
import com.example.filmrental.model.*;

import com.example.filmrental.service.*;
 
import jakarta.validation.Valid;
 
@RestController

@RequestMapping("/api/films")

public class FilmController {
 
	@Autowired
	private FilmService filmService;
	
	@PostMapping("/post")

	public ResponseEntity<String> addFilm(@Valid @RequestBody FilmDto filmDTO) throws InvalidInputException {

		filmService.addFilm(filmDTO);

		return ResponseEntity.ok("Record Saved");

	}
 
	@GetMapping

	public List<FilmDto> getAllFilm() {

		return filmService.getAllFilm();

	}
 
	@GetMapping("/title/{title}")

	public ResponseEntity<FilmDto> findByTitle(@PathVariable String title) throws ResourceNotFoundException {

		FilmDto filmDTO = filmService.findFilmsByTitle(title);

		if (filmDTO == null) {

			throw new ResourceNotFoundException("Film with title " + title + " not found");

		}

		return ResponseEntity.ok(filmDTO);

	}
 
	@GetMapping("/year/{release_year}")

	public ResponseEntity<List<FilmDto>> findFilmsByReleaseYear(@PathVariable int release_year) throws ResourceNotFoundException {

		List<FilmDto> filmDTOList = filmService.findFilmsByReleaseYear(release_year);

		if (filmDTOList.isEmpty()) {

			throw new ResourceNotFoundException("No records of films found for " + release_year + " release year");

		}

		return ResponseEntity.ok(filmDTOList);

	}
 
	@GetMapping("/duration/gt/{rd}")

	public ResponseEntity<List<FilmDto>> findFilmsWhereRentalDurationIsGreater(@PathVariable int rd) throws ResourceNotFoundException {

		List<FilmDto> filmDTOListByRentalDuration = filmService.findFilmsByRentalDuration(rd);

		if (filmDTOListByRentalDuration.isEmpty()) {

			throw new ResourceNotFoundException("No films found where rental duration is greater than " + rd);

		}

		return ResponseEntity.ok(filmDTOListByRentalDuration);

	}
 
	@GetMapping("/rate/gt/{rate}")

	public ResponseEntity<List<FilmDto>> findFilmsWhereRentalRateIsGreater(@PathVariable int rate) throws ResourceNotFoundException {

		List<FilmDto> filmDTOListByRentalRate = filmService.findFilmsWhereRentalRateIsGreater(rate);

		if (filmDTOListByRentalRate.isEmpty()) {

			throw new ResourceNotFoundException("No films found where rental rate is greater than " + rate);

		}

		return ResponseEntity.ok(filmDTOListByRentalRate);

	}
 
 
	@GetMapping("/length/gt/{length}")

	public ResponseEntity<List<FilmDto>> findFilmsWhereLengthIsGreater(@PathVariable int length) throws ResourceNotFoundException{

		List<FilmDto> filmDTOListByLength = filmService.findFilmsWhereLengthIsGreater(length);

		if(filmDTOListByLength.isEmpty()) {

			throw new ResourceNotFoundException("No films found where length is greater than " + length);

		}
 
		return ResponseEntity.ok(filmDTOListByLength);
 
	}
 
 
	@GetMapping("/duration/lt/{rd}")

	public ResponseEntity<List<FilmDto>> findFilmsWhereRentalDurationIsLower(@PathVariable int rd)throws ResourceNotFoundException {

		List<FilmDto> filmDTOListByRentalDuration = filmService.findFilmsWhereRentalDurationIsLower(rd);

		if(filmDTOListByRentalDuration.isEmpty()) {

			throw new ResourceNotFoundException("No films found where rental duration is lower than " + rd);

		}
 
		return ResponseEntity.ok(filmDTOListByRentalDuration);
 
	}
 
	@GetMapping("/rate/lt/{rate}")

	public ResponseEntity<List<FilmDto>> findFilmsWhereRentalRateIsLower(@PathVariable int rate) throws ResourceNotFoundException {

		List<FilmDto> filmDTOListByRentalRate =filmService.findFilmsWhereRateIsLower(rate);

		if(filmDTOListByRentalRate.isEmpty()) {

			throw new ResourceNotFoundException("No films found where rental rate is lower than " + rate);

		}
 
		return ResponseEntity.ok(filmDTOListByRentalRate);
 
 
	}
 
 
	@GetMapping("/length/lt/{length}")

	public ResponseEntity<List<FilmDto>> findFilmsWhereLengthIsLower(@PathVariable int length) throws ResourceNotFoundException {

		List<FilmDto> filmDTOListByLength = filmService.findFilmsWhereLengthIsLower(length);

		if(filmDTOListByLength.isEmpty()) {

			throw new ResourceNotFoundException("No films found where length is greater than " + length);

		}
 
		return ResponseEntity.ok(filmDTOListByLength);
 
	}
 
 
	@GetMapping("/betweenyear/{from}/{to}")

	public ResponseEntity<List<FilmDto>> findFilmBetweenYear(@PathVariable int from, @PathVariable int to) throws ResourceNotFoundException {

		List<FilmDto> filmsBetweenYear = filmService.findFilmBetweenYear(from,to);

		if(filmsBetweenYear.isEmpty()) {

			throw new ResourceNotFoundException("No films found between year " + from +" to " +to);
 
		}
 
		return ResponseEntity.ok(filmsBetweenYear);

	}
 
 
	@GetMapping("/rating/lt/{rating}")

	public ResponseEntity<List<FilmDto>> findFilmsWhereRatingIsLower(@PathVariable int rating) throws ResourceNotFoundException{

		List<FilmDto> filmsByRating = filmService.findFilmsWhereRatingIsLower(rating);

		if(filmsByRating.isEmpty()) {

			throw new ResourceNotFoundException("No films founds with rating lower than "+rating);

		}

		return ResponseEntity.ok(filmsByRating);

	}
 
 
	@GetMapping("/rating/gt/{rating}")

	public ResponseEntity<List<FilmDto>> findFilmsWhereRatingIsHigher(@PathVariable int rating) throws ResourceNotFoundException{

		List<FilmDto> filmsByRating = filmService.findFilmsWhereRatingIsHigher(rating);

		if(filmsByRating.isEmpty()) {

			throw new ResourceNotFoundException("No films found with rating higher than "+rating);

		}
 
		return ResponseEntity.ok(filmsByRating);
 
	}
 
 
	@GetMapping("/language/{lang}")

	public ResponseEntity<List<FilmDto>> findFilmsByLanguage(@PathVariable String lang) throws ResourceNotFoundException{

		List<FilmDto> filmsByLang = filmService.findFilmsByLanguage(lang);

		if(filmsByLang.isEmpty()) {

			throw new ResourceNotFoundException("No film found with langauge "+ lang);

		}

		return ResponseEntity.ok(filmsByLang);

	}
 
	@GetMapping("/countbyyear")

	public ResponseEntity<Map<Integer,Integer>> displayFilmsNumberByYear() {

		Map<Integer, Integer> numberOfFilmsByYear = filmService.displayFilmsNumberByYear();

		return ResponseEntity.ok(numberOfFilmsByYear);

	}
 
	@PutMapping("/update/title/{id}")

	public ResponseEntity<FilmDto> updateTitle(@PathVariable int id, @RequestBody String title) throws InvalidInputException {

		FilmDto updatedFilm = filmService.updateTitle(id,title);
 
		return ResponseEntity.ok(updatedFilm);

	}
 
	@PutMapping("/update/releaseyear/{id}") 

	public ResponseEntity<FilmDto> updateReleaseYear(@PathVariable int id, @RequestBody int year)throws InvalidInputException{

		FilmDto updatedFilm = filmService.updateReleaseYear(id,year);

		return ResponseEntity.ok(updatedFilm);

	}
 
	@PutMapping("/update/rentalduration/{id}")

	public ResponseEntity<FilmDto> updateRentalDuration(@PathVariable int id, @RequestBody int rental_duration) throws InvalidInputException{

		FilmDto updatedFilm = filmService.updateRentalDuration(id,rental_duration);

		return ResponseEntity.ok(updatedFilm);

	}

	@PutMapping("/update/rentalrate/{id}")

	public ResponseEntity<FilmDto> updateRentalRate(@PathVariable int id, @RequestBody int rental_rate) throws InvalidInputException{

		FilmDto updatedFilm = filmService.updateRentalRate(id,rental_rate);

		return ResponseEntity.ok(updatedFilm);

	}
 
	@PutMapping("/update/rating/{id}")

	public ResponseEntity<FilmDto> updateRating(@PathVariable Integer id, @RequestBody int rating) throws InvalidInputException{

		FilmDto updatedFilm = filmService.updateRating(id,rating);

		return ResponseEntity.ok(updatedFilm);

	}
 
	@PutMapping("/update/language/{id}")

	public ResponseEntity<FilmDto> updateLanguage(@PathVariable Integer id,@RequestBody Integer lang_id) throws InvalidInputException{

		FilmDto updatedFilm = filmService.updateLanguage(id,lang_id);

		return ResponseEntity.ok(updatedFilm);

	}

 
	 @PutMapping("/{filmId}/actors")

	    public ResponseEntity<String> assignActorsToFilm(@PathVariable Integer filmId, @RequestBody Collection<Integer> actorIds) {

	        
	            filmService.assignActorsToFilm(filmId, actorIds);

	            return ResponseEntity.ok("Actors assigned to film successfully");

	      
	 }


		@PutMapping("/update/category/{id}")

		public ResponseEntity<FilmCategory> updateCategory(@PathVariable Integer id, @RequestBody Integer category_id) throws InvalidInputException{

			FilmCategory updatedFilm= filmService.updateCategory(id,category_id);

			return ResponseEntity.ok(updatedFilm);

		}
		@GetMapping("/api/films/{id}/actors")
		public ResponseEntity<List<ActorDto>> getActorsOfFilm(@PathVariable Integer id) throws InvalidInputException {
			return ResponseEntity.ok(filmService.getActorsOfFilm(id));
}
		@GetMapping("/api/films/category/{category}")
				public ResponseEntity<List<FilmDto>> getFilmsByCategory(@PathVariable String category) throws InvalidInputException {
					return ResponseEntity.ok(filmService.findFilmsByCategory(category));
}

 
 
 
 
 
 
 
}
 
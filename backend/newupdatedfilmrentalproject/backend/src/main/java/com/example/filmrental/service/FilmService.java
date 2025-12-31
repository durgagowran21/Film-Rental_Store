package com.example.filmrental.service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
 
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.filmrental.dto.ActorDto;
import com.example.filmrental.dto.FilmDto;
import com.example.filmrental.exception.InvalidInputException;
import com.example.filmrental.model.Actor;
import com.example.filmrental.model.Category;
import com.example.filmrental.model.Film;
import com.example.filmrental.model.FilmActor;
import com.example.filmrental.model.FilmCategory;
import com.example.filmrental.model.Language;
import com.example.filmrental.repository.ActorRepository;
import com.example.filmrental.repository.CategoryRepository;
import com.example.filmrental.repository.FilmActorRepository;
import com.example.filmrental.repository.FilmCategoryRepository;
import com.example.filmrental.repository.FilmRepository;

import jakarta.transaction.Transactional;
 
@Service
public class FilmService  {
 
	@Autowired
	private FilmRepository filmRepository;
 
	@Autowired
	private ModelMapper modelMapper;
 
 
	@Autowired
	private FilmActorRepository filmActorRepository;
 
 
	@Autowired
	private ActorRepository actorRepository;
 
	@Autowired
	private CategoryRepository categoryRepository;
 
	@Autowired
	private FilmCategoryRepository filmCategoryRepository;
	
	@Transactional
	public void addFilm(FilmDto filmDTO) {
		Film film = modelMapper.map(filmDTO, Film.class);
		filmRepository.save(film);
		// TODO Auto-generated method stub
	}
	public FilmDto findFilmsByTitle(String title) {
		List<Film> film = filmRepository.findFilmByTitle(title);
		return film != null ? modelMapper.map(film, FilmDto.class) : null;
	}
 
	public List<FilmDto> getAllFilm() {
		List<Film> films = filmRepository.findAll();
 
		films.stream().map(film -> modelMapper.map(film, FilmDto.class)).collect(Collectors.toList()).forEach(System.out::println);
 
		return films.stream().map(film -> modelMapper.map(film, FilmDto.class)).collect(Collectors.toList());
	}
 
 
	public List<FilmDto> findFilmsByReleaseYear(int releaseYear) {
		List<Film> films = filmRepository.findAll().stream()
				.filter(film -> film.getReleaseYear() == releaseYear)
				.collect(Collectors.toList());
		return films.stream().map(film -> modelMapper.map(film, FilmDto.class)).collect(Collectors.toList());
	}
 
	public List<FilmDto> findFilmsByRentalDuration(int rd) {
		List<Film> films = filmRepository.findAll().stream()
				.filter(film -> film.getRentalDuration() > rd)
				.collect(Collectors.toList());
		return films.stream().map(film -> modelMapper.map(film, FilmDto.class)).collect(Collectors.toList());
	}
 
	public List<FilmDto> findFilmsWhereRentalRateIsGreater(int rate) {
		List<Film> films = filmRepository.findAll().stream()
				.filter(film -> film.getRentalRate() > rate)
				.collect(Collectors.toList());
		return films.stream().map(film -> modelMapper.map(film, FilmDto.class)).collect(Collectors.toList());
	}
 
	public List<FilmDto> findFilmsWhereLengthIsGreater(int length) {
		// TODO Auto-generated method stub
		List<Film> films = filmRepository.findAll().stream()
				.filter(film -> film.getLength()>length)
				.collect(Collectors.toList());
 
		return films.stream().map(film -> modelMapper.map(film, FilmDto.class)).collect(Collectors.toList());
	}
	
	public List<FilmDto> findFilmsWhereRentalDurationIsLower(int rd) {
		// TODO Auto-generated method stub
		List<Film> films = filmRepository.findAll().stream()
				.filter(film -> film.getRentalDuration() < rd)
				.collect(Collectors.toList());
		return films.stream().map(film -> modelMapper.map(film, FilmDto.class)).collect(Collectors.toList());
 
	}
 
 
	public List<FilmDto> findFilmsWhereRateIsLower(int rate) {
		// TODO Auto-generated method stub
		List<Film> films = filmRepository.findAll().stream()
				.filter(film -> film.getRentalRate() < rate)
				.collect(Collectors.toList());
		return films.stream().map(film -> modelMapper.map(film, FilmDto.class)).collect(Collectors.toList());
	}
 
	public List<FilmDto> findFilmsWhereLengthIsLower(int length) {
		// TODO Auto-generated method stub
		List<Film> films = filmRepository.findAll().stream()
				.filter(film -> film.getLength()<length)
				.collect(Collectors.toList());
 
		return films.stream().map(film -> modelMapper.map(film, FilmDto.class)).collect(Collectors.toList());
 
	}
 
	
	public List<FilmDto> findFilmBetweenYear(int from, int to) {
		// TODO Auto-generated method stub
		List<Film> films = filmRepository.findAll().stream()
				.filter(film -> film.getReleaseYear()>from && film.getReleaseYear()<to)
				.collect(Collectors.toList());
		return films.stream().map(film -> modelMapper.map(film, FilmDto.class)).collect(Collectors.toList());
	}
 
	
	public List<FilmDto> findFilmsWhereRatingIsLower(int rating) {
		// TODO Auto-generated method stub
		List<Film> films = filmRepository.findAll().stream()
				.filter(film -> film.getRating() < rating)
				.collect(Collectors.toList());
		return films.stream().map(film -> modelMapper.map(film, FilmDto.class)).collect(Collectors.toList());
	}
 
	
	public List<FilmDto> findFilmsWhereRatingIsHigher(int rating) {
		// TODO Auto-generated method stub
		List<Film> films = filmRepository.findAll().stream()
				.filter(film -> film.getRating()>rating)
				.collect(Collectors.toList());
		return films.stream().map(film -> modelMapper.map(film, FilmDto.class)).collect(Collectors.toList());
 
 
	}
 
	
	public List<FilmDto> findFilmsByLanguage(String lang) {
		// TODO Auto-generated method stub
 
		if (lang == null || lang.trim().isEmpty()) {
			throw new IllegalArgumentException("Language cannot be null or empty");
		}
 
		List<Film> films = filmRepository.findAll().stream()
				.filter(film -> film.getLanguage() != null && film.getLanguage().getName().equals(lang))
				.collect(Collectors.toList());
 
		return films.stream()
				.map(film -> modelMapper.map(film, FilmDto.class))
				.collect(Collectors.toList());
	}
 
	public Map<Integer, Integer> displayFilmsNumberByYear() {
		// TODO Auto-generated method stub
		Map<Integer,Integer> filmCountByYear= new HashMap<>();
		for(int i=1900; i<=2100;i++ ) {
			filmCountByYear.put(i, 0);
		}
 
		List<Film> films = filmRepository.findAll();
 
		for (Film film : films) {
			Integer releaseYear = film.getReleaseYear();
			if (releaseYear != null && releaseYear >= 1900 && releaseYear <= 2100) {
				filmCountByYear.put(releaseYear, filmCountByYear.get(releaseYear) + 1);
			}
		}
 
		return filmCountByYear;
	}
 
	
	public FilmDto updateTitle(Integer id, String title) throws InvalidInputException {
		Film film = filmRepository.findById(id)
				.orElseThrow(() -> new InvalidInputException("Film with ID " + id + " not found"));
 
		if (title == null || title.trim().isEmpty()) {
			throw new InvalidInputException("New title cannot be null or empty");
		}
 
		film.setTitle(title);
 
		Film updatedFilm = filmRepository.save(film);
 
		return modelMapper.map(updatedFilm, FilmDto.class);
	}
 
	
	public FilmDto updateReleaseYear(Integer id,int year) throws InvalidInputException {
		// TODO Auto-generated method stub
		Film film = filmRepository.findById(id)
				.orElseThrow(() -> new InvalidInputException("Film with ID " + id + " not found"));
 
		film.setReleaseYear(year);
 
		Film updatedFilm = filmRepository.save(film);
 
 
		return modelMapper.map(updatedFilm, FilmDto.class);
	}
 
	
	public FilmDto updateRentalDuration(Integer id, int rental_duration) throws InvalidInputException {
		// TODO Auto-generated method stub
		Film film = filmRepository.findById(id)
				.orElseThrow(() -> new InvalidInputException("Film with ID " + id + " not found"));
		film.setRentalDuration(rental_duration);
		Film updatedFilm = filmRepository.save(film);
 
		return modelMapper.map(updatedFilm, FilmDto.class);
	}
 
	public FilmDto updateRentalRate(Integer id, int rental_rate) throws InvalidInputException {
		// TODO Auto-generated method stub
		Film film = filmRepository.findById(id)
				.orElseThrow(() -> new InvalidInputException("Film with ID " + id + " not found"));
		film.setRentalRate(rental_rate);
		Film updatedFilm = filmRepository.save(film);
 
		return modelMapper.map(updatedFilm, FilmDto.class);
	}
 
	public FilmDto updateRating(Integer id, int rating) throws InvalidInputException {
		// TODO Auto-generated method stub
		Optional<Film> film = Optional.of(filmRepository.findById(id)
				.orElseThrow(() -> new InvalidInputException("Film with ID " + id + " not found")));
 
		film.get().setRating(rating);
		Film updatedFilm = filmRepository.save(film.get());
		return modelMapper.map(updatedFilm, FilmDto.class);
	}
 
	public FilmDto updateLanguage(Integer id, Integer lang_id) throws InvalidInputException {
		// TODO Auto-generated method stub
		Film film = filmRepository.findById(id)
				.orElseThrow(() -> new InvalidInputException("Film with ID " + id + " not found"));
		film.getLanguage().setLanguageId(lang_id);
		Film updatedFilm = filmRepository.save(film);
		return modelMapper.map(updatedFilm, FilmDto.class);
	}
 
	@Transactional
	public void assignActorsToFilm(Integer filmId, Collection<Integer> actorIds) {
 
		Film film = filmRepository.findById(filmId)
				.orElseThrow(() -> new IllegalArgumentException("Film with ID " + filmId + " not found"));
 
		for (Integer actorId : actorIds) {
			Actor actor = actorRepository.findById(actorId)
					.orElseThrow(() -> new IllegalArgumentException("Actor with ID " + actorId + " not found"));
 
			// Check if the association already exists
			if (!filmActorRepository.existsByFilmAndActor(film, actor)) {
				FilmActor filmActor = new FilmActor();
				filmActor.setFilm(film);
				filmActor.setActor(actor);
				filmActor.setLastUpdate(Instant.now());
 
				filmActorRepository.save(filmActor);
			}
		}
	}
 
	
	public FilmCategory updateCategory(Integer id, Integer category_id) throws InvalidInputException {
		Optional<Film> film = filmRepository.findById(id);
        Optional<Category> category = categoryRepository.findById(category_id);
      	FilmCategory filmCategory = new FilmCategory();
		if(!filmCategoryRepository.existsByFilmAndCategory(film,category)) {
 
			filmCategory.setFilm(film.get());
			filmCategory.setCategory(category.get());
			filmCategoryRepository.save(filmCategory);
 
		}
		return filmCategory;
	}
	@Transactional
	public List<ActorDto> getActorsOfFilm(Integer filmId) throws InvalidInputException {
	    Film film = filmRepository.findById(filmId)
	            .orElseThrow(() -> new InvalidInputException("Film with ID " + filmId + " not found"));

	    return filmActorRepository.findActorsByFilmId(film.getFilmId())
	            .stream()
	            .map(a -> modelMapper.map(a, ActorDto.class))
	            .collect(Collectors.toList());
	}

	@Transactional
	public List<FilmDto> findFilmsByCategory(String categoryName) throws InvalidInputException {
	    if (categoryName == null || categoryName.trim().isEmpty()) {
	        throw new InvalidInputException("Category must not be empty");
	    }
	    return filmCategoryRepository.findFilmsByCategoryName(categoryName.trim())
	            .stream()
	            .map(f -> modelMapper.map(f, FilmDto.class))
	            .collect(Collectors.toList());
	}


}
 
 
 
 
 
 
 
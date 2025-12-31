
package com.example.filmrental.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.modelmapper.ModelMapper;

import com.example.filmrental.dto.ActorDto;
import com.example.filmrental.dto.FilmDto;
import com.example.filmrental.exception.InvalidInputException;
import com.example.filmrental.model.Actor;
import com.example.filmrental.model.Film;
import com.example.filmrental.model.FilmActor;
import com.example.filmrental.model.Language;
import com.example.filmrental.repository.ActorRepository;
import com.example.filmrental.repository.FilmActorRepository;
import com.example.filmrental.repository.FilmCategoryRepository;
import com.example.filmrental.repository.FilmRepository;
import com.example.filmrental.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @Mock private FilmRepository filmRepository;
    @Mock private FilmActorRepository filmActorRepository;
    @Mock private ActorRepository actorRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private FilmCategoryRepository filmCategoryRepository;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private FilmService filmService;

    @Test
    void addFilm_success() {
        FilmDto input = new FilmDto();
        Film mapped = new Film();
        when(modelMapper.map(input, Film.class)).thenReturn(mapped);
        when(filmRepository.save(mapped)).thenReturn(mapped);

        filmService.addFilm(input);

        verify(modelMapper).map(input, Film.class);
        verify(filmRepository).save(mapped);
    }

    @Test
    void findFilmsByTitle_mapsListToDto() {
        Film f1 = new Film(); f1.setFilmId(1);
        when(filmRepository.findFilmByTitle("ABC")).thenReturn(Arrays.asList(f1));
        FilmDto dto = new FilmDto(); dto.setFilmId(1);
        when(modelMapper.map(any(List.class), eq(FilmDto.class))).thenReturn(dto);

        FilmDto result = filmService.findFilmsByTitle("ABC");

        assertThat(result.getFilmId()).isEqualTo(1);
        verify(filmRepository).findFilmByTitle("ABC");
    }

    @Test
    void getAllFilm_success() {
        Film f1 = new Film(); f1.setFilmId(1);
        Film f2 = new Film(); f2.setFilmId(2);
        when(filmRepository.findAll()).thenReturn(Arrays.asList(f1, f2));

        FilmDto d1 = new FilmDto(); d1.setFilmId(1);
        FilmDto d2 = new FilmDto(); d2.setFilmId(2);
        when(modelMapper.map(f1, FilmDto.class)).thenReturn(d1);
        when(modelMapper.map(f2, FilmDto.class)).thenReturn(d2);

        List<FilmDto> result = filmService.getAllFilm();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFilmId()).isEqualTo(1);
        assertThat(result.get(1).getFilmId()).isEqualTo(2);
    }

    @Test
    void findFilmsByReleaseYear_filtersCorrectly() {
        Film f1 = new Film(); f1.setReleaseYear(2000);
        Film f2 = new Film(); f2.setReleaseYear(2001);
        when(filmRepository.findAll()).thenReturn(Arrays.asList(f1, f2));

        FilmDto d1 = new FilmDto(); FilmDto d2 = new FilmDto();
        when(modelMapper.map(any(Film.class), eq(FilmDto.class)))
            .thenReturn(d1, d2);

        List<FilmDto> result = filmService.findFilmsByReleaseYear(2001);
        assertThat(result).hasSize(1);
    }

    @Test
    void findFilmsByLanguage_success() {
        Language en = new Language(); en.setName("English");
        Film f1 = new Film(); f1.setLanguage(en);
        Film f2 = new Film(); f2.setLanguage(new Language()); f2.getLanguage().setName("French");

        when(filmRepository.findAll()).thenReturn(Arrays.asList(f1, f2));
        FilmDto d1 = new FilmDto();
        when(modelMapper.map(f1, FilmDto.class)).thenReturn(d1);

        List<FilmDto> result = filmService.findFilmsByLanguage("English");
        assertThat(result).hasSize(1);
    }

    @Test
    void findFilmsByLanguage_invalidParam_throws() {
        assertThrows(IllegalArgumentException.class, () -> filmService.findFilmsByLanguage(null));
        assertThrows(IllegalArgumentException.class, () -> filmService.findFilmsByLanguage("   "));
    }

    @Test
    void displayFilmsNumberByYear_countsCorrectly() {
        Film a = new Film(); a.setReleaseYear(2001);
        Film b = new Film(); b.setReleaseYear(2001);
        Film c = new Film(); c.setReleaseYear(1999);
        when(filmRepository.findAll()).thenReturn(Arrays.asList(a, b, c));

        Map<Integer,Integer> map = filmService.displayFilmsNumberByYear();

        assertThat(map.get(2001)).isEqualTo(2);
        assertThat(map.get(1999)).isEqualTo(1);
        assertThat(map.get(1900)).isNotNull();
        assertThat(map.get(2100)).isNotNull();
    }

    @Test
    void updateTitle_success() throws Exception {
        Film existing = new Film(); existing.setFilmId(5); existing.setTitle("OLD");
        Film saved = new Film(); saved.setFilmId(5); saved.setTitle("NEW");

        when(filmRepository.findById(5)).thenReturn(Optional.of(existing));
        when(filmRepository.save(existing)).thenReturn(saved);

        FilmDto dto = new FilmDto(); dto.setFilmId(5); dto.setTitle("NEW");
        when(modelMapper.map(saved, FilmDto.class)).thenReturn(dto);

        FilmDto result = filmService.updateTitle(5, "NEW");
        assertThat(result.getTitle()).isEqualTo("NEW");
    }

    @Test
    void updateTitle_empty_throws() {
        Film existing = new Film(); existing.setFilmId(5); existing.setTitle("OLD");
        when(filmRepository.findById(5)).thenReturn(Optional.of(existing));
        assertThrows(InvalidInputException.class, () -> filmService.updateTitle(5, " "));
    }

    @Test
    void updateReleaseYear_success() throws Exception {
        Film existing = new Film(); existing.setFilmId(1);
        Film saved = new Film(); saved.setFilmId(1); saved.setReleaseYear(2020);

        when(filmRepository.findById(1)).thenReturn(Optional.of(existing));
        when(filmRepository.save(existing)).thenReturn(saved);

        FilmDto dto = new FilmDto(); dto.setFilmId(1);
        when(modelMapper.map(saved, FilmDto.class)).thenReturn(dto);

        FilmDto result = filmService.updateReleaseYear(1, 2020);
        assertThat(result.getFilmId()).isEqualTo(1);
    }

    @Test
    void updateRentalRate_success() throws Exception {
        Film existing = new Film(); existing.setFilmId(2);
        Film saved = new Film(); saved.setFilmId(2); saved.setRentalRate(5);

        when(filmRepository.findById(2)).thenReturn(Optional.of(existing));
        when(filmRepository.save(existing)).thenReturn(saved);
        FilmDto dto = new FilmDto(); dto.setFilmId(2);
        when(modelMapper.map(saved, FilmDto.class)).thenReturn(dto);

        FilmDto result = filmService.updateRentalRate(2, 5);
        assertThat(result.getFilmId()).isEqualTo(2);
    }

    @Test
    void updateRating_success() throws Exception {
        Film existing = new Film(); existing.setFilmId(3);
        Film saved = new Film(); saved.setFilmId(3); saved.setRating(8);

        when(filmRepository.findById(3)).thenReturn(Optional.of(existing));
        when(filmRepository.save(existing)).thenReturn(saved);
        FilmDto dto = new FilmDto(); dto.setFilmId(3);
        when(modelMapper.map(saved, FilmDto.class)).thenReturn(dto);

        FilmDto result = filmService.updateRating(3, 8);
        assertThat(result.getFilmId()).isEqualTo(3);
    }

    @Test
    void updateLanguage_success() throws Exception {
        Language lang = new Language(); lang.setLanguageId(1); lang.setName("English");
        Film existing = new Film(); existing.setFilmId(4); existing.setLanguage(lang);
        Film saved = new Film(); saved.setFilmId(4); saved.setLanguage(lang);

        when(filmRepository.findById(4)).thenReturn(Optional.of(existing));
        when(filmRepository.save(existing)).thenReturn(saved);
        FilmDto dto = new FilmDto(); dto.setFilmId(4);
        when(modelMapper.map(saved, FilmDto.class)).thenReturn(dto);

        FilmDto result = filmService.updateLanguage(4, 2);
        assertThat(existing.getLanguage().getLanguageId()).isEqualTo(2);
        assertThat(result.getFilmId()).isEqualTo(4);
    }

    @Test
    void assignActorsToFilm_createsNewAssociations() {
        Film film = new Film(); film.setFilmId(10);
        Actor a1 = new Actor(); a1.setId(1);
        Actor a2 = new Actor(); a2.setId(2);

        when(filmRepository.findById(10)).thenReturn(Optional.of(film));
        when(actorRepository.findById(1)).thenReturn(Optional.of(a1));
        when(actorRepository.findById(2)).thenReturn(Optional.of(a2));
        when(filmActorRepository.existsByFilmAndActor(film, a1)).thenReturn(false);
        when(filmActorRepository.existsByFilmAndActor(film, a2)).thenReturn(false);
        when(filmActorRepository.save(any(FilmActor.class))).thenAnswer(inv -> inv.getArgument(0));

        filmService.assignActorsToFilm(10, Arrays.asList(1, 2));

        verify(filmActorRepository, times(2)).save(any(FilmActor.class));
    }

    @Test
    void assignActorsToFilm_skipsExistingAssociation() {
        Film film = new Film(); film.setFilmId(10);
        Actor a1 = new Actor(); a1.setId(1);

        when(filmRepository.findById(10)).thenReturn(Optional.of(film));
        when(actorRepository.findById(1)).thenReturn(Optional.of(a1));
        when(filmActorRepository.existsByFilmAndActor(film, a1)).thenReturn(true);

        filmService.assignActorsToFilm(10, Collections.singletonList(1));

        verify(filmActorRepository, never()).save(any(FilmActor.class));
    }

    @Test
    void getActorsOfFilm_success() throws Exception {
        Film film = new Film(); film.setFilmId(15);
        Actor a = new Actor(); a.setId(3);

        when(filmRepository.findById(15)).thenReturn(Optional.of(film));
        when(filmActorRepository.findActorsByFilmId(15)).thenReturn(Arrays.asList(a));
        ActorDto ad = new ActorDto(); ad.setId(3);
        when(modelMapper.map(a, ActorDto.class)).thenReturn(ad);

        List<ActorDto> result = filmService.getActorsOfFilm(15);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(3);
    }

    @Test
    void findFilmsByCategory_success() throws Exception {
        Film f = new Film(); f.setFilmId(20);
        when(filmCategoryRepository.findFilmsByCategoryName("Action")).thenReturn(Arrays.asList(f));
        FilmDto d = new FilmDto(); d.setFilmId(20);
        when(modelMapper.map(f, FilmDto.class)).thenReturn(d);

        List<FilmDto> result = filmService.findFilmsByCategory("Action");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFilmId()).isEqualTo(20);
    }

    @Test
    void findFilmsByCategory_empty_throws() {
        assertThrows(InvalidInputException.class, () -> filmService.findFilmsByCategory(" "));
    }
}

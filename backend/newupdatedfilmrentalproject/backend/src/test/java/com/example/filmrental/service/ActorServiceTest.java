package com.example.filmrental.service;

import com.example.filmrental.dto.ActorDto;

import com.example.filmrental.exception.InvalidInputException;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Actor;
import com.example.filmrental.model.Film;
import com.example.filmrental.model.FilmActor;
import com.example.filmrental.repository.ActorRepository;
import com.example.filmrental.repository.FilmActorRepository;
import com.example.filmrental.repository.FilmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private FilmActorRepository filmActorRepository;

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ActorService actorService;

    private Actor actor1;
    private Actor actor2;

    @BeforeEach
    void init() {
        actor1 = buildActor(1, "PENELOPE", "GUINESS");
        actor2 = buildActor(2, "NICK", "WAHLBERG");
    }

    private Actor buildActor(Integer id, String first, String last) {
        Actor a = new Actor();
        a.setId(id);
        a.setFirstName(first);
        a.setLastName(last);
        return a;
    }

    private Film buildFilm(Integer id, String title) {
        Film f = new Film();
        f.setFilmId(id);
        f.setTitle(title);
        return f;
    }

    private FilmActor buildFilmActor(Film film, Actor actor, Instant lastUpdate) {
        FilmActor fa = new FilmActor();
        fa.setFilm(film);
        fa.setActor(actor);
        fa.setLastUpdate(lastUpdate);
        return fa;
    }

    @Test
    void getActorsByLastName_success() throws Exception {
        when(actorRepository.findByLastName("GUINESS"))
                .thenReturn(List.of(actor1));

        List<ActorDto> result = actorService.getActorsByLastName("GUINESS");

        assertEquals(1, result.size());
        assertEquals(actor1.getId(), result.get(0).getId());
        assertEquals(actor1.getFirstName(), result.get(0).getFirstName());
        assertEquals(actor1.getLastName(), result.get(0).getLastName());
    }

    @Test
    void getActorsByLastName_null_throwsInvalidInput() {
        assertThrows(InvalidInputException.class, () ->
                actorService.getActorsByLastName(null));
        verifyNoInteractions(actorRepository);
    }

    @Test
    void getActorsByLastName_notFound_throwsResourceNotFound() {
        when(actorRepository.findByLastName("UNKNOWN")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () ->
                actorService.getActorsByLastName("UNKNOWN"));
    }

    @Test
    void getActorsByFirstName_success() throws Exception {
        when(actorRepository.findByFirstName("PENELOPE"))
                .thenReturn(List.of(actor1));

        List<ActorDto> result = actorService.getActorsByFirstName("PENELOPE");

        assertEquals(1, result.size());
        assertEquals(actor1.getId(), result.get(0).getId());
        assertEquals(actor1.getFirstName(), result.get(0).getFirstName());
        assertEquals(actor1.getLastName(), result.get(0).getLastName());
    }

    @Test
    void getActorsByFirstName_null_throwsInvalidInput() {
        assertThrows(InvalidInputException.class, () ->
                actorService.getActorsByFirstName(null));
        verifyNoInteractions(actorRepository);
    }

    @Test
    void getActorsByFirstName_notFound_throwsResourceNotFound() {
        when(actorRepository.findByFirstName("UNKNOWN")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () ->
                actorService.getActorsByFirstName("UNKNOWN"));
    }

    @Test
    void updateLastName_success() throws Exception {
        when(actorRepository.findById(1)).thenReturn(Optional.of(actor1));

        actorService.updateLastName(1, "NEWLAST");

        ArgumentCaptor<Actor> captor = ArgumentCaptor.forClass(Actor.class);
        verify(actorRepository).save(captor.capture());
        Actor saved = captor.getValue();
        assertEquals("NEWLAST", saved.getLastName());
        assertEquals(1, saved.getId());
    }

    @Test
    void updateLastName_empty_throwsInvalidInput() {
        assertThrows(InvalidInputException.class, () ->
                actorService.updateLastName(1, ""));
        verify(actorRepository, never()).save(any());
    }

    @Test
    void updateLastName_notFound_throwsResourceNotFound() {
        when(actorRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                actorService.updateLastName(99, "ANY"));
    }

    @Test
    void updateFirstName_success() throws Exception {
        when(actorRepository.findById(2)).thenReturn(Optional.of(actor2));

        actorService.updateFirstName(2, "NEWFIRST");

        ArgumentCaptor<Actor> captor = ArgumentCaptor.forClass(Actor.class);
        verify(actorRepository).save(captor.capture());
        Actor saved = captor.getValue();
        assertEquals("NEWFIRST", saved.getFirstName());
        assertEquals(2, saved.getId());
    }

    @Test
    void updateFirstName_empty_throwsInvalidInput() {
        assertThrows(InvalidInputException.class, () ->
                actorService.updateFirstName(2, ""));
        verify(actorRepository, never()).save(any());
    }

    @Test
    void updateFirstName_notFound_throwsResourceNotFound() {
        when(actorRepository.findById(77)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                actorService.updateFirstName(77, "X"));
    }

    @Test
    void getTopTenActorsByFilmCount_returnsFirstTen() throws Exception {
        List<Actor> many = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            many.add(buildActor(i, "F" + i, "L" + i));
        }
        when(actorRepository.findAll()).thenReturn(many);

        List<ActorDto> result = actorService.getTopTenActorsByFilmCount();

        assertEquals(10, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(10, result.get(9).getId());
    }

    @Test
    void getTopTenActorsByFilmCount_noActors_throwsResourceNotFound() {
        when(actorRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () ->
                actorService.getTopTenActorsByFilmCount());
    }

    @Test
    void addActor_success() throws Exception {
        ActorDto dto = new ActorDto(10, "A", "B");

        String res = actorService.addActor(dto);

        assertEquals("Record Created Successfully", res);
        ArgumentCaptor<Actor> captor = ArgumentCaptor.forClass(Actor.class);
        verify(actorRepository).save(captor.capture());
        Actor saved = captor.getValue();
        assertEquals(10, saved.getId());
        assertEquals("A", saved.getFirstName());
        assertEquals("B", saved.getLastName());
    }

    @Test
    void addActor_invalidInput_throwsInvalidInput() {
        ActorDto dto = new ActorDto(1, null, "B");
        assertThrows(InvalidInputException.class, () -> actorService.addActor(dto));

        ActorDto dto2 = new ActorDto(1, "A", "");
        assertThrows(InvalidInputException.class, () -> actorService.addActor(dto2));
        verify(actorRepository, never()).save(any());
    }

    @Test
    void assignFilmToActor_success_savesOnlyNewLinks() throws Exception {
        Actor actor = buildActor(5, "X", "Y");
        Film film1 = buildFilm(100, "Film 100");
        Film film2 = buildFilm(200, "Film 200");

        when(actorRepository.findById(5)).thenReturn(Optional.of(actor));
        when(filmRepository.findById(100)).thenReturn(Optional.of(film1));
        when(filmRepository.findById(200)).thenReturn(Optional.of(film2));

        when(filmActorRepository.existsByFilmAndActor(film1, actor)).thenReturn(true);
        when(filmActorRepository.existsByFilmAndActor(film2, actor)).thenReturn(false);

        actorService.assignFilmToActor(5, List.of(100, 200));

        ArgumentCaptor<FilmActor> captor = ArgumentCaptor.forClass(FilmActor.class);
        verify(filmActorRepository, times(1)).save(captor.capture());
        FilmActor saved = captor.getValue();
        assertEquals(film2, saved.getFilm());
        assertEquals(actor, saved.getActor());
        assertNotNull(saved.getLastUpdate());
    }

    @Test
    void assignFilmToActor_actorNotFound_throwsResourceNotFound() {
        when(actorRepository.findById(5)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                actorService.assignFilmToActor(5, List.of(1, 2)));
        verifyNoInteractions(filmRepository);
        verify(filmActorRepository, never()).save(any());
    }

    @Test
    void assignFilmToActor_filmNotFound_throwsResourceNotFound() {
        Actor actor = buildActor(5, "X", "Y");
        when(actorRepository.findById(5)).thenReturn(Optional.of(actor));
        when(filmRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                actorService.assignFilmToActor(5, List.of(1)));
        verify(filmActorRepository, never()).save(any());
    }

    @Test
    void getFilmsByActorId_success() throws Exception {
        Actor a = buildActor(9, "A", "B");
        Film f1 = buildFilm(10, "F1");
        Film f2 = buildFilm(11, "F2");

        FilmActor fa1 = buildFilmActor(f1, a, Instant.now());
        FilmActor fa2 = buildFilmActor(f2, a, Instant.now());
        FilmActor faOther = buildFilmActor(buildFilm(12, "F3"), buildActor(99, "C", "D"), Instant.now());

        when(filmActorRepository.findAll()).thenReturn(List.of(fa1, fa2, faOther));

        List<Film> films = actorService.getFilmsByActorId(9);

        assertEquals(2, films.size());
        assertTrue(films.stream().anyMatch(f -> f.getFilmId().equals(10)));
        assertTrue(films.stream().anyMatch(f -> f.getFilmId().equals(11)));
    }

    @Test
    void getFilmsByActorId_notFound_throwsResourceNotFound() {
        when(filmActorRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () ->
                actorService.getFilmsByActorId(1));
    }
}

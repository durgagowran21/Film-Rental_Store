
package com.example.filmrental.controller;

import com.example.filmrental.dto.FilmDto;
import com.example.filmrental.exception.InvalidInputException;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.FilmCategory;
import com.example.filmrental.service.FilmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pure unit tests for FilmController using Mockito + standalone MockMvc.
 * No Spring context is started here.
 * NOTE: Since the controller does not define @ExceptionHandler,
 * exceptions thrown during requests will result in HTTP 500 in these tests.
 */

class FilmControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private FilmService filmService;

    @InjectMocks
    private FilmController filmController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
    }

    private static final String BASE = "/api/films";

    // ------------------ Create ------------------

   /* @Test
    @DisplayName("POST /api/films/post => 200 OK with 'Record Saved'")
    void addFilm_ok() throws Exception {
        FilmDto dto = new FilmDto();

        mockMvc.perform(post(BASE + "/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Record Saved"));

        // addFilm(...) is void => just verify
        verify(filmService).addFilm(any(FilmDto.class));
    }
    *?
/*
    @Test
    @DisplayName("POST /api/films/post => 500 when service throws InvalidInputException (void method requires doThrow)")
    void addFilm_invalid_returns500() throws Exception {
        FilmDto dto = new FilmDto();

        // Correct stubbing for void methods:
        doThrow(new InvalidInputException("Invalid film data"))
                .when(filmService).addFilm(any(FilmDto.class));

        mockMvc.perform(post(BASE + "/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Invalid film data")));
    }
*/
    // ------------------ Read ------------------

    @Test
    @DisplayName("GET /api/films => 200 with list of FilmDto")
    void getAllFilm_ok() throws Exception {
        when(filmService.getAllFilm()).thenReturn(List.of(new FilmDto(), new FilmDto()));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(filmService).getAllFilm();
    }

    @Test
    @DisplayName("GET /api/films/title/{title} => 200 with FilmDto when found")
    void findByTitle_ok() throws Exception {
        String title = "Inception";
        when(filmService.findFilmsByTitle(title)).thenReturn(new FilmDto());

        mockMvc.perform(get(BASE + "/title/{title}", title))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(filmService).findFilmsByTitle(title);
    }

   /* @Test
    @DisplayName("GET /api/films/title/{title} => 500 when not found (controller throws ResourceNotFoundException)")
    void findByTitle_notFound_returns500() throws Exception {
        String title = "Unknown";
        when(filmService.findFilmsByTitle(title)).thenReturn(null); // controller will throw

        mockMvc.perform(get(BASE + "/title/{title}", title))
                .andExpect(status().isInternalServerError());
    }
*/
    @Test
    @DisplayName("GET /api/films/year/{year} => 200 with list")
    void findFilmsByReleaseYear_ok() throws Exception {
        int year = 2020;
        when(filmService.findFilmsByReleaseYear(year)).thenReturn(List.of(new FilmDto()));

        mockMvc.perform(get(BASE + "/year/{release_year}", year))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(filmService).findFilmsByReleaseYear(year);
    }
/*
    @Test
    @DisplayName("GET /api/films/year/{year} => 500 when empty")
    void findFilmsByReleaseYear_empty_returns500() throws Exception {
        int year = 1999;
        when(filmService.findFilmsByReleaseYear(year)).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/year/{release_year}", year))
                .andExpect(status().isInternalServerError());
    }
    */

    @Test
    @DisplayName("GET /api/films/duration/gt/{rd} => 200 with list")
    void findFilmsWhereRentalDurationIsGreater_ok() throws Exception {
        int rd = 5;
        when(filmService.findFilmsByRentalDuration(rd)).thenReturn(List.of(new FilmDto(), new FilmDto()));

        mockMvc.perform(get(BASE + "/duration/gt/{rd}", rd))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(filmService).findFilmsByRentalDuration(rd);
    }

   /* @Test
    @DisplayName("GET /api/films/duration/gt/{rd} => 500 when empty")
    void findFilmsWhereRentalDurationIsGreater_empty_returns500() throws Exception {
        int rd = 10;
        when(filmService.findFilmsByRentalDuration(rd)).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/duration/gt/{rd}", rd))
                .andExpect(status().isInternalServerError());
    }
    */

    @Test
    @DisplayName("GET /api/films/rate/gt/{rate} => 200 with list")
    void findFilmsWhereRentalRateIsGreater_ok() throws Exception {
        int rate = 4;
        when(filmService.findFilmsWhereRentalRateIsGreater(rate)).thenReturn(List.of(new FilmDto()));

        mockMvc.perform(get(BASE + "/rate/gt/{rate}", rate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(filmService).findFilmsWhereRentalRateIsGreater(rate);
    }

   /* @Test
    @DisplayName("GET /api/films/rate/gt/{rate} => 500 when empty")
    void findFilmsWhereRentalRateIsGreater_empty_returns500() throws Exception {
        int rate = 9;
        when(filmService.findFilmsWhereRentalRateIsGreater(rate)).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/rate/gt/{rate}", rate))
                .andExpect(status().isInternalServerError());
    }
    */

    @Test
    @DisplayName("GET /api/films/length/gt/{length} => 200 with list")
    void findFilmsWhereLengthIsGreater_ok() throws Exception {
        int length = 120;
        when(filmService.findFilmsWhereLengthIsGreater(length)).thenReturn(List.of(new FilmDto(), new FilmDto(), new FilmDto()));

        mockMvc.perform(get(BASE + "/length/gt/{length}", length))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        verify(filmService).findFilmsWhereLengthIsGreater(length);
    }

   /* @Test
    @DisplayName("GET /api/films/length/gt/{length} => 500 when empty")
    void findFilmsWhereLengthIsGreater_empty_returns500() throws Exception {
        int length = 200;
        when(filmService.findFilmsWhereLengthIsGreater(length)).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/length/gt/{length}", length))
                .andExpect(status().isInternalServerError());
    }*/

    @Test
    @DisplayName("GET /api/films/duration/lt/{rd} => 200 with list")
    void findFilmsWhereRentalDurationIsLower_ok() throws Exception {
        int rd = 3;
        when(filmService.findFilmsWhereRentalDurationIsLower(rd)).thenReturn(List.of(new FilmDto()));

        mockMvc.perform(get(BASE + "/duration/lt/{rd}", rd))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(filmService).findFilmsWhereRentalDurationIsLower(rd);
    }

    /*@Test
    @DisplayName("GET /api/films/duration/lt/{rd} => 500 when empty")
    void findFilmsWhereRentalDurationIsLower_empty_returns500() throws Exception {
        int rd = 1;
        when(filmService.findFilmsWhereRentalDurationIsLower(rd)).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/duration/lt/{rd}", rd))
                .andExpect(status().isInternalServerError());
    }
    */

    @Test
    @DisplayName("GET /api/films/rate/lt/{rate} => 200 with list")
    void findFilmsWhereRentalRateIsLower_ok() throws Exception {
        int rate = 2;
        when(filmService.findFilmsWhereRateIsLower(rate)).thenReturn(List.of(new FilmDto(), new FilmDto()));

        mockMvc.perform(get(BASE + "/rate/lt/{rate}", rate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(filmService).findFilmsWhereRateIsLower(rate);
    }

   /* @Test
    @DisplayName("GET /api/films/rate/lt/{rate} => 500 when empty")
    void findFilmsWhereRentalRateIsLower_empty_returns500() throws Exception {
        int rate = 1;
        when(filmService.findFilmsWhereRateIsLower(rate)).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/rate/lt/{rate}", rate))
                .andExpect(status().isInternalServerError());
    }
    */

    @Test
    @DisplayName("GET /api/films/length/lt/{length} => 200 with list")
    void findFilmsWhereLengthIsLower_ok() throws Exception {
        int length = 100;
        when(filmService.findFilmsWhereLengthIsLower(length)).thenReturn(List.of(new FilmDto()));

        mockMvc.perform(get(BASE + "/length/lt/{length}", length))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(filmService).findFilmsWhereLengthIsLower(length);
    }

   /* @Test
    @DisplayName("GET /api/films/length/lt/{length} => 500 when empty")
    void findFilmsWhereLengthIsLower_empty_returns500() throws Exception {
        int length = 50;
        when(filmService.findFilmsWhereLengthIsLower(length)).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/length/lt/{length}", length))
                .andExpect(status().isInternalServerError());
    }
    */

    @Test
    @DisplayName("GET /api/films/betweenyear/{from}/{to} => 200 with list")
    void findFilmBetweenYear_ok() throws Exception {
        int from = 2010, to = 2015;
        when(filmService.findFilmBetweenYear(from, to)).thenReturn(List.of(new FilmDto(), new FilmDto()));

        mockMvc.perform(get(BASE + "/betweenyear/{from}/{to}", from, to))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(filmService).findFilmBetweenYear(from, to);
    }

   /* @Test
    @DisplayName("GET /api/films/betweenyear/{from}/{to} => 500 when empty")
    void findFilmBetweenYear_empty_returns500() throws Exception {
        int from = 1900, to = 1905;
        when(filmService.findFilmBetweenYear(from, to)).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/betweenyear/{from}/{to}", from, to))
                .andExpect(status().isInternalServerError());
    }
    */

    @Test
    @DisplayName("GET /api/films/rating/lt/{rating} => 200 with list")
    void findFilmsWhereRatingIsLower_ok() throws Exception {
        int rating = 3;
        when(filmService.findFilmsWhereRatingIsLower(rating)).thenReturn(List.of(new FilmDto()));

        mockMvc.perform(get(BASE + "/rating/lt/{rating}", rating))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(filmService).findFilmsWhereRatingIsLower(rating);
    }

   /* @Test
    @DisplayName("GET /api/films/rating/lt/{rating} => 500 when empty")
    void findFilmsWhereRatingIsLower_empty_returns500() throws Exception {
        int rating = 0;
        when(filmService.findFilmsWhereRatingIsLower(rating)).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/rating/lt/{rating}", rating))
                .andExpect(status().isInternalServerError());
    }
    */

    @Test
    @DisplayName("GET /api/films/rating/gt/{rating} => 200 with list")
    void findFilmsWhereRatingIsHigher_ok() throws Exception {
        int rating = 4;
        when(filmService.findFilmsWhereRatingIsHigher(rating)).thenReturn(List.of(new FilmDto(), new FilmDto()));

        mockMvc.perform(get(BASE + "/rating/gt/{rating}", rating))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(filmService).findFilmsWhereRatingIsHigher(rating);
    }

    /*@Test
    @DisplayName("GET /api/films/rating/gt/{rating} => 500 when empty")
    void findFilmsWhereRatingIsHigher_empty_returns500() throws Exception {
        int rating = 10;
        when(filmService.findFilmsWhereRatingIsHigher(rating)).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/rating/gt/{rating}", rating))
                .andExpect(status().isInternalServerError());
    }
    */

    @Test
    @DisplayName("GET /api/films/language/{lang} => 200 with list")
    void findFilmsByLanguage_ok() throws Exception {
        String lang = "English";
        when(filmService.findFilmsByLanguage(lang)).thenReturn(List.of(new FilmDto()));

        mockMvc.perform(get(BASE + "/language/{lang}", lang))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(filmService).findFilmsByLanguage(lang);
    }

   /* @Test
    @DisplayName("GET /api/films/language/{lang} => 500 when empty")
    void findFilmsByLanguage_empty_returns500() throws Exception {
        String lang = "Klingon";
        when(filmService.findFilmsByLanguage(lang)).thenReturn(List.of());

        mockMvc.perform(get(BASE + "/language/{lang}", lang))
                .andExpect(status().isInternalServerError());
    }
    */

    @Test
    @DisplayName("GET /api/films/countbyyear => 200 with Map<Integer, Integer>")
    void displayFilmsNumberByYear_ok() throws Exception {
        Map<Integer, Integer> countByYear = new LinkedHashMap<>();
        countByYear.put(2020, 5);
        countByYear.put(2021, 7);

        when(filmService.displayFilmsNumberByYear()).thenReturn(countByYear);

        mockMvc.perform(get(BASE + "/countbyyear"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // JSON object keys are strings
                .andExpect(jsonPath("$", hasKey("2020")))
                .andExpect(jsonPath("$", hasKey("2021")))
                .andExpect(jsonPath("$['2020']").value(5))
                .andExpect(jsonPath("$['2021']").value(7));

        verify(filmService).displayFilmsNumberByYear();
    }

    // ------------------ Update (new endpoints) ------------------

    @Test
    @DisplayName("PUT /api/films/update/title/{id} => 200 with FilmDto")
    void updateTitle_ok() throws Exception {
        int id = 10;
        String title = "New Title";
        FilmDto updated = new FilmDto();

        when(filmService.updateTitle(eq(id), eq(title))).thenReturn(updated);

        mockMvc.perform(put(BASE + "/update/title/{id}", id)
                        .contentType(MediaType.TEXT_PLAIN)  // String body as text/plain
                        .content(title))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(filmService).updateTitle(id, title);
    }

    /*@Test
    @DisplayName("PUT /api/films/update/title/{id} => 500 when service throws InvalidInputException")
    void updateTitle_invalid_returns500() throws Exception {
        int id = 10;
        String title = "";

        when(filmService.updateTitle(eq(id), eq(title)))
                .thenThrow(new InvalidInputException("Title must not be empty"));

        mockMvc.perform(put(BASE + "/update/title/{id}", id)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(title))
                .andExpect(status().isInternalServerError());
    }
    */

    @Test
    @DisplayName("PUT /api/films/update/releaseyear/{id} => 200 with FilmDto")
    void updateReleaseYear_ok() throws Exception {
        int id = 12;
        int year = 2021;
        FilmDto updated = new FilmDto();

        when(filmService.updateReleaseYear(eq(id), eq(year))).thenReturn(updated);

        mockMvc.perform(put(BASE + "/update/releaseyear/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(year))) // JSON number
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(filmService).updateReleaseYear(id, year);
    }

    @Test
    @DisplayName("PUT /api/films/update/rentalduration/{id} => 200 with FilmDto")
    void updateRentalDuration_ok() throws Exception {
        int id = 15;
        int rentalDuration = 7;
        FilmDto updated = new FilmDto();

        when(filmService.updateRentalDuration(eq(id), eq(rentalDuration))).thenReturn(updated);

        mockMvc.perform(put(BASE + "/update/rentalduration/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rentalDuration))) // JSON number
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(filmService).updateRentalDuration(id, rentalDuration);
    }

    @Test
    @DisplayName("PUT /api/films/update/rentalrate/{id} => 200 with FilmDto")
    void updateRentalRate_ok() throws Exception {
        int id = 18;
        int rentalRate = 5;
        FilmDto updated = new FilmDto();

        when(filmService.updateRentalRate(eq(id), eq(rentalRate))).thenReturn(updated);

        mockMvc.perform(put(BASE + "/update/rentalrate/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rentalRate))) // JSON number
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(filmService).updateRentalRate(id, rentalRate);
    }

    @Test
    @DisplayName("PUT /api/films/update/rating/{id} => 200 with FilmDto")
    void updateRating_ok() throws Exception {
        int id = 20;
        int rating = 4;
        FilmDto updated = new FilmDto();

        when(filmService.updateRating(eq(id), eq(rating))).thenReturn(updated);

        mockMvc.perform(put(BASE + "/update/rating/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rating))) // JSON number
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(filmService).updateRating(id, rating);
    }

    @Test
    @DisplayName("PUT /api/films/update/language/{id} => 200 with FilmDto")
    void updateLanguage_ok() throws Exception {
        int id = 22;
        Integer langId = 3;
        FilmDto updated = new FilmDto();

        when(filmService.updateLanguage(eq(id), eq(langId))).thenReturn(updated);

        mockMvc.perform(put(BASE + "/update/language/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(langId))) // JSON number
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(filmService).updateLanguage(id, langId);
    }

    @Test
    @DisplayName("PUT /api/films/{filmId}/actors => 200 with success message")
    void assignActorsToFilm_ok() throws Exception {
        Integer filmId = 30;
        Collection<Integer> actorIds = List.of(1, 2, 3);

        // Optional: doNothing for clarity; default is no-op
        doNothing().when(filmService).assignActorsToFilm(filmId, actorIds);

        mockMvc.perform(put(BASE + "/{filmId}/actors", filmId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actorIds)))
                .andExpect(status().isOk())
                .andExpect(content().string("Actors assigned to film successfully"));

        verify(filmService).assignActorsToFilm(filmId, actorIds);
    }

   /* @Test
    @DisplayName("PUT /api/films/{filmId}/actors => 500 when service throws RuntimeException (void => doThrow)")
    void assignActorsToFilm_error_returns500() throws Exception {
        Integer filmId = 30;
        Collection<Integer> actorIds = List.of(1, 2, 3);

        doThrow(new RuntimeException("Service error"))
                .when(filmService).assignActorsToFilm(filmId, actorIds);

        mockMvc.perform(put(BASE + "/{filmId}/actors", filmId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actorIds)))
                .andExpect(status().isInternalServerError());
    }
    */

    @Test
    @DisplayName("PUT /api/films/update/category/{id} => 200 with FilmCategory")
    void updateCategory_ok() throws Exception {
        Integer id = 40;
        Integer categoryId = 5;
        FilmCategory updatedCategory = new FilmCategory();

        when(filmService.updateCategory(eq(id), eq(categoryId))).thenReturn(updatedCategory);

        mockMvc.perform(put(BASE + "/update/category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryId))) // JSON number
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(filmService).updateCategory(id, categoryId);
    }

    /*@Test
    @DisplayName("PUT /api/films/update/category/{id} => 500 when service throws InvalidInputException")
    void updateCategory_invalid_returns500() throws Exception {
        Integer id = 40;
        Integer categoryId = -1;

        when(filmService.updateCategory(eq(id), eq(categoryId)))
                .thenThrow(new InvalidInputException("Invalid category"));

        mockMvc.perform(put(BASE + "/update/category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryId)))
                .andExpect(status().isInternalServerError());
    }
    */
}


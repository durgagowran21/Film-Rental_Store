
package com.example.filmrental.controller;

import com.example.filmrental.dto.ActorDto;
import com.example.filmrental.exception.InvalidInputException;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Film;
import com.example.filmrental.service.ActorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pure unit tests for ActorController (no Spring context).
 * Note: This controller does NOT have @ExceptionHandler methods.
 * Exceptions will result in HTTP 500 unless handled inside the method (e.g., assignFilmToActor).
 */
class ActorControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ActorService actorService;

    @InjectMocks
    private ActorController actorController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(actorController).build();
    }

    private static final String BASE = "/api/actors";

    // ---------- GET: /lastname/{ln} ----------
    @Test
    @DisplayName("GET /api/actors/lastname/{ln} => 200 with list")
    void getActorsByLastName_ok() throws Exception {
        ActorDto a1 = new ActorDto();
        ActorDto a2 = new ActorDto();

        when(actorService.getActorsByLastName("Doe")).thenReturn(List.of(a1, a2));

        mockMvc.perform(get(BASE + "/lastname/{ln}", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(actorService).getActorsByLastName("Doe");
    }

    // ---------- GET: /firstname/{fn} ----------
    @Test
    @DisplayName("GET /api/actors/firstname/{fn} => 200 with list")
    void getActorsByFirstName_ok() throws Exception {
        ActorDto a1 = new ActorDto();
        ActorDto a2 = new ActorDto();

        when(actorService.getActorsByFirstName("John")).thenReturn(List.of(a1, a2));

        mockMvc.perform(get(BASE + "/firstname/{fn}", "John"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(actorService).getActorsByFirstName("John");
    }

    // ---------- PUT: /update/lastname/{id} (text/plain) ----------
    @Test
    @DisplayName("PUT /api/actors/update/lastname/{id} with text/plain => trims and calls service; returns 200")
    void updateLastName_ok_trims() throws Exception {
        int id = 10;
        String raw = "   Parker  ";

        mockMvc.perform(put(BASE + "/update/lastname/{id}", id)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(raw))
                .andExpect(status().isOk())
                .andExpect(content().string(isEmptyString()));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(actorService).updateLastName(eq(id), captor.capture());
        assert "Parker".equals(captor.getValue());
    }
/*
    @Test
    @DisplayName("PUT /api/actors/update/lastname/{id} with empty text => 500 (InvalidInputException, no handler)")
    void updateLastName_empty_returns500() throws Exception {
        int id = 10;

        mockMvc.perform(put(BASE + "/update/lastname/{id}", id)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("   "))
                .andExpect(status().isInternalServerError()); // no @ExceptionHandler -> 500
    }
    */

    // ---------- PUT: /update/firstname/{id} (text/plain) ----------
    @Test
    @DisplayName("PUT /api/actors/update/firstname/{id} with text/plain => trims and calls service; returns 200")
    void updateFirstName_ok_trims() throws Exception {
        int id = 11;
        String raw = "   Peter  ";

        mockMvc.perform(put(BASE + "/update/firstname/{id}", id)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(raw))
                .andExpect(status().isOk())
                .andExpect(content().string(isEmptyString()));

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(actorService).updateFirstName(eq(id), captor.capture());
        assert "Peter".equals(captor.getValue());
    }
/*
    @Test
    @DisplayName("PUT /api/actors/update/firstname/{id} with empty text => 500 (InvalidInputException, no handler)")
    void updateFirstName_empty_returns500() throws Exception {
        int id = 11;

        mockMvc.perform(put(BASE + "/update/firstname/{id}", id)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(""))
                .andExpect(status().isInternalServerError()); // no @ExceptionHandler -> 500
    }
    */

    // ---------- GET: /toptenbyfilmcount ----------
    @Test
    @DisplayName("GET /api/actors/toptenbyfilmcount => 200 with list")
    void getTopTenActorsByFilmCount_ok() throws Exception {
        when(actorService.getTopTenActorsByFilmCount())
                .thenReturn(List.of(new ActorDto(), new ActorDto()));

        mockMvc.perform(get(BASE + "/toptenbyfilmcount"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(actorService).getTopTenActorsByFilmCount();
    }

    // ---------- POST: /post ----------
    @Test
    @DisplayName("POST /api/actors/post => 201 Created with message")
    void addActor_created() throws Exception {
        ActorDto payload = new ActorDto();

        when(actorService.addActor(any(ActorDto.class)))
                .thenReturn("Actor created successfully");

        mockMvc.perform(post(BASE + "/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Actor created successfully"));

        verify(actorService).addActor(any(ActorDto.class));
    }

    // ---------- PUT: /{actorId}/film ----------
    @Test
    @DisplayName("PUT /api/actors/{actorId}/film => 200 OK when assigning films succeeds")
    void assignFilmToActor_ok() throws Exception {
        Integer actorId = 5;
        Collection<Integer> filmIds = List.of(1, 2, 3);

        doNothing().when(actorService).assignFilmToActor(actorId, filmIds);

        mockMvc.perform(put(BASE + "/{actorId}/film", actorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmIds)))
                .andExpect(status().isOk())
                .andExpect(content().string("Films assigned to Actor successfully"));

        verify(actorService).assignFilmToActor(actorId, filmIds);
    }

    @Test
    @DisplayName("PUT /api/actors/{actorId}/film => 400 Bad Request when service throws ResourceNotFoundException (controller catches)")
    void assignFilmToActor_notFound_returnsBadRequest() throws Exception {
        Integer actorId = 999;
        Collection<Integer> filmIds = List.of(4, 5);

        doThrow(new ResourceNotFoundException("Actor not found"))
                .when(actorService).assignFilmToActor(actorId, filmIds);

        mockMvc.perform(put(BASE + "/{actorId}/film", actorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmIds)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Actor not found")));

        verify(actorService).assignFilmToActor(actorId, filmIds);
    }

    // ---------- GET: /{id}/films ----------
    @Test
    @DisplayName("GET /api/actors/{id}/films => 200 with film list")
    void getFilmsByActorId_ok() throws Exception {
        Film f1 = new Film();
        Film f2 = new Film();

        when(actorService.getFilmsByActorId(42))
                .thenReturn(List.of(f1, f2));

        mockMvc.perform(get(BASE + "/{id}/films", 42))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(actorService).getFilmsByActorId(42);
    }
/*
    @Test
    @DisplayName("GET /api/actors/{id}/films => 500 when service throws ResourceNotFoundException (no handler)")
    void getFilmsByActorId_notFound_returns500() throws Exception {
        when(actorService.getFilmsByActorId(404))
                .thenThrow(new ResourceNotFoundException("Actor not found"));

        mockMvc.perform(get(BASE + "/{id}/films", 404))
                .andExpect(status().isInternalServerError()); // no @ExceptionHandler -> 500
    }
    */
}

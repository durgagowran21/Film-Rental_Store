//package com.example.filmrental.dto;
//
//import java.time.LocalDateTime;
//
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.PastOrPresent;
//import jakarta.validation.constraints.Positive;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class FilmCategoryDto 
//{
//
//    @NotNull(message = "Film ID is required")
//    @Positive(message = "Film ID must be positive")
//    private Long filmId;
//
//    @NotNull(message = "Category ID is required")
//    @Positive(message = "Category ID must be positive")
//    private Long categoryId;
//
//    @NotNull(message = "Last update is required")
//    @PastOrPresent(message = "Last update cannot be in the future")
//    private LocalDateTime lastUpdate;
//}

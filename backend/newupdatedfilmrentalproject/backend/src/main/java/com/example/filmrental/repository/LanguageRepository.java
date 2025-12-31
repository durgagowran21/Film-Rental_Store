package com.example.filmrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.filmrental.model.Language;

public interface LanguageRepository extends JpaRepository<Language, Integer> {

}

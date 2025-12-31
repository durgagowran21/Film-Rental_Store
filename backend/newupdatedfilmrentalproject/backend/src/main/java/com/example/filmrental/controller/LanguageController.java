package com.example.filmrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filmrental.model.Country;
import com.example.filmrental.model.Language;
import com.example.filmrental.service.CountryService;
import com.example.filmrental.service.LanguageService;
@RestController
@RequestMapping("/api/language")
public class LanguageController {
	@Autowired
	private LanguageService languageservice;
	
	@PostMapping("/post")
	public ResponseEntity<String> createlanguage(@RequestBody Language lang) {

		languageservice.createlanguage(lang);

		return new ResponseEntity<>("Record Created Successfully", HttpStatus.CREATED);

	}

}

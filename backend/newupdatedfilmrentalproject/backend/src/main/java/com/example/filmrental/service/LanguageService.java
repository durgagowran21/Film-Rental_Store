package com.example.filmrental.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.filmrental.model.Country;
import com.example.filmrental.model.Language;
import com.example.filmrental.repository.CountryRepository;
import com.example.filmrental.repository.LanguageRepository;
@Service
public class LanguageService {
	@Autowired
	private LanguageRepository languagerepo;
//	@Autowired
//    ModelMapper modelMapper;

	
	public Language createlanguage(Language lang) {


        Language savedlang = languagerepo.save(lang);

        return savedlang;

    }

}

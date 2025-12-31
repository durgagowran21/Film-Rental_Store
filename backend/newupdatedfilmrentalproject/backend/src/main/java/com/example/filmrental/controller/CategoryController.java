package com.example.filmrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filmrental.model.Category;
import com.example.filmrental.model.Country;
import com.example.filmrental.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController 
{
	@Autowired
	private CategoryService categoryserv;
	@PostMapping("/post")
	public ResponseEntity<String> createCategory(@RequestBody Category category) {
		categoryserv.createcategory(category);
		return new ResponseEntity<>("Record Created Successfully", HttpStatus.CREATED);

	}

}

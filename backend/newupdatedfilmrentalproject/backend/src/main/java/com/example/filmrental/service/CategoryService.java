package com.example.filmrental.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.filmrental.model.Category;
import com.example.filmrental.repository.CategoryRepository;


@Service
public class CategoryService 
{
	@Autowired
	private CategoryRepository categoryrepo;
	public Category createcategory(Category category) {
        Category savedcategory = categoryrepo.save(category);
        return savedcategory;

    }


}

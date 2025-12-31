


package com.example.filmrental.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.filmrental.model.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> 
{
    boolean existsByNameIgnoreCase(String name);
}

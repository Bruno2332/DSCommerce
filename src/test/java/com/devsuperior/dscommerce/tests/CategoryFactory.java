package com.devsuperior.dscommerce.tests;

import com.devsuperior.dscommerce.entities.Category;

public class CategoryFactory {

    public static Category createCategory(){
        return new Category(1L, "Livros");
    }
}

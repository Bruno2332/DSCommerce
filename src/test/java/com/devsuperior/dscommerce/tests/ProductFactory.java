package com.devsuperior.dscommerce.tests;

import com.devsuperior.dscommerce.DTO.ProductDTO;
import com.devsuperior.dscommerce.DTO.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct(){
        Product product = new Product(1L, "Pc gamer", "Meu novo pc", 200.0, "www.pc.com");
        product.getCategories().add(CategoryFactory.createCategory());
        return product;
    }

    public static ProductDTO createProductDTO(){
        return new ProductDTO(createProduct());
    }

    public static ProductMinDTO createProductMinDTO(){
        return new ProductMinDTO(createProduct());
    }
}

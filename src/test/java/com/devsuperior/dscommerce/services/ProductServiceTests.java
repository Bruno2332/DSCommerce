package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.DTO.ProductDTO;
import com.devsuperior.dscommerce.DTO.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.CategoryRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscommerce.tests.CategoryFactory;
import com.devsuperior.dscommerce.tests.ProductFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private Product product;
    private Long existsId;
    private Long nonExistsId;
    private Long dependentId;
    private ProductDTO dto;
    private Category category;
    private Page<Product> page;

    @BeforeEach
    void setUp() throws Exception{
        product = ProductFactory.createProduct();
        dto = ProductFactory.createProductDTO();
        existsId = 1L;
        nonExistsId = 2L;
        dependentId = 3L;
        category = CategoryFactory.createCategory();
        page = new PageImpl<>(List.of(product));

        when(repository.getReferenceById(existsId)).thenReturn(product);
        when(repository.getReferenceById(nonExistsId)).thenThrow(EntityNotFoundException.class);
        when(categoryRepository.getReferenceById(existsId)).thenReturn(category);
        when(categoryRepository.getReferenceById(nonExistsId)).thenThrow(EntityNotFoundException.class);
        when(repository.searchByName(ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(page);
        when(repository.save(ArgumentMatchers.any())).thenReturn(product);
        when(repository.findById(existsId)).thenReturn(Optional.of(product));
        when(repository.findById(nonExistsId)).thenReturn(Optional.empty());
        doNothing().when(repository).deleteById(existsId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        when(repository.existsById(existsId)).thenReturn(true);
        when(repository.existsById(nonExistsId)).thenReturn(false);
        when(repository.existsById(dependentId)).thenReturn(true);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenExistsId(){
        ProductDTO result = service.findById(existsId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenNotExistsId(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistsId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductMinDTO> result = service.findAll("", pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(repository).searchByName("", pageable);
    }

    @Test
    public void insertProductShouldReturnProductDtoWhenBodyValid(){
        ProductDTO result = service.insert(dto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getName(), dto.getName());

    }

    @Test
    public void updateShouldUpdateWhenExistsId(){
        ProductDTO result = service.update(existsId, dto);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenNotExistsId(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistsId, dto);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistsId);
        });
    }

    @Test
    public void deleteShouldNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existsId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(existsId);
    }
}

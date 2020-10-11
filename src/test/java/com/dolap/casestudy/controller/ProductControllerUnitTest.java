package com.dolap.casestudy.controller;

import com.dolap.casestudy.exception.ApiRequestException;
import com.dolap.casestudy.model.Category;
import com.dolap.casestudy.model.Product;
import com.dolap.casestudy.model.User;
import com.dolap.casestudy.repository.CategoryRepository;
import com.dolap.casestudy.repository.ProductRepository;
import com.dolap.casestudy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ProductControllerUnitTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;

    private Product newProduct;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setId(new Long(1));
        User user2 = new User();
        user2.setId(new Long(2));
        Category category1 = new Category("Elbise");
        category1.setId(new Long(1));
        Category category2 = new Category("aksesuar");
        category2.setId(new Long(2));
        Category category3 = new Category("Ev Eşyası");
        category3.setId(new Long(3));
        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);

        when(categoryRepository.findAll()).thenReturn(categories);

        when(productRepository.findByCategoryNameContaining("Elbise")).thenReturn(Stream.of(
                new Product("Mavi Elbise","Uzun, M, mavi",200, user2, category1),
                new Product("Kırmızı Elbise","Uzun, M, kırmızı",200, user1, category1)).collect(Collectors.toList()));

        when(productRepository.findByCategoryNameContaining("aksesuar")).thenReturn(Stream.of(
                new Product( "İnci küpe","küçük",500, user2, category2)).collect(Collectors.toList()));

        when(productRepository.findByCategoryId(new Long(2))).thenReturn(Stream.of(
                new Product( "İnci küpe","küçük",500, user2, category2)).collect(Collectors.toList()));

        when(productRepository.findByCategoryId(new Long(1))).thenReturn(Stream.of(
                new Product( "Mavi Elbise","Uzun, M, mavi",200, user2, category1),
                new Product( "Kırmızı Elbise","Uzun, M, kırmızı",200, user1, category1)).collect(Collectors.toList()));

        newProduct = new Product("Mavi Elbise","Uzun, M, mavi", 200, user2,category1);

        when(productRepository.save(newProduct)).thenReturn(newProduct);
        when(categoryRepository.findById(new Long(1))).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(new Long(2))).thenReturn(Optional.of(category2));
        when(userRepository.findById(new Long(1))).thenReturn(Optional.of(user1));
        when(userRepository.findById(new Long(2))).thenReturn(Optional.of(user2));
    }

    @Test
    public void testAddProduct() throws Exception {
        productController.addProduct(newProduct);
        verify(userRepository, times(1)).findById(newProduct.getSeller().getId());
        verify(categoryRepository, times(1)).findById(newProduct.getCategory().getId());
        verify(productRepository,times(1)).save(newProduct);

        newProduct.getSeller().setId(new Long(10));
        assertThrows(ApiRequestException.class, () -> {
            productController.addProduct(newProduct);
        });

        newProduct.getCategory().setId(new Long(10));
        assertThrows(ApiRequestException.class, () -> {
            productController.addProduct(newProduct);
        });
    }

    @Test
    public void testFindProductsByCategoryId() {
        Long categoryId = new Long(1);

        List<Product> productList = productController.findProductsByCategoryId(categoryId);
        assertEquals(2, productList.size());
        assertEquals(new Long(1), productList.get(0).getCategory().getId());

        categoryId = new Long(2);
        List<Product> productList2 = productController.findProductsByCategoryId(categoryId);
        assertEquals(1, productList2.size());
        assertEquals(new Long(2), productList2.get(0).getCategory().getId());

    }

    @Test
    public void testFindProductsByCategoryName() {
        String categoryName = "Elbise";

        List<Product> productList = productController.findProductsByCategoryName(categoryName);
        assertEquals(2, productList.size());
        assertEquals("Elbise", productList.get(0).getCategory().getName());

        categoryName = "aksesuar";
        List<Product> productList2 = productController.findProductsByCategoryName(categoryName);
        assertEquals(1, productList2.size());
        assertEquals("aksesuar", productList2.get(0).getCategory().getName());
    }

    @Test
    public void testListCategories(){
        List<Category> categories = productController.listCategories();
        assertEquals(3,categories.size());
    }

    @Test
    public void testFindCategory(){
        Long categoryId1 = new Long(1);
        Category category1 = productController.findCategory(categoryId1);
        assertEquals(categoryId1, category1.getId());

        Long categoryId2 = new Long(2);
        Category category2 = productController.findCategory(categoryId2);
        assertEquals(categoryId2, category2.getId());

    }
}
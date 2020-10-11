package com.dolap.casestudy.controller;

import com.dolap.casestudy.model.Category;
import com.dolap.casestudy.model.Product;
import com.dolap.casestudy.model.User;
import com.dolap.casestudy.repository.CategoryRepository;
import com.dolap.casestudy.repository.ProductRepository;
import com.dolap.casestudy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private UserRepository userRepository;

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

        when(productRepository.save(Mockito.any(Product.class))).thenReturn(newProduct);
        when(categoryRepository.findById(new Long(1))).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(new Long(2))).thenReturn(Optional.of(category2));
        when(userRepository.findById(new Long(1))).thenReturn(Optional.of(user1));
        when(userRepository.findById(new Long(2))).thenReturn(Optional.of(user2));
    }

    @Test
    public void testAddProductController() throws Exception {
        StringBuilder jsonRequest = new StringBuilder();
        jsonRequest.append("{\n");
        jsonRequest.append("\"name\": \"Mavi Elbise\",\n");
        jsonRequest.append("\"explanation\" : \"Uzun, M beden, mavi\",\n");
        jsonRequest.append("\"category\" : {\n");
        jsonRequest.append("\"id\" : 2\n");
        jsonRequest.append("},\n");
        jsonRequest.append("\"seller\"   : {\n");
        jsonRequest.append("\"id\" : 1\n");
        jsonRequest.append("},\n");
        jsonRequest.append("\"price\"       : 200\n}");

        MockHttpServletRequestBuilder request = post("/addProduct").contentType(MediaType.APPLICATION_JSON).content(jsonRequest.toString());
        ResultActions resultActions = mvc.perform(request);
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void testFindProductsByCategoryNameController() throws Exception {
        String expected = "\"name\":\"Mavi Elbise\",\"explanation\":\"Uzun, M, mavi\"";
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/categories/Elbise/products");
        ResultActions resultActions = mvc.perform(request);
        resultActions.andExpect(content().string(containsString(expected)));
    }

    @Test
    public void testFindProductsByCategoryIdController() throws Exception {
        String expected = "\"name\":\"Mavi Elbise\",\"explanation\":\"Uzun, M, mavi\"";
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/categoriesById/1/products");
        ResultActions resultActions = mvc.perform(request);
        resultActions.andExpect(content().string(containsString(expected)));
    }

    @Test
    public void testListCategoriesController() throws Exception {
        String expected1 = "Ev ";
        String expected2 = "Elbise";
        String expected3 = "aksesuar";
        ResultActions resultActions = mvc.perform(get("/categories"));
        resultActions.andExpect(content().string(containsString(expected1)))
                                                 .andExpect(content().string(containsString(expected2)))
                                                 .andExpect(content().string(containsString(expected3)));
    }

    @Test
    public void testFindCategoryController() throws Exception {
        String expected = "aksesuar";
        mvc.perform(get("/categories/2")).andExpect(content().string(containsString(expected)));
    }
}
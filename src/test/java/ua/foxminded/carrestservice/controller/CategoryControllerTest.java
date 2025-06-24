package ua.foxminded.carrestservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.carrestservice.dto.create.CategoryCreateDto;
import ua.foxminded.carrestservice.dto.response.CategoryResponseDto;
import ua.foxminded.carrestservice.dto.update.CategoryUpdateDto;
import ua.foxminded.carrestservice.exception.EntityNotFoundException;
import ua.foxminded.carrestservice.service.CategoryService;

@WebMvcTest(controllers = {CategoryController.class})
class CategoryControllerTest {

    private static final String MANUFACTURER_LOCATION_URI = "/api/v1/categories/%s";
    private static final String ERROR_MESSAGE = "Error message.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void getPageWithCategories_shouldReturnPageWithContent_whenPageSizeAndNumberNotSpecified() throws Exception {
        Long categoryId = 1L;
        String categoryName = "Category Name";
        CategoryResponseDto categoryResponse = CategoryResponseDto.builder()
            .id(categoryId)
            .name(categoryName)
            .build();
        int pageSize = 1;
        int pageNumber = 0;
        Page<CategoryResponseDto> categoriesPage = new PageImpl<>(List.of(categoryResponse));
        when(categoryService.getAll(any(Pageable.class))).thenReturn(categoriesPage);

        mockMvc.perform(get("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(categoryId))
            .andExpect(jsonPath("$.content.[0].name").value(categoryName))
            .andExpect(jsonPath("$.size").value(pageSize))
            .andExpect(jsonPath("$.number").value(pageNumber));

        verify(categoryService, times(1)).getAll(any(Pageable.class));
    }

    @Test
    void getCategoryById_shouldReturnFoundCategory_whenCategoryWithGivenIdExists() throws Exception {
        Long categoryId = 1L;
        String categoryName = "Category Name";
        CategoryResponseDto manufacturer = CategoryResponseDto.builder()
            .id(categoryId)
            .name(categoryName)
            .build();
        when(categoryService.getById(categoryId)).thenReturn(manufacturer);

        mockMvc.perform(get("/api/v1/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(categoryId))
            .andExpect(jsonPath("$.name").value(categoryName));

        verify(categoryService, times(1)).getById(categoryId);
    }

    @Test
    void getCategoryById_shouldReturnDtoWithErrorDescription_whenCategoryServiceThrowEntityNotFoundException() throws Exception {
        Long categoryId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        when(categoryService.getById(categoryId)).thenThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE));

        mockMvc.perform(get("/api/v1/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(categoryService, times(1)).getById(categoryId);
    }

    @Test
    void createNewCategory_shouldSuccessfullySaveNewCategory_whenRequestBodyIsValid() throws Exception {
        Long categoryId = 1L;
        String categoryName = "New Category Name";
        CategoryCreateDto newCategory = CategoryCreateDto.builder()
            .name(categoryName)
            .build();
        CategoryResponseDto newCategoryAfterSaving = CategoryResponseDto.builder()
            .id(categoryId)
            .name(categoryName)
            .build();
        when(categoryService.save(any(CategoryCreateDto.class))).thenReturn(newCategoryAfterSaving);

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCategory)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", MANUFACTURER_LOCATION_URI.formatted(categoryId)))
            .andExpect(jsonPath("$.id").value(categoryId))
            .andExpect(jsonPath("$.name").value(categoryName));

        verify(categoryService, times(1)).save(any(CategoryCreateDto.class));
    }

    @Test
    void updateCategory_shouldSuccessfullyUpdateCategory_whenRequestBodyIsValid() throws Exception {
        Long categoryId = 1L;
        String categoryName = "Updated Category Name";
        CategoryUpdateDto updateManufacturer = CategoryUpdateDto.builder()
            .id(categoryId)
            .name(categoryName)
            .build();
        CategoryResponseDto categoryAfterUpdating = CategoryResponseDto.builder()
            .id(categoryId)
            .name(categoryName)
            .build();
        when(categoryService.update(any(CategoryUpdateDto.class))).thenReturn(categoryAfterUpdating);

        mockMvc.perform(put("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateManufacturer)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(categoryId))
            .andExpect(jsonPath("$.name").value(categoryName));

        verify(categoryService, times(1)).update(any(CategoryUpdateDto.class));
    }

    @Test
    void updateCategory_shouldReturnDtoWithErrorDescription_whenCategoryServiceThrowEntityNotFoundException() throws Exception {
        Long categoryId = 1L;
        String categoryName = "Updated Category Name";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        CategoryUpdateDto updateCategory = CategoryUpdateDto.builder()
            .id(categoryId)
            .name(categoryName)
            .build();
        when(categoryService.update(any(CategoryUpdateDto.class))).thenThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE));

        mockMvc.perform(put("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCategory)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(categoryService, times(1)).update(any(CategoryUpdateDto.class));
    }

    @Test
    void deleteCategoryById_shouldSuccessfullyDeleteCategory_whenCategoryWithGivenIdExists() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(delete("/api/v1/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteById(categoryId);
    }

    @Test
    void deleteCategoryById_shouldReturnDtoWithErrorDescription_whenCategoryServiceThrowEntityNotFoundException() throws Exception {
        Long categoryId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        doThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE)).when(categoryService).deleteById(categoryId);

        mockMvc.perform(delete("/api/v1/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(categoryService, times(1)).deleteById(categoryId);
    }

}

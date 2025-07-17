package ua.foxminded.carrestservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.CATEGORY_NAME_MANDATORY;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.carrestservice.config.JacksonConfig;
import ua.foxminded.carrestservice.config.SecurityConfig;
import ua.foxminded.carrestservice.dto.create.CategoryCreateDto;
import ua.foxminded.carrestservice.dto.response.CategoryResponseDto;
import ua.foxminded.carrestservice.dto.update.CategoryUpdateDto;
import ua.foxminded.carrestservice.exception.EntityNotFoundException;
import ua.foxminded.carrestservice.repository.CategoryRepository;
import ua.foxminded.carrestservice.service.CategoryService;

@Import({SecurityConfig.class, JacksonConfig.class})
@WebMvcTest(controllers = {CategoryController.class})
class CategoryControllerTest {

    private static final String MANUFACTURER_LOCATION_URI = "/api/v1/categories/%s";
    private static final String ERROR_MESSAGE = "Error message.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryRepository categoryRepositoryMock;

    @MockitoBean
    private CategoryService categoryServiceMock;

    @Test
    void getPageWithCategories_shouldReturnPageWithContent_whenPageSizeAndNumberNotSpecified() throws Exception {
        Long categoryId = 1L;
        String categoryName = "Category Name";
        CategoryResponseDto category = CategoryResponseDto.builder()
            .id(categoryId)
            .name(categoryName)
            .build();
        int pageSize = 1;
        int pageNumber = 0;
        Page<CategoryResponseDto> categoriesPage = new PageImpl<>(List.of(category));
        when(categoryServiceMock.getAll(any(Pageable.class))).thenReturn(categoriesPage);

        mockMvc.perform(get("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(categoryId))
            .andExpect(jsonPath("$.content.[0].name").value(categoryName))
            .andExpect(jsonPath("$.page.size").value(pageSize))
            .andExpect(jsonPath("$.page.number").value(pageNumber));

        verify(categoryServiceMock, times(1)).getAll(any(Pageable.class));
    }

    @Test
    void getCategoryById_shouldReturnFoundCategory_whenCategoryWithGivenIdExists() throws Exception {
        Long categoryId = 1L;
        String categoryName = "Category Name";
        CategoryResponseDto category = CategoryResponseDto.builder()
            .id(categoryId)
            .name(categoryName)
            .build();
        when(categoryServiceMock.getById(categoryId)).thenReturn(category);

        mockMvc.perform(get("/api/v1/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(categoryId))
            .andExpect(jsonPath("$.name").value(categoryName));

        verify(categoryServiceMock, times(1)).getById(categoryId);
    }

    @Test
    void getCategoryById_shouldReturnDtoWithErrorDescription_whenCategoryServiceThrowEntityNotFoundException() throws Exception {
        Long categoryId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        when(categoryServiceMock.getById(categoryId)).thenThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE));

        mockMvc.perform(get("/api/v1/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.errors.[0]").value(ERROR_MESSAGE));

        verify(categoryServiceMock, times(1)).getById(categoryId);
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
        when(categoryRepositoryMock.existsByName(categoryName)).thenReturn(false);
        when(categoryServiceMock.save(any(CategoryCreateDto.class))).thenReturn(newCategoryAfterSaving);

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(newCategory)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", MANUFACTURER_LOCATION_URI.formatted(categoryId)))
            .andExpect(jsonPath("$.id").value(categoryId))
            .andExpect(jsonPath("$.name").value(categoryName));

        verify(categoryServiceMock, times(1)).save(any(CategoryCreateDto.class));
    }

    @Test
    void createNewCategory_shouldReturnDtoWithErrorDescription_whenRequestBodyInvalid() throws Exception {
        String invalidCategoryName = " ";
        CategoryCreateDto newCategory = CategoryCreateDto.builder()
            .name(invalidCategoryName)
            .build();

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(newCategory)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reasonPhrase").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0]").value(CATEGORY_NAME_MANDATORY));

        verify(categoryServiceMock, never()).save(any(CategoryCreateDto.class));
    }

    @Test
    void updateCategory_shouldSuccessfullyUpdateCategory_whenRequestBodyIsValid() throws Exception {
        Long categoryId = 1L;
        String categoryName = "Updated Category Name";
        CategoryUpdateDto updateCategory = CategoryUpdateDto.builder()
            .id(categoryId)
            .name(categoryName)
            .build();
        CategoryResponseDto categoryAfterUpdating = CategoryResponseDto.builder()
            .id(categoryId)
            .name(categoryName)
            .build();
        when(categoryRepositoryMock.existsByName(categoryName)).thenReturn(false);
        when(categoryServiceMock.update(any(CategoryUpdateDto.class))).thenReturn(categoryAfterUpdating);

        mockMvc.perform(put("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCategory))
                .with(jwt()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(categoryId))
            .andExpect(jsonPath("$.name").value(categoryName));

        verify(categoryServiceMock, times(1)).update(any(CategoryUpdateDto.class));
    }

    @Test
    void updateCategory_shouldReturnDtoWithErrorDescription_whenRequestBodyInvalid() throws Exception {
        Long categoryId = 1L;
        String invalidCategoryName = "";
        CategoryUpdateDto updateCategory = CategoryUpdateDto.builder()
            .id(categoryId)
            .name(invalidCategoryName)
            .build();

        mockMvc.perform(put("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCategory))
                .with(jwt()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reasonPhrase").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0]").value(CATEGORY_NAME_MANDATORY));

        verify(categoryServiceMock, never()).update(any(CategoryUpdateDto.class));
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
        when(categoryRepositoryMock.existsByName(categoryName)).thenReturn(false);
        when(categoryServiceMock.update(any(CategoryUpdateDto.class))).thenThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE));

        mockMvc.perform(put("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(updateCategory)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.errors.[0]").value(ERROR_MESSAGE));

        verify(categoryServiceMock, times(1)).update(any(CategoryUpdateDto.class));
    }

    @Test
    void deleteCategoryById_shouldSuccessfullyDeleteCategory_whenCategoryWithGivenIdExists() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(delete("/api/v1/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()))
            .andExpect(status().isNoContent());

        verify(categoryServiceMock, times(1)).deleteById(categoryId);
    }

    @Test
    void deleteCategoryById_shouldReturnDtoWithErrorDescription_whenCategoryServiceThrowEntityNotFoundException() throws Exception {
        Long categoryId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        doThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE)).when(categoryServiceMock).deleteById(categoryId);

        mockMvc.perform(delete("/api/v1/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.errors.[0]").value(ERROR_MESSAGE));

        verify(categoryServiceMock, times(1)).deleteById(categoryId);
    }

}

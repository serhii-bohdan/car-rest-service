package ua.foxminded.carrestservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.MODEL_ID_MANDATORY;
import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.PRODUCTION_YEAR_LATER;
import java.util.List;
import java.util.Set;
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
import ua.foxminded.carrestservice.dto.create.CarCreateDto;
import ua.foxminded.carrestservice.dto.request.CarSearchRequestDto;
import ua.foxminded.carrestservice.dto.response.CarResponseDto;
import ua.foxminded.carrestservice.dto.response.CategoryResponseDto;
import ua.foxminded.carrestservice.dto.response.ManufacturerResponseDto;
import ua.foxminded.carrestservice.dto.response.ModelResponseDto;
import ua.foxminded.carrestservice.dto.update.CarUpdateDto;
import ua.foxminded.carrestservice.entity.Category;
import ua.foxminded.carrestservice.exception.EntityNotFoundException;
import ua.foxminded.carrestservice.repository.CategoryRepository;
import ua.foxminded.carrestservice.repository.ModelRepository;
import ua.foxminded.carrestservice.service.CarService;

@Import({SecurityConfig.class, JacksonConfig.class})
@WebMvcTest(controllers = {CarController.class})
class CarControllerTest {

    private static final String CAR_LOCATION_URI = "/api/v1/cars/%s";
    private static final String ERROR_MESSAGE = "Error message.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ModelRepository modelRepositoryMock;

    @MockitoBean
    private CategoryRepository categoryRepositoryMock;

    @MockitoBean
    private CarService carServiceMock;

    @Test
    void getPageWithCars_shouldReturnPageWithContent_whenAllRequestParametersPresent() throws Exception {
        Long carId = 1L;
        String objectId = "someObjectId";
        Integer productionYear = 2025;
        ModelResponseDto modelMock = mock(ModelResponseDto.class);
        CategoryResponseDto categoryMock = mock(CategoryResponseDto.class);
        CarResponseDto car = CarResponseDto.builder()
            .id(carId)
            .objectId(objectId)
            .productionYear(productionYear)
            .model(modelMock)
            .categories(Set.of(categoryMock))
            .build();
        String manufacturerParam = "Manufacturer";
        String modelParam = "Model";
        String categoryParam = "Category";
        Integer minYearParam = 2020;
        Integer maxYearParam = 2030;
        int pageSize = 1;
        int pageNumber = 0;
        Page<CarResponseDto> carsPage = new PageImpl<>(List.of(car));
        when(carServiceMock.findCarsByCriteria(any(CarSearchRequestDto.class), any(Pageable.class))).thenReturn(carsPage);

        mockMvc.perform(get("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .param("manufacturer", manufacturerParam)
                .param("model", modelParam)
                .param("category", categoryParam)
                .param("minYear", String.valueOf(minYearParam))
                .param("maxYear", String.valueOf(maxYearParam)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(carId))
            .andExpect(jsonPath("$.content.[0].objectId").value(objectId))
            .andExpect(jsonPath("$.content.[0].productionYear").value(productionYear))
            .andExpect(jsonPath("$.page.size").value(pageSize))
            .andExpect(jsonPath("$.page.number").value(pageNumber));

        verify(carServiceMock, times(1)).findCarsByCriteria(any(CarSearchRequestDto.class), any(Pageable.class));
    }

    @Test
    void getPageWithCars_shouldReturnPageWithContent_whenNoAnyRequestParameter() throws Exception {
        Long carId = 1L;
        String objectId = "someObjectId";
        Integer productionYear = 2025;
        ModelResponseDto modelMock = mock(ModelResponseDto.class);
        CategoryResponseDto categoryMock = mock(CategoryResponseDto.class);
        CarResponseDto car = CarResponseDto.builder()
            .id(carId)
            .objectId(objectId)
            .productionYear(productionYear)
            .model(modelMock)
            .categories(Set.of(categoryMock))
            .build();
        int pageSize = 1;
        int pageNumber = 0;
        Page<CarResponseDto> carsPage = new PageImpl<>(List.of(car));
        when(carServiceMock.findCarsByCriteria(any(CarSearchRequestDto.class), any(Pageable.class))).thenReturn(carsPage);

        mockMvc.perform(get("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(carId))
            .andExpect(jsonPath("$.content.[0].objectId").value(objectId))
            .andExpect(jsonPath("$.content.[0].productionYear").value(productionYear))
            .andExpect(jsonPath("$.page.size").value(pageSize))
            .andExpect(jsonPath("$.page.number").value(pageNumber));

        verify(carServiceMock, times(1)).findCarsByCriteria(any(CarSearchRequestDto.class), any(Pageable.class));
    }

    @Test
    void getCarById_shouldReturnFoundCar_whenCarWithGivenIdExists() throws Exception {
        Long carId = 1L;
        String objectId = "someObjectId";
        Integer productionYear = 2025;
        ModelResponseDto modelMock = mock(ModelResponseDto.class);
        CategoryResponseDto categoryMock = mock(CategoryResponseDto.class);
        CarResponseDto car = CarResponseDto.builder()
            .id(carId)
            .objectId(objectId)
            .productionYear(productionYear)
            .model(modelMock)
            .categories(Set.of(categoryMock))
            .build();
        when(carServiceMock.getById(carId)).thenReturn(car);

        mockMvc.perform(get("/api/v1/cars/{id}", carId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(carId))
            .andExpect(jsonPath("$.objectId").value(objectId))
            .andExpect(jsonPath("$.productionYear").value(productionYear));

        verify(carServiceMock, times(1)).getById(carId);
    }

    @Test
    void getCarById_shouldReturnDtoWithErrorDescription_whenCarServiceThrowEntityNotFoundException() throws Exception {
        Long carId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        when(carServiceMock.getById(carId)).thenThrow(new EntityNotFoundException(ERROR_MESSAGE));

        mockMvc.perform(get("/api/v1/cars/{id}", carId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.errors.[0]").value(ERROR_MESSAGE));

        verify(carServiceMock, times(1)).getById(carId);
    }

    @Test
    void createNewCar_shouldSuccessfullySaveNewCar_whenRequestBodyIsValid() throws Exception {
        Long carId = 1L;
        String carObjectId = "someObjectId";
        Integer carProductionYear = 2025;
        Long manufacturerId = 1L;
        String manufacturerName = "Manufacturer Name";
        Long modelId = 1L;
        String modelName = "Model Name";
        Long categoryId = 1L;
        String categoryName = "Category Name";
        CarCreateDto newCar = CarCreateDto.builder()
            .objectId(null)
            .productionYear(carProductionYear)
            .modelId(modelId)
            .categoryIds(Set.of(categoryId))
            .build();
        ManufacturerResponseDto manufacturer = ManufacturerResponseDto.builder()
            .id(manufacturerId)
            .name(manufacturerName)
            .build();
        ModelResponseDto model = ModelResponseDto.builder()
            .id(modelId)
            .name(modelName)
            .manufacturer(manufacturer)
            .build();
        CategoryResponseDto category = CategoryResponseDto.builder()
            .id(carId)
            .name(categoryName)
            .build();
        CarResponseDto newCarAfterSaving = CarResponseDto.builder()
            .id(carId)
            .objectId(carObjectId)
            .productionYear(carProductionYear)
            .model(model)
            .categories(Set.of(category))
            .build();
        when(modelRepositoryMock.existsById(modelId)).thenReturn(true);
        when(categoryRepositoryMock.findAll()).thenReturn(List.of(Category.builder().id(categoryId).build()));
        when(carServiceMock.save(any(CarCreateDto.class))).thenReturn(newCarAfterSaving);

        mockMvc.perform(post("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(newCar)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", CAR_LOCATION_URI.formatted(modelId)))
            .andExpect(jsonPath("$.id").value(carId))
            .andExpect(jsonPath("$.objectId").value(carObjectId))
            .andExpect(jsonPath("$.productionYear").value(carProductionYear))
            .andExpect(jsonPath("$.model.id").value(modelId))
            .andExpect(jsonPath("$.model.name").value(modelName))
            .andExpect(jsonPath("$.model.manufacturer.id").value(manufacturerId))
            .andExpect(jsonPath("$.model.manufacturer.name").value(manufacturerName))
            .andExpect(jsonPath("$.categories.[0].id").value(categoryId))
            .andExpect(jsonPath("$.categories.[0].name").value(categoryName));

        verify(carServiceMock, times(1)).save(any(CarCreateDto.class));
    }

    @Test
    void createNewCar_shouldReturnDtoWithErrorDescription_whenRequestBodyInvalid() throws Exception {
        Integer invalidCarProductionYear = 1000;
        Long modelId = 1L;
        Long categoryId = 1L;
        CarCreateDto newCar = CarCreateDto.builder()
            .objectId(null)
            .productionYear(invalidCarProductionYear)
            .modelId(modelId)
            .categoryIds(Set.of(categoryId))
            .build();
        when(modelRepositoryMock.existsById(modelId)).thenReturn(true);
        when(categoryRepositoryMock.findAll()).thenReturn(List.of(Category.builder().id(categoryId).build()));

        mockMvc.perform(post("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(newCar)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reasonPhrase").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0]").value(PRODUCTION_YEAR_LATER));

        verify(carServiceMock, never()).save(any(CarCreateDto.class));
    }

    @Test
    void updateCar_shouldSuccessfullyUpdateCar_whenRequestBodyIsValid() throws Exception {
        Long carId = 1L;
        String carObjectId = "someObjectId";
        Integer carProductionYear = 2025;
        Long manufacturerId = 1L;
        String manufacturerName = "Manufacturer Name";
        Long modelId = 1L;
        String modelName = "Model Name";
        Long categoryId = 1L;
        String categoryName = "Category Name";
        CarUpdateDto newCar = CarUpdateDto.builder()
            .id(carId)
            .productionYear(carProductionYear)
            .modelId(modelId)
            .categoryIds(Set.of(categoryId))
            .build();
        ManufacturerResponseDto manufacturer = ManufacturerResponseDto.builder()
            .id(manufacturerId)
            .name(manufacturerName)
            .build();
        ModelResponseDto model = ModelResponseDto.builder()
            .id(modelId)
            .name(modelName)
            .manufacturer(manufacturer)
            .build();
        CategoryResponseDto category = CategoryResponseDto.builder()
            .id(carId)
            .name(categoryName)
            .build();
        CarResponseDto carAfterUpdating = CarResponseDto.builder()
            .id(carId)
            .objectId(carObjectId)
            .productionYear(carProductionYear)
            .model(model)
            .categories(Set.of(category))
            .build();
        when(modelRepositoryMock.existsById(modelId)).thenReturn(true);
        when(categoryRepositoryMock.findAll()).thenReturn(List.of(Category.builder().id(categoryId).build()));
        when(carServiceMock.update(any(CarUpdateDto.class))).thenReturn(carAfterUpdating);

        mockMvc.perform(put("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(newCar)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(carId))
            .andExpect(jsonPath("$.objectId").value(carObjectId))
            .andExpect(jsonPath("$.productionYear").value(carProductionYear))
            .andExpect(jsonPath("$.model.id").value(modelId))
            .andExpect(jsonPath("$.model.name").value(modelName))
            .andExpect(jsonPath("$.model.manufacturer.id").value(manufacturerId))
            .andExpect(jsonPath("$.model.manufacturer.name").value(manufacturerName))
            .andExpect(jsonPath("$.categories.[0].id").value(categoryId))
            .andExpect(jsonPath("$.categories.[0].name").value(categoryName));

        verify(carServiceMock, times(1)).update(any(CarUpdateDto.class));
    }

    @Test
    void updateCar_shouldReturnDtoWithErrorDescription_whenRequestBodyInvalid() throws Exception {
        Long carId = 1L;
        Integer carProductionYear = 2025;
        Long invalidModelId = null;
        Long categoryId = 1L;
        CarUpdateDto newCar = CarUpdateDto.builder()
            .id(carId)
            .productionYear(carProductionYear)
            .modelId(invalidModelId)
            .categoryIds(Set.of(categoryId))
            .build();
        when(categoryRepositoryMock.findAll()).thenReturn(List.of(Category.builder().id(categoryId).build()));

        mockMvc.perform(put("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(newCar)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reasonPhrase").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0]").value(MODEL_ID_MANDATORY));

        verify(carServiceMock, never()).update(any(CarUpdateDto.class));
    }

    @Test
    void updateCar_shouldReturnDtoWithErrorDescription_whenCarServiceThrowEntityNotFoundException() throws Exception {
        Long carId = 1L;
        Integer productionYear = 2025;
        Long modelId = 1L;
        Long categoryId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        CarUpdateDto updateCar = CarUpdateDto.builder()
            .id(carId)
            .productionYear(productionYear)
            .modelId(modelId)
            .categoryIds(Set.of(categoryId))
            .build();
        when(modelRepositoryMock.existsById(modelId)).thenReturn(true);
        when(categoryRepositoryMock.findAll()).thenReturn(List.of(Category.builder().id(categoryId).build()));
        when(carServiceMock.update(any(CarUpdateDto.class))).thenThrow(new EntityNotFoundException(ERROR_MESSAGE));

        mockMvc.perform(put("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCar))
                .with(jwt()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.errors.[0]").value(ERROR_MESSAGE));

        verify(carServiceMock, times(1)).update(any(CarUpdateDto.class));
    }

    @Test
    void deleteCarById_shouldSuccessfullyDeleteCar_whenCarWithGivenIdExists() throws Exception {
        Long carId = 1L;

        mockMvc.perform(delete("/api/v1/cars/{id}", carId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()))
            .andExpect(status().isNoContent());

        verify(carServiceMock, times(1)).deleteById(carId);
    }

    @Test
    void deleteCarById_shouldReturnDtoWithErrorDescription_whenCarServiceThrowEntityNotFoundException() throws Exception {
        Long carId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        doThrow(new EntityNotFoundException(ERROR_MESSAGE)).when(carServiceMock).deleteById(carId);

        mockMvc.perform(delete("/api/v1/cars/{id}", carId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.errors.[0]").value(ERROR_MESSAGE));

        verify(carServiceMock, times(1)).deleteById(carId);
    }

}

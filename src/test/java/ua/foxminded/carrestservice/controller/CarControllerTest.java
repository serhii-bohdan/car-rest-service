package ua.foxminded.carrestservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import java.util.Set;
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
import ua.foxminded.carrestservice.dto.create.CarCreateDto;
import ua.foxminded.carrestservice.dto.response.CarResponseDto;
import ua.foxminded.carrestservice.dto.response.CategoryResponseDto;
import ua.foxminded.carrestservice.dto.response.ManufacturerResponseDto;
import ua.foxminded.carrestservice.dto.response.ModelResponseDto;
import ua.foxminded.carrestservice.dto.update.CarUpdateDto;
import ua.foxminded.carrestservice.exception.EntityNotFoundException;
import ua.foxminded.carrestservice.service.CarService;

@WebMvcTest(controllers = {CarController.class})
class CarControllerTest {

    private static final String CAR_LOCATION_URI = "/api/v1/cars/%s";
    private static final String ERROR_MESSAGE = "Error message.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CarService carService;

    @Test
    void getPageWithCars_shouldReturnPageWithContent_whenPageSizeAndNumberNotSpecifiedAndModelIdParamIsNull() throws Exception {
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
        Page<CarResponseDto> modelsPage = new PageImpl<>(List.of(car));
        when(carService.findCarsByModelId(eq(null), any(Pageable.class))).thenReturn(modelsPage);

        mockMvc.perform(get("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(carId))
            .andExpect(jsonPath("$.content.[0].objectId").value(objectId))
            .andExpect(jsonPath("$.content.[0].productionYear").value(productionYear))
            .andExpect(jsonPath("$.size").value(pageSize))
            .andExpect(jsonPath("$.number").value(pageNumber));

        verify(carService, times(1)).findCarsByModelId(eq(null), any(Pageable.class));
    }

    @Test
    void getPageWithCars_shouldReturnPageWithContent_whenPageSizeAndNumberNotSpecifiedAndModelIdParamNotNull() throws Exception {
        Long carId = 1L;
        String objectId = "someObjectId";
        Integer productionYear = 2025;
        Long modelId = 1L;
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
        Page<CarResponseDto> modelsPage = new PageImpl<>(List.of(car));
        when(carService.findCarsByModelId(eq(modelId), any(Pageable.class))).thenReturn(modelsPage);

        mockMvc.perform(get("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .param("modelId", String.valueOf(modelId)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(carId))
            .andExpect(jsonPath("$.content.[0].objectId").value(objectId))
            .andExpect(jsonPath("$.content.[0].productionYear").value(productionYear))
            .andExpect(jsonPath("$.size").value(pageSize))
            .andExpect(jsonPath("$.number").value(pageNumber));

        verify(carService, times(1)).findCarsByModelId(eq(modelId), any(Pageable.class));
    }

    @Test
    void getCarById_shouldReturnFoundCar_whenCarWithGivenIdExists() throws Exception {
        Long carId = 1L;
        String objectId = "someObjectId";
        Integer productionYear = 2025;
        ModelResponseDto modelMock = mock(ModelResponseDto.class);
        CategoryResponseDto categoryMock = mock(CategoryResponseDto.class);
        CarResponseDto carResponse = CarResponseDto.builder()
            .id(carId)
            .objectId(objectId)
            .productionYear(productionYear)
            .model(modelMock)
            .categories(Set.of(categoryMock))
            .build();
        when(carService.getById(carId)).thenReturn(carResponse);

        mockMvc.perform(get("/api/v1/cars/{id}", carId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(carId))
            .andExpect(jsonPath("$.objectId").value(objectId))
            .andExpect(jsonPath("$.productionYear").value(productionYear));

        verify(carService, times(1)).getById(carId);
    }

    @Test
    void getCarById_shouldReturnDtoWithErrorDescription_whenCarServiceThrowEntityNotFoundException() throws Exception {
        Long carId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        when(carService.getById(carId)).thenThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE));

        mockMvc.perform(get("/api/v1/cars/{id}", carId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(carService, times(1)).getById(carId);
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
        when(carService.save(any(CarCreateDto.class))).thenReturn(newCarAfterSaving);

        mockMvc.perform(post("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON)
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

        verify(carService, times(1)).save(any(CarCreateDto.class));
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
        when(carService.update(any(CarUpdateDto.class))).thenReturn(carAfterUpdating);

        mockMvc.perform(put("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON)
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

        verify(carService, times(1)).update(any(CarUpdateDto.class));
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
        when(carService.update(any(CarUpdateDto.class))).thenThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE));

        mockMvc.perform(put("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCar)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(carService, times(1)).update(any(CarUpdateDto.class));
    }

    @Test
    void deleteCarById_shouldSuccessfullyDeleteCar_whenCarWithGivenIdExists() throws Exception {
        Long carId = 1L;

        mockMvc.perform(delete("/api/v1/cars/{id}", carId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(carService, times(1)).deleteById(carId);
    }

    @Test
    void deleteCarById_shouldReturnDtoWithErrorDescription_whenCarServiceThrowEntityNotFoundException() throws Exception {
        Long carId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        doThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE)).when(carService).deleteById(carId);

        mockMvc.perform(delete("/api/v1/cars/{id}", carId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(carService, times(1)).deleteById(carId);
    }

}

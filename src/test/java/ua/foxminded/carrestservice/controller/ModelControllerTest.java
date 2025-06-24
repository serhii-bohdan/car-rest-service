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
import ua.foxminded.carrestservice.dto.create.ModelCreateDto;
import ua.foxminded.carrestservice.dto.response.ManufacturerResponseDto;
import ua.foxminded.carrestservice.dto.response.ModelResponseDto;
import ua.foxminded.carrestservice.dto.update.ModelUpdateDto;
import ua.foxminded.carrestservice.exception.EntityNotFoundException;
import ua.foxminded.carrestservice.service.ModelService;

@WebMvcTest(controllers = {ModelController.class})
class ModelControllerTest {

    private static final String MODEL_LOCATION_URI = "/api/v1/models/%s";
    private static final String ERROR_MESSAGE = "Error message.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ModelService modelService;

    @Test
    void getPageWithModels_shouldReturnPageWithContent_whenPageSizeAndNumberNotSpecifiedAndManufacturerIdParamIsNull() throws Exception {
        Long modelId = 1L;
        String modelName = "Model Name";
        ManufacturerResponseDto manufacturerMock = mock(ManufacturerResponseDto.class);
        ModelResponseDto model = ModelResponseDto.builder()
            .id(modelId)
            .name(modelName)
            .manufacturer(manufacturerMock)
            .build();
        int pageSize = 1;
        int pageNumber = 0;
        Page<ModelResponseDto> modelsPage = new PageImpl<>(List.of(model));
        when(modelService.findModelsByManufacturerId(eq(null), any(Pageable.class))).thenReturn(modelsPage);

        mockMvc.perform(get("/api/v1/models")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(modelId))
            .andExpect(jsonPath("$.content.[0].name").value(modelName))
            .andExpect(jsonPath("$.size").value(pageSize))
            .andExpect(jsonPath("$.number").value(pageNumber));

        verify(modelService, times(1)).findModelsByManufacturerId(eq(null), any(Pageable.class));
    }

    @Test
    void getPageWithModels_shouldReturnPageWithContent_whenPageSizeAndNumberNotSpecifiedAndManufacturerIdParamNotNull() throws Exception {
        Long modelId = 1L;
        String modelName = "Model Name";
        Long manufacturerId = 1L;
        ManufacturerResponseDto manufacturerMock = mock(ManufacturerResponseDto.class);
        ModelResponseDto model = ModelResponseDto.builder()
            .id(modelId)
            .name(modelName)
            .manufacturer(manufacturerMock)
            .build();
        int pageSize = 1;
        int pageNumber = 0;
        Page<ModelResponseDto> modelsPage = new PageImpl<>(List.of(model));
        when(modelService.findModelsByManufacturerId(eq(manufacturerId), any(Pageable.class))).thenReturn(modelsPage);

        mockMvc.perform(get("/api/v1/models")
                .contentType(MediaType.APPLICATION_JSON)
                .param("manufacturerId", String.valueOf(manufacturerId)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(modelId))
            .andExpect(jsonPath("$.content.[0].name").value(modelName))
            .andExpect(jsonPath("$.size").value(pageSize))
            .andExpect(jsonPath("$.number").value(pageNumber));

        verify(modelService, times(1)).findModelsByManufacturerId(eq(manufacturerId), any(Pageable.class));
    }

    @Test
    void getModelById_shouldReturnFoundModel_whenModelWithGivenIdExists() throws Exception {
        Long modelId = 1L;
        String modelName = "Model Name";
        ManufacturerResponseDto manufacturerMock = mock(ManufacturerResponseDto.class);
        ModelResponseDto modelResponse = ModelResponseDto.builder()
            .id(modelId)
            .name(modelName)
            .manufacturer(manufacturerMock)
            .build();
        when(modelService.getById(modelId)).thenReturn(modelResponse);

        mockMvc.perform(get("/api/v1/models/{id}", modelId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(modelId))
            .andExpect(jsonPath("$.name").value(modelName));

        verify(modelService, times(1)).getById(modelId);
    }

    @Test
    void getModelById_shouldReturnDtoWithErrorDescription_whenModelServiceThrowEntityNotFoundException() throws Exception {
        Long modelId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        when(modelService.getById(modelId)).thenThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE));

        mockMvc.perform(get("/api/v1/models/{id}", modelId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(modelService, times(1)).getById(modelId);
    }

    @Test
    void createNewModel_shouldSuccessfullySaveNewModel_whenRequestBodyIsValid() throws Exception {
        Long modelId = 1L;
        String modelName = "New Model Name";
        Long manufacturerId = 1L;
        String manufacturerName = "Manufacturer Name";
        ModelCreateDto newModel = ModelCreateDto.builder()
            .name(modelName)
            .manufacturerId(manufacturerId)
            .build();
        ManufacturerResponseDto manufacturer = ManufacturerResponseDto.builder()
            .id(manufacturerId)
            .name(manufacturerName)
            .build();
        ModelResponseDto newModelAfterSaving = ModelResponseDto.builder()
            .id(modelId)
            .name(modelName)
            .manufacturer(manufacturer)
            .build();
        when(modelService.save(any(ModelCreateDto.class))).thenReturn(newModelAfterSaving);

        mockMvc.perform(post("/api/v1/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newModel)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", MODEL_LOCATION_URI.formatted(modelId)))
            .andExpect(jsonPath("$.id").value(modelId))
            .andExpect(jsonPath("$.name").value(modelName))
            .andExpect(jsonPath("$.manufacturer.id").value(manufacturerId))
            .andExpect(jsonPath("$.manufacturer.name").value(manufacturerName));

        verify(modelService, times(1)).save(any(ModelCreateDto.class));
    }

    @Test
    void updateManufacturer_shouldSuccessfullyUpdateManufacturer_whenRequestBodyIsValid() throws Exception {
        Long modelId = 1L;
        String modelName = "Model Name";
        Long manufacturerId = 1L;
        String manufacturerName = "Manufacturer Name";
        ModelUpdateDto updateManufacturer = ModelUpdateDto.builder()
            .id(modelId)
            .name(modelName)
            .manufacturerId(manufacturerId)
            .build();
        ManufacturerResponseDto manufacturer = ManufacturerResponseDto.builder()
            .id(manufacturerId)
            .name(manufacturerName)
            .build();
        ModelResponseDto modelAfterUpdating = ModelResponseDto.builder()
            .id(modelId)
            .name(modelName)
            .manufacturer(manufacturer)
            .build();
        when(modelService.update(any(ModelUpdateDto.class))).thenReturn(modelAfterUpdating);

        mockMvc.perform(put("/api/v1/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateManufacturer)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(modelId))
            .andExpect(jsonPath("$.name").value(modelName));

        verify(modelService, times(1)).update(any(ModelUpdateDto.class));
    }

    @Test
    void updateManufacturer_shouldReturnDtoWithErrorDescription_whenModelServiceThrowEntityNotFoundException() throws Exception {
        Long modelId = 1L;
        String manufacturerName = "Updated Model Name";
        Long manufacturerId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ModelUpdateDto updateModel = ModelUpdateDto.builder()
            .id(modelId)
            .name(manufacturerName)
            .manufacturerId(manufacturerId)
            .build();
        when(modelService.update(any(ModelUpdateDto.class))).thenThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE));

        mockMvc.perform(put("/api/v1/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateModel)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(modelService, times(1)).update(any(ModelUpdateDto.class));
    }

    @Test
    void deleteModelById_shouldSuccessfullyDeleteModel_whenModelWithGivenIdExists() throws Exception {
        Long modelId = 1L;

        mockMvc.perform(delete("/api/v1/models/{id}", modelId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(modelService, times(1)).deleteById(modelId);
    }

    @Test
    void deleteModelById_shouldReturnDtoWithErrorDescription_whenModelServiceThrowEntityNotFoundException() throws Exception {
        Long modelId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        doThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE)).when(modelService).deleteById(modelId);

        mockMvc.perform(delete("/api/v1/models/{id}", modelId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(modelService, times(1)).deleteById(modelId);
    }

}

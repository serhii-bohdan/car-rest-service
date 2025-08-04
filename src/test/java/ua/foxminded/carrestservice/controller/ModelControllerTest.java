package ua.foxminded.carrestservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.MANUFACTURER_ID_MANDATORY;
import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.MODEL_NAME_MANDATORY;
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
import ua.foxminded.carrestservice.dto.create.ModelCreateDto;
import ua.foxminded.carrestservice.dto.response.ManufacturerResponseDto;
import ua.foxminded.carrestservice.dto.response.ModelResponseDto;
import ua.foxminded.carrestservice.dto.update.ModelUpdateDto;
import ua.foxminded.carrestservice.exception.EntityNotFoundException;
import ua.foxminded.carrestservice.repository.ManufacturerRepository;
import ua.foxminded.carrestservice.repository.ModelRepository;
import ua.foxminded.carrestservice.service.ModelService;

@Import({SecurityConfig.class, JacksonConfig.class})
@WebMvcTest(controllers = {ModelController.class})
class ModelControllerTest {

    private static final String MODEL_LOCATION_URI = "/api/v1/models/%s";
    private static final String ERROR_MESSAGE = "Error message.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ManufacturerRepository manufacturerRepositoryMock;

    @MockitoBean
    private ModelRepository modelRepositoryMock;

    @MockitoBean
    private ModelService modelServiceMock;

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
        when(modelServiceMock.findModelsByManufacturerId(eq(null), any(Pageable.class))).thenReturn(modelsPage);

        mockMvc.perform(get("/api/v1/models")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(modelId))
            .andExpect(jsonPath("$.content.[0].name").value(modelName))
            .andExpect(jsonPath("$.page.size").value(pageSize))
            .andExpect(jsonPath("$.page.number").value(pageNumber));

        verify(modelServiceMock, times(1)).findModelsByManufacturerId(eq(null), any(Pageable.class));
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
        when(modelServiceMock.findModelsByManufacturerId(eq(manufacturerId), any(Pageable.class))).thenReturn(modelsPage);

        mockMvc.perform(get("/api/v1/models")
                .contentType(MediaType.APPLICATION_JSON)
                .param("manufacturerId", String.valueOf(manufacturerId)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(modelId))
            .andExpect(jsonPath("$.content.[0].name").value(modelName))
            .andExpect(jsonPath("$.page.size").value(pageSize))
            .andExpect(jsonPath("$.page.number").value(pageNumber));

        verify(modelServiceMock, times(1)).findModelsByManufacturerId(eq(manufacturerId), any(Pageable.class));
    }

    @Test
    void getModelById_shouldReturnFoundModel_whenModelWithGivenIdExists() throws Exception {
        Long modelId = 1L;
        String modelName = "Model Name";
        ManufacturerResponseDto manufacturerMock = mock(ManufacturerResponseDto.class);
        ModelResponseDto model = ModelResponseDto.builder()
            .id(modelId)
            .name(modelName)
            .manufacturer(manufacturerMock)
            .build();
        when(modelServiceMock.getById(modelId)).thenReturn(model);

        mockMvc.perform(get("/api/v1/models/{id}", modelId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(modelId))
            .andExpect(jsonPath("$.name").value(modelName));

        verify(modelServiceMock, times(1)).getById(modelId);
    }

    @Test
    void getModelById_shouldReturnDtoWithErrorDescription_whenModelServiceThrowEntityNotFoundException() throws Exception {
        Long modelId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        when(modelServiceMock.getById(modelId)).thenThrow(new EntityNotFoundException(ERROR_MESSAGE));

        mockMvc.perform(get("/api/v1/models/{id}", modelId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.errors.[0]").value(ERROR_MESSAGE));

        verify(modelServiceMock, times(1)).getById(modelId);
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
        when(manufacturerRepositoryMock.existsById(manufacturerId)).thenReturn(true);
        when(modelRepositoryMock.existsByManufacturerIdAndName(manufacturerId, modelName)).thenReturn(false);
        when(modelServiceMock.save(any(ModelCreateDto.class))).thenReturn(newModelAfterSaving);

        mockMvc.perform(post("/api/v1/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newModel))
                .with(jwt()))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", MODEL_LOCATION_URI.formatted(modelId)))
            .andExpect(jsonPath("$.id").value(modelId))
            .andExpect(jsonPath("$.name").value(modelName))
            .andExpect(jsonPath("$.manufacturer.id").value(manufacturerId))
            .andExpect(jsonPath("$.manufacturer.name").value(manufacturerName));

        verify(modelServiceMock, times(1)).save(any(ModelCreateDto.class));
    }

    @Test
    void createNewModel_shouldReturnDtoWithErrorDescription_whenRequestBodyInvalid() throws Exception {
        String modelName = "New Model Name";
        Long invalidModelId = null;
        ModelCreateDto newModel = ModelCreateDto.builder()
            .name(modelName)
            .manufacturerId(invalidModelId)
            .build();

        mockMvc.perform(post("/api/v1/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newModel))
                .with(jwt()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reasonPhrase").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0]").value(MANUFACTURER_ID_MANDATORY));

        verify(modelServiceMock, never()).save(any(ModelCreateDto.class));
    }

    @Test
    void updateModel_shouldSuccessfullyUpdateModel_whenRequestBodyIsValid() throws Exception {
        Long modelId = 1L;
        String modelName = "Model Name";
        Long manufacturerId = 1L;
        String manufacturerName = "Manufacturer Name";
        ModelUpdateDto updateModel = ModelUpdateDto.builder()
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
        when(manufacturerRepositoryMock.existsById(manufacturerId)).thenReturn(true);
        when(modelRepositoryMock.existsByManufacturerIdAndNameAndIdIsNot(manufacturerId, modelName, modelId)).thenReturn(false);
        when(modelServiceMock.update(any(ModelUpdateDto.class))).thenReturn(modelAfterUpdating);

        mockMvc.perform(put("/api/v1/models")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(updateModel)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(modelId))
            .andExpect(jsonPath("$.name").value(modelName));

        verify(modelServiceMock, times(1)).update(any(ModelUpdateDto.class));
    }

    @Test
    void updateModel_shouldReturnDtoWithErrorDescription_whenRequestBodyInvalid() throws Exception {
        Long modelId = 1L;
        String invalidModelName = "   ";
        Long manufacturerId = 1L;
        ModelUpdateDto updateModel = ModelUpdateDto.builder()
            .id(modelId)
            .name(invalidModelName)
            .manufacturerId(manufacturerId)
            .build();
        when(manufacturerRepositoryMock.existsById(manufacturerId)).thenReturn(true);

        mockMvc.perform(put("/api/v1/models")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(updateModel)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reasonPhrase").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0]").value(MODEL_NAME_MANDATORY));

        verify(modelServiceMock, never()).update(any(ModelUpdateDto.class));
    }

    @Test
    void updateManufacturer_shouldReturnDtoWithErrorDescription_whenModelServiceThrowEntityNotFoundException() throws Exception {
        long modelId = 1L;
        String modelName = "Model Name";
        String manufacturerName = "Updated Model Name";
        Long manufacturerId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ModelUpdateDto updateModel = ModelUpdateDto.builder()
            .id(modelId)
            .name(manufacturerName)
            .manufacturerId(manufacturerId)
            .build();
        when(manufacturerRepositoryMock.existsById(manufacturerId)).thenReturn(true);
        when(modelRepositoryMock.existsByManufacturerIdAndNameAndIdIsNot(manufacturerId, modelName, modelId)).thenReturn(false);
        when(modelServiceMock.update(any(ModelUpdateDto.class))).thenThrow(new EntityNotFoundException(ERROR_MESSAGE));

        mockMvc.perform(put("/api/v1/models")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(updateModel)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.errors.[0]").value(ERROR_MESSAGE));

        verify(modelServiceMock, times(1)).update(any(ModelUpdateDto.class));
    }

    @Test
    void deleteModelById_shouldSuccessfullyDeleteModel_whenModelWithGivenIdExists() throws Exception {
        Long modelId = 1L;

        mockMvc.perform(delete("/api/v1/models/{id}", modelId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()))
            .andExpect(status().isNoContent());

        verify(modelServiceMock, times(1)).deleteById(modelId);
    }

    @Test
    void deleteModelById_shouldReturnDtoWithErrorDescription_whenModelServiceThrowEntityNotFoundException() throws Exception {
        Long modelId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        doThrow(new EntityNotFoundException(ERROR_MESSAGE)).when(modelServiceMock).deleteById(modelId);

        mockMvc.perform(delete("/api/v1/models/{id}", modelId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.errors.[0]").value(ERROR_MESSAGE));

        verify(modelServiceMock, times(1)).deleteById(modelId);
    }

}

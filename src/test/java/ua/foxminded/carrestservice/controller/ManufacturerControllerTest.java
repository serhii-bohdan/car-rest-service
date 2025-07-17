package ua.foxminded.carrestservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.carrestservice.util.validation.ValidationErrorMessages.MANUFACTURER_NAME_MANDATORY;
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
import ua.foxminded.carrestservice.dto.create.ManufacturerCreateDto;
import ua.foxminded.carrestservice.dto.response.ManufacturerResponseDto;
import ua.foxminded.carrestservice.dto.update.ManufacturerUpdateDto;
import ua.foxminded.carrestservice.exception.EntityNotFoundException;
import ua.foxminded.carrestservice.repository.ManufacturerRepository;
import ua.foxminded.carrestservice.service.ManufacturerService;

@Import({SecurityConfig.class, JacksonConfig.class})
@WebMvcTest(controllers = {ManufacturerController.class})
class ManufacturerControllerTest {

    private static final String MANUFACTURER_LOCATION_URI = "/api/v1/manufacturers/%s";
    private static final String ERROR_MESSAGE = "Error message.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ManufacturerRepository manufacturerRepositoryMock;

    @MockitoBean
    private ManufacturerService manufacturerServiceMock;

    @Test
    void getPageWithManufacturers_shouldReturnPageWithContent_whenPageSizeAndNumberNotSpecified() throws Exception {
        Long manufacturerId = 1L;
        String manufacturerName = "Manufacturer Name";
        ManufacturerResponseDto manufacturer = ManufacturerResponseDto.builder()
            .id(manufacturerId)
            .name(manufacturerName)
            .build();
        int pageSize = 1;
        int pageNumber = 0;
        Page<ManufacturerResponseDto> manufacturersPage = new PageImpl<>(List.of(manufacturer));
        when(manufacturerServiceMock.getAll(any(Pageable.class))).thenReturn(manufacturersPage);

        mockMvc.perform(get("/api/v1/manufacturers")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(manufacturerId))
            .andExpect(jsonPath("$.content.[0].name").value(manufacturerName))
            .andExpect(jsonPath("$.page.size").value(pageSize))
            .andExpect(jsonPath("$.page.number").value(pageNumber));

        verify(manufacturerServiceMock, times(1)).getAll(any(Pageable.class));
    }

    @Test
    void getManufacturerById_shouldReturnFoundManufacturer_whenManufacturerWithGivenIdExists() throws Exception {
        Long manufacturerId = 1L;
        String manufacturerName = "Manufacturer Name";
        ManufacturerResponseDto manufacturer = ManufacturerResponseDto.builder()
            .id(manufacturerId)
            .name(manufacturerName)
            .build();
        when(manufacturerServiceMock.getById(manufacturerId)).thenReturn(manufacturer);

        mockMvc.perform(get("/api/v1/manufacturers/{id}", manufacturerId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(manufacturerId))
            .andExpect(jsonPath("$.name").value(manufacturerName));

        verify(manufacturerServiceMock, times(1)).getById(manufacturerId);
    }

    @Test
    void getManufacturerById_shouldReturnDtoWithErrorDescription_whenManufacturerServiceThrowEntityNotFoundException() throws Exception {
        Long manufacturerId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        when(manufacturerServiceMock.getById(manufacturerId)).thenThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE));

        mockMvc.perform(get("/api/v1/manufacturers/{id}", manufacturerId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.errors.[0]").value(ERROR_MESSAGE));

        verify(manufacturerServiceMock, times(1)).getById(manufacturerId);
    }

    @Test
    void createNewManufacturer_shouldSuccessfullySaveNewManufacturer_whenRequestBodyIsValid() throws Exception {
        Long manufacturerId = 1L;
        String manufacturerName = "New Manufacturer Name";
        ManufacturerCreateDto newManufacturer = ManufacturerCreateDto.builder()
            .name(manufacturerName)
            .build();
        ManufacturerResponseDto newManufacturerAfterSaving = ManufacturerResponseDto.builder()
            .id(manufacturerId)
            .name(manufacturerName)
            .build();
        when(manufacturerRepositoryMock.existsByName(manufacturerName)).thenReturn(false);
        when(manufacturerServiceMock.save(any(ManufacturerCreateDto.class))).thenReturn(newManufacturerAfterSaving);

        mockMvc.perform(post("/api/v1/manufacturers")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(newManufacturer)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", MANUFACTURER_LOCATION_URI.formatted(manufacturerId)))
            .andExpect(jsonPath("$.id").value(manufacturerId))
            .andExpect(jsonPath("$.name").value(manufacturerName));

        verify(manufacturerServiceMock, times(1)).save(any(ManufacturerCreateDto.class));
    }

    @Test
    void createNewManufacturer_shouldReturnDtoWithErrorDescription_whenRequestBodyInvalid() throws Exception {
        String invalidManufacturerName = "     ";
        ManufacturerCreateDto newManufacturer = ManufacturerCreateDto.builder()
            .name(invalidManufacturerName)
            .build();

        mockMvc.perform(post("/api/v1/manufacturers")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(newManufacturer)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reasonPhrase").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0]").value(MANUFACTURER_NAME_MANDATORY));

        verify(manufacturerServiceMock, never()).save(any(ManufacturerCreateDto.class));
    }

    @Test
    void updateManufacturer_shouldSuccessfullyUpdateManufacturer_whenRequestBodyIsValid() throws Exception {
        Long manufacturerId = 1L;
        String manufacturerName = "Updated Manufacturer Name";
        ManufacturerUpdateDto updateManufacturer = ManufacturerUpdateDto.builder()
            .id(manufacturerId)
            .name(manufacturerName)
            .build();
        ManufacturerResponseDto manufacturerAfterUpdating = ManufacturerResponseDto.builder()
            .id(manufacturerId)
            .name(manufacturerName)
            .build();
        when(manufacturerRepositoryMock.existsByName(manufacturerName)).thenReturn(false);
        when(manufacturerServiceMock.update(any(ManufacturerUpdateDto.class))).thenReturn(manufacturerAfterUpdating);

        mockMvc.perform(put("/api/v1/manufacturers")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(updateManufacturer)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(manufacturerId))
            .andExpect(jsonPath("$.name").value(manufacturerName));

        verify(manufacturerServiceMock, times(1)).update(any(ManufacturerUpdateDto.class));
    }

    @Test
    void updateManufacturer_shouldReturnDtoWithErrorDescription_whenRequestBodyInvalid() throws Exception {
        Long manufacturerId = 1L;
        String invalidManufacturerName = "";
        ManufacturerUpdateDto updateManufacturer = ManufacturerUpdateDto.builder()
            .id(manufacturerId)
            .name(invalidManufacturerName)
            .build();

        mockMvc.perform(put("/api/v1/manufacturers")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(updateManufacturer)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reasonPhrase").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors[0]").value(MANUFACTURER_NAME_MANDATORY));

        verify(manufacturerServiceMock, never()).update(any(ManufacturerUpdateDto.class));
    }

    @Test
    void updateManufacturer_shouldReturnDtoWithErrorDescription_whenManufacturerServiceThrowEntityNotFoundException() throws Exception {
        Long manufacturerId = 1L;
        String manufacturerName = "Updated Manufacturer Name";
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ManufacturerUpdateDto updateManufacturer = ManufacturerUpdateDto.builder()
            .id(manufacturerId)
            .name(manufacturerName)
            .build();
        when(manufacturerRepositoryMock.existsByName(manufacturerName)).thenReturn(false);
        when(manufacturerServiceMock.update(any(ManufacturerUpdateDto.class))).thenThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE));

        mockMvc.perform(put("/api/v1/manufacturers")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt())
                .content(objectMapper.writeValueAsString(updateManufacturer)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.errors.[0]").value(ERROR_MESSAGE));

        verify(manufacturerServiceMock, times(1)).update(any(ManufacturerUpdateDto.class));
    }

    @Test
    void deleteManufacturerById_shouldSuccessfullyDeleteManufacturer_whenManufacturerWithGivenIdExists() throws Exception {
        Long manufacturerId = 1L;

        mockMvc.perform(delete("/api/v1/manufacturers/{id}", manufacturerId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()))
            .andExpect(status().isNoContent());

        verify(manufacturerServiceMock, times(1)).deleteById(manufacturerId);
    }

    @Test
    void deleteManufacturerById_shouldReturnDtoWithErrorDescription_whenManufacturerServiceThrowEntityNotFoundException() throws Exception {
        Long manufacturerId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        doThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE)).when(manufacturerServiceMock).deleteById(manufacturerId);

        mockMvc.perform(delete("/api/v1/manufacturers/{id}", manufacturerId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.errors").value(ERROR_MESSAGE));

        verify(manufacturerServiceMock, times(1)).deleteById(manufacturerId);
    }

}

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
import ua.foxminded.carrestservice.dto.create.ManufacturerCreateDto;
import ua.foxminded.carrestservice.dto.response.ManufacturerResponseDto;
import ua.foxminded.carrestservice.dto.update.ManufacturerUpdateDto;
import ua.foxminded.carrestservice.exception.EntityNotFoundException;
import ua.foxminded.carrestservice.service.ManufacturerService;

@WebMvcTest(controllers = {ManufacturerController.class})
class ManufacturerControllerTest {

    private static final String MANUFACTURER_LOCATION_URI = "/api/v1/manufacturers/%s";
    private static final String ERROR_MESSAGE = "Error message.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ManufacturerService manufacturerService;

    @Test
    void getPageWithManufacturers_shouldReturnPageWithContent_whenPageSizeAndNumberNotSpecified() throws Exception {
        Long manufacturerId = 1L;
        String manufacturerName = "Manufacturer Name";
        ManufacturerResponseDto manufacturerResponse = ManufacturerResponseDto.builder()
            .id(manufacturerId)
            .name(manufacturerName)
            .build();
        int pageSize = 1;
        int pageNumber = 0;
        Page<ManufacturerResponseDto> manufacturersPage = new PageImpl<>(List.of(manufacturerResponse));
        when(manufacturerService.getAll(any(Pageable.class))).thenReturn(manufacturersPage);

        mockMvc.perform(get("/api/v1/manufacturers")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.[0].id").value(manufacturerId))
            .andExpect(jsonPath("$.content.[0].name").value(manufacturerName))
            .andExpect(jsonPath("$.size").value(pageSize))
            .andExpect(jsonPath("$.number").value(pageNumber));

        verify(manufacturerService, times(1)).getAll(any(Pageable.class));
    }

    @Test
    void getManufacturerById_shouldReturnFoundManufacturer_whenManufacturerWithGivenIdExists() throws Exception {
        Long manufacturerId = 1L;
        String manufacturerName = "Manufacturer Name";
        ManufacturerResponseDto manufacturer = ManufacturerResponseDto.builder()
            .id(manufacturerId)
            .name(manufacturerName)
            .build();
        when(manufacturerService.getById(manufacturerId)).thenReturn(manufacturer);

        mockMvc.perform(get("/api/v1/manufacturers/{id}", manufacturerId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(manufacturerId))
            .andExpect(jsonPath("$.name").value(manufacturerName));

        verify(manufacturerService, times(1)).getById(manufacturerId);
    }

    @Test
    void getManufacturerById_shouldReturnDtoWithErrorDescription_whenManufacturerServiceThrowEntityNotFoundException() throws Exception {
        Long manufacturerId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        when(manufacturerService.getById(manufacturerId)).thenThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE));

        mockMvc.perform(get("/api/v1/manufacturers/{id}", manufacturerId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(manufacturerService, times(1)).getById(manufacturerId);
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
        when(manufacturerService.save(any(ManufacturerCreateDto.class))).thenReturn(newManufacturerAfterSaving);

        mockMvc.perform(post("/api/v1/manufacturers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newManufacturer)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", MANUFACTURER_LOCATION_URI.formatted(manufacturerId)))
            .andExpect(jsonPath("$.id").value(manufacturerId))
            .andExpect(jsonPath("$.name").value(manufacturerName));

        verify(manufacturerService, times(1)).save(any(ManufacturerCreateDto.class));
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
        when(manufacturerService.update(any(ManufacturerUpdateDto.class))).thenReturn(manufacturerAfterUpdating);

        mockMvc.perform(put("/api/v1/manufacturers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateManufacturer)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(manufacturerId))
            .andExpect(jsonPath("$.name").value(manufacturerName));

        verify(manufacturerService, times(1)).update(any(ManufacturerUpdateDto.class));
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
        when(manufacturerService.update(any(ManufacturerUpdateDto.class))).thenThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE));

        mockMvc.perform(put("/api/v1/manufacturers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateManufacturer)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(manufacturerService, times(1)).update(any(ManufacturerUpdateDto.class));
    }

    @Test
    void deleteManufacturerById_shouldSuccessfullyDeleteManufacturer_whenManufacturerWithGivenIdExists() throws Exception {
        Long manufacturerId = 1L;

        mockMvc.perform(delete("/api/v1/manufacturers/{id}", manufacturerId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(manufacturerService, times(1)).deleteById(manufacturerId);
    }

    @Test
    void deleteManufacturerById_shouldReturnDtoWithErrorDescription_whenManufacturerServiceThrowEntityNotFoundException() throws Exception {
        Long manufacturerId = 1L;
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        doThrow(new EntityNotFoundException(httpStatus, ERROR_MESSAGE)).when(manufacturerService).deleteById(manufacturerId);

        mockMvc.perform(delete("/api/v1/manufacturers/{id}", manufacturerId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reasonPhrase").value(httpStatus.getReasonPhrase()))
            .andExpect(jsonPath("$.statusCode").value(httpStatus.value()))
            .andExpect(jsonPath("$.message").value(ERROR_MESSAGE));

        verify(manufacturerService, times(1)).deleteById(manufacturerId);
    }

}

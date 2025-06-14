package com.ndungutse.project_tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndungutse.project_tracker.dto.DeveloperDTO;
import com.ndungutse.project_tracker.service.DeveloperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class DeveloperControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DeveloperService developerService;

    @InjectMocks
    private DeveloperController developerController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private DeveloperDTO developerDTO;
    private List<DeveloperDTO> developerDTOList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(developerController).build();
        developerDTO = new DeveloperDTO(1L, "John Doe", "john@example.com", "Java, Spring Boot");
        DeveloperDTO developer2 = new DeveloperDTO(2L, "Jane Smith", "jane@example.com", "JavaScript, React");
        developerDTOList = Arrays.asList(developerDTO, developer2);
    }

    @Test
    void createDeveloper_ShouldReturnCreatedDeveloper() throws Exception {
        when(developerService.create(any(DeveloperDTO.class))).thenReturn(developerDTO);

        mockMvc.perform(post("/api/v1/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(developerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")))
                .andExpect(jsonPath("$.skills", is("Java, Spring Boot")));

        verify(developerService, times(1)).create(any(DeveloperDTO.class));
    }

    @Test
    void getAllDevelopers_ShouldReturnAllDevelopers() throws Exception {
        when(developerService.getAll()).thenReturn(developerDTOList);

        mockMvc.perform(get("/api/v1/developers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")));

        verify(developerService, times(1)).getAll();
    }

    @Test
    void getDeveloperById_WhenDeveloperExists_ShouldReturnDeveloper() throws Exception {
        when(developerService.getById(1L)).thenReturn(Optional.of(developerDTO));

        mockMvc.perform(get("/api/v1/developers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));

        verify(developerService, times(1)).getById(1L);
    }

    @Test
    void getDeveloperById_WhenDeveloperDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(developerService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/developers/99"))
                .andExpect(status().isNotFound());

        verify(developerService, times(1)).getById(99L);
    }

    @Test
    void updateDeveloper_WhenDeveloperExists_ShouldReturnUpdatedDeveloper() throws Exception {
        DeveloperDTO updatedDTO = new DeveloperDTO(1L, "John Updated", "john.updated@example.com", "Java, Spring Boot, Kotlin");

        when(developerService.exists(1L)).thenReturn(true);
        when(developerService.update(eq(1L), any(DeveloperDTO.class))).thenReturn(Optional.of(updatedDTO));

        mockMvc.perform(patch("/api/v1/developers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Updated")))
                .andExpect(jsonPath("$.email", is("john.updated@example.com")))
                .andExpect(jsonPath("$.skills", is("Java, Spring Boot, Kotlin")));

        verify(developerService, times(1)).exists(1L);
        verify(developerService, times(1)).update(eq(1L), any(DeveloperDTO.class));
    }

    @Test
    void updateDeveloper_WhenDeveloperDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(developerService.exists(99L)).thenReturn(false);

        mockMvc.perform(patch("/api/v1/developers/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(developerDTO)))
                .andExpect(status().isNotFound());

        verify(developerService, times(1)).exists(99L);
        verify(developerService, never()).update(eq(99L), any(DeveloperDTO.class));
    }

    @Test
    void deleteDeveloper_WhenDeveloperExists_ShouldReturnNoContent() throws Exception {
        when(developerService.exists(1L)).thenReturn(true);
        doNothing().when(developerService).delete(1L);

        mockMvc.perform(delete("/api/v1/developers/1"))
                .andExpect(status().isNoContent());

        verify(developerService, times(1)).exists(1L);
        verify(developerService, times(1)).delete(1L);
    }

    @Test
    void deleteDeveloper_WhenDeveloperDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(developerService.exists(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/developers/99"))
                .andExpect(status().isNotFound());

        verify(developerService, times(1)).exists(99L);
        verify(developerService, never()).delete(99L);
    }

    @Test
    void assignTask_WhenDeveloperAndTaskExist_ShouldReturnUpdatedDeveloper() throws Exception {
        DeveloperDTO developerWithTask = new DeveloperDTO(1L, "John Doe", "john@example.com", "Java, Spring Boot");
        developerWithTask.setTaskIds(Arrays.asList(10L));

        when(developerService.assignTask(1L, 10L)).thenReturn(Optional.of(developerWithTask));

        mockMvc.perform(post("/api/v1/developers/1/tasks/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.taskIds", hasSize(1)))
                .andExpect(jsonPath("$.taskIds[0]", is(10)));

        verify(developerService, times(1)).assignTask(1L, 10L);
    }

    @Test
    void assignTask_WhenDeveloperOrTaskDoNotExist_ShouldReturnNotFound() throws Exception {
        when(developerService.assignTask(99L, 10L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/developers/99/tasks/10"))
                .andExpect(status().isNotFound());

        verify(developerService, times(1)).assignTask(99L, 10L);
    }

    @Test
    void removeTask_WhenDeveloperAndTaskExist_ShouldReturnUpdatedDeveloper() throws Exception {
        DeveloperDTO developerWithoutTask = new DeveloperDTO(1L, "John Doe", "john@example.com", "Java, Spring Boot");
        developerWithoutTask.setTaskIds(Arrays.asList());

        when(developerService.removeTask(1L, 10L)).thenReturn(Optional.of(developerWithoutTask));

        mockMvc.perform(delete("/api/v1/developers/1/tasks/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.taskIds", hasSize(0)));

        verify(developerService, times(1)).removeTask(1L, 10L);
    }

    @Test
    void removeTask_WhenDeveloperOrTaskDoNotExist_ShouldReturnNotFound() throws Exception {
        when(developerService.removeTask(99L, 10L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/developers/99/tasks/10"))
                .andExpect(status().isNotFound());

        verify(developerService, times(1)).removeTask(99L, 10L);
    }
}

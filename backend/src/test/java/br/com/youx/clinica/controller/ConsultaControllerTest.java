package br.com.youx.clinica.controller;

import br.com.youx.clinica.dto.consulta.ConsultaRequestDTO;
import br.com.youx.clinica.dto.consulta.ConsultaResponseDTO;
import br.com.youx.clinica.dto.paciente.PacienteResponseDTO;
import br.com.youx.clinica.dto.usuario.UsuarioResponseDTO;
import br.com.youx.clinica.service.ConsultaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes unitários para ConsultaController
 */
@ExtendWith(MockitoExtension.class)
class ConsultaControllerTest {

    @Mock
    private ConsultaService consultaService;

    @InjectMocks
    private ConsultaController consultaController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(consultaController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testBuscarPaginadoComFiltros_SemFiltros_DeveRetornarPaginaCompleta() throws Exception {
        // Arrange
        PacienteResponseDTO paciente1 = new PacienteResponseDTO();
        paciente1.setId(1L);
        paciente1.setNome("João Silva");
        
        UsuarioResponseDTO medico1 = new UsuarioResponseDTO();
        medico1.setId(1L);
        medico1.setNome("Dr. Carlos");
        
        ConsultaResponseDTO consulta1 = new ConsultaResponseDTO();
        consulta1.setId(1L);
        consulta1.setPaciente(paciente1);
        consulta1.setMedico(medico1);
        consulta1.setData(LocalDateTime.now());

        PacienteResponseDTO paciente2 = new PacienteResponseDTO();
        paciente2.setId(2L);
        paciente2.setNome("Maria Santos");
        
        UsuarioResponseDTO medico2 = new UsuarioResponseDTO();
        medico2.setId(2L);
        medico2.setNome("Dr. Ana");
        
        ConsultaResponseDTO consulta2 = new ConsultaResponseDTO();
        consulta2.setId(2L);
        consulta2.setPaciente(paciente2);
        consulta2.setMedico(medico2);
        consulta2.setData(LocalDateTime.now().plusDays(1));

        List<ConsultaResponseDTO> consultas = Arrays.asList(consulta1, consulta2);
        Page<ConsultaResponseDTO> page = new PageImpl<>(consultas);

        when(consultaService.buscarPaginadoComFiltros(
                isNull(), isNull(), isNull(), isNull(), isNull(), isNull(),
                eq(0), eq(10), eq("data"), eq("desc")
        )).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/consultas/paginated")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].paciente.nome").value("João Silva"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].paciente.nome").value("Maria Santos"));
    }

    @Test
    void testBuscarPaginadoComFiltros_ComFiltroNomePaciente_DeveRetornarFiltrado() throws Exception {
        // Arrange
        PacienteResponseDTO paciente = new PacienteResponseDTO();
        paciente.setId(1L);
        paciente.setNome("João Silva");
        
        UsuarioResponseDTO medico = new UsuarioResponseDTO();
        medico.setId(1L);
        medico.setNome("Dr. Carlos");
        
        ConsultaResponseDTO consulta = new ConsultaResponseDTO();
        consulta.setId(1L);
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setData(LocalDateTime.now());

        List<ConsultaResponseDTO> consultas = Arrays.asList(consulta);
        Page<ConsultaResponseDTO> page = new PageImpl<>(consultas);

        when(consultaService.buscarPaginadoComFiltros(
                eq("João"), isNull(), isNull(), isNull(), isNull(), isNull(),
                eq(0), eq(10), eq("data"), eq("desc")
        )).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/consultas/paginated")
                .param("nomePaciente", "João")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].paciente.nome").value("João Silva"));
    }

    @Test
    void testBuscarPaginadoComFiltros_ComFiltroNomeMedico_DeveRetornarFiltrado() throws Exception {
        // Arrange
        PacienteResponseDTO paciente = new PacienteResponseDTO();
        paciente.setId(1L);
        paciente.setNome("João Silva");
        
        UsuarioResponseDTO medico = new UsuarioResponseDTO();
        medico.setId(1L);
        medico.setNome("Dr. Carlos");
        
        ConsultaResponseDTO consulta = new ConsultaResponseDTO();
        consulta.setId(1L);
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setData(LocalDateTime.now());

        List<ConsultaResponseDTO> consultas = Arrays.asList(consulta);
        Page<ConsultaResponseDTO> page = new PageImpl<>(consultas);

        when(consultaService.buscarPaginadoComFiltros(
                isNull(), eq("Carlos"), isNull(), isNull(), isNull(), isNull(),
                eq(0), eq(10), eq("data"), eq("desc")
        )).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/consultas/paginated")
                .param("nomeMedico", "Carlos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].medico.nome").value("Dr. Carlos"));
    }

    @Test
    void testBuscarPaginadoComFiltros_ComPaginacaoCustomizada_DeveRetornarConfiguracaoCorreta() throws Exception {
        // Arrange
        PacienteResponseDTO paciente = new PacienteResponseDTO();
        paciente.setId(1L);
        paciente.setNome("João Silva");
        
        UsuarioResponseDTO medico = new UsuarioResponseDTO();
        medico.setId(1L);
        medico.setNome("Dr. Carlos");
        
        ConsultaResponseDTO consulta = new ConsultaResponseDTO();
        consulta.setId(1L);
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setData(LocalDateTime.now());

        List<ConsultaResponseDTO> consultas = Arrays.asList(consulta);
        Page<ConsultaResponseDTO> page = new PageImpl<>(consultas);

        when(consultaService.buscarPaginadoComFiltros(
                isNull(), isNull(), isNull(), isNull(), isNull(), isNull(),
                eq(1), eq(5), eq("id"), eq("asc")
        )).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/consultas/paginated")
                .param("page", "1")
                .param("size", "5")
                .param("sort", "id")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testBuscarPaginadoComFiltros_ComMultiplosFiltros_DeveRetornarFiltrado() throws Exception {
        // Arrange
        PacienteResponseDTO paciente = new PacienteResponseDTO();
        paciente.setId(1L);
        paciente.setNome("João Silva");
        
        UsuarioResponseDTO medico = new UsuarioResponseDTO();
        medico.setId(1L);
        medico.setNome("Dr. Carlos");
        
        ConsultaResponseDTO consulta = new ConsultaResponseDTO();
        consulta.setId(1L);
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setData(LocalDateTime.now());

        List<ConsultaResponseDTO> consultas = Arrays.asList(consulta);
        Page<ConsultaResponseDTO> page = new PageImpl<>(consultas);

        when(consultaService.buscarPaginadoComFiltros(
                eq("João"), eq("Carlos"), isNull(), isNull(), eq(1L), eq(2L),
                eq(0), eq(10), eq("data"), eq("desc")
        )).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/consultas/paginated")
                .param("nomePaciente", "João")
                .param("nomeMedico", "Carlos")
                .param("pacienteId", "1")
                .param("medicoId", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }
} 
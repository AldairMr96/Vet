package com.mycompany.veterinaria.controllerTest;

import com.mycompany.veterinaria.controller.MascotaController;
import com.mycompany.veterinaria.dto.MascotaDTO;
import com.mycompany.veterinaria.model.Duenho;
import com.mycompany.veterinaria.model.Mascota;
import com.mycompany.veterinaria.service.MascotaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MascotaControllerTest {


    @Mock
    private MascotaService mascotaService;
    @InjectMocks
    private MascotaController mascotaController;

    @BeforeEach
    private void setUp (){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMascotasControllerTest(){

        List<Mascota> mascotas = List.of(
                new Mascota(1L, "Max", "Perro", "Labrador", "Negro", new Duenho()),
                new Mascota(2L, "Luna", "Gato", "Siamés", "Blanco",new Duenho() ),
                new Mascota (3L, "Rex", "Perro", "Pastor Alemán", "'Marrón", new Duenho()),
                new Mascota(4L, "Mimi", "Gato", "Persa", "Gris", new Duenho())
        );

        when(mascotaService.getMascotas()).thenReturn(mascotas);
        List<Mascota> results = mascotaController.getMascotas();

        assertFalse(mascotas.isEmpty());
        assertEquals(4, mascotas.size());
        verify(mascotaService, times(1)).getMascotas();
    }

    @Test
    void getMascotasEmptyControllerTest(){
        when(mascotaService.getMascotas()).thenReturn(Collections.emptyList());

        List<Mascota> mascotas = mascotaController.getMascotas();

        assertTrue(mascotas.isEmpty());
        assertFalse(mascotas.contains(isNull()));

    }
    @Test
    void findOneMascotaControllerTest(){
        Long idPetTest = 1L;
        Mascota mascota = new Mascota(1L, "Max", "Perro", "Labrador", "Negro", new Duenho());

        when(mascotaService.findOneMascota(idPetTest)).thenReturn(mascota);

        ResponseEntity response = mascotaController.findOneMascota(idPetTest);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(mascota, response.getBody());
        verify(mascotaService, times(1)).findOneMascota(any());


    }
    @Test
    void findOnePetNotFoundControllerTest (){


        when(mascotaService.findOneMascota(any())).thenThrow(new EntityNotFoundException("Dueño no encontrado"));

        ResponseEntity response = mascotaController.findOneMascota(any());

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Dueño no encontrado", response.getBody());
        verify(mascotaService, times(1)).findOneMascota(any());

    }

    @Test
    void findOnePetServerErrorControllerTest (){

        when(mascotaService.findOneMascota(any())).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity response = mascotaController.findOneMascota(any());

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(mascotaService, times(1)).findOneMascota(any());

    }
    @Test
    void findMascotaDTOControllerTest (){
        Duenho duenho = new Duenho(1L, "12345678", "Juan", "Perez", "3001234567",new ArrayList<>());
        Mascota mascota = new Mascota(1L, "Max", "Perro", "Labrador", "Negro", duenho);
        MascotaDTO mascotaDTO = new MascotaDTO();
        mascotaDTO.setNombreMascota(mascota.getNombreMascota());
        mascotaDTO.setEspecie(mascota.getEspecie());
        mascotaDTO.setRaza(mascota.getRaza());
        mascotaDTO.setNombreDuenho(duenho.getNombreDuenho());
        mascotaDTO.setApellidoDuenho(duenho.getApellidoDuenho());
        List<MascotaDTO> mascotaDTOList = new ArrayList<>();
        mascotaDTOList.add(mascotaDTO);

        when(mascotaService.findMascotaDTO()).thenReturn(mascotaDTOList);

        List<MascotaDTO> result = mascotaController.findMascotaDTO();

        assertNotNull(result);
        assertInstanceOf(ArrayList.class, result);
        assertEquals(1, result.size() );
        assertFalse(result.isEmpty());

    }

    @Test
    void findMascotaDTOEmptyControllerTest (){
        when(mascotaService.findMascotaDTO()).thenReturn(Collections.emptyList());

        List<MascotaDTO> result = mascotaController.findMascotaDTO();

        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(mascotaService, times(1)).findMascotaDTO();
    }

    @Test
    void createMascotasControllerTest (){
        Mascota mascota =   new Mascota(1L, "Max", "Perro", "Labrador", "Negro", new Duenho());

        when(mascotaService.saveMascota(any())).thenReturn(mascota);

        Mascota result = mascotaController.saveMascota(mascota);

        verify(mascotaService, times(1)).saveMascota(mascota);
        assertNotNull(result);
        assertEquals(result.getIdMascota(), mascota.getIdMascota());
        assertEquals(result.getNombreMascota(), mascota.getNombreMascota());
        assertEquals(result.getEspecie(), mascota.getEspecie());
        assertEquals(result.getRaza(), mascota.getRaza());
        assertEquals(result.getColor(), mascota.getColor());
        assertInstanceOf(Mascota.class, mascota);

    }

    @Test
    void editMascotaControllerTest (){
        Mascota mascota = new Mascota(1L, "Max", "Perro", "Labrador", "Negro", new Duenho());

        when(mascotaService.editMascota(any())).thenReturn(mascota);

        ResponseEntity response = mascotaController.editMascota(mascota);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Mascota editada exitosamente: " + mascota, response.getBody());
        verify(mascotaService, times(1)).editMascota(mascota);

    }

    @Test
    void editMascotaNotFoundControllerTest (){
        when(mascotaService.editMascota(any())).thenThrow(new EntityNotFoundException("mascota no encontrado"));

        ResponseEntity response = mascotaController.editMascota(any());

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("mascota no encontrado", response.getBody());
        verify(mascotaService, times(1)).editMascota(any());
    }
    @Test
    void editMascotaInternalErrorControllerTest (){
        when(mascotaService.editMascota(any())).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity response = mascotaController.editMascota(any());

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(mascotaService, times(1)).editMascota(any());
    }

    @Test
    void deleteMascotaControllerTest (){
        Long idPetTest = 1L;

        doNothing().when(mascotaService).deleteMascota(idPetTest);

        ResponseEntity response = mascotaController.deleteMascota(idPetTest);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Mascota eliminada exitosamente ", response.getBody());
        verify(mascotaService, times(1)).deleteMascota(idPetTest);
    }

    @Test
    void  deleteMascotaNotFoundControllerTest (){
        doThrow(new EntityNotFoundException("Mascota no encontrada")).when(mascotaService).deleteMascota(any());

        ResponseEntity response = mascotaController.deleteMascota(any());

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Mascota no encontrada", response.getBody());
        verify(mascotaService, times(1)).deleteMascota(any());
    }

    @Test
    void deleteMascotaInternalErrorControllerTest(){
        doThrow(new RuntimeException("Server internal Error")).when(mascotaService).deleteMascota(any());

        ResponseEntity response = mascotaController.deleteMascota(any());

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(mascotaService, times(1)).deleteMascota(any());
    }

}

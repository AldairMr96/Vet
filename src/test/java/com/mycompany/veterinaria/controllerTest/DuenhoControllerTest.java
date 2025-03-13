package com.mycompany.veterinaria.controllerTest;

import com.mycompany.veterinaria.controller.DuenhoController;
import com.mycompany.veterinaria.model.Duenho;
import com.mycompany.veterinaria.service.DuenhoService;
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

public class DuenhoControllerTest {

    @Mock
    private DuenhoService duenhoService;

    @InjectMocks
    private DuenhoController duenhoController;

    @BeforeEach
    private void setUp (){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDuenhosEmptyListTest(){
        when(duenhoService.getDuenhos()).thenReturn(Collections.emptyList());
        List<Duenho> duenhos = duenhoController.getDuenhos();

        assertTrue(duenhos.isEmpty());
    }

    @Test
    void getDuenhosControllerTest(){
        List<Duenho> duenhos = List.of(
                new Duenho(1L, "12345678", "Juan", "Perez", "3001234567",new ArrayList<>()),
               new Duenho (2L, "87654321", "Maria", "Lopez", "3007654321", new ArrayList<>())

        );
              when(duenhoService.getDuenhos()).thenReturn(duenhos);
              List<Duenho> result = duenhoController.getDuenhos();

        assertFalse(duenhos.isEmpty());
        assertEquals(2, duenhos.size());
    }

    @Test
    void findOneDuenhoControllerTest(){
        Long idDuenhoTest =1L ;
        Duenho duenho = new Duenho(1L, "12345678", "Juan", "Perez", "3001234567",new ArrayList<>());

        when(duenhoService.findOneDuenho(idDuenhoTest)).thenReturn(duenho);
        ResponseEntity<?> response = duenhoController.findDuenho(idDuenhoTest);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(duenho, response.getBody());
        verify(duenhoService, times(1)).findOneDuenho(idDuenhoTest);
    }

    @Test
    void findOneDuenhoNotFoundTest(){
        Long idDuenhoTest = 1L;

        when(duenhoService.findOneDuenho(idDuenhoTest)).thenThrow(new EntityNotFoundException("Dueño no encontrado"));
        ResponseEntity<?> response = duenhoController.findDuenho(idDuenhoTest);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Dueño no encontrado", response.getBody());
        verify(duenhoService, times(1)).findOneDuenho(idDuenhoTest);

    }
    @Test
    void findOneDuenhoServerErrorTest(){
        Long idDuenhoTest = 1L ;

        when(duenhoService.findOneDuenho(idDuenhoTest)).thenThrow(new RuntimeException("Server internal Error"));
        ResponseEntity<?> response = duenhoController.findDuenho(idDuenhoTest);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(duenhoService, times(1)).findOneDuenho(idDuenhoTest);
    }

    @Test
    void saveDuenhoControllerTest (){
        Duenho duenhoTest = new Duenho(1L, "12345678", "Juan", "Perez", "3001234567",new ArrayList<>());


        when(duenhoService.saveDuenho( any())).thenReturn(duenhoTest);
        Duenho duenhoResult = duenhoController.saveDuenho(duenhoTest);

        verify(duenhoService, times(1))
                .saveDuenho(duenhoTest);

        assertNotNull(duenhoResult);
        assertEquals(duenhoResult.getIdDuenho(), duenhoTest.getIdDuenho());
        assertEquals(duenhoResult.getDni(), duenhoTest.getDni());
        assertEquals(duenhoResult.getNombreDuenho(), duenhoTest.getNombreDuenho());
        assertEquals(duenhoResult.getApellidoDuenho(), duenhoTest.getApellidoDuenho());
        assertEquals(duenhoResult.getCelularDuenho(), duenhoTest.getCelularDuenho());
        assertTrue(duenhoResult.getMascotas().isEmpty());

    }

    @Test
    void deleteDuenhoControllerTest (){
        Long idDuenhoTest = 1L;
        doNothing().when(duenhoService).deleteDuenho(idDuenhoTest);

        ResponseEntity response = duenhoController.deleteDuenho(idDuenhoTest);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Dueño eliminado  exitosamente ", response.getBody());
        verify(duenhoService, times(1)).deleteDuenho(idDuenhoTest);
    }

    @Test
    void deleteDuenhoNotFoundError(){
        Long idDuenhoTest=1L ;
        doThrow(new EntityNotFoundException("Dueño no Encontrado")).when(duenhoService).deleteDuenho(idDuenhoTest);

        ResponseEntity response = duenhoController.deleteDuenho(idDuenhoTest);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Dueño no Encontrado", response.getBody());
        verify(duenhoService, times(1)).deleteDuenho(idDuenhoTest);

    }

    @Test
    void deleteDuenhoInternalError(){
        Long idDuenhoTest=1L ;
        doThrow(new RuntimeException("Server internal Error")).when(duenhoService).deleteDuenho(idDuenhoTest);

        ResponseEntity response = duenhoController.deleteDuenho(idDuenhoTest);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(duenhoService, times(1)).deleteDuenho(idDuenhoTest);

    }

    @Test
    void editDuenhoControllerTest (){
        Duenho duenhoTest = new Duenho(1L, "12345678", "Juan", "Perez", "3001234567",new ArrayList<>());

        when(duenhoService.editDuenho(any())).thenReturn(duenhoTest);
        when(duenhoService.findOneDuenho(duenhoTest.getIdDuenho())).thenReturn(duenhoTest);

        ResponseEntity response = duenhoController.editDuenho(duenhoTest);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Dueño editado exitosamente: " + duenhoTest, response.getBody());
        verify(duenhoService, times(1)).editDuenho(duenhoTest);
        verify(duenhoService, times(1)).findOneDuenho(duenhoTest.getIdDuenho());
    }

    @Test
    void editDuenhoNotFoundErrorTest(){
        Duenho duenhoTest = new Duenho(1L, "12345678", "Juan", "Perez", "3001234567",new ArrayList<>());

        doThrow(new EntityNotFoundException("Dueño no encontrado")).when(duenhoService).editDuenho(duenhoTest);

        ResponseEntity response = duenhoController.editDuenho(duenhoTest);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Dueño no encontrado", response.getBody());
        verify(duenhoService, times(1)).editDuenho(duenhoTest);
        verify(duenhoService, times(0)).findOneDuenho(duenhoTest.getIdDuenho());
    }

    @Test
    void editDuenhoInternalErrorTest() {
        Duenho duenhoTest = new Duenho(1L, "12345678", "Juan", "Perez", "3001234567", new ArrayList<>());

        doThrow(new RuntimeException("Server internal Error")).when(duenhoService).editDuenho(duenhoTest);

        ResponseEntity response = duenhoController.editDuenho(duenhoTest);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(duenhoService, times(1)).editDuenho(duenhoTest);
        verify(duenhoService, times(0)).findOneDuenho(duenhoTest.getIdDuenho());

    }
}

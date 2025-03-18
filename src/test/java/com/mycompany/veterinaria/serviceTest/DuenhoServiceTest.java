package com.mycompany.veterinaria.serviceTest;

import com.mycompany.veterinaria.model.Duenho;
import com.mycompany.veterinaria.model.Mascota;
import com.mycompany.veterinaria.repository.IDuenhoRespository;
import com.mycompany.veterinaria.service.DuenhoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DuenhoServiceTest {

    @Mock
    private IDuenhoRespository duenhoRespository;

    @Mock
    private DuenhoService duenhoServiceMock;

    @InjectMocks
    private DuenhoService duenhoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDuenhoSuccessTest() {
        Duenho duenho = new Duenho(1L, "12345678", "Juan", "Perez", "3001234567", new ArrayList<>());

        when(duenhoRespository.save(any())).thenReturn(duenho);

        Duenho result = duenhoService.saveDuenho(duenho);

        verify(duenhoRespository, times(1)).save(duenho);
        assertEquals(result.getIdDuenho(), duenho.getIdDuenho());
        assertEquals(result.getDni(), duenho.getDni());
        assertEquals(result.getNombreDuenho(), duenho.getNombreDuenho());
        assertEquals(result.getApellidoDuenho(), duenho.getApellidoDuenho());
        assertEquals(result.getCelularDuenho(), duenho.getCelularDuenho());
        assertTrue(result.getMascotas().isEmpty());
    }


    @Test
    void getDuenhosSuccesTest() {
        List<Duenho> duenhos = List.of(
                new Duenho(1L, "12345678", "Juan", "Perez", "3001234567", new ArrayList<>()),
                new Duenho(2L, "8915246320", "Nicolas", "Paz", "3001234798", new ArrayList<>())
        );
        when(duenhoRespository.findAll()).thenReturn(duenhos);

        List<Duenho> result = duenhoService.getDuenhos();

        assertEquals(2, result.size());
        assertFalse(result.isEmpty());
        verify(duenhoRespository, times(1)).findAll();

    }

    @Test
    void getDuenhosEmptyTest() {

        when(duenhoRespository.findAll()).thenReturn(Collections.emptyList());

        List<Duenho> result = duenhoService.getDuenhos();

        verify(duenhoRespository, times(1)).findAll();
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());

    }

    @Test
    void findOneDuenhoNotFoundTest() {

        when(duenhoRespository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> duenhoService.findOneDuenho(any()));
        verify(duenhoRespository, times(1)).findById(any());

    }
    @Test
    void findDuenhoSuccessTest (){

        Duenho duenho =  new Duenho(1L, "12345678", "Juan", "Perez", "3001234567", new ArrayList<>());

        when(duenhoRespository.findById(any())).thenReturn(Optional.of(duenho));

        Duenho result = duenhoService.findOneDuenho(any());

        assertNotNull(result);
        assertEquals(duenho.getIdDuenho(), result.getIdDuenho());
        assertEquals(duenho.getDni(), result.getDni());
        assertEquals(duenho.getNombreDuenho(), result.getNombreDuenho());
        assertEquals(duenho.getApellidoDuenho(), result.getApellidoDuenho());
        assertEquals(0, result.getMascotas().size());
        assertTrue(result.getMascotas().isEmpty());
        verify(duenhoRespository, times(1)).findById(any());

    }


    @Test
    void editDuenhoErrorTest (){

        Duenho updatedDuenho = new Duenho(1L, "654321", "Jane", "Smith", "987654321", new ArrayList<>());

        when(duenhoRespository.findById(updatedDuenho.getIdDuenho())).thenThrow(new EntityNotFoundException("Dueño no encontrado"));


        assertThrows(EntityNotFoundException.class, () -> duenhoService.editDuenho(updatedDuenho));

        verify(duenhoRespository, times(1)).findById(updatedDuenho.getIdDuenho());
        verify(duenhoRespository, times(0)).save(updatedDuenho);


    }

    @Test
    void editDuenhoSuccessTest (){
        Long duenhoId = 1L;
        Duenho existingDuenho = new Duenho(duenhoId, "123456", "John", "Doe", "123456789", new ArrayList<>());
        Duenho updatedDuenho = new Duenho(duenhoId, "654321", "Jane", "Smith", "987654321", new ArrayList<>());


        when(duenhoRespository.findById(duenhoId)).thenReturn(Optional.of(existingDuenho));
        when(duenhoRespository.save(existingDuenho)).thenReturn(updatedDuenho);


        Duenho result = duenhoService.editDuenho(updatedDuenho);


        assertNotNull(result);
        assertEquals("654321", result.getDni());
        assertEquals("Jane", result.getNombreDuenho());
        assertEquals("Smith", result.getApellidoDuenho());
        assertEquals("987654321", result.getCelularDuenho());


        verify(duenhoRespository, times(1)).findById(duenhoId);
        verify(duenhoRespository, times(1)).save(existingDuenho);
    }

    @Test
    void deleteDuenhoErrorTest (){
        when(duenhoRespository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, ()->duenhoService.deleteDuenho(any()));
        verify(duenhoRespository, times(1)).existsById(any());
        verify(duenhoRespository, never()).deleteById(any());
    }

    @Test
    void deleteDuenhoSuccesTest () {
        Long idDelete = 1L;
        Duenho duenho = new Duenho(idDelete, "123456", "John", "Doe", "123456789", new ArrayList<>());

        when(duenhoRespository.existsById(idDelete)).thenReturn(true);
        doNothing().when(duenhoRespository).deleteById(idDelete);

        duenhoService.deleteDuenho(idDelete);

        verify(duenhoRespository, times(1)).deleteById(idDelete);


    }





}

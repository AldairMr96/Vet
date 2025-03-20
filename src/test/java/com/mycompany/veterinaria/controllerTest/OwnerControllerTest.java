package com.mycompany.veterinaria.controllerTest;

import com.mycompany.veterinaria.controller.OwnerController;
import com.mycompany.veterinaria.model.Owner;
import com.mycompany.veterinaria.service.OwnerService;
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

public class OwnerControllerTest {

    @Mock
    private OwnerService ownerService;

    @InjectMocks
    private OwnerController ownerController;

    @BeforeEach
    private void setUp (){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOwnersEmptyListTest(){
        when(ownerService.getOwners()).thenReturn(Collections.emptyList());
        List<Owner> owners = ownerController.getDuenhos();

        assertTrue(owners.isEmpty());
    }

    @Test
    void getOwnersControllerTest(){
        List<Owner> owners = List.of(
                new Owner(1L, "12345678", "Juan", "Perez", "3001234567",new ArrayList<>()),
               new Owner(2L, "87654321", "Maria", "Lopez", "3007654321", new ArrayList<>())

        );
              when(ownerService.getOwners()).thenReturn(owners);
              List<Owner> result = ownerController.getDuenhos();

        assertFalse(owners.isEmpty());
        assertEquals(2, owners.size());
    }

    @Test
    void findOneOwnerControllerTest(){
        Long idDuenhoTest =1L ;
        Owner owner = new Owner(1L, "12345678", "Juan", "Perez", "3001234567",new ArrayList<>());

        when(ownerService.findOneOwner(idDuenhoTest)).thenReturn(owner);
        ResponseEntity<?> response = ownerController.findDuenho(idDuenhoTest);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(owner, response.getBody());
        verify(ownerService, times(1)).findOneOwner(idDuenhoTest);
    }

    @Test
    void findOneOwnerNotFoundTest(){
        Long idDuenhoTest = 1L;

        when(ownerService.findOneOwner(idDuenhoTest)).thenThrow(new EntityNotFoundException("Owner not found"));
        ResponseEntity<?> response = ownerController.findDuenho(idDuenhoTest);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Owner not found", response.getBody());
        verify(ownerService, times(1)).findOneOwner(idDuenhoTest);

    }
    @Test
    void findOneOwnerServerErrorTest(){
        Long idDuenhoTest = 1L ;

        when(ownerService.findOneOwner(idDuenhoTest)).thenThrow(new RuntimeException("Server internal Error"));
        ResponseEntity<?> response = ownerController.findDuenho(idDuenhoTest);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(ownerService, times(1)).findOneOwner(idDuenhoTest);
    }

    @Test
    void saveOwnerControllerTest (){
        Owner ownerTest = new Owner(1L, "12345678", "Juan", "Perez", "3001234567",new ArrayList<>());


        when(ownerService.saveOwner( any())).thenReturn(ownerTest);
        Owner ownerResult = ownerController.saveDuenho(ownerTest);

        verify(ownerService, times(1))
                .saveOwner(ownerTest);

        assertNotNull(ownerResult);
        assertEquals(ownerResult.getIdOwner(), ownerTest.getIdOwner());
        assertEquals(ownerResult.getDni(), ownerTest.getDni());
        assertEquals(ownerResult.getOwnerName(), ownerTest.getOwnerName());
        assertEquals(ownerResult.getOwnerLastname(), ownerTest.getOwnerLastname());
        assertEquals(ownerResult.getOwnerCellphone(), ownerTest.getOwnerCellphone());
        assertTrue(ownerResult.getPets().isEmpty());

    }

    @Test
    void deleteOwnerControllerTest (){
        Long idDuenhoTest = 1L;
        doNothing().when(ownerService).deleteOwner(idDuenhoTest);

        ResponseEntity response = ownerController.deleteDuenho(idDuenhoTest);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Owner successfully delete: ", response.getBody());
        verify(ownerService, times(1)).deleteOwner(idDuenhoTest);
    }

    @Test
    void deleteOwnerNotFoundError(){
        Long idDuenhoTest=1L ;
        doThrow(new EntityNotFoundException("Owner not exist")).when(ownerService).deleteOwner(idDuenhoTest);

        ResponseEntity response = ownerController.deleteDuenho(idDuenhoTest);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Owner not exist", response.getBody());
        verify(ownerService, times(1)).deleteOwner(idDuenhoTest);

    }

    @Test
    void deleteOwnerInternalError(){
        Long idDuenhoTest=1L ;
        doThrow(new RuntimeException("Server internal Error")).when(ownerService).deleteOwner(idDuenhoTest);

        ResponseEntity response = ownerController.deleteDuenho(idDuenhoTest);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(ownerService, times(1)).deleteOwner(idDuenhoTest);

    }

    @Test
    void editOwnerControllerTest (){
        Owner ownerTest = new Owner(1L, "12345678", "Juan", "Perez", "3001234567",new ArrayList<>());

        when(ownerService.editOwner(any())).thenReturn(ownerTest);
        when(ownerService.findOneOwner(ownerTest.getIdOwner())).thenReturn(ownerTest);

        ResponseEntity response = ownerController.editDuenho(ownerTest);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Owner successfully edited: " + ownerTest, response.getBody());
        verify(ownerService, times(1)).editOwner(ownerTest);
        verify(ownerService, times(1)).findOneOwner(ownerTest.getIdOwner());
    }

    @Test
    void editOwnerNotFoundErrorTest(){
        Owner ownerTest = new Owner(1L, "12345678", "Juan", "Perez", "3001234567",new ArrayList<>());

        doThrow(new EntityNotFoundException("Owner not found")).when(ownerService).editOwner(ownerTest);

        ResponseEntity response = ownerController.editDuenho(ownerTest);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Owner not found", response.getBody());
        verify(ownerService, times(1)).editOwner(ownerTest);
        verify(ownerService, times(0)).findOneOwner(ownerTest.getIdOwner());
    }

    @Test
    void editOwnerInternalErrorTest() {
        Owner ownerTest = new Owner(1L, "12345678", "Juan", "Perez", "3001234567", new ArrayList<>());

        doThrow(new RuntimeException("Server internal Error")).when(ownerService).editOwner(ownerTest);

        ResponseEntity response = ownerController.editDuenho(ownerTest);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(ownerService, times(1)).editOwner(ownerTest);
        verify(ownerService, times(0)).findOneOwner(ownerTest.getIdOwner());

    }
}

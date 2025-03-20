package com.mycompany.veterinaria.serviceTest;

import com.mycompany.veterinaria.model.Owner;
import com.mycompany.veterinaria.model.Pet;
import com.mycompany.veterinaria.repository.IOwnerRespository;
import com.mycompany.veterinaria.service.OwnerService;
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

public class OwnerServiceTest {

    @Mock
    private IOwnerRespository ownerRespository;



    @InjectMocks
    private OwnerService ownerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOwnerSuccessTest() {
        Owner owner = new Owner(1L, "12345678", "Juan", "Perez", "3001234567", new ArrayList<>());

        when(ownerRespository.save(any())).thenReturn(owner);

        Owner result = ownerService.saveOwner(owner);

        verify(ownerRespository, times(1)).save(owner);
        assertEquals(result.getIdOwner(), owner.getIdOwner());
        assertEquals(result.getDni(), owner.getDni());
        assertEquals(result.getOwnerName(), owner.getOwnerName());
        assertEquals(result.getOwnerLastname(), owner.getOwnerLastname());
        assertEquals(result.getOwnerCellphone(), owner.getOwnerCellphone());
        assertTrue(result.getPets().isEmpty());
    }


    @Test
    void getOwnerSuccesTest() {
        List<Pet> pets = List.of(new Pet());
        List<Owner> owners = List.of(
                new Owner(1L, "12345678", "Juan", "Perez", "3001234567", pets),
                new Owner(2L, "8915246320", "Nicolas", "Paz", "3001234798",pets)
        );
        when(ownerRespository.findAll()).thenReturn(owners);

        List<Owner> result = ownerService.getOwners();

        assertEquals(2, result.size());
        assertFalse(result.isEmpty());
        verify(ownerRespository, times(1)).findAll();

    }

    @Test
    void getOwnersEmptyTest() {

        when(ownerRespository.findAll()).thenReturn(Collections.emptyList());

        List<Owner> result = ownerService.getOwners();

        verify(ownerRespository, times(1)).findAll();
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());

    }

    @Test
    void findOneOwnerNotFoundTest() {

        when(ownerRespository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ownerService.findOneOwner(any()));
        verify(ownerRespository, times(1)).findById(any());

    }
    @Test
    void findOwnerSuccessTest (){

        Owner owner =  new Owner(1L, "12345678", "Juan", "Perez", "3001234567", new ArrayList<>());

        when(ownerRespository.findById(any())).thenReturn(Optional.of(owner));

        Owner result = ownerService.findOneOwner(any());

        assertNotNull(result);
        assertEquals(owner.getIdOwner(), result.getIdOwner());
        assertEquals(owner.getDni(), result.getDni());
        assertEquals(owner.getOwnerName(), result.getOwnerName());
        assertEquals(owner.getOwnerLastname(), result.getOwnerLastname());
        assertEquals(0, result.getPets().size());
        assertTrue(result.getPets().isEmpty());
        verify(ownerRespository, times(1)).findById(any());

    }


    @Test
    void editOwnerErrorTest (){

        Owner updatedOwner = new Owner(1L, "654321", "Jane", "Smith", "987654321", new ArrayList<>());

        when(ownerRespository.findById(updatedOwner.getIdOwner())).thenThrow(new EntityNotFoundException("Pet not found"));


        assertThrows(EntityNotFoundException.class, () -> ownerService.editOwner(updatedOwner));

        verify(ownerRespository, times(1)).findById(updatedOwner.getIdOwner());
        verify(ownerRespository, times(0)).save(updatedOwner);


    }

    @Test
    void editOwnerSuccessTest (){
        Long duenhoId = 1L;
        Owner existingOwner = new Owner(duenhoId, "123456", "John", "Doe", "123456789", new ArrayList<>());
        Owner updatedOwner = new Owner(duenhoId, "654321", "Jane", "Smith", "987654321", new ArrayList<>());


        when(ownerRespository.findById(duenhoId)).thenReturn(Optional.of(existingOwner));
        when(ownerRespository.save(existingOwner)).thenReturn(updatedOwner);


        Owner result = ownerService.editOwner(existingOwner);


        assertNotNull(result);
        assertEquals( result.getDni(), updatedOwner.getDni());
        assertEquals( result.getOwnerName(), updatedOwner.getOwnerName());
        assertEquals( result.getOwnerLastname(), updatedOwner.getOwnerLastname());
        assertEquals(result.getOwnerCellphone(), updatedOwner.getOwnerCellphone());


        verify(ownerRespository, times(1)).findById(duenhoId);
        verify(ownerRespository, times(1)).save(existingOwner);
    }

    @Test
    void deleteOwnerErrorTest (){
        when(ownerRespository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, ()-> ownerService.deleteOwner(any()));
        verify(ownerRespository, times(1)).existsById(any());
        verify(ownerRespository, never()).deleteById(any());
    }

    @Test
    void deleteOwnerSuccesTest () {

        Owner owner = new Owner(1L, "123456", "John", "Doe", "123456789", new ArrayList<>());

        when(ownerRespository.existsById(owner.getIdOwner())).thenReturn(true);
        doNothing().when(ownerRespository).deleteById(owner.getIdOwner());

        ownerService.deleteOwner(owner.getIdOwner());

        verify(ownerRespository, times(1)).deleteById(owner.getIdOwner());


    }





}

package com.mycompany.veterinaria.controllerTest;

import com.mycompany.veterinaria.controller.PetController;
import com.mycompany.veterinaria.dto.PetDTO;
import com.mycompany.veterinaria.model.Owner;
import com.mycompany.veterinaria.model.Pet;
import com.mycompany.veterinaria.service.PetService;
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

public class PetControllerTest {


    @Mock
    private PetService petService;
    @InjectMocks
    private PetController petController;

    @BeforeEach
    private void setUp (){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPetsControllerTest(){

        List<Pet> pets = List.of(
                new Pet(1L, "Max", "Perro", "Labrador", "Negro", new Owner()),
                new Pet(2L, "Luna", "Gato", "Siamés", "Blanco",new Owner() ),
                new Pet(3L, "Rex", "Perro", "Pastor Alemán", "'Marrón", new Owner()),
                new Pet(4L, "Mimi", "Gato", "Persa", "Gris", new Owner())
        );

        when(petService.getPets()).thenReturn(pets);
        List<Pet> results = petController.getMascotas();

        assertFalse(pets.isEmpty());
        assertEquals(4, pets.size());
        verify(petService, times(1)).getPets();
    }

    @Test
    void getPetsEmptyControllerTest(){
        when(petService.getPets()).thenReturn(Collections.emptyList());

        List<Pet> pets = petController.getMascotas();

        assertTrue(pets.isEmpty());
        assertFalse(pets.contains(isNull()));

    }
    @Test
    void findOnePetControllerTest(){
        Long idPetTest = 1L;
        Pet pet = new Pet(1L, "Max", "Perro", "Labrador", "Negro", new Owner());

        when(petService.findOnePet(idPetTest)).thenReturn(pet);

        ResponseEntity response = petController.findOneMascota(idPetTest);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(pet, response.getBody());
        verify(petService, times(1)).findOnePet(any());


    }
    @Test
    void findOnePetNotFoundControllerTest (){


        when(petService.findOnePet(any())).thenThrow(new EntityNotFoundException("Dueño no encontrado"));

        ResponseEntity response = petController.findOneMascota(any());

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Dueño no encontrado", response.getBody());
        verify(petService, times(1)).findOnePet(any());

    }

    @Test
    void findOnePetServerErrorControllerTest (){

        when(petService.findOnePet(any())).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity response = petController.findOneMascota(any());

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(petService, times(1)).findOnePet(any());

    }
    @Test
    void findPetDTOControllerTest (){
        Owner owner = new Owner(1L, "12345678", "Juan", "Perez", "3001234567",new ArrayList<>());
        Pet pet = new Pet(1L, "Max", "Perro", "Labrador", "Negro", owner);
        PetDTO petDTO = new PetDTO();
        petDTO.setPetName(pet.getPetName());
        petDTO.setSpecies(pet.getSpecies());
        petDTO.setRace(pet.getRace());
        petDTO.setOwnerName(owner.getOwnerName());
        petDTO.setOwnerLastname(owner.getOwnerLastname());
        List<PetDTO> petDTOList = new ArrayList<>();
        petDTOList.add(petDTO);

        when(petService.findPetDTO()).thenReturn(petDTOList);

        List<PetDTO> result = petController.findPetDTO();

        assertNotNull(result);
        assertInstanceOf(ArrayList.class, result);
        assertEquals(1, result.size() );
        assertFalse(result.isEmpty());

    }

    @Test
    void findPetDTOEmptyControllerTest (){
        when(petService.findPetDTO()).thenReturn(Collections.emptyList());

        List<PetDTO> result = petController.findPetDTO();

        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(petService, times(1)).findPetDTO();
    }

    @Test
    void createPetControllerTest (){
        Pet pet =   new Pet(1L, "Max", "Perro", "Labrador", "Negro", new Owner());

        when(petService.savePet(any())).thenReturn(pet);

        Pet result = petController.saveMascota(pet);

        verify(petService, times(1)).savePet(pet);
        assertNotNull(result);
        assertEquals(result.getIdPet(), pet.getIdPet());
        assertEquals(result.getPetName(), pet.getPetName());
        assertEquals(result.getSpecies(), pet.getSpecies());
        assertEquals(result.getRace(), pet.getRace());
        assertEquals(result.getColor(), pet.getColor());
        assertInstanceOf(Pet.class, pet);

    }

    @Test
    void editPetControllerTest (){
        Pet pet = new Pet(1L, "Max", "Perro", "Labrador", "Negro", new Owner());

        when(petService.editPet(any())).thenReturn(pet);

        ResponseEntity response = petController.editMascota(pet);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Pet successfully edited: " + pet, response.getBody());
        verify(petService, times(1)).editPet(pet);

    }

    @Test
    void editPetNotFoundControllerTest (){
        when(petService.editPet(any())).thenThrow(new EntityNotFoundException("Pet not found"));

        ResponseEntity response = petController.editMascota(any());

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Pet not found", response.getBody());
        verify(petService, times(1)).editPet(any());
    }
    @Test
    void editPetInternalErrorControllerTest (){
        when(petService.editPet(any())).thenThrow(new RuntimeException("Server internal Error"));

        ResponseEntity response = petController.editMascota(any());

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(petService, times(1)).editPet(any());
    }

    @Test
    void deletePetControllerTest (){
        Long idPetTest = 1L;

        doNothing().when(petService).deletePet(idPetTest);

        ResponseEntity response = petController.deleteMascota(idPetTest);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Pet successfully eliminated", response.getBody());
        verify(petService, times(1)).deletePet(idPetTest);
    }

    @Test
    void  deletePetNotFoundControllerTest (){
        doThrow(new EntityNotFoundException("Pet not found")).when(petService).deletePet(any());

        ResponseEntity response = petController.deleteMascota(any());

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Pet not found", response.getBody());
        verify(petService, times(1)).deletePet(any());
    }

    @Test
    void deletePetInternalErrorControllerTest(){
        doThrow(new RuntimeException("Server internal Error")).when(petService).deletePet(any());

        ResponseEntity response = petController.deleteMascota(any());

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server internal Error", response.getBody());
        verify(petService, times(1)).deletePet(any());
    }

}

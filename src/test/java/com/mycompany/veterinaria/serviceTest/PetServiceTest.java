package com.mycompany.veterinaria.serviceTest;

import com.mycompany.veterinaria.dto.PetDTO;
import com.mycompany.veterinaria.model.Owner;
import com.mycompany.veterinaria.model.Pet;
import com.mycompany.veterinaria.repository.IOwnerRespository;
import com.mycompany.veterinaria.repository.IPetRepository;
import com.mycompany.veterinaria.service.PetService;
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

public class PetServiceTest {
    @Mock
    private IOwnerRespository ownerRespository;
    @Mock
    private IPetRepository petRepository;

    @InjectMocks
    private PetService petService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createPetSuccessTest (){
        Pet pet = new Pet(1L, "Firulais", "Canino", "Labrador", "Cafe", new Owner());

        when(petRepository.save(any())).thenReturn(pet);

        Pet result = petService.savePet(pet);

        verify(petRepository, times(1)).save(pet);
        assertEquals(result.getIdPet(), pet.getIdPet());
        assertEquals(result.getPetName(), pet.getPetName());
        assertEquals(result.getSpecies(), pet.getSpecies());
        assertEquals(result.getRace(), pet.getRace());
        assertEquals(result.getColor(), pet.getColor());
        assertInstanceOf(Owner.class, result.getOwner());
    }

    @Test
    void getPetSuccessTest(){
        List<Pet> pets = List.of(
        new Pet(1L, "Firulais", "Canino", "Labrador", "Marrón", new Owner()),
        new Pet(2L, "Mishifu", "Felino", "Siamés", "Blanco", new Owner()),
        new Pet(3L, "Rex", "Canino", "Pastor Alemán", "Negro",new Owner())
        );

        when(petRepository.findAll()).thenReturn(pets);

        List<Pet> result = petService.getPets();

        verify(petRepository, times(1)).findAll();
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());

    }
    @Test
    void getPetEmptyTest(){
        when(petRepository.findAll()).thenReturn(Collections.emptyList());

        List<Pet> result = petService.getPets();

        verify(petRepository, times(1)).findAll();
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    @Test
    void findOnePetNotFoundTest(){
        Long idPetTest =1L;

        when(petRepository.findById(idPetTest)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()-> petService.findOnePet(idPetTest));
        verify(petRepository, times(1)).findById(idPetTest);

    }

    @Test
    void findOnePetSuccessTest (){
        Pet pet = new Pet(1L, "Firulais", "Canino", "Labrador", "Marrón", new Owner());

        when(petRepository.findById(pet.getIdPet())).thenReturn(Optional.of(pet));

        Pet result = petService.findOnePet(pet.getIdPet());

        assertEquals(result.getIdPet(), pet.getIdPet());
        assertEquals(result.getPetName(), pet.getPetName());
        assertEquals(result.getSpecies(), pet.getSpecies());
        assertEquals(result.getRace(), pet.getRace());
        assertEquals(result.getColor(), pet.getColor());
        assertInstanceOf(Owner.class, result.getOwner());
        verify(petRepository, times(1)).findById(pet.getIdPet());
    }

    @Test
    void editPetErrorTest (){
        Pet updatePet = new Pet(1L, "Firulais", "Canino", "Labrador", "Marrón", new Owner());
        when(petRepository.findById(updatePet.getIdPet())).thenThrow(new EntityNotFoundException("Pet not found"));


        assertThrows(EntityNotFoundException.class, () -> petService.editPet(updatePet));

        verify(petRepository, times(1)).findById(updatePet.getIdPet());
        verify(petRepository, times(0)).save(updatePet);
    }

    @Test
    void editPetSuccessTest(){
        Long idPet = 1L;
        Pet existPet = new Pet(idPet,"Firulaisito", "Canino", "Labrador", "Marrón", new Owner() );
        Pet updatePet = new Pet(idPet, "Firulais", "Canino", "Labrador", "Marrón", new Owner());

        when(petRepository.findById(idPet)).thenReturn(Optional.of(existPet));
        when(petRepository.save(existPet)).thenReturn(updatePet);

        Pet result = petService.editPet(existPet);

        assertEquals(result.getIdPet(), updatePet.getIdPet());
        assertEquals(result.getPetName(), updatePet.getPetName());
        assertEquals(result.getSpecies(), updatePet.getSpecies());
        assertEquals(result.getRace(), updatePet.getRace());
        assertEquals(result.getColor(), updatePet.getColor());
        verify(petRepository, times(1)).findById(idPet);
        verify(petRepository,times(1)).save(existPet);

    }

    @Test
    void deletePetErrorTest (){
        when(petRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, ()-> petService.deletePet(any()));
        verify(petRepository, times(1)).existsById(any());
        verify(petRepository, never()).deleteById(any());
    }
    @Test
    void deletePetSuccessTest(){
        Pet pet = new Pet(1L, "Firulais", "Canino", "Labrador", "Marrón", new Owner());

        when(petRepository.existsById(pet.getIdPet())).thenReturn(true);
        doNothing().when(petRepository).deleteById(pet.getIdPet());

        petService.deletePet(pet.getIdPet());

        verify(petRepository, times(1)).deleteById(pet.getIdPet());
    }

    @Test
    void findPetDTOEmptyTest (){
        List<PetDTO> petDTOS = new ArrayList<>();
        when(petRepository.findAll()).thenReturn(Collections.emptyList());

        List<PetDTO> pets = petService.findPetDTO();

        verify(petRepository,times(1)).findAll();
        assertEquals(0, pets.size() );
        assertTrue(pets.isEmpty());

    }

    @Test
    void findPetDTOSuccessTest(){
        Owner owner1 =   new Owner(1L, "12345678", "Juan", "Perez", "3001234567", new ArrayList<>());
        Owner owner2 = new Owner(2L, "8915246320", "Nicolas", "Paz", "3001234798", new ArrayList<>());
        Pet pet1 = new Pet(1L, "Firulais", "Canino", "Labrador", "Marrón", owner1);
        Pet pet2 = new Pet(2L, "Mishifu", "Felino", "Siamés", "Blanco", owner2);
        List<Pet> pets = new ArrayList<>();
        pets.add(pet1);
        pets.add(pet2);
        PetDTO petDTO = new PetDTO(pet1.getPetName(), pet1.getSpecies(), pet1.getRace(), owner1.getOwnerName(), owner1.getOwnerLastname());

        when(petRepository.findAll()).thenReturn(pets);

        List<PetDTO> petDTOS = petService.findPetDTO();

        verify(petRepository, times(1)).findAll();
        assertFalse(petDTOS.isEmpty());
        assertEquals(2, petDTOS.size());

        PetDTO petDTOResult = petDTOS.get(0);
        assertEquals(petDTO.getPetName(), petDTOResult.getPetName());
        assertEquals(petDTO.getSpecies(), petDTOResult.getSpecies());
        assertEquals(petDTO.getRace(),petDTOResult.getRace());
        assertEquals(petDTO.getOwnerName(), petDTOResult.getOwnerName());
        assertEquals(petDTO.getOwnerLastname(), petDTOResult.getOwnerLastname());

    }
}

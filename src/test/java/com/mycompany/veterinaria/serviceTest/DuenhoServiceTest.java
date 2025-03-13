package com.mycompany.veterinaria.serviceTest;

import com.mycompany.veterinaria.model.Duenho;
import com.mycompany.veterinaria.repository.IDuenhoRespository;
import com.mycompany.veterinaria.service.DuenhoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DuenhoServiceTest {

    @Mock
    private IDuenhoRespository duenhoRespository;

    @InjectMocks
    private DuenhoService duenhoService;

    @BeforeEach
    public void setUp (){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDuenhoSuccessTest (){
        Duenho duenho = new Duenho(1L, "12345678", "Juan", "Perez", "3001234567",new ArrayList<>());

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





}

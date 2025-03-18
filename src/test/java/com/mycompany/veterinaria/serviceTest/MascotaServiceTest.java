package com.mycompany.veterinaria.serviceTest;

import com.mycompany.veterinaria.repository.IDuenhoRespository;
import com.mycompany.veterinaria.repository.IMascotaRepository;
import com.mycompany.veterinaria.service.MascotaService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MascotaServiceTest {
    @Mock
    private IDuenhoRespository duenhoRespository;
    @Mock
    private IMascotaRepository mascotaRepository;

    @InjectMocks
    private MascotaService mascotaService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


}

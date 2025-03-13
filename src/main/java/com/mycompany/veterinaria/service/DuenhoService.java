package com.mycompany.veterinaria.service;import com.mycompany.veterinaria.model.Duenho;import com.mycompany.veterinaria.repository.IDuenhoRespository;import jakarta.persistence.EntityNotFoundException;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import java.util.List;@Servicepublic class DuenhoService implements  IDuenhoService{    @Autowired(required = true)    private IDuenhoRespository duenhoRespository;    @Override    public Duenho saveDuenho(Duenho duenho) {       return duenhoRespository.save(duenho);    }    @Override    public void deleteDuenho(Long idDuenho) {        if (!duenhoRespository.existsById(idDuenho)) {            throw new EntityNotFoundException("Dueño no Encontrado");        }        duenhoRespository.deleteById(idDuenho);    }    @Override    public List<Duenho> getDuenhos() {        List<Duenho> duenhoList = duenhoRespository.findAll();        return duenhoList;    }    @Override    public Duenho editDuenho(Duenho duenho) {        Duenho resultDuenho = this.findOneDuenho(duenho.getIdDuenho());        resultDuenho.setDni(duenho.getDni());        resultDuenho.setNombreDuenho(duenho.getNombreDuenho());        resultDuenho.setApellidoDuenho(duenho.getApellidoDuenho());        resultDuenho.setCelularDuenho(duenho.getCelularDuenho());        Duenho duenhoSave = this.saveDuenho(resultDuenho);        return duenhoSave;    }    @Override    public Duenho findOneDuenho(Long idDuenho) {        Duenho duenho = duenhoRespository.findById(idDuenho)                .orElseThrow(()->new EntityNotFoundException("Dueño no encontrado"));        return duenho;    }}
package com.mycompany.veterinaria.service;import com.mycompany.veterinaria.model.Owner;import java.util.List;public interface IOwnerService {    //Create duenho    public Owner saveOwner(Owner owner);    //Delete duenho    public void deleteOwner(Long idOwner);    //Read duenhos    public List<Owner> getOwners();    //edit duenho    public Owner editOwner(Owner owner);    //Buscar un duenho    public Owner findOneOwner(Long idOwner);}
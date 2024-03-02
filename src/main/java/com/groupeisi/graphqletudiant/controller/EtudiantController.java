package com.groupeisi.graphqletudiant.controller;

import com.groupeisi.graphqletudiant.dao.EtudiantRepository;
import com.groupeisi.graphqletudiant.entity.Etudiant;
import com.groupeisi.graphqletudiant.service.EtudiantService;
import com.groupeisi.graphqletudiant.service.EtudiantServiceImpl;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EtudiantController {


    private final EtudiantRepository repository;
    private final EtudiantService etudiantService;

    public EtudiantController(EtudiantRepository repository, EtudiantServiceImpl etudiantService) {
        this.repository = repository;
        this.etudiantService = etudiantService;
    }

    @QueryMapping(name = "getAllEtudiant")
    public List<Etudiant> getAllEtudiant() {
        return etudiantService.lire();
    }

    @QueryMapping(name = "findEtudiant")
    public Etudiant findEtudiantByEmail(@Argument String email) {
        return repository.findByEmail(email);
    }

    @MutationMapping(name = "addEtudiant")
    public Etudiant addEtudiant(@Argument Etudiant etudiant) {
        return etudiantService.creer(etudiant);
    }

    @MutationMapping(name = "updateEtudiant")
    public Etudiant updateEtudiant(@Argument int id, @Argument Etudiant etudiant) {
        return etudiantService.modifier(id, etudiant);
    }

    @MutationMapping(name = "deleteEtudiant")
    public String deleteEtudiant(@Argument int id) {
        etudiantService.supprimer(id);
        return "L'étudiant avec l'ID " + id + " a été supprimé.";
    }

}


package com.groupeisi.graphqletudiant.service;

import com.groupeisi.graphqletudiant.entity.Etudiant;

import java.util.List;

public interface EtudiantService {
    Etudiant creer(Etudiant etudiant);
    List<Etudiant> lire();
    Etudiant modifier(int id, Etudiant etudiant);
    String supprimer(int id);

}

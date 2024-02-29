package com.groupeisi.graphqletudiant.dao;

import com.groupeisi.graphqletudiant.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface EtudiantRepository extends JpaRepository<Etudiant,Integer> {
	Etudiant findByEmail(String email);
}

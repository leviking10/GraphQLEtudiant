package com.groupeisi.graphqletudiant.service;

import com.groupeisi.graphqletudiant.dao.EtudiantRepository;
import com.groupeisi.graphqletudiant.entity.Etudiant;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EtudiantServiceImpl implements EtudiantService {
        private final EtudiantRepository etudiantRepository;

    public EtudiantServiceImpl(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    @Override
        public Etudiant creer(Etudiant etudiant) {
            return etudiantRepository.save(etudiant);
        }

        @Override
        public List<Etudiant> lire() {
            return etudiantRepository.findAll();
        }

        @Override
        public Etudiant modifier(int id, Etudiant etudiant) {
            return etudiantRepository
                .findById(id)
                .map(e -> {
                    e.setName(etudiant.getName());
                    e.setEmail(etudiant.getEmail());
                    e.setTelephone(etudiant.getTelephone());
                    e.setAddress(etudiant.getAddress());
                    return etudiantRepository.save(e);
                })
                .orElseThrow(() -> new RuntimeException("Etudiant non trouve !"));
        }

        @Override
        public String supprimer(int id) {
            etudiantRepository.deleteById(id);
            return "Etudiant supprime avec succes !";
        }
    }


package com.groupeisi.graphqletudiant.controller;

import com.groupeisi.graphqletudiant.dao.EtudiantRepository;
import com.groupeisi.graphqletudiant.entity.Etudiant;
import com.groupeisi.graphqletudiant.service.EtudiantService;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@RequestMapping("/etudiant")
public class EtudiantController {


    private final EtudiantRepository repository;
    private final EtudiantService etudiantService;
    @Value("classpath:etudiant.graphqls")
    private Resource schemaResource;

    private GraphQL graphQL;

    public EtudiantController(EtudiantRepository repository, EtudiantService etudiantService) {
        this.repository = repository;
        this.etudiantService = etudiantService;
    }

    @PostConstruct
    public void loadSchema() throws IOException {
        try (InputStream schemaStream = schemaResource.getInputStream()) {
            TypeDefinitionRegistry registry = new SchemaParser().parse(new InputStreamReader(schemaStream));
            RuntimeWiring wiring = buildwiring();
            GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
            graphQL = GraphQL.newGraphQL(schema).build();
        }
    }


    @PostMapping("/graphql")
    public ResponseEntity<Object> handleGraphQL(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<>(result.toSpecification(), HttpStatus.OK);
    }
    private RuntimeWiring buildwiring() {
        DataFetcher<List<Etudiant>> fetcher1 = data -> {
            return repository.findAll();
        };
        DataFetcher<Etudiant> fetcher2 = data -> {
            return repository.findByEmail(data.getArgument("email"));
        };
        return RuntimeWiring
            .newRuntimeWiring()
            .type("Query", typewriting -> typewriting.dataFetcher("getAllEtudiant", fetcher1).dataFetcher("findEtudiant", fetcher2))
            .build();
    }


    @PostMapping("/addEtudiant")
    public String addEtudiant(@RequestBody List<Etudiant> etudiants) {
        repository.saveAll(etudiants);
        return "Records inserted: " + etudiants.size();
    }

    @GetMapping("/findAllEtudiant")
    public List<Etudiant> findAllEtudiant() {
        return repository.findAll();
    }

    @PostMapping("/getAll")
    public ResponseEntity<Object> getAll(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/getEtudiantByEmail")
    public ResponseEntity<Object> getEtudiantByEmail(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PostMapping("/create")
    public Etudiant create(@RequestBody Etudiant etudiant) {
        return etudiantService.creer(etudiant);
    }

    @GetMapping("/read")
    public List<Etudiant> read() {
        return etudiantService.lire();
    }

    @PutMapping("/update/{id}")
    public Etudiant update(int id, Etudiant etudiant) {
        return etudiantService.modifier(id, etudiant);
    }

    @DeleteMapping("/delete")
    public String delete(@PathVariable int id) {
        return etudiantService.supprimer(id);
    }

    @PostMapping("/getEtudiantById")
    public ResponseEntity<Object> getEtudiantById(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }
}


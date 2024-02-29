package com.groupeisi.graphqletudiant.controller;

import com.groupeisi.graphqletudiant.dao.EtudiantRepository;
import com.groupeisi.graphqletudiant.entity.Etudiant;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@RestController
public class EtudiantController {

    @Autowired
    private EtudiantRepository repository;

    @Value("classpath:etudiant.graphql")
    private Resource schemaResource;

    private GraphQL graphQL;

    public EtudiantController() {
    }

    @PostConstruct
    public void loadSchema() throws IOException {
        try (InputStream schemaStream = schemaResource.getInputStream()) {
            TypeDefinitionRegistry registry = new SchemaParser().parse(new InputStreamReader(schemaStream));
            RuntimeWiring wiring = buildWiring();
            GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
            graphQL = GraphQL.newGraphQL(schema).build();
        }
    }
    @PostMapping("/graphql")
    public ResponseEntity<Object> handleGraphQL(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<>(result.toSpecification(), HttpStatus.OK);
    }

    private RuntimeWiring buildWiring() {
        DataFetcher<List<Etudiant>> fetcher1 = data -> (List<Etudiant>) repository.findAll();
        DataFetcher<Etudiant> fetcher2 = data -> repository.findByEmail(data.getArgument("email"));

        return RuntimeWiring.newRuntimeWiring()
            .type("Query", typeWriting -> typeWriting
                .dataFetcher("getAllEtudiant", fetcher1) // Correspond à getAllEtudiant dans le schéma
                .dataFetcher("findEtudiant", fetcher2)) // Correspond à findEtudiant(email: String) dans le schéma
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

}
